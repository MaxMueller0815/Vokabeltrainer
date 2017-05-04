package com.example.puppetmaster.vokabeltrainer.Activities;

import android.app.AlarmManager;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.puppetmaster.vokabeltrainer.DatabaseCommunication.MyDatabase;
import com.example.puppetmaster.vokabeltrainer.DatabaseCommunication.SRSDataBaseCommunicator;
import com.example.puppetmaster.vokabeltrainer.Entities.Topic;
import com.example.puppetmaster.vokabeltrainer.Fragments.GameFragment;
import com.example.puppetmaster.vokabeltrainer.Fragments.HomeFragment;
import com.example.puppetmaster.vokabeltrainer.Fragments.ProfileFragment;
import com.example.puppetmaster.vokabeltrainer.Fragments.TopicsFragment;
import com.example.puppetmaster.vokabeltrainer.Helper.BottomNavigationViewHelper;
import com.example.puppetmaster.vokabeltrainer.R;
import com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem.Notifier;
import com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem.PermanentAlarmReceiver;
import com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem.SpacedRepititionSystem;

import java.util.ArrayList;
import java.util.Calendar;

import static com.example.puppetmaster.vokabeltrainer.R.string.bnb_topics;

public class StartActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;
    private ArrayList<Topic> topics = new ArrayList<Topic>();
    private static FragmentManager fragmentManager;

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

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(StartActivity.this);

        if(!prefs.getBoolean("firstTime", false)){
            // Max du kannst hier deine Anweisungen für die introscreens einbauen

            Intent permanentAlarmIntent = new Intent(this, PermanentAlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, permanentAlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 1);

            manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);

            System.out.println("______setze permanenten alarm auf 00:00:01 Uhr");

            Notifier notifier = new Notifier(this, new SRSDataBaseCommunicator(this));

            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("workload", 40);
            editor.putInt("hourStart", 10);
            editor.putInt("hourEnd", 18);
            editor.putInt("alarmIntentCount", 1);
            editor.putInt("numberOfAlarmsSet", 0);
            editor.putBoolean("firstTime", true);
            editor.apply();
        }

        GetTask getTask = new GetTask(getApplicationContext());
        getTask.execute();
        fragmentManager = this.getFragmentManager();
    }

    /**
     * Versieht BottomNavigation mit OnClickListener
     */
    private void setupBottomNavigation() {
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        // Verhindert, dass Icons sich in der Toolbar bewegen, wenn mehr als 3 Elemente vorhanden sind
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        //OnClickListener
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_start:
                                fragmentManager.beginTransaction().replace(R.id.container_start, new HomeFragment()).addToBackStack("HOME_FRAGMENT").commit();
                                toolbar.setTitle(R.string.app_name);
                                break;
                            case R.id.action_topics:
                                fragmentManager.beginTransaction().replace(R.id.container_start, new TopicsFragment()).addToBackStack("TOPICS_FRAGMENT").commit();
                                toolbar.setTitle(R.string.bnb_topics);
                                break;
                            case R.id.action_game:
                                fragmentManager.beginTransaction().replace(R.id.container_start, new GameFragment()).commit();
                                toolbar.setTitle(R.string.bnb_game);
                                break;
                            case R.id.action_profile:
                                fragmentManager.beginTransaction().replace(R.id.container_start, new ProfileFragment()).commit();
                                toolbar.setTitle(R.string.bnb_profile);
                                break;
                            default:
                        }
                        return true;
                    }
                });
        bottomNavigationView.setVisibility(View.VISIBLE);
    }

    /**
     * Funktion ermöglicht es, das richtige Fragment aus einer anderen Klasse aufzurufen mithilfe eines Intent-Extras ("frgToLoad")
     * @param intentFragment Sollte den R.id.xy-Werten der Elementen der BottomNavigationBar entsprechen, wie bspw. R.id.action_start
     */
    public void loadFragment(int intentFragment) {
            switch (intentFragment) {
                case R.id.action_start:
                    bottomNavigationView.setSelectedItemId(R.id.action_start);
                    break;
                case R.id.action_topics:
                    bottomNavigationView.setSelectedItemId(R.id.action_topics);
                    break;
                case R.id.action_game:
                    bottomNavigationView.setSelectedItemId(R.id.action_game);
                    break;
                case R.id.action_profile:
                    bottomNavigationView.setSelectedItemId(R.id.action_profile);
                    break;
                default:
                    bottomNavigationView.setSelectedItemId(R.id.action_topics);
                    break;
            }
    }


    // Eigene Elemente in Toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_start, menu);
        return true;
    }

    // OnClickListener für die Toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                break;
            case R.id.menu_list_of_vocabs:
                Intent intent = new Intent(getApplicationContext(), AllVocabsActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Getter, der im HomeFragment benötigt wird
     * @return Liste aller Topics. Die Topics beinhalten Units, die wiederum Vokabeln enthalten
     */
    public ArrayList<Topic> getTopics() {
        return topics;
    }

//    @Override
//    public void onRestart() {
//        super.onRestart();
//            GetTask getTask = new GetTask(getApplicationContext());
//            getTask.execute();
//    }

    /**
     * Liest Topics, Units und Vokabeln aus Datenbank
     */
    private class GetTask extends AsyncTask<Object, Void, Void> {
        Context context;

        GetTask(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(Object... params) {
            // Die Topics beinhalten Units, die wiederum Vokabeln enthalten
            topics = new MyDatabase(context).getTopics();
            SpacedRepititionSystem srs = new SpacedRepititionSystem(StartActivity.this);
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);


            // Toolbar
            toolbar = (Toolbar) findViewById(R.id.toolbar_start);
            setSupportActionBar(toolbar);
            toolbar.setVisibility(View.VISIBLE);


            setupBottomNavigation();

            //Splash Screen verstecken
            ImageView loadingImage = (ImageView) findViewById(R.id.iv_loading);
            loadingImage.setVisibility(View.GONE);

            if (getIntent().hasExtra("frgToLoad")) {
                Toast.makeText(context, "Updating stats…", Toast.LENGTH_SHORT).show();
                loadFragment(getIntent().getExtras().getInt("frgToLoad"));
            } else {
                loadFragment(R.id.action_start);
            }
        }
    }
}
