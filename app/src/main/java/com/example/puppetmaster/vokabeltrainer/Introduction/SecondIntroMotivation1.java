package com.example.puppetmaster.vokabeltrainer.Introduction;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import com.example.puppetmaster.vokabeltrainer.R;



public class SecondIntroMotivation1 extends AppCompatActivity {

    private Button buttonNext, buttonPrevious;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.introduction_second_motivation1);

        buttonNext = (Button) findViewById(R.id.buttonNext);
        buttonPrevious = (Button) findViewById(R.id.buttonBack);


        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ThirdMotivationText.class);
                startActivity(i);
                finish();

            }
        });

        buttonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), FirstName.class);
                startActivity(i);
                finish();
            }
        });
    }



}
