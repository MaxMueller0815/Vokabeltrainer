package com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem;

import android.content.Context;

import com.example.puppetmaster.vokabeltrainer.DatabaseCommunication.SRSDataBaseCommunicator;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.SortedSet;

/**
 * Created by florian on 11.01.17.
 */

//TODO push notifications

public class SpacedRepititionSystem {

    private Notifier notifier;
    private SRSDataBaseCommunicator dbCommunicator;

    private ArrayList<Vocab> currentRequestList = new ArrayList<Vocab>();
    private Vocab currentVocab;

    private int currentRequestListLength = 5;

    public SpacedRepititionSystem(Context context){
        notifier = new Notifier();
        dbCommunicator = new SRSDataBaseCommunicator(context);
        initCurrentRequestList();
    }

    public Vocab getVocabRequest(){
        
        if(currentRequestList.size() == 0){
            initCurrentRequestList();
        }

        currentVocab = currentRequestList.get(0);
        currentRequestList.remove(0);

        return currentVocab;
    }

    public void initCurrentRequestList(){
        // HashMap helps to sort the vocab by the datedifference (currentdate - nextRevision)
        HashMap<Long, Vocab> helperVocabHashMap = new HashMap<Long, Vocab>();
        ArrayList<Vocab> allVocab = new ArrayList<Vocab>();
        ArrayList<Long> revisionDifferences = new ArrayList<Long>();

        allVocab = dbCommunicator.getAllVocab();

        for (int i = 0; i<allVocab.size(); i++ ){
            Vocab vocab = allVocab.get(i);
            long revisionDifference = vocab.getRevisionDifference();

            if(revisionDifference > 0){
                helperVocabHashMap.put(revisionDifference, vocab);
                revisionDifferences.add(revisionDifference);
            }
        }

        Collections.sort(revisionDifferences);

        for (int i = 1; i <= currentRequestListLength; i++){
            Vocab vocabToAdd = helperVocabHashMap.get(revisionDifferences.get(revisionDifferences.size()-i));
            currentRequestList.add(vocabToAdd);
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
