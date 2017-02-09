package com.example.puppetmaster.vokabeltrainer.DatabaseCommunication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem.Vocab;
import com.example.puppetmaster.vokabeltrainer.Topic;
import com.example.puppetmaster.vokabeltrainer.Unit;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.text.ParseException;
import java.util.ArrayList;

public class MyDatabase extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "vocabDB.db";
    private static final int DATABASE_VERSION = 5;

    public MyDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        setForcedUpgrade();
        // you can use an alternate constructor to specify a database location
        // (such as a folder on the sd card)
        // you must ensure that this folder is available and you have permission
        // to write to it
        //super(context, DATABASE_NAME, context.getExternalFilesDir(null).getAbsolutePath(), null, DATABASE_VERSION);
        // call this method to force a database overwrite every time the version number increments:
        // call this method to force a database overwrite if the version number
        // is below a certain threshold:
        //setForcedUpgrade(2);
    }

    public ArrayList<Topic> getTopics() {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String [] sqlSelect = {"id, title"};
        String sqlTables = "topic";

        qb.setTables(sqlTables);
        Cursor c = qb.query(db, sqlSelect, null, null,
                null, null, null);
        ArrayList<Topic> topicsList = new ArrayList<Topic>();
        try {
            c.moveToFirst();
            while(!c.isAfterLast()) {
                topicsList.add(new Topic(c.getInt(0), c.getString(1)));
                c.moveToNext();
            }
        } finally {
            c.close();
        }
        return topicsList;
    }

    public ArrayList<Unit> getUnitsOfTopic(int topicID) {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String [] sqlSelect = {"id ,title, topicID"};
        String sqlTables = "unit";

        qb.setTables(sqlTables);
        qb.appendWhere("TopicID=" + topicID);
        Cursor c = qb.query(db, sqlSelect, null, null,
                null, null, null);
        ArrayList<Unit> unitsList = new ArrayList<>();

        try {
            c.moveToFirst();
            while(!c.isAfterLast()) {
                unitsList.add(new Unit(c.getInt(0), c.getString(1), c.getInt(2)));
                c.moveToNext();
            }
        } finally {
            c.close();
        }
        return unitsList;
    }

        /*
        * TODO die einzelne Vokabel in der Datenbank updaten
        *
        * */
    public void updateSingleVocab(Vocab updatedVocab){
//        Vocab currentVocab;
//        for(Vocab vocab : getListOfAllVocab()) {
//            if(vocab.getId() == updatedVocab.getId()) {
//                currentVocab = vocab;
//            }
//        }
        SQLiteDatabase db = this.getWritableDatabase();

        String whereClause = "id=" + updatedVocab.getId();
        ContentValues contentValues = new ContentValues();
        contentValues.put("srsLevel", updatedVocab.getSrsLevel());
        contentValues.put("countCorrect", updatedVocab.getCountCorrect());
        contentValues.put("countFalse", updatedVocab.getCountFalse());
        contentValues.put("lastRevision", updatedVocab.getLastRevision().toString());
        contentValues.put("nextRevision", updatedVocab.getNextRevision().toString());
        db.update("srs", contentValues, whereClause, null);


    }

    public ArrayList<Vocab> getVocabOfUnit(int unitID) {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String [] sqlSelect = {"*"};
        String sqlTables = "srs";

        qb.setTables(sqlTables);
        qb.appendWhere("unitID=" + unitID);
        Cursor c = qb.query(db, sqlSelect, null, null,
                null, null, null);
        ArrayList<Vocab> vocabList = new ArrayList<>();

        try {
            c.moveToFirst();
            while(!c.isAfterLast()) {
                try {
                    vocabList.add(new Vocab(c.getInt(0), c.getString(1), c.getString(2), c.getInt(3), c.getInt(4), c.getString(5), c.getString(6), c.getInt(7), c.getInt(8)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                c.moveToNext();
            }
        }  finally {
            c.close();
        }

        return vocabList;
    }

    /*
        return all vocabulary as a representation of the spaced repetition system
    * */
    public ArrayList<Vocab> getListOfAllVocab(){
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String [] sqlSelect = {"*"};
        String sqlTables = "srs";

        qb.setTables(sqlTables);
        Cursor c = qb.query(db, sqlSelect, null, null,
                null, null, null);
        ArrayList<Vocab> listOfAllVocab = new ArrayList<>();

        try {
            c.moveToFirst();
            while(!c.isAfterLast()) {
                try {
                    listOfAllVocab.add(new Vocab(c.getInt(0), c.getString(1), c.getString(2), c.getInt(3), c.getInt(4), c.getString(5), c.getString(6), c.getInt(7), c.getInt(8)));
                    Log.d("Added", listOfAllVocab.get(listOfAllVocab.size() - 1) + "");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                c.moveToNext();
            }
        } finally {
            c.close();
        }

        return listOfAllVocab;
    }

}