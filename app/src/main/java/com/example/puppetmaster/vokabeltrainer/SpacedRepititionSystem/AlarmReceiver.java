package com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        //TODO random number generieren

        // TODO zu push notification ändern
        Toast.makeText(context, "Alarm running", Toast.LENGTH_SHORT).show();
    }

}
