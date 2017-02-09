package com.example.puppetmaster.vokabeltrainer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.puppetmaster.vokabeltrainer.Fragments.GameFragment;
import com.example.puppetmaster.vokabeltrainer.Fragments.HomeFragment;
import com.example.puppetmaster.vokabeltrainer.Fragments.ProfileFragment;
import com.example.puppetmaster.vokabeltrainer.Fragments.TopicsFragment;

public class StartScreen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);


        getFragmentManager().beginTransaction()
                .replace(R.id.container, new HomeFragment())
                .commit();

        setUpBottomNavigation();
    }

    private void setUpBottomNavigation() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_start:
                                getFragmentManager().beginTransaction()
                                        .replace(R.id.container, new HomeFragment())
                                        .commit();
                                break;
                            case R.id.action_topics:
                                getFragmentManager().beginTransaction()
                                        .replace(R.id.container, new TopicsFragment())
                                        .commit();
                                break;
                            case R.id.action_game:
                                getFragmentManager().beginTransaction()
                                        .replace(R.id.container, new GameFragment())
                                        .commit();
                                break;
                            case R.id.action_profile:
                                getFragmentManager().beginTransaction()
                                        .replace(R.id.container, new ProfileFragment())
                                        .commit();
                                break;
                        }
                        return true;
                    }
                });
    }
}
