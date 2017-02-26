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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MyDatabase extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "vocabDB.db";
    private static final int DATABASE_VERSION = 2;
    private SQLiteDatabase db;
    private SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

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
        db = getReadableDatabase();
        String[] sqlSelect = {"id, title"};
        String sqlTables = "topic";

        qb.setTables(sqlTables);
        Cursor c = qb.query(db, sqlSelect, null, null,
                null, null, null);
        ArrayList<Topic> topicsList = new ArrayList<Topic>();
        try {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                int topicID = c.getInt(0);
                String topicTitle = c.getString(1);
                ArrayList<Unit> unitsOfTopic = getUnitsOfTopic(topicID);
                topicsList.add(new Topic(topicID, topicTitle, unitsOfTopic));
                c.moveToNext();
            }
        } finally {
            c.close();
        }
        return topicsList;
    }

    private ArrayList<Unit> getUnitsOfTopic(int topicID) {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String[] sqlSelect = {"id ,title"};
        String sqlTables = "unit";

        qb.setTables(sqlTables);
        qb.appendWhere("TopicID=" + topicID);
        Cursor c = qb.query(db, sqlSelect, null, null,
                null, null, null);
        ArrayList<Unit> unitsList = new ArrayList<>();

        try {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                int unitID = c.getInt(0);
                String title = c.getString(1);
                ArrayList<Vocab> vocabsOfUnit = getVocabOfUnit(unitID);
                unitsList.add(new Unit(unitID, title, vocabsOfUnit));
                c.moveToNext();
            }
        } finally {
            c.close();
        }
        return unitsList;
    }


    private ArrayList<Vocab> getVocabOfUnit(int unitID) {
        db = getReadableDatabase();
        String[] sqlSelect = {"*"};
        String sqlTables = "srs";
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(sqlTables);
        queryBuilder.appendWhere("unitID=" + unitID);
        Cursor c = queryBuilder.query(db, sqlSelect, null, null,
                null, null, null);
        ArrayList<Vocab> vocabList = new ArrayList<Vocab>();
        ArrayList<String> translations = new ArrayList<String>();


        try {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                try {
                    translations = getTranslations(c.getInt(0));
                    vocabList.add(new Vocab(c.getInt(0), c.getString(2), translations, c.getInt(3), c.getInt(4), c.getString(5), c.getString(6), c.getInt(7), c.getInt(8)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                c.moveToNext();
            }
        } finally {
            c.close();
        }

        return vocabList;
    }


    private ArrayList<String> getTranslations(int srsID) {
        db = getReadableDatabase();
        String[] sqlSelect = {"translation"};
        String sqlTables = "translations";
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(sqlTables);
        queryBuilder.appendWhere("srsID=" + srsID);
        Cursor c = queryBuilder.query(db, sqlSelect, null, null,
                null, null, null);
        ArrayList<String> translations = new ArrayList<String>();

        try {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                translations.add(c.getString(0));
                c.moveToNext();
            }
        } finally {
            c.close();
        }
        return translations;
    }

    /*
    * TODO die einzelne Vokabel in der Datenbank updaten
    *
    * */
    public void updateSingleVocab(Vocab updatedVocab) {
        db = this.getWritableDatabase();
        String whereClause = "id=" + updatedVocab.getId();
        ContentValues contentValues = new ContentValues();
        contentValues.put("srsLevel", updatedVocab.getSrsLevel());
        contentValues.put("countCorrect", updatedVocab.getCountCorrect());
        contentValues.put("countFalse", updatedVocab.getCountFalse());
        // TODO Date ins richtige Format bringen
        updatedVocab.setLastRevision();
        updatedVocab.setNextRevision();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        String lastRevision = dateFormat.format(updatedVocab.getLastRevision());
        String nextRevision = dateFormat.format(updatedVocab.getNextRevision());
        contentValues.put("lastRevision", lastRevision);
        contentValues.put("nextRevision", nextRevision);
        db.update("srs", contentValues, whereClause, null);
        Log.i("MyDatabase", updatedVocab.getGerman()+ ": "+contentValues.toString());
    }

    /*
        return all vocabulary as a representation of the spaced repetition system
    * */
    public ArrayList<Vocab> getListOfAllVocab() {
        db = getReadableDatabase();
        String[] sqlSelect = {"*"};
        String sqlTables = "srs";

        qb.setTables(sqlTables);
        Cursor c = qb.query(db, sqlSelect, null, null,
                null, null, null);
        ArrayList<Vocab> listOfAllVocab = new ArrayList<>();
        ArrayList<String> translations = new ArrayList<String>();

        try {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                try {
                    translations = getTranslations(c.getInt(0));
                    listOfAllVocab.add(new Vocab(c.getInt(0), c.getString(1), translations, c.getInt(3), c.getInt(4), c.getString(5), c.getString(6), c.getInt(7), c.getInt(8)));
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

    public ArrayList<Object> getSettings() {
        ArrayList<Object> settings = new ArrayList<Object>();
        db = getReadableDatabase();
        String[] sqlSelect = {"workload, exerciseStart, exerciseEnd"};
        String sqlTables = "userPreferences";

        qb.setTables(sqlTables);
        Cursor c = qb.query(db, sqlSelect, null, null,
                null, null, null);


        try {
            c.moveToFirst();
            settings.add(c.getInt(0));
            settings.add(c.getString(1));
            settings.add(c.getString(2));
        } finally {
            c.close();
        }
        return settings;
    }

    public int getWorkload() {
        int workload = 0;
        db = getReadableDatabase();
        String[] sqlSelect = {"workload"};
        String sqlTables = "userPreferences";

        qb.setTables(sqlTables);
        Cursor c = qb.query(db, sqlSelect, null, null,
                null, null, null);


        try {
            c.moveToFirst();
            workload = c.getInt(0);
        } finally {
            c.close();
        }
        return workload;
    }

    // TODO Set Workload
    public void saveSettings(int workload, String strStart, String strEnd) {
        db = this.getWritableDatabase();
        String whereClause = "ROWID=1";
        ContentValues contentValues = new ContentValues();
        contentValues.put("workload", workload);
        contentValues.put("exerciseStart", strStart);
        contentValues.put("exerciseEnd", strEnd);
        db.update("userPreferences", contentValues, whereClause, null);
    }
}