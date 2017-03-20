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

            Calendar timeblock1 = Calendar.getInstance();
            Calendar timeblock2 = Calendar.getInstance();
            Calendar timeblock3 = Calendar.getInstance();
            Calendar timeblock4 = Calendar.getInstance();
            Calendar timeblock5 = Calendar.getInstance();

            timeblock1.setTimeInMillis(System.currentTimeMillis());
            timeblock2.setTimeInMillis(System.currentTimeMillis());
            timeblock3.setTimeInMillis(System.currentTimeMillis());
            timeblock4.setTimeInMillis(System.currentTimeMillis());
            timeblock5.setTimeInMillis(System.currentTimeMillis());

            //TODO zeit richtig setzen
            timeblock1.set(Calendar.HOUR_OF_DAY, 7);
            timeblock1.set(Calendar.MINUTE, 0);
            timeblock1.set(Calendar.SECOND, 1);

            timeblock2.set(Calendar.HOUR_OF_DAY, 7);
            timeblock2.set(Calendar.MINUTE, 0);
            timeblock2.set(Calendar.SECOND, 1);

            timeblock3.set(Calendar.HOUR_OF_DAY, 7);
            timeblock3.set(Calendar.MINUTE, 0);
            timeblock3.set(Calendar.SECOND, 1);

            timeblock4.set(Calendar.HOUR_OF_DAY, 7);
            timeblock4.set(Calendar.MINUTE, 0);
            timeblock4.set(Calendar.SECOND, 1);

            timeblock5.set(Calendar.HOUR_OF_DAY, 7);
            timeblock5.set(Calendar.MINUTE, 0);
            timeblock5.set(Calendar.SECOND, 1);


            //TODO für jeden zeitblock einen alarm setzen
            manager.setRepeating(AlarmManager.RTC_WAKEUP, timeblock1.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);


            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.apply();
        }

    }

}
