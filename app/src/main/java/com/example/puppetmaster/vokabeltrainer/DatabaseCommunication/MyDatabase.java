package com.example.puppetmaster.vokabeltrainer.DatabaseCommunication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;

public class MyDatabase extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "vocabDB.db";
    private static final int DATABASE_VERSION = 2;

    public MyDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        // you can use an alternate constructor to specify a database location
        // (such as a folder on the sd card)
        // you must ensure that this folder is available and you have permission
        // to write to it
        //super(context, DATABASE_NAME, context.getExternalFilesDir(null).getAbsolutePath(), null, DATABASE_VERSION);

        // call this method to force a database overwrite every time the version number increments:
        setForcedUpgrade();

        // call this method to force a database overwrite if the version number
        // is below a certain threshold:
        //setForcedUpgrade(2);
    }

    public String getTopics() {

        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String [] sqlSelect = {"title"};
        String sqlTables = "topic";

        qb.setTables(sqlTables);
        Cursor c = qb.query(db, sqlSelect, null, null,
                null, null, null);

        c.moveToFirst();
        ArrayList<String> topicList = new ArrayList<String>();
        while(!c.isAfterLast()) {
            topicList.add(c.getString(c.getColumnIndex("title")));
            c.moveToNext();
        }
        c.close();

        String listString = "Hier eine Liste aller Themenblöcke in der Datenbank: ";
        for (String topic : topicList)
        {
            listString += topic + ",\t";
        }



        return listString;
    }

}