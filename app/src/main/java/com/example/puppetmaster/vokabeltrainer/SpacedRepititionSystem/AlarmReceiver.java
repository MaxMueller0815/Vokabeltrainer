package com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.puppetmaster.vokabeltrainer.Activities.ExerciseActivity;
import com.example.puppetmaster.vokabeltrainer.Activities.StartActivity;
import com.example.puppetmaster.vokabeltrainer.DatabaseCommunication.SRSDataBaseCommunicator;
import com.example.puppetmaster.vokabeltrainer.R;

import java.util.Calendar;
import java.util.HashMap;


public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        SRSDataBaseCommunicator dbCommunicator = new SRSDataBaseCommunicator(context);

        int actualTimeFrame = getActualTimeFrame();

        // get time block count information
        // structure <key, [countDeclined, countAccepted, countManual, multiplier]>
        HashMap<Integer, Double[]> timeBlockCountInfo = new HashMap<Integer, Double[]>();
        timeBlockCountInfo = dbCommunicator.getCountInformationForEveryTimeBlock();
        Double [] countInformation = timeBlockCountInfo.get(actualTimeFrame);

        //calculate whether to push a notification now or not
        boolean pushNotification = false;
        pushNotification = Notifier.PushProbabilityCalculator.pushOnThisHourOfTheDay(countInformation[1], countInformation[0], countInformation[2], countInformation[3]);

        // Toast.makeText(context, "Alarm running", Toast.LENGTH_SHORT).show();
        // TODO: Können wir das noch so machen, dass die Notification nur geschickt wird, wenn man mehr als 0 Wörter hat? Von mir aus könnten wir auch 5 Wörter als Minimum definieren, wernn das nicht alles komplizierter macht.
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int wordsWaitingForRevision = prefs.getInt("numberOfWordsInWorkload", 0);
        int hourStart = prefs.getInt("hourStart", 0);
        int hourEnd = prefs.getInt("hourEnd", 0);

        //only push the notification if actual time matches the defined time settings
        if(actualTimeFrame >= hourStart && actualTimeFrame < hourEnd && pushNotification){

            // at first add the actual time frame to the shared preferences
            addActualTimeToSharedPreferences(actualTimeFrame, prefs);

            //now build the notification
            NotificationManager notif=(NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            Notification.Builder builder = new Notification.Builder(context);
            builder.setContentTitle("lEarned push notification")
                    .setContentTitle("lEarned")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setAutoCancel(true);

            if (wordsWaitingForRevision > 0) {
                Intent revisionIntent = new Intent(context, ExerciseActivity.class);
                revisionIntent.putExtra("RETURN_TO","START_ACTIVITY");
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, revisionIntent, PendingIntent.FLAG_ONE_SHOT);
                builder.setContentIntent(pendingIntent);
                builder.setContentText(wordsWaitingForRevision + " words are currently waiting for revision.");

            } else {
                // Wenn keine Vokabeln wiederholt werden müssen, soll eine Notification geschickt werden, die anregen soll, etwas Neues zu lernen (soll die Seite mit den Topics zeigen)
                Intent learnIntent = new Intent(context, StartActivity.class);
                learnIntent.putExtra("frgToLoad", R.id.action_topics);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, learnIntent, PendingIntent.FLAG_ONE_SHOT);
                builder.setContentIntent(pendingIntent);
                builder.setContentText("Time to learn something new.");
            }

            Notification notify = builder.build();
            notify.flags |= Notification.FLAG_AUTO_CANCEL;
            notif.notify(0, notify);
        }
    }

    public int getActualTimeFrame(){
        Calendar calendar = Calendar.getInstance();

        if(calendar.get(Calendar.HOUR_OF_DAY) % 2 == 0){
            return calendar.get(Calendar.HOUR_OF_DAY);
        }else{
            return (calendar.get(Calendar.HOUR_OF_DAY) - 1);
        }
    }

    public void addActualTimeToSharedPreferences(int time, SharedPreferences prefs){
        String savedString = prefs.getString("pushNotificationHours", "100");
        String[] st = savedString.split(",");

        int[] notList = new int[st.length + 1];
        for (int i = 0; i < st.length; i++) {
            notList [i] = Integer.parseInt(st[i]);
        }

        notList[notList.length-1] = time;

        StringBuilder strb = new StringBuilder();
        for (int i = 0; i < notList.length; i++) {
            strb.append(notList[i]).append(",");
        }

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("pushNotificationHours", strb.toString());
        editor.apply();
    }
}
