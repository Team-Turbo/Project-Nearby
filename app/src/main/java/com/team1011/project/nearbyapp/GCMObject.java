package com.team1011.project.nearbyapp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;

import java.io.IOException;

/**
 * This class register's the device wtih GCM and facilitates the sending of messages via GCM
 *
 * @author Alex Dellow, Thomas Tallentire
 */
public class GCMObject {

    GoogleCloudMessaging gcm;
    public static final String SEND_ID = "291560809451";
    String registrationID = "";

    //GcmBroadcastReceiver incoming;


    public GCMObject() {

    }


    /**
     *
     * @param ctx
     */
    public void registerInBackground(final Context ctx) {
        if(!registrationID.isEmpty()){ return; }

        new AsyncTask<Object, Integer, String>() {

            protected void onPostExecute(String regID) {
                Log.d("info", "Registration Complete");
                UI_Shell.myRegID = regID;
            }

            @Override
            protected String doInBackground(Object[] params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(ctx);
                    }
                    registrationID = gcm.register(SEND_ID);
                    Log.d("info", "Device registered, registration ID=" + registrationID);
                    Log.d("info", "Device registered, registration ID length is " + registrationID.length());
                    /*
                    // You should send the registration ID to your server over HTTP,
                    // so it can use GCM/HTTP or CCS to send messages to your app.
                    // The request to your server should be authenticated if your app
                    // is using accounts.
                    sendRegistrationIdToBackend();

                    // For this demo: we don't need to send it because the device
                    // will send upstream messages to a server that echo back the
                    // message using the 'from' address in the message.
                    */
                    // Persist the regID - no need to register again.

                } catch (IOException ex) {
                    ex.printStackTrace();
                    Log.d("info", "Error :" + ex.getMessage());
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return registrationID;
            }
        }.execute();
    }

    /**
     *
     * @param data
     * @param regId
     */
    public void sendMessage(final String data, final String regId){
        new AsyncTask<Object, Integer, String>(){

            @Override
            protected String doInBackground(Object[] objects) {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("https://android.googleapis.com/gcm/send");
                HttpResponse response;

                try {
                    httpPost.addHeader(new BasicHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8 "));
                    httpPost.addHeader(new BasicHeader("Authorization", "key=AIzaSyCJsDphSiFKz42BWOC580RoMEpQ0AeIdkU"));

                    String outGoing = "data.text=" + data + "&registration_id=" + regId;
                    StringEntity se = new StringEntity(outGoing);

                    httpPost.setEntity(se);

                    response = httpClient.execute(httpPost);

                    Log.d("Sending To", regId);

                    Log.d("info", "Response : " + response.toString());
                    Log.d("info", "Response : " + response.getStatusLine().toString());

                } catch (ClientProtocolException e) {
                    //swag
                    e.printStackTrace();
                    Log.d("info", "Protocol Error");
                } catch (IOException e) {
                    //more Swag
                    e.printStackTrace();
                    Log.d("info", "IO Error");
                }

                return null;
            }
        }.execute(null, null, null);
    }

    /**
     *
     * @return
     */
    public String getRegistrationID() {
        return registrationID;
    }

    /**
     *
     * @param data
     * @param regId
     */
    public void postData(String data, String regId) {


    }
}
