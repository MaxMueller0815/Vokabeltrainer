package com.example.puppetmaster.vokabeltrainer.Activities;

import android.app.AlarmManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.puppetmaster.vokabeltrainer.DatabaseCommunication.MyDatabase;
import com.example.puppetmaster.vokabeltrainer.Entities.Topic;
import com.example.puppetmaster.vokabeltrainer.Fragments.GameFragment;
import com.example.puppetmaster.vokabeltrainer.Fragments.HomeFragment;
import com.example.puppetmaster.vokabeltrainer.Fragments.ProfileFragment;
import com.example.puppetmaster.vokabeltrainer.Fragments.TopicsFragment;
import com.example.puppetmaster.vokabeltrainer.Helper.BottomNavigationViewHelper;
import com.example.puppetmaster.vokabeltrainer.R;
import com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem.PermanentAlarmReceiver;

import java.util.ArrayList;
import java.util.Calendar;

public class StartScreen extends AppCompatActivity {
    private Toolbar toolbar;

    public ArrayList<Topic> getTopics() {
        return topics;
    }

    private ArrayList<Topic> topics = new ArrayList<Topic>();
    private static FragmentManager fragmentManager;
    private boolean alreadyVisited = false;

    /*
    *   Vorschlag: beim Appstart einmal die Settings aus der Datenbank laden und nochmal in die
    *   SharedPreferences speichern, bei jedem Speichervorgang der Settings die Datenbank überschreiben
    *   dadurch kann sichergestellt werden, dass die SharedPreferences beim Appgebrauch auf dem aktuellen Stand sind?
    *
    * */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(StartScreen.this);

        if(!prefs.getBoolean("firstTime", false)){
            // Max du kannst hier deine Anweisungen für die introscreens einbauen

            Intent permanentAlarmIntent = new Intent(this, PermanentAlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, permanentAlarmIntent, 0);

            AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 1);

            manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);

            System.out.println("______setze permanenten alarm auf 00:00:01 Uhr");

            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("workload", 40);
            editor.putInt("hourStart", 10);
            editor.putInt("hourEnd", 18);
            editor.putBoolean("firstTime", true);
            editor.apply();
        }

        fragmentManager = this.getFragmentManager();
        GetTask getTask = new GetTask(getApplicationContext());
        getTask.execute();
    }

    private void initBottomNavigation() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_start:
                                Fragment homeFragment = new HomeFragment();//Get Fragment Instance
                                //homeFragment.setArguments(topicAsBundle());//Finally set argument bundle to fragment
                                fragmentManager.beginTransaction().replace(R.id.container_start, homeFragment).commit();
                                break;
                            case R.id.action_topics:
                                Fragment topicsFragment = new TopicsFragment();//Get Fragment Instance
                                //topicsFragment.setArguments(topicAsBundle());//Finally set argument bundle to fragment
                                fragmentManager.beginTransaction().replace(R.id.container_start, topicsFragment).commit();
                                break;
                            case R.id.action_game:
                                fragmentManager.beginTransaction()
                                        .replace(R.id.container_start, new GameFragment())
                                        .commit();
                                break;
                            case R.id.action_profile:
                                fragmentManager.beginTransaction()
                                        .replace(R.id.container_start, new ProfileFragment())
                                        .commit();
                                break;
                            default:
                        }
                        return true;
                    }
                });
        bottomNavigationView.setVisibility(View.VISIBLE);
    }

    public void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_start);
        setSupportActionBar(toolbar);
        toolbar.setVisibility(View.VISIBLE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                Log.d("debug", "activity: action home has clicked");
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                break;
            case R.id.menu_list_of_vocabs:
                Intent intent = new Intent(getApplicationContext(), AllVocabsActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRestart() {
        super.onRestart();
            GetTask getTask = new GetTask(getApplicationContext());
            getTask.execute();
    }

    private class GetTask extends AsyncTask<Object, Void, Void> {
        Context context;

        GetTask(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(Object... params) {
            topics = new MyDatabase(context).getTopics();
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            initToolBar();
            initBottomNavigation();
            //TODO: Richtiges Fragment aufrufen
            if (!alreadyVisited) {
                Fragment homeFragment = new HomeFragment();
                fragmentManager.beginTransaction().replace(R.id.container_start, homeFragment).commit();
                ImageView loadingImage = (ImageView) findViewById(R.id.iv_loading);
                loadingImage.setVisibility(View.GONE);
            }
            //alreadyVisited = true;
        }
    }
}
