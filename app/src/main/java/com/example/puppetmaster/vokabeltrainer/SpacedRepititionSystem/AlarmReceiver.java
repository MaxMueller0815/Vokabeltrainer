package com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.puppetmaster.vokabeltrainer.Activities.ExerciseActivity;
import com.example.puppetmaster.vokabeltrainer.R;


public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        // Toast.makeText(context, "Alarm running", Toast.LENGTH_SHORT).show();
        // TODO: Können wir das noch so machen, dass die Notification nur geschickt wird, wenn man mehr als 0 Wörter hat? Von mir aus könnten wir auch 5 Wörter als Minimum definieren, wernn das nicht alles komplizierter macht.
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int wordsWaitingForRevision = prefs.getInt("numberOfWordsInWorkload", 0);


        NotificationManager notif=(NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle("lEarned push notification")
                .setContentTitle("lEarned")
                .setSmallIcon(R.drawable.ic_launcher);

        if (wordsWaitingForRevision > 1) {
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, ExerciseActivity.class), PendingIntent.FLAG_ONE_SHOT);
            builder.setContentIntent(pendingIntent);
            builder.setContentText(wordsWaitingForRevision + " words are currently waiting for revision.");

        } else {
            // @Flo: Erklärung meiner Absicht: wenn keine Vokabeln wiederholt werden müssen, soll eine Notification geschickt werden, die anregen soll, etwas Neues zu lernen (soll die Seite mit den Topics zeigen)
            //TODO: Richtiges Fragment aufrufen: http://stackoverflow.com/a/36064344
            Intent exerciseIntent = new Intent(context, ExerciseActivity.class);
            //Wird immer und immer wieder aufgerufen…aber warum?
            //exerciseIntent.putExtra("WORD_LIST", new SpacedRepititionSystem(context).pickListOfNewWords());
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, exerciseIntent, PendingIntent.FLAG_ONE_SHOT);
            builder.setContentIntent(pendingIntent);
            builder.setContentText("Time to learn something new.");
        }

        Notification notify = builder.build();
        notify.flags |= Notification.FLAG_AUTO_CANCEL;
        notif.notify(0, notify);



    }

}
