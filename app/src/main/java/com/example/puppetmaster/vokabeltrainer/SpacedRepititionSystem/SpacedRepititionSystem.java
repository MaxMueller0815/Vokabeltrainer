package com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem;

import com.example.puppetmaster.vokabeltrainer.DatabaseCommunication.SRSDataBaseCommunicator;

import org.json.JSONObject;

import java.util.Date;

/**
 * Created by florian on 11.01.17.
 */

//TODO push notifications

public class SpacedRepititionSystem {

    private Vocab currentVocab;
    private Notifier notifier;
    private SRSDataBaseCommunicator dbCommunicator;

    public void SpacedRepititionSystem(){
        //TODO
    }

    public Vocab getVocabRequest(){
        return currentVocab;
    }

    public void initCurrentVocab(int id, String english, String german, int unitId, int srsLevel,
                                 Date lastRevision, Date nextRevision, int countCorrect, int countFalse){

        currentVocab = new Vocab(id, english, german, unitId, srsLevel, lastRevision, nextRevision, countCorrect, countFalse);


    }

    public String getSrsAsJson(){
        return "true";

        //TODO
    }

    public boolean checkAnswer(String answer){
        return false;
    }

}
