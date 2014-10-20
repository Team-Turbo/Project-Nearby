package com.team1011.project.nearbyapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

/**
 * Created by Filip on 2014-10-20.
 */
public class Profile extends PlaceholderFragment {

    private String userName;
    private String displayName;
    private String birthDay;
    private String imageUrl;
    private String aboutMe;
    private ImageView imgProfilePic;
    private TextView txtAccountName;
    private TextView txtDisplayName;
    private TextView txtBday;
    private static boolean imageLock = false;

   public Profile() {

   }


    public Profile(String usrName, String dispName, String bDay, String imgUrl, String abtMe) {

        userName = usrName;
        displayName = dispName;
        birthDay = bDay;
        imageUrl = imgUrl;
        aboutMe = abtMe;




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.profile, container, false);

        txtAccountName = (TextView) rootView.findViewById(R.id.accountName);
        txtDisplayName = (TextView) rootView.findViewById(R.id.displayName);
        txtBday = (TextView) rootView.findViewById(R.id.birthDay);
        imgProfilePic = (ImageView) rootView.findViewById(R.id.profilePic);

        if (!imageLock) {
            //imageLock = true;
            new LoadProfileImage(imgProfilePic).execute(imageUrl);
        }

        txtAccountName.setText(userName);
        txtDisplayName.setText(displayName);
        txtBday.setText(birthDay);

        return rootView;
    }

    /**
     * Background Async task to load user profile picture from url
     * */
    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public LoadProfileImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
