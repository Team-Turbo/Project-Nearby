
package com.team1011.project.nearbyapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.team1011.Database.Chat;
import com.team1011.Database.ChatDataSource;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * This fragment handles chat related UI which includes a list view for messages
 * and a message entry field with send button.
 */
public class ChatFragment extends Fragment {

    private View view;
    private TextView chatLine;
    private ListView listView;
    public ChatMessageAdapter adapter = null;
    private List<Chat> chats = new ArrayList<Chat>();

    private JSONObject data = new JSONObject();

    private String targetRegID;
    private String usrName;

    public ChatDataSource chatDataSource;

    private static boolean lock = false;

    public ChatFragment() {super();}

    public void setArgs(String r, String u)
    {
        targetRegID = r;
        usrName = u;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Make sure we only receive one message at a time
        if (!lock) {
            lock = true;

            //Receive broadcast from the GCMintentService, update the chat UI with chat messages
            BroadcastReceiver receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    Bundle bundle = intent.getExtras();

                    //Push received message to the UI
                    pushMessage(bundle.getString("USER_NAME"), Chat.TYPE_FROM, bundle.getString("MSG"));

                }
            };
            //Accept intents only with the "chatBroadcastIntent" filter
            LocalBroadcastManager.getInstance(getActivity().getApplicationContext())
                    .registerReceiver(receiver, new IntentFilter("chatBroadcastIntent"));

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chat, container, false);
        chatLine = (TextView) view.findViewById(R.id.txtChatLine);
        listView = (ListView) view.findViewById(R.id.chat_list);
        registerForContextMenu(listView);

        //Grab all chat messages you have with this person
        chatDataSource = new ChatDataSource(getActivity().getApplicationContext());
        chatDataSource.open();
        chats = chatDataSource.getChatsByUsrname(usrName);

        //Set the adapter
        adapter = new ChatMessageAdapter(getActivity(), R.layout.chat_list_item,
                chats);

        listView.setAdapter(adapter);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        //List is idle
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                        //List is scrolling under the direct touch of the user
                        InputMethodManager imm = (InputMethodManager) view.getContext()
                                .getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                        //The user did a 'fling' on the list and it's still scrolling
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        //Set the send button listener
        view.findViewById(R.id.chat_send_button).setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    if (GCMstatic.gcm != null
                            && ((EditText)view.findViewById(R.id.txtChatLine)).getText().toString().trim().length() > 0) {
                        try {
                            //put the message into the JSON object to be sent over GCM
                            data.put("TYPE", "chat");
                            data.put("USER_NAME", UI_Shell.userName);
                            data.put("REG_ID", UI_Shell.myRegID);
                            data.put("MESSAGE", chatLine.getText().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //Send the message over GCM to the target registration ID
                        GCMstatic.gcm.sendMessage(data.toString(), targetRegID);

                        chatDataSource.createChat(usrName, Chat.TYPE_TO, chatLine.getText().toString());

                        //Push my message to the Fragment
                        pushMessage(usrName, Chat.TYPE_TO, chatLine.getText().toString());
                        chatLine.setText("");
                        //chatLine.clearFocus();
                    }
                }
            });

        getActivity().setTitle(usrName.split("@")[0]);

        listView.setSelection(adapter.getCount() - 1);

        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==R.id.chat_list) {
            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.menu_chat_list, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()) {
            case R.id.delete:
                chatDataSource.deleteSingleChat(usrName, info.position);
                adapter.remove(adapter.getItem(info.position));
                adapter.notifyDataSetChanged();
                return true;
            case R.id.delete_all:
                chatDataSource.deleteAllChat(usrName);
                adapter.clear();
                adapter.notifyDataSetChanged();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    /**
     * Push a message to the user interface
     * @param usr user name to be displayed
     * @param type type of chat, (from or to)
     * @param readMessage Message to be displayed
     */
    public void pushMessage(String usr, String type, String readMessage) {
        Chat chat = new Chat();
        chat.setMsg(readMessage);
        chat.setUserName(usr);
        chat.setType(type);
        //Add to database


        //Add the chat to the adapter
        if (usr.equalsIgnoreCase(usrName)) {
            adapter.add(chat);
            //Update the UI
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * Format the messages
     */
    public class ChatMessageAdapter extends ArrayAdapter<Chat> {


        public ChatMessageAdapter(Context context, int textViewResourceId,
                List<Chat> items) {
            super(context, textViewResourceId, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView v = (TextView)convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getActivity()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = (TextView)vi.inflate(R.layout.chat_list_item, null);
            }
            if (chats != null) {
                Chat currChat = chats.get(position);
                String message = currChat.toString();

                if (message != null && !message.isEmpty()) {

                    if (v != null) {

                        v.setText(message);

                        //User sends message
                        if (currChat.getType().equalsIgnoreCase(Chat.TYPE_TO)) {
                            v.setTextAppearance(getActivity(),
                                    R.style.normalText);
                            v.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
                        //Receive message
                        } else if (currChat.getType().equalsIgnoreCase(Chat.TYPE_FROM)) {
                            v.setTextAppearance(getActivity(),
                                    R.style.boldText);
                            v.setGravity(Gravity.CENTER_VERTICAL);
                        }
                    }
                }
            }
            return v;
        }
    }
}
