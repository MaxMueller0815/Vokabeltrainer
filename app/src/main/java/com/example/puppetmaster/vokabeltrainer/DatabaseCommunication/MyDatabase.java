package com.example.puppetmaster.vokabeltrainer.DatabaseCommunication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.example.puppetmaster.vokabeltrainer.Entities.Topic;
import com.example.puppetmaster.vokabeltrainer.Entities.Unit;
import com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem.Vocab;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Diese Klasse erbt von SQLiteAssetHelper (https://github.com/jgilfelt/android-sqlite-asset-helper). Der Helper vereinfacht den Import von mitgelieferten Datenbanken (Vokabeln)
 */
public class MyDatabase extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "vocabDB.db";
    private static final int DATABASE_VERSION = 11;
    private SQLiteDatabase db;
    private SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

    public MyDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        setForcedUpgrade();
    }


    /*
    * returns the content of the count information table in the following structure:
    *
    *       <key, [countDeclined, countAccepted, countManual, multiplier]>
    * */
    public HashMap<Integer, Double[]> getCountInformationForEveryTimeBlock() {

        HashMap<Integer, Double[]> resultHashMap = new HashMap<Integer, Double[]>();

        db = getReadableDatabase();
        String[] sqlSelect = {"*"};
        String sqlTables = "pushNotificationLog";
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(sqlTables);
        Cursor c = queryBuilder.query(db, sqlSelect, null, null,
                null, null, null);

        try {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                Double[] countArray = new Double[4];
                countArray[0] = (double) c.getInt(1);
                countArray[1] = (double) c.getInt(2);
                countArray[2] = (double) c.getInt(3);
                countArray[3] = (double) c.getFloat(4);

                resultHashMap.put(c.getInt(0), countArray);

                c.moveToNext();
            }
        } finally {
            c.close();
        }
        return resultHashMap;
    }

    public void updateLogForHour(double[] logInfo) {
        db = this.getWritableDatabase();
        String whereClause = "timeSlot=" + (int) logInfo[0];

        ContentValues contentValues = new ContentValues();

        contentValues.put("counterDeclined", (int) logInfo[1]);
        contentValues.put("counterAccepted", (int) logInfo[2]);
        contentValues.put("counterManual", (int) logInfo[3]);
        contentValues.put("multiplier", (float) logInfo[4]);

        db.update("pushNotificationLog", contentValues, whereClause, null);
    }

    public double[] getLogInfoForHour(int hour) {
        db = getReadableDatabase();

        String[] sqlSelect = {"*"};
        String sqlTables = "pushNotificationLog";
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(sqlTables);
        queryBuilder.appendWhere("timeSlot=" + hour);
        Cursor c = queryBuilder.query(db, sqlSelect, null, null,
                null, null, null);

        double[] logInfo = new double[5];

        try {
            c.moveToFirst();
            while (!c.isAfterLast()) {

                logInfo[0] = (double) c.getInt(0);
                logInfo[1] = (double) c.getInt(1);
                logInfo[2] = (double) c.getInt(2);
                logInfo[3] = (double) c.getInt(3);
                logInfo[4] = (double) c.getInt(4);

                c.moveToNext();
            }
        } finally {
            c.close();
        }

        return logInfo;
    }

    public ArrayList<double[]> getLogInfoWithoutHour(int hour) {
        db = getReadableDatabase();

        ArrayList<double[]> resultList = new ArrayList<double[]>();

        String[] sqlSelect = {"*"};
        String sqlTables = "pushNotificationLog";
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(sqlTables);
        queryBuilder.appendWhere("NOT timeSlot=" + hour);
        Cursor c = queryBuilder.query(db, sqlSelect, null, null,
                null, null, null);

        try {
            c.moveToFirst();
            while (!c.isAfterLast()) {

                double[] logInfo = new double[5];

                logInfo[0] = (double) c.getInt(0);
                logInfo[1] = (double) c.getInt(1);
                logInfo[2] = (double) c.getInt(2);
                logInfo[3] = (double) c.getInt(3);
                logInfo[4] = (double) c.getInt(4);

                resultList.add(logInfo);
                c.moveToNext();
            }
        } finally {
            c.close();
        }

        return resultList;
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
                unitsList.add(new Unit(unitID, title, vocabsOfUnit, topicID));
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
                    vocabList.add(new Vocab(c.getInt(0), c.getString(1), translations, c.getInt(2), c.getInt(3), (long) c.getDouble(4), (long) c.getDouble(5), c.getInt(6), c.getInt(7)));
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

    public void updateSingleVocab(Vocab updatedVocab) {
        try {
            db = this.getWritableDatabase();
            String whereClause = "id=" + updatedVocab.getId();
            ContentValues contentValues = new ContentValues();
            contentValues.put("srsLevel", updatedVocab.getSrsLevel());
            contentValues.put("countCorrect", updatedVocab.getCountCorrect());
            contentValues.put("countFalse", updatedVocab.getCountFalse());
            contentValues.put("lastRevision", updatedVocab.getLastRevision());
            contentValues.put("nextRevision", updatedVocab.getNextRevision());
            db.update("srs", contentValues, whereClause, null);
            Log.i("MyDatabase", updatedVocab.getGerman() + ": " + contentValues.toString());
        } finally {
            db.close();
        }
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
        ArrayList<String> translations = new ArrayList<>();

        try {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                try {
                    translations = getTranslations(c.getInt(0));
                    listOfAllVocab.add(new Vocab(c.getInt(0), c.getString(1), translations, c.getInt(2), c.getInt(3), (long) c.getDouble(4), (long) c.getDouble(5), c.getInt(6), c.getInt(7)));
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
        String[] sqlSelect = {"workload, exerciseStart, exerciseEnd, inputRequiresArticle, inputRequiresCapitalisation"};
        String sqlTables = "userPreferences";

        qb.setTables(sqlTables);
        Cursor c = qb.query(db, sqlSelect, null, null, null, null, null);

        try {
            c.moveToFirst();
            //Workload
            settings.add(c.getInt(0));
            //exerciseStart
            settings.add(c.getInt(1));
            //exerciseEnd
            settings.add(c.getInt(2));
            //inputRequiresArticle
            settings.add(c.getInt(3));
            //inputRequiresCapitalisation
            settings.add(c.getInt(4));
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
    public void saveSettings(int workload, int start, int end, boolean withArticle, boolean caseSensitive) {
        db = this.getWritableDatabase();
        String whereClause = "ROWID=1";
        ContentValues contentValues = new ContentValues();
        contentValues.put("workload", workload);
        contentValues.put("exerciseStart", start);
        contentValues.put("exerciseEnd", end);
        int inputRequiresArticle;
        if (withArticle) {
            inputRequiresArticle = 1;
        } else {
            inputRequiresArticle = 0;
        }
        contentValues.put("inputRequiresArticle", inputRequiresArticle);

        int inputRequiresCapitalisation;
        if (caseSensitive) {
            inputRequiresCapitalisation = 1;
        } else {
            inputRequiresCapitalisation = 0;
        }
        contentValues.put("inputRequiresCapitalisation", inputRequiresCapitalisation);

        db.update("userPreferences", contentValues, whereClause, null);
        db.close();
    }

    public boolean[] getInputMode() {
        boolean[] inputMode = new boolean[2];
        boolean withArticle;
        boolean caseSensitve;

        db = getReadableDatabase();
        String[] sqlSelect = {"inputRequiresArticle, inputRequiresCapitalisation"};
        String sqlTables = "userPreferences";

        qb.setTables(sqlTables);
        Cursor c = qb.query(db, sqlSelect, null, null,
                null, null, null);


        try {
            c.moveToFirst();
            if (c.getInt(0) == 0) {
                withArticle = false;
            } else {
                withArticle = true;
            }
            ;

            if (c.getInt(1) == 0) {
                caseSensitve = false;
            } else {
                caseSensitve = true;
            }
        } finally {
            c.close();
        }
        inputMode[0] = withArticle;
        inputMode[1] = caseSensitve;
        return inputMode;
    }

    public void updateProgressLog() {
        // Auf Mitternacht setzen
        Calendar date = Calendar.getInstance();
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);

        db = this.getWritableDatabase();
        String whereClause = "date=" + date.getTimeInMillis();
        ContentValues contentValues = new ContentValues();
        contentValues.put("vocabsPractised", getNumVocabsPractisedForDate(date.getTimeInMillis()) + 1);
        try {
            if (hasProgressEntryForDate(date.getTimeInMillis())) {
                db.update("progressLog", contentValues, whereClause, null);
            } else {
                contentValues.put("date", date.getTimeInMillis());
                db.insert("progressLog", null, contentValues);
            }
        } finally {
            db.close();
        }
    }

    private boolean hasProgressEntryForDate(long date) {
        db = this.getReadableDatabase();
        String[] sqlSelect = {"date"};
        String sqlTables = "progressLog";

        qb.setTables(sqlTables);
        Cursor c = qb.query(db, sqlSelect, "date=" + date, null,
                null, null, null);
        boolean exist = (c.getCount() > 0);
        c.close();
        return exist;
    }

    public int getNumVocabsPractisedForDate(long date) {
        db = this.getReadableDatabase();
        String[] sqlSelect = {"*"};
        String sqlTables = "progressLog";
        qb.setTables(sqlTables);
        Cursor c = qb.query(db, sqlSelect, "date=" + date, null,
                null, null, null);

        int numVocabs;

        if (c.getCount() > 0) {
            c.moveToFirst();
            numVocabs = c.getInt(1);
        } else {
            numVocabs = 0;
        }
        c.close();
        return numVocabs;
    }
}