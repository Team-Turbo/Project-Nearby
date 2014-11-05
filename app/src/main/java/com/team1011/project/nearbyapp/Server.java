package com.team1011.project.nearbyapp;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas on 2014-11-05.
 */
public class Server {
    private static String authorization;

    Server(){}

    Server(String autId) {
        authorization = autId;
    }

    public static void postData(String data, String regId) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("https://android.googleapis.com/gcm/send");

        try {
            httpPost.addHeader(new BasicHeader("Authorization", authorization));
            httpPost.addHeader(new BasicHeader("Content-Type", "application/json"));
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("registration_ids", regId));
            nameValuePairs.add(new BasicNameValuePair("data", data));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = httpClient.execute(httpPost);
        } catch (ClientProtocolException e) {
            //swag
        } catch (IOException e) {
            //more Swag
        }
    }

}
