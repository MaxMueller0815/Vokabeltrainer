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

     //   int numberOfTimeBlocks = calculateNumberOfTimeBlocks();
     //   initTimeBlockArrayList(timeRange[0], numberOfTimeBlocks);

        this.alarmIntent = new Intent(context, AlarmReceiver.class);
        this.manager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
/**
            // set alarm for every timeblock
            for (int i = 0; i<timeblockList.size(); i++) {
                this.pendingIntent = PendingIntent.getBroadcast(context, timeblockList.get(i).get(Calendar.HOUR_OF_DAY), alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                manager.set(AlarmManager.RTC_WAKEUP, timeblockList.get(i).getTimeInMillis(), pendingIntent);
            }

 **/
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 1);

        //set alarm every two hours on every day
        for(int i = 0; i<12; i++){
            calendar.set(Calendar.HOUR_OF_DAY, (i*2));
            this.pendingIntent = PendingIntent.getBroadcast(context, (100+(i*2)), this.alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            this.manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, this.pendingIntent);
        }

    }

    // calculate the number of time blocks available in the defined time range
    private int calculateNumberOfTimeBlocks(){
        // divide the time range settings by 2 to get the number of available timeblocks with a range of 2hrs
        double numberOfTimeBlocks = StrictMath.ceil(((double)timeRange[1] - (double)timeRange[0]) / 2.0);
        return (int) numberOfTimeBlocks;
    }

    private void initTimeBlockArrayList(int startTime, int numberOfTimeBlocks){

        int [] timeBlockStartHours = new int [numberOfTimeBlocks];

        // get time block count information
        // structure <key, [countDeclined, countAccepted, countManual, multiplier]>
        HashMap<Integer, Double[]> timeBlockCountInfo = new HashMap<Integer, Double[]>();
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
            Double [] countInformation = timeBlockCountInfo.get(hourOfTheDay);
            if(PushProbabilityCalculator.pushOnThisHourOfTheDay(countInformation[1], countInformation[0], countInformation[2], hourOfTheDay)) {
                timeblockList.add(timeblock);
                timeBlockStartHours[i] = hourOfTheDay;
                System.out.println("######## adding push notification for hour: " + hourOfTheDay);
            }
        }

        // convert the int [] to String for saving it in the shared preferences
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < timeBlockStartHours.length; i++) {
            str.append(timeBlockStartHours[i]).append(",");
        }

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("pushNotificationHours", str.toString());
        editor.apply();

        System.out.println("Initializing time block array list.... greatest possible length " + timeblockList.size() + " , timerange: " + this.timeRange.toString());
    }

    public static class PushProbabilityCalculator {

        // higher weight for manual app opening
        static double factorManual = 3.0;

        public static boolean pushOnThisHourOfTheDay(double accepted, double declined, double manual, double multiplier){
        //    double daytimefactor = getDayTimeFactor(hourOfTheDay);
            double result = multiplier * ((accepted + (manual*factorManual)) / (accepted + declined + (manual*factorManual)));

            // get random number between 0.0 and 1.0 to compare the probability of the notification to get pushed or not
            return (result >= Math.random());
        }

        /*
        *
        *   old settings method, will be stored in the database now, consequently not actually used
        *
        * */
        public static double getDayTimeFactor(int hourOfTheDay){

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
