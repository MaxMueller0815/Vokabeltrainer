package com.example.puppetmaster.vokabeltrainer;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class StartScreen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        Button btnShowNext = (Button) findViewById(R.id.bt_switch_to_topics);
        btnShowNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent itn = new Intent(v.getContext(), TopicsActivity.class);
                v.getContext().startActivity(itn);
            }
        });

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_start:
                                //fragment = new FavouriteFragment();
                                break;
                            case R.id.action_vocab:

                                break;
                            case R.id.action_game:
                                break;
                            case R.id.action_profile:
                                break;
                        }
                        //final FragmentTransaction transaction = fragmentManager.beginTransaction();
                        //transaction.replace(R.id.main_container, fragment).commit();
                        return true;
                    }
                });
    }
    }
}
