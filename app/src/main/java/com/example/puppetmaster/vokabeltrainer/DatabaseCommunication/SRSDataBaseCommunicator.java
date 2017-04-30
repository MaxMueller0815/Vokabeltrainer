package com.example.puppetmaster.vokabeltrainer.DatabaseCommunication;

import android.content.Context;

import com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem.Vocab;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by florian on 12.01.17.
 */

public class SRSDataBaseCommunicator {

    /*
    *       define the hour multiplier behaviour in these doubles
    *       the MULTIPLIER_PART should be 1/11 of the MULTIPLIER_FULL
    * */
    private static double MULTIPLIER_FULL = 0.011;
    private static double MULTIPLIER_PART = 0.001;

    private MyDatabase myDatabase;

    public SRSDataBaseCommunicator(Context context){
        myDatabase = new MyDatabase(context);
    }

    public void updateVocab(Vocab vocab){
        myDatabase.updateSingleVocab(vocab);
    }

    public void handleAcceptedInHour(int hourOfTheDay){

        for (int i=0; i<12; i++){
            double [] logInformation = myDatabase.getLogInfoForHour(i*2);
            double [] newLogInformation = new double[logInformation.length];

            if(i*2 == hourOfTheDay){
                newLogInformation[2] = logInformation[2] + 1.0;
                newLogInformation[4] = logInformation[4] + MULTIPLIER_FULL;
            }else{
                newLogInformation[4] = logInformation[4] - MULTIPLIER_PART;
            }

            myDatabase.updateLogForHour(newLogInformation);
        }

    }

    public void handleDeclinedInHour(int hourOfTheDay){

        for (int i=0; i<12; i++){
            double [] logInformation = myDatabase.getLogInfoForHour(i*2);
            double [] newLogInformation = new double[logInformation.length];

            if(i*2 == hourOfTheDay){
                newLogInformation[1] = logInformation[1] + 1.0;
                newLogInformation[4] = logInformation[4] - MULTIPLIER_FULL;
            }else{
                newLogInformation[4] = logInformation[4] + MULTIPLIER_PART;
            }

            myDatabase.updateLogForHour(newLogInformation);
        }

    }

    public void handleManualInHour(int hourOfTheDay){

        for (int i=0; i<12; i++){
            double [] logInformation = myDatabase.getLogInfoForHour(i*2);
            double [] newLogInformation = new double[logInformation.length];

            if(i*2 == hourOfTheDay){
                newLogInformation[3] = logInformation[3] + 1.0;
                newLogInformation[4] = logInformation[4] + MULTIPLIER_FULL;
            }else{
                newLogInformation[4] = logInformation[4] - MULTIPLIER_PART;
            }

            myDatabase.updateLogForHour(newLogInformation);
        }
    }

    public HashMap<Integer, Double[]> getCountInformationForEveryTimeBlock(){
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
