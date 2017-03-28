package com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.renderscript.Script;

import com.example.puppetmaster.vokabeltrainer.DatabaseCommunication.SRSDataBaseCommunicator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by florian on 12.01.17.
 */

public class Notifier {

    private int [] timeRange = {0,0};

    private Intent alarmIntent;
    private PendingIntent pendingIntent;
    private AlarmManager manager;
    private SharedPreferences prefs;
    private SRSDataBaseCommunicator dbCommunicator;

    private ArrayList<Calendar> timeblockList = new ArrayList<Calendar>();

    public Notifier(Context context, SRSDataBaseCommunicator communicator){

        this.prefs = PreferenceManager.getDefaultSharedPreferences(context);
        this.dbCommunicator = communicator;

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

        // get time block count information
        // structure <key, [countDeclined, countAccepted, countManual]>
        HashMap<Integer, Integer[]> timeBlockCountInfo = new HashMap<Integer, Integer[]>();
        timeBlockCountInfo = dbCommunicator.getCountInformationForEveryTimeBlock();

        for(int i = 0; i < numberOfTimeBlocks; i++){

            //increase the hourOfTheDay by 2 every loop run
            int hourOfTheDay = startTime + (i*2);

            Calendar timeblock = Calendar.getInstance();
            timeblock.setTimeInMillis(System.currentTimeMillis());
            timeblock.set(Calendar.HOUR_OF_DAY, hourOfTheDay);
            timeblock.set(Calendar.MINUTE, 0);
            timeblock.set(Calendar.SECOND, 1);

            //calculate the probability of a push notification for the actual time block
            Integer [] countInformation = timeBlockCountInfo.get(hourOfTheDay);
            if(PushProbabilityCalculator.pushOnThisHourOfTheDay(countInformation[1], countInformation[0], countInformation[2], hourOfTheDay)) {
                timeblockList.add(timeblock);
                System.out.println("######## adding push notification for hour: " + hourOfTheDay);
            }
        }

        System.out.println("Initializing time block array list.... length " + timeblockList.size() + " , timerange: " + this.timeRange.toString());
    }

    public static class PushProbabilityCalculator {

        static double factorManual = 3.0;

        public static boolean pushOnThisHourOfTheDay(double accepted, double declined, double manual, int hourOfTheDay){
            double daytimefactor = getDayTimeFactor(hourOfTheDay);
            double result = daytimefactor * ((accepted + (manual*factorManual)) / (accepted + declined + (manual*factorManual)));

            // get random number between 0.0 and 1.0 to compare the probability of the notification to get pushed or not
            return (result <= Math.random());
        }

        public static double getDayTimeFactor(int hourOfTheDay){

            //TODO tageszeitfaktoren einstellen

            switch (hourOfTheDay){
                case 2: return 1.0;
                case 4: return 1.0;
                case 6: return 1.0;
                case 8: return 1.0;
                case 10: return 1.0;
                case 12: return 1.0;
                case 14: return 1.0;
                case 16: return 1.0;
                case 18: return 1.0;
                case 20: return 1.0;
                case 22: return 1.0;
                case 24: return 1.0;
                default: return 1.0;
            }
        }

    }

}
