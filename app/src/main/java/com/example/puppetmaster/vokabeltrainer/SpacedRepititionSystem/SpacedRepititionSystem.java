package com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem;

import android.content.Context;

import com.example.puppetmaster.vokabeltrainer.DatabaseCommunication.SRSDataBaseCommunicator;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by florian on 11.01.17.
 */

//TODO push notifications

public class SpacedRepititionSystem {

    private Vocab currentVocab;
    private Notifier notifier;
    private SRSDataBaseCommunicator dbCommunicator;

    public SpacedRepititionSystem(Context context){
        notifier = new Notifier();
        dbCommunicator = new SRSDataBaseCommunicator(context);
        initCurrentVocab();
    }

    public Vocab getVocabRequest(){
        return currentVocab;
    }

    public void initCurrentVocab(){

        ArrayList<Vocab> allVocab = new ArrayList<Vocab>();
        allVocab = dbCommunicator.getAllVocab();

        for (int i = 0; i<allVocab.size(); i++ ){
            //TODO vokabel mit der höchsten dringlichkeit suchen und und zwischenspeichern
        }
    }

    public boolean checkAnswer(String answer){
        return false;
    }

}
