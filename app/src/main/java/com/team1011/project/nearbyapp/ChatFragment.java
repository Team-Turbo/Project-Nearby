
package com.team1011.project.nearbyapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
    public static ChatMessageAdapter adapter = null;
    private List<Chat> chats = new ArrayList<Chat>();

    private JSONObject data = new JSONObject();

    private String targetRegID;
    private String usrName;

    public ChatDataSource chatDataSource;

    private static boolean lock = false;

    public ChatFragment() {super();}

    public void setArgs(String r, String u) {

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
        listView = (ListView) view.findViewById(android.R.id.list);

        //Grab all chat messages you have with this person
        chatDataSource = new ChatDataSource(getActivity().getApplicationContext());
        chatDataSource.open();
        chats = chatDataSource.getChatsByUsrname(usrName);

        //Set the adapter
        adapter = new ChatMessageAdapter(getActivity(), android.R.id.text1,
                chats);
        listView.setAdapter(adapter);

        //Set the send button listener
        view.findViewById(R.id.button1).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        if (GCMstatic.gcm != null) {
                            try {
                                //put the message into the JSON object to be sent over GCM
                                data.put("TYPE", "chat");
                                data.put("USER_NAME", UI_Shell.userName);
                                data.put("MESSAGE", chatLine.getText().toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            //Send the message over GCM to the target registration ID
                            GCMstatic.gcm.sendMessage(data.toString(), targetRegID);

                            //Push my message to the Fragment
                            pushMessage(usrName, Chat.TYPE_TO, chatLine.getText().toString());
                            chatLine.setText("");
                            //chatLine.clearFocus();
                        }
                    }
                });



        return view;
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
        chatDataSource.createChat(usr, type, readMessage);
        //Add the chat to the adapter
        adapter.add(chat);
        //Update the UI
        adapter.notifyDataSetChanged();
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
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getActivity()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(android.R.layout.simple_list_item_1, null);
            }
            if (chats != null) {
                Chat currChat = chats.get(position);
                String message = currChat.toString();

                if (message != null && !message.isEmpty()) {
                    TextView nameText = (TextView) v
                            .findViewById(android.R.id.text1);

                    if (nameText != null) {
                        nameText.setText(message);
                        //User sends message
                        if (currChat.getType().equalsIgnoreCase(Chat.TYPE_TO)) {
                            nameText.setTextAppearance(getActivity(),
                                    R.style.normalText);

                            nameText.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
                        //Receive message
                        } else {
                            nameText.setTextAppearance(getActivity(),
                                    R.style.boldText);

                        }
                    }
                }
            }
            return v;
        }
    }
}
