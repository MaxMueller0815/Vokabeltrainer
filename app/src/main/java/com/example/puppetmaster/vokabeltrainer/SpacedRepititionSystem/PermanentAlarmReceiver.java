package com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.puppetmaster.vokabeltrainer.DatabaseCommunication.SRSDataBaseCommunicator;

import java.util.ArrayList;

/**
 * Created by florian on 29.03.17.
 */

public class PermanentAlarmReceiver extends BroadcastReceiver {

    // two arrays containing the hours on which either only push notifications or only training happend
    private ArrayList<Integer> pushHoursOnly;
    private ArrayList<Integer> trainingHoursOnly;

    // array to store the hours both push and training happened

    private ArrayList<Integer> pushAndTraining;

    @Override
    public void onReceive(Context context, Intent intent) {

        pushHoursOnly = new ArrayList<Integer>();
        trainingHoursOnly = new ArrayList<Integer>();
        pushAndTraining = new ArrayList<Integer>();

        SRSDataBaseCommunicator dbCommunicator = new SRSDataBaseCommunicator(context);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        // handle the number of notifications and vocab trainings of the last day and reset the counts

        String savedString1 = prefs.getString("pushNotificationHours", "100");
        String[] st1 = savedString1.split(",");

        int[] notList = new int[st1.length];
        for (int i = 0; i < st1.length; i++) {
            notList [i] = Integer.parseInt(st1[i]);
        }

        String savedString2 = prefs.getString("trainingVocabHours", "100");
        String[] st2 = savedString2.split(",");

        int[] trainList = new int[st2.length];
        for (int i = 0; i < st2.length; i++) {
            trainList [i] = Integer.parseInt(st2[i]);
        }

        initializeArraysForCountManagement(notList, trainList);

        for(int i = 0; i<pushAndTraining.size(); i++){
            dbCommunicator.handleAcceptedInHour(pushAndTraining.get(i));
        }

        for(int i = 0; i<pushHoursOnly.size(); i++){
            dbCommunicator.handleDeclinedInHour(pushHoursOnly.get(i));
        }

        for(int i = 0; i<trainingHoursOnly.size(); i++){
            dbCommunicator.handleManualInHour(trainingHoursOnly.get(i));
        }

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("pushNotificationHours", "100");
        editor.putString("trainingVocabHours", "100");
        editor.apply();
        
    }

    private void initializeArraysForCountManagement(int [] notList, int [] trainList){
        for (int i=0; i<notList.length; i++){
            if(contains(trainList, notList[i])){
                pushAndTraining.add(notList[i]);
            }else{
                pushHoursOnly.add(notList[i]);
            }
        }

        for (int i=0; i<trainList.length; i++){
            if(!contains(notList, trainList[i])){
                trainingHoursOnly.add(trainList[i]);
            }
        }
    }

    public static boolean contains(int[] arr, int item) {
        for (int n : arr) {
            if (item == n) {
                return true;
            }
        }
        return false;
    }
}
