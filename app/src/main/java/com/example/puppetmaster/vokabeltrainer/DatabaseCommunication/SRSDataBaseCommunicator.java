package com.example.puppetmaster.vokabeltrainer.DatabaseCommunication;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem.Vocab;

/**
 * Created by florian on 12.01.17.
 */

public class SRSDataBaseCommunicator {

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
