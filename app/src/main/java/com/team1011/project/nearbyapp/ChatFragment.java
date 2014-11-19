
package com.team1011.project.nearbyapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
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


    private String regID;
    private String usrName;

    public ChatDataSource chatDataSource;

    private static boolean lock = false;

    public ChatFragment(String r, String u) {

        regID = r;
        usrName = u;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!lock) {
            lock = true;


            BroadcastReceiver receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    Bundle bundle = intent.getExtras();


                    pushMessage(bundle.getString("USER_NAME"), bundle.getString("REG_ID"), bundle.getString("MSG"));

               /* adapter = new ChatMessageAdapter(getActivity(), android.R.id.text1,
                        chats);

                listView.setAdapter(adapter);

                adapter.notifyDataSetChanged();*/

                    Log.d("ASF", "RECEIVED BROACASTINTENT");
                    // ... do something ...
                }
            };
            LocalBroadcastManager.getInstance(getActivity().getApplicationContext())
                    .registerReceiver(receiver, new IntentFilter("myBroadcastIntent"));

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chat, container, false);
        chatLine = (TextView) view.findViewById(R.id.txtChatLine);
        listView = (ListView) view.findViewById(android.R.id.list);

        chatDataSource = new ChatDataSource(getActivity().getApplicationContext());
        chatDataSource.open();

        chats = chatDataSource.getChatsByUsrname(usrName);

        adapter = new ChatMessageAdapter(getActivity(), android.R.id.text1,
                chats);

        listView.setAdapter(adapter);
        view.findViewById(R.id.button1).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        if (GCMhandlerService.gcm != null) {
                            try {
                                data.put("TYPE", "chat");
                                data.put("USER_NAME", UI_Shell.userName);
                                data.put("REG_ID", UI_Shell.myRegID);
                                data.put("MESSAGE", chatLine.getText().toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            GCMhandlerService.gcm.sendMessage(data.toString(), regID);
                            pushMessage(UI_Shell.userName, UI_Shell.myRegID, chatLine.getText().toString());
                            chatLine.setText("");
                            //chatLine.clearFocus();
                        }
                    }
                });



        return view;
    }

    public interface MessageTarget {
        public Handler getHandler();
    }

    public void pushMessage(String usr, String reg, String readMessage) {
        Chat chat = new Chat();
        chat.setMsg(readMessage);
        chat.setUserName(usr);
        chat.setRegID(reg);
        chatDataSource.createChat(usr,reg, readMessage);
        adapter.add(chat);
        adapter.notifyDataSetChanged();
    }

    /**
     * ArrayAdapter to manage chat messages.
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
                String message = chats.get(position).toString();

                if (message != null && !message.isEmpty()) {
                    TextView nameText = (TextView) v
                            .findViewById(android.R.id.text1);

                    if (nameText != null) {
                        nameText.setText(message);
                        if (message.startsWith(UI_Shell.userName + ": ")) {
                            nameText.setTextAppearance(getActivity(),
                                    R.style.normalText);
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
