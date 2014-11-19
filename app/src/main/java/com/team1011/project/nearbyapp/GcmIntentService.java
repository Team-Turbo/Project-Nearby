package com.team1011.project.nearbyapp;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.team1011.Database.Person;
import com.team1011.Database.PersonDataSource;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GcmIntentService extends IntentService {



    public static ArrayList<Person> messages = new ArrayList<Person>();
    public static List<String> chat = new ArrayList<String>();

    private List<Person> values;
    private ArrayAdapter<Person> adapter;

    public PersonDataSource dataSource = new PersonDataSource(this);

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        dataSource.open();
        messages = dataSource.getAllPeople();  //Notifications


        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM
             * will be extended in the future with new message types, just ignore
             * any message types you're not interested in, or that you don't
             * recognize.
             */
            String msg = extras.get("text").toString();
            try {
                JSONObject obj = new JSONObject(msg);

                //Add discovered person to notifications
                if (obj.get("TYPE").equals("control")) {
                    final Person person;
                    final String personUsrName;
                    final String personRegId;
                    personUsrName = obj.get("USER_NAME").toString();
                    personRegId = obj.get("REG_ID").toString();

                    person = dataSource.createPerson(personUsrName, personRegId);

                    if (person != null)
                        messages.add(person);
                }
                else {
                    //Send the received chat to the chatFragment
                    final String chatUsrname;
                    final String chatRegId;
                    final String chatmsg;

                    chatUsrname = obj.get("USER_NAME").toString();
                    chatRegId = obj.get("REG_ID").toString();
                    chatmsg = obj.get("MESSAGE").toString();

                    Log.d("CHAT_RECEIVED", chatmsg);

                    Intent sendIntent = new Intent("chatBroadcastIntent");
                    Bundle chatBundle = new Bundle();
                    chatBundle.putString("USER_NAME", chatUsrname);
                    chatBundle.putString("REG_ID", chatRegId);
                    chatBundle.putString("MSG", chatmsg);

                    sendIntent.putExtras(chatBundle);

                    //Send intent to be received by the chatFragment
                    LocalBroadcastManager.getInstance(this).sendBroadcast(sendIntent);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

}
