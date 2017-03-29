package com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

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

    }

}
