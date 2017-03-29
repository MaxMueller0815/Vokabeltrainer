package com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.example.puppetmaster.vokabeltrainer.R;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

      //  Toast.makeText(context, "Alarm running", Toast.LENGTH_SHORT).show();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int wordsWaitingForRevision = prefs.getInt("numberOfWordsInWorkload", 0);

        NotificationManager notif=(NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        Notification notify=new Notification.Builder
                (context).setContentTitle("lEarned push notification").setContentText(wordsWaitingForRevision + " words are currently waiting for revision.").
                setContentTitle("lEarned").setSmallIcon(R.drawable.ic_launcher).build();

        notify.flags |= Notification.FLAG_AUTO_CANCEL;
        notif.notify(0, notify);
    }

}
