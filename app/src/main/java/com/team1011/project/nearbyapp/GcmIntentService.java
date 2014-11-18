package com.team1011.project.nearbyapp;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
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
                    chat.add(extras.get("text").toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            //messages.add(extras.get("text").toString());




            for (int i = 0; i< messages.size(); i++)
                Log.d("GCM Message received", messages.get(i).getPerson());


        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

}
