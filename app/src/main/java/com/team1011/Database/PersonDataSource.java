package com.team1011.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;

/**
 * Created by Filip on 2014-10-26.
 */
public class PersonDataSource {

    // Database fields
    private final SQLiteHelper dbHelper;
    private final String[] allColumns;
    private SQLiteDatabase database;

    {
        allColumns = new String[]
                {
                        SQLiteHelper.PERSON_ID,
                        SQLiteHelper.COLUMN_PERSON,
                        SQLiteHelper.REG_ID
                };
    }

    public PersonDataSource(Context context)
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

    public Person createPerson(String person, String regID)
    {
        final ContentValues values;
        final long          insertId;
        final Cursor cursor;
        final Person        newPerson;

        values = new ContentValues();
        values.put(SQLiteHelper.COLUMN_PERSON, person);
        values.put(SQLiteHelper.REG_ID, regID);

        try {
            insertId = database.insertWithOnConflict(SQLiteHelper.TABLE_PEOPLE,
                    null,
                    values,
                    database.CONFLICT_REPLACE);
            cursor = database.query(SQLiteHelper.TABLE_PEOPLE,
                    allColumns,
                    SQLiteHelper.PERSON_ID + " = " + insertId,
                    null,
                    null,
                    null,
                    null);
            cursor.moveToFirst();
            newPerson = cursorToPerson(cursor);
            cursor.close();

            return (newPerson);
        } catch (SQLiteException e) {

        }

        return null;

    }

    public void deletePerson(final Person person)
    {
        final long id;

        id = person.getId();
        System.out.println("Comment deleted with id: " + id);
        database.delete(SQLiteHelper.TABLE_PEOPLE,
                SQLiteHelper.PERSON_ID + " = " + id,
                null);
    }

    public ArrayList<Person> getAllPeople()
    {
        final ArrayList<Person> people;
        final Cursor cursor;

        people = new ArrayList<Person>();
        cursor   = database.query(SQLiteHelper.TABLE_PEOPLE,
                allColumns,
                null,
                null,
                null,
                null,
                null);

        try
        {
            cursor.moveToFirst();

            while(!(cursor.isAfterLast()))
            {
                final Person person;

                person = cursorToPerson(cursor);
                people.add(person);
                cursor.moveToNext();
            }

        }
        finally
        {
            // make sure to close the cursor
            cursor.close();
        }

        return (people);
    }

    private Person cursorToPerson(final Cursor cursor)
    {
        final Person person;

        person = new Person();
        person.setId(cursor.getLong(0));
        person.setPerson(cursor.getString(1));
        person.setRegID(cursor.getString(2));

        return (person);
    }
}
