package com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by florian on 11.01.17.
 */

public class DataBaseCommunicator {

    private Cursor cursor;
    private SQLiteDatabase database;


    public void DataBaseCommunicator(String dbname){
       // database = this.openOrCreateDatabase(dbname, Context.MODE_PRIVATE, null);
    }

    public void updateVocab(Vocab vocab){
        //TODO
    }

    public Vocab getVocab(int id){
        return new Vocab();

        //TODO
    }

    public String getAllVocab(){
        return "true";

        //TODO
    }

}
