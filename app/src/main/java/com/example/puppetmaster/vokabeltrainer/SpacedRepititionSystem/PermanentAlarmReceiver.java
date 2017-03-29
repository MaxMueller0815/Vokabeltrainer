package com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.StringTokenizer;

/**
 * Created by florian on 29.03.17.
 */

public class PermanentAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        // set notifications for the new day
        SpacedRepititionSystem srs = new SpacedRepititionSystem(context);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        // handle the number of notifications and vocab trainings of the last day and reset the counts

        String savedString1 = prefs.getString("pushNotificationHours", "");
        String[] st1 = savedString1.split(",");

        int[] savedList1 = new int[st1.length];
        for (int i = 0; i < st1.length; i++) {
            savedList1 [i] = Integer.parseInt(st1[i]);
        }

        String savedString2 = prefs.getString("trainingVocabHours", "");
        String[] st2 = savedString2.split(",");

        int[] savedList2 = new int[st2.length];
        for (int i = 0; i < st2.length; i++) {
            savedList2 [i] = Integer.parseInt(st2[i]);
        }
    }

}
