package com.example.puppetmaster.vokabeltrainer.Introduction;

import android.content.SharedPreferences;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import com.example.puppetmaster.vokabeltrainer.Activities.StartScreen;
import com.example.puppetmaster.vokabeltrainer.R;


public class FirstName extends AppCompatActivity {

    private SharedPreferences prefs;
    private Button buttonStart;
    private int startIntro = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.introduction_first_name);

        initLayout();
        initClickListener();
        checkPrefs();
        setPrefsStart();

    }

    private void initLayout() {
        buttonStart = (Button) findViewById(R.id.buttonStartIntro);

    }

    private void initClickListener() {
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SecondIntroMotivation1.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void checkPrefs(){

        this.prefs = PreferenceManager.getDefaultSharedPreferences(FirstName.this);

        int introStarted = prefs.getInt("startedIntro",0);

        if (introStarted == 1) {
            Intent i = new Intent(getApplicationContext(), StartScreen.class);
            startActivity(i);
            finish();
        }

        System.out.println("### speichere  " + prefs.getInt("startedIntro",0));

    }

    private void setPrefsStart(){

        SharedPreferences.Editor editor = prefs.edit();

        editor.putInt("startedIntro", startIntro);

        editor.apply();

        System.out.println("####### gespeichert:  " + prefs.getInt("startedIntro",0));
    }
}
