package com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem;

import org.json.JSONObject;

/**
 * Created by florian on 11.01.17.
 */

//TODO push notifications

public class SpacedRepititionSystem {

    private Vocab currentVocab;
    private DataBaseCommunicator dbCommunicator;
    private Notifier notifier;

    public void SpacedRepititionSystem(){
        //TODO
    }

    public Vocab getVocabRequest(){
        return new Vocab();

        //TODO
    }

    public String getSrsAsJson(){
        return "true";

        //TODO
    }

    public boolean checkAnswer(String answer){
        return false;
    }

}
