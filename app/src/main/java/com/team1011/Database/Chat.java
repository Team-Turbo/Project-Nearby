package com.team1011.Database;

import com.team1011.project.nearbyapp.UI_Shell;

/**
 * Created by Filip on 2014-11-18.
 */
public class Chat
{
    public static final String TYPE_TO = "typeTo";
    public static final String TYPE_FROM = "typeFrom";

    private long id;
    private String userName;
    private String type;
    private String msg;


    public long getId()
    {
        return (id);
    }

    public void setId(final long i)
    {
        id = i;
    }

    public void setType(final String t) {
        type = t;
    }

    public String getType() {
        return type;
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
        if (type.equalsIgnoreCase(TYPE_TO))
            return userName + ": " + msg;
        else
            return UI_Shell.userName + ": " + msg;
    }
}
