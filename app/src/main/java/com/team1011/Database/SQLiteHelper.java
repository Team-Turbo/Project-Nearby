package com.team1011.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class SQLiteHelper
        extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME;
    private static final int    DATABASE_VERSION;
    private static final String DATABASE_CREATE_PEOPLE;
    private static final String DATABASE_CREATE_CHAT;

    public static final String  TABLE_PEOPLE;
    public static final String  PERSON_ID;
    public static final String  COLUMN_PERSON;
    public static final String  REG_ID;

    public static final String  TABLE_CHAT;
    public static final String  CHAT_ID;
    public static final String  COLUMN_USERNAME_TO;
    public static final String  COLUMN_TYPE;
    public static final String  COLUMN_MSG;

    static
    {
        DATABASE_NAME    = "people.db";
        DATABASE_VERSION = 8;

        TABLE_PEOPLE = "people";
        PERSON_ID = "_id";
        COLUMN_PERSON = "person";
        REG_ID = "id";

        TABLE_CHAT = "chat";
        CHAT_ID = "_cid";
        COLUMN_USERNAME_TO = "name_to";
        COLUMN_TYPE = "type";
        COLUMN_MSG = "msg";

        DATABASE_CREATE_PEOPLE  =
                "create table " +
                TABLE_PEOPLE + "(" + PERSON_ID +
                " integer primary key autoincrement, " + COLUMN_PERSON +
                " text not null unique, " + REG_ID + " text not null unique);";

        DATABASE_CREATE_CHAT =
                "create table " +
                TABLE_CHAT + "(" + CHAT_ID + " integer primary key autoincrement, " + COLUMN_USERNAME_TO +
                " text not null, " + COLUMN_TYPE + " text not null, " + COLUMN_MSG + " text not null);";
    }

    public SQLiteHelper(final Context context)
    {
        super(context,
                DATABASE_NAME,
                null,
                DATABASE_VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase database)
    {
        database.execSQL(DATABASE_CREATE_PEOPLE);
        database.execSQL(DATABASE_CREATE_CHAT);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db,
                          final int            oldVersion,
                          final int            newVersion)
    {
        Log.w(SQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PEOPLE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHAT);
        onCreate(db);
    }
}