package com.example.puppetmaster.vokabeltrainer.DatabaseCommunication;

import android.content.Context;

import com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem.Vocab;

import java.util.ArrayList;
import java.util.HashMap;

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

    public void handleAcceptedInHour(int hourOfTheDay){
        double [] logInfo = myDatabase.getLogInfoForHour(hourOfTheDay);
        double [] newLogInfo = new double[logInfo.length];

        newLogInfo[2] = logInfo[2] + 1.0;

        myDatabase.updateLogForHour(newLogInfo);
    }

    public void handleDeclinedInHour(int hourOfTheDay){
        double [] logInfo = myDatabase.getLogInfoForHour(hourOfTheDay);
        double [] newLogInfo = new double[logInfo.length];

        newLogInfo[1] = logInfo[1] + 1.0;

        myDatabase.updateLogForHour(newLogInfo);
    }

    public void handleManualInHour(int hourOfTheDay){
        double [] logInfo = myDatabase.getLogInfoForHour(hourOfTheDay);
        double [] newLogInfo = new double[logInfo.length];

        newLogInfo[3] = logInfo[3] + 1.0;

        myDatabase.updateLogForHour(newLogInfo);
    }

    public HashMap<Integer, Integer[]> getCountInformationForEveryTimeBlock(){
        return myDatabase.getCountInformationForEveryTimeBlock();
    }

    public ArrayList<Vocab> getAllVocab(){

        ArrayList<Vocab> allVocab = new ArrayList<Vocab>();
        allVocab = myDatabase.getListOfAllVocab();

        return allVocab;

    }

    public ArrayList<Vocab> getVocabOfUnit(int id){
        return myDatabase.getListOfAllVocab();
    }


}
