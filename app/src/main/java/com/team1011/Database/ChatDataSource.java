package com.team1011.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Filip on 2014-11-18.
 */
public class ChatDataSource {

    // Database fields
    private final SQLiteHelper dbHelper;
    private final String[] allColumns;
    private SQLiteDatabase database;

    {
        allColumns = new String[]
                {
                        SQLiteHelper.CHAT_ID,
                        SQLiteHelper.COLUMN_USERNAME_TO,
                        SQLiteHelper.COLUMN_TYPE,
                        SQLiteHelper.COLUMN_MSG
                };
    }

    public ChatDataSource(Context context)
    {
        dbHelper = new SQLiteHelper(context);
    }


    public void open()
            throws SQLException
    {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Chat createChat(String usrName, String type, String msg) {
        final ContentValues values;
        final long insertId;
        final Cursor cursor;
        final Chat newChat;

        values = new ContentValues();
        values.put(SQLiteHelper.COLUMN_USERNAME_TO, usrName);
        values.put(SQLiteHelper.COLUMN_TYPE, type);
        values.put(SQLiteHelper.COLUMN_MSG, msg);

        try {
            insertId = database.insertWithOnConflict(SQLiteHelper.TABLE_CHAT,
                    null,
                    values,
                    database.CONFLICT_REPLACE);
            cursor = database.query(SQLiteHelper.TABLE_CHAT,
                    allColumns,
                    SQLiteHelper.CHAT_ID + " = " + insertId,
                    null,
                    null,
                    null,
                    null,
                    null);
            cursor.moveToFirst();
            newChat = cursorToChat(cursor);
            cursor.close();

            return (newChat);
        } catch (SQLiteException e) {

        }

        return null;

    }

  /*  public void deleteSingleChat(String u, int position)
    {
        /*
        DELETE FROM TABLE_CHAT
        WHERE CHAT_ID in (
            SELECT CHAT_ID FROM TABLE_CHAT LIMIT 1 OFFSET position
            WHERE COLUMN_USERNAME_TO = u
        )

    }*/

    public void deleteSingleChat(final Chat chat)
    {
        final long id;

        id = chat.getId();
        System.out.println("Comment deleted with id: " + id);
        database.delete(SQLiteHelper.TABLE_CHAT,
                SQLiteHelper.CHAT_ID + " = " + id,
                null);
    }

    public void deleteAllChat(String u)
    {
        database.delete(SQLiteHelper.TABLE_CHAT,
                SQLiteHelper.COLUMN_USERNAME_TO + " = \"" + u + "\"",
                null);
    }

    public List<Chat> getChatsByUsrname(String u)
    {
        final List<Chat> chats;
        final Cursor cursor;

        chats = new ArrayList<Chat>();
        cursor   = database.query(SQLiteHelper.TABLE_CHAT,
                allColumns,
                SQLiteHelper.COLUMN_USERNAME_TO + " = \"" + u + "\"",
                null,
                null,
                null,
                null);

        try
        {
            cursor.moveToFirst();

            while(!(cursor.isAfterLast()))
            {
                final Chat chat;

                chat = cursorToChat(cursor);
                chats.add(chat);
                cursor.moveToNext();
            }

        }
        finally
        {
            // make sure to close the cursor
            cursor.close();
        }

        return (chats);
    }

    private Chat cursorToChat(final Cursor cursor)
    {
        final Chat chat;

        chat = new Chat();
        chat.setId(cursor.getLong(0));
        chat.setUserName(cursor.getString(1));
        chat.setType(cursor.getString(2));
        chat.setMsg(cursor.getString(3));

        return (chat);
    }
}
