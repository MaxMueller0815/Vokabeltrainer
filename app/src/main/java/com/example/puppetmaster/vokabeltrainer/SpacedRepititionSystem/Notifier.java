package com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.renderscript.Script;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by florian on 12.01.17.
 */

public class Notifier {

    private int [] timeRange = {0,0};

    private Intent alarmIntent;
    private PendingIntent pendingIntent;
    private AlarmManager manager;
    private SharedPreferences prefs;

    private ArrayList<Calendar> timeblockList = new ArrayList<Calendar>();

    public Notifier(Context context){

        this.prefs = PreferenceManager.getDefaultSharedPreferences(context);

        // Notification Zeitpunkt Start
        this.timeRange[0] = prefs.getInt("hourStart", 0);
        // Notification Zeitpunkt Ende
        this.timeRange[1] = prefs.getInt("hourEnd", 0);

        int numberOfTimeBlocks = calculateNumberOfTimeBlocks();
        initTimeBlockArrayList(timeRange[0], numberOfTimeBlocks);

    //    if (!prefs.getBoolean("firstTime", false)){

            this.alarmIntent = new Intent(context, AlarmReceiver.class);
            this.pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);

            this.manager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);

            // set alarm for every timeblock
            for (int i = 0; i<timeblockList.size(); i++) {
                manager.setRepeating(AlarmManager.RTC_WAKEUP, timeblockList.get(i).getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
            }

            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.apply();
     //   }

    }

    // calculate the number of time blocks available in the defined time range
    private int calculateNumberOfTimeBlocks(){
        // divide the time range settings by 2 to get the number of available timeblocks with a range of 2hrs
        double numberOfTimeBlocks = StrictMath.ceil(((double)timeRange[1] - (double)timeRange[0]) / 2.0);
        return (int) numberOfTimeBlocks;
    }

    private void initTimeBlockArrayList(int startTime, int numberOfTimeBlocks){

        for(int i = 0; i < numberOfTimeBlocks; i++){

            Calendar timeblock = Calendar.getInstance();
            timeblock.setTimeInMillis(System.currentTimeMillis());
            timeblock.set(Calendar.HOUR_OF_DAY, (startTime + (i*2)));
            timeblock.set(Calendar.MINUTE, 0);
            timeblock.set(Calendar.SECOND, 1);

            timeblockList.add(timeblock);
        }

        System.out.println("Initializing time block array list.... " + timeblockList.toString());
    }

    public static class PushProbabilityCalculator {

        static double factorManual = 3.0;

        public static double calculateProbabilityToPush(double accepted, double declined, double manual, double daytimefactor){

            double result = daytimefactor * ((accepted + (manual*factorManual)) / (accepted + declined + (manual*factorManual)));

            return result;
        }

    }

}
