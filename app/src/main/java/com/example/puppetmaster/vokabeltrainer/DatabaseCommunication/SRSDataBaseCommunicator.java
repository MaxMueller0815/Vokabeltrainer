package com.example.puppetmaster.vokabeltrainer.DatabaseCommunication;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;

import com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem.Vocab;

import java.util.ArrayList;

/**
 * Created by florian on 12.01.17.
 */

public class SRSDataBaseCommunicator {

    private MyDatabase myDatabase;

    public SRSDataBaseCommunicator(Context context){
        myDatabase = new MyDatabase(context);
    }

    public void updateVocab(Vocab vocab){
        myDatabase.updateSingleVocab(vocab);
    }

 //   public Vocab getVocab(int id){

        //TODO
   // }

    public ArrayList<Vocab> getAllVocab(){

        ArrayList<Vocab> allVocab = new ArrayList<Vocab>();
        allVocab = myDatabase.getListOfAllVocab();

        return allVocab;

    }

}
