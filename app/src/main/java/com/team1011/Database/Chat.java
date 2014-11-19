package com.team1011.Database;

/**
 * Created by Filip on 2014-11-18.
 */
public class Chat {

    private long id;
    private String userName;
    private String registrationID;
    private String msg;


    public long getId()
    {
        return (id);
    }

    public void setId(final long i)
    {
        id = i;
    }

    public void setRegID(final String r) {
        registrationID = r;
    }

    public String getRegID() {
        return registrationID;
    }

    public void setUserName(final String u) {
        userName = u;
    }

    public String getUserName() {
        return userName;
    }

    public String getMsg()
    {
        return (msg);
    }

    public void setMsg(final String s)
    {
        msg = s;
    }

    @Override
    public String toString()
    {
        return userName + ": " + msg;
    }

}
