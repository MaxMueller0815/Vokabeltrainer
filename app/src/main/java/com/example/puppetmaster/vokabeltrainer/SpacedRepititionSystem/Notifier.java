package com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Calendar;

/**
 * Created by florian on 12.01.17.
 */

public class Notifier {

    private Intent alarmIntent;
    private PendingIntent pendingIntent;
    private AlarmManager manager;
    private SharedPreferences prefs;

    public void Notifier(Context context){

        this.prefs = PreferenceManager.getDefaultSharedPreferences(context);

        if (!prefs.getBoolean("firstTime", false)){

            this.alarmIntent = new Intent(context, AlarmReceiver.class);
            this.pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);

            this.manager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());

            //TODO zeit richtig setzen
            calendar.set(Calendar.HOUR_OF_DAY, 7);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 1);


            //TODO für jeden zeitblock einen alarm setzen
            manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);


            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.apply();
        }

    }

}
