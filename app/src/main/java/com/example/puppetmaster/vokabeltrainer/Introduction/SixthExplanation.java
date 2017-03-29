package com.example.puppetmaster.vokabeltrainer.Introduction;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.puppetmaster.vokabeltrainer.Activities.StartScreen;
import com.example.puppetmaster.vokabeltrainer.R;


public class SixthExplanation extends AppCompatActivity {

    private Button buttonNext, buttonPrevious;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.introduction_sixth_explanation);

        assignLayoutElements();
        attachListener();

    }

    private void assignLayoutElements() {
        buttonNext = (Button) findViewById(R.id.buttonNext);
        buttonPrevious = (Button) findViewById(R.id.buttonBack);
    }

    private void attachListener () {


        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), StartScreen.class);
                startActivity(i);
                finish();
            }
        });

        buttonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), FifthSettings.class);
                startActivity(i);
                finish();
            }
        });
    }



}
