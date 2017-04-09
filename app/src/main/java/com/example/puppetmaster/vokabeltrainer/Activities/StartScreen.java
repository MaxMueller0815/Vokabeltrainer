package com.example.puppetmaster.vokabeltrainer.Activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

import java.util.ArrayList;

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
