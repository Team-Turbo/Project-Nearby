package com.team1011.project.nearbyapp;

/**
 * Created by Filip on 2014-10-20.
 */
public class Profile extends PlaceholderFragment {

    private String userName;
    private String displayName;
    private String birthDay;
    private String imageUrl;
    private String aboutMe;

   public Profile() {
       
   }


    public Profile(String usrName, String dispName, String bDay, String imgUrl, String abtMe) {

        userName = usrName;
        displayName = dispName;
        birthDay = bDay;
        imageUrl = imgUrl;
        aboutMe = abtMe;
    }

}
