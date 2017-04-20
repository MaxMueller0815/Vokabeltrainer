package com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.puppetmaster.vokabeltrainer.DatabaseCommunication.SRSDataBaseCommunicator;
import com.example.puppetmaster.vokabeltrainer.Helper.WordHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by florian on 11.01.17.
 */

public class SpacedRepititionSystem {

    private Notifier notifier;
    private SRSDataBaseCommunicator dbCommunicator;

    private SharedPreferences prefs;

    private ArrayList<Vocab> currentRequestList = new ArrayList<Vocab>();

    private Vocab currentVocab;

    private int currentRequestListLength = 100;
    private int newVocabRequestedToday = 0;
    private int newVocabToRequestOnOneDay = 12;

    private int [] vocabTrainingHours;

    public SpacedRepititionSystem(Context context) {

        this.dbCommunicator = new SRSDataBaseCommunicator(context);

        this.prefs = PreferenceManager.getDefaultSharedPreferences(context);

        if (prefs.getInt("lastTrainingDay", 0) == getDateAsInt()) {
            newVocabRequestedToday = prefs.getInt("newVocabThisDay", 0);
        } else {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("lastTrainingDay", getDateAsInt());
            editor.apply();

            newVocabRequestedToday = 0;
        }

        initCurrentRequestList();


        // dafault value 100 just if nothing is stored yet
        String trainingHours = prefs.getString("trainingVocabHours", "100");
        String[] st2 = trainingHours.split(",");

        int[] savedList2 = new int[st2.length];
            for (int i = 0; i < st2.length; i++) {
                savedList2[i] = Integer.parseInt(st2[i]);
            }

        this.vocabTrainingHours = savedList2;

        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("numberOfWordsInWorkload", currentRequestList.size());
        editor.apply();
    }

    /*
    *       use current request list for the next vocab to request
    *       if the list is empty init a new list, otherwise request one vocab and delete it from the list
    *
    * */
    public Vocab getVocabRequest() {

            if (currentRequestList.size() == 0) {
                System.out.println("request list ist leer.....");
                return null;

            }else {
                currentVocab = currentRequestList.get(0);
                currentRequestList.remove(0);

                if (currentVocab.isNewVocab()) {
                    if (newVocabRequestedToday >= newVocabToRequestOnOneDay) {
                        return getVocabRequest();
                    } else {
                        newVocabRequestedToday += 1;
                        return currentVocab;
                    }
                } else {
                    return currentVocab;
                }
            }
    }

    /*
    *       generates a list of vocab to request with the declared size
    *
    *
    *
    * */
    public void initCurrentRequestList() {
        // HashMap helps to sort the vocab by the datedifference (currentdate - nextRevision)
        HashMap<Long, Vocab> helperVocabHashMap = new HashMap<Long, Vocab>();
        ArrayList<Vocab> allVocab = new ArrayList<Vocab>();
        ArrayList<Long> revisionDifferences = new ArrayList<Long>();

        allVocab = dbCommunicator.getAllVocab();

        System.out.println( "Allvocabsize::::::   " + allVocab.size());

        for (int i = 0; i < allVocab.size(); i++) {
            Vocab vocab = allVocab.get(i);
            long revisionDifference = vocab.getRevisionDifference();

            if (revisionDifference > 0) {
                //System.out.println("revisiondifferenz gefunden:  #####################");
                Log.v("Revision-Diff. gefunden", vocab.getEnglish() + ": " + revisionDifference/3600000 + "h");
                helperVocabHashMap.put(revisionDifference, vocab);
                revisionDifferences.add(revisionDifference);
            }
        }

        Collections.sort(revisionDifferences);
        System.out.println("helperVocabHashmap :  ###### " + helperVocabHashMap.size());
        System.out.println(" #### revisiondifferences: " + revisionDifferences.toString());

        // Ich glaube, die doppelte For-Schleife ist falsch und führt zu Duplikaten - oder übersehe ich da was?
        //for (int i = 0; i < revisionDifferences.size(); i++) {
            for (int j = 1; j <= currentRequestListLength; j++) {
                if(revisionDifferences.size() >= j){
                    Vocab vocabToAdd = helperVocabHashMap.get(revisionDifferences.get(revisionDifferences.size() - j));
                    currentRequestList.add(vocabToAdd);
                }
            }
        //}

        System.out.println("requestlist initialisiert:  " + currentRequestList.toString());
    }

    /*
    *   manage the srs level of the requested vocab depending on the answer
    *   save the number of new vocab requested on the day
    *   set date of last handled answer on the actual day
    * */

    public boolean handleAnswer(int language, Vocab vocab, String answer) {

        boolean isCorrect = checkAnswer(language, vocab, answer);
        if (isCorrect) {
            vocab.increaseSrsLevel();
            vocab.increaseCountCorrect();
        } else {
            vocab.decreaseSrsLevel();
            vocab.increaseCountFalse();
        }

        vocab.setLastRevision();
        vocab.setNextRevision();
        dbCommunicator.updateVocab(vocab);

        // if actual time block hour is not in the list, add it
        if(!contains(vocabTrainingHours, getActualTimeBlockHour())){
            int [] newVocabTrainingHours = new int [vocabTrainingHours.length+1];
            for(int i = 0; i < vocabTrainingHours.length; i++){
                newVocabTrainingHours[i] = vocabTrainingHours[i];
            }
            newVocabTrainingHours[vocabTrainingHours.length] = getActualTimeBlockHour();
            this.vocabTrainingHours = newVocabTrainingHours;
        }

        //append stuff to the shared preferences
        SharedPreferences.Editor editor = prefs.edit();

        StringBuilder str = new StringBuilder();
        for (int i = 0; i < vocabTrainingHours.length; i++) {
            str.append(vocabTrainingHours[i]).append(",");
        }

        editor.putString("trainingVocabHours", str.toString());
        editor.putInt("lastTrainingDay", getDateAsInt());
        editor.putInt("newVocabThisDay", newVocabRequestedToday);
        editor.putInt("numberOfWordsInWorkload", currentRequestList.size());
        editor.apply();

        return isCorrect;
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
    public boolean checkAnswer(int language, Vocab vocab, String answer) {

        if (language == 1) {
            return vocab.getEnglish().equals(answer);
        } else if (language == 2) {
            boolean isCorrect = false;
            for (int i = 0; i < vocab.getGerman().size(); i++) {
                if (WordHelper.cleanString(answer.toLowerCase()).equals(WordHelper.cleanString(vocab.getGerman().get(i).toLowerCase()))) {
                    isCorrect = true;
                }
            }
            return isCorrect;
        } else {
            return false;
        }
    }
    
    public void startNewUnit(int id) {
        ArrayList<Vocab> vocabsInUnit = new ArrayList<Vocab>();
        vocabsInUnit = dbCommunicator.getVocabOfUnit(id);

        for (int i = 0; i < vocabsInUnit.size(); i++) {
            Vocab currentVocab = vocabsInUnit.get(i);
            currentVocab.pushIntoWorkload();
            dbCommunicator.updateVocab(currentVocab);

        }
    }

    public int getDateAsInt() {
        DateFormat formatter = new SimpleDateFormat("ddMMyyyy");
        return Integer.parseInt(formatter.format(new Date()));
    }

    public int getActualTimeBlockHour(){
        SimpleDateFormat format = new SimpleDateFormat("H");
        String formattedDate = format.format(new Date());
        int hour = Integer.parseInt(formattedDate);

        // check if hour is even, otherwise substract 1 to get the beginning of the time block
        if(!((hour%2) == 0)){
            hour -=1;
        }

        return hour;
    }

    public static boolean contains(int[] arr, int item) {
        for (int n : arr) {
            if (item == n) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Vocab> getCurrentRequestList() {
        return currentRequestList;
    }


}
