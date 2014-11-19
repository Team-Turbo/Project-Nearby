package com.team1011.Database;

/**
 * Created by Filip on 2014-10-26.
 */
public class Person {

    private long id;
    private String person;
    private String regID;

    public long getId()
    {
        return (id);
    }

    public void setId(final long i)
    {
        id = i;
    }

    public void setRegID(final String r) {
        regID = r;
    }

    public String getRegID() { return regID;}

    public String getPerson()
    {
        return (person);
    }

    public void setPerson(final String s)
    {
        person = s;
    }


    @Override
    public String toString()
    {
        return person;
    }
}
