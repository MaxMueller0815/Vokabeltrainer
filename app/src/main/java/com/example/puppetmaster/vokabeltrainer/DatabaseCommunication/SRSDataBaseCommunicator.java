package com.example.puppetmaster.vokabeltrainer.DatabaseCommunication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem.Vocab;

import java.util.ArrayList;

/**
 * Created by florian on 12.01.17.
 */

public class SRSDataBaseCommunicator {

    private Cursor cursor;
    private SQLiteDatabase database;
    private MyDatabase myDatabase;

    private ArrayList<Vocab> allVocab = new ArrayList<Vocab>();

    public SRSDataBaseCommunicator(){
         //database = this.openOrCreateDatabase(dbname, Context.MODE_PRIVATE, null);
    }

    public void updateVocab(Vocab vocab){
        //TODO
    }

 //   public Vocab getVocab(int id){

        //TODO
   // }

    public ArrayList<Vocab> getAllVocab(){

        //TODO Datenbank auslesen und alle benötigten Vokabeln in eine ArrayList speichern
        // benötigte Vokabeln aus den Einstellungen auslesen

        return allVocab;

    }

}
