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

    /*
    *   manage the srs level of the requested vocab depending on the answer
    * */

    public void handleAnswer(int language, Vocab vocab, String answer){
        if(checkAnswer(language, vocab, answer)){
            vocab.increaseSrsLevel();
            vocab.increaseCountCorrect();
        }else{
            vocab.decreaseSrsLevel();
            vocab.increaseCountFalse();
        }

        dbCommunicator.updateVocab(vocab);

    }

/*
*       check if the given answer is right or wrong
*       use int parameter to select the language of the GIVEN ANSWER
 *
 *       1 = ENGLISH
 *       2 = GERMAN
*
*
* */
    public boolean checkAnswer(int language, Vocab vocab, String answer){
        if(language == 1){
            return vocab.getEnglish().equals(answer);
        }else if(language == 2){
            return vocab.getGerman().equals(answer);
        }else{
            return false;
        }
    }

}
