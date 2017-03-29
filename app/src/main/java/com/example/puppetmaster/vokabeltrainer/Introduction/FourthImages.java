package com.example.puppetmaster.vokabeltrainer.Introduction;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import com.example.puppetmaster.vokabeltrainer.R;


public class FourthImages extends AppCompatActivity {

    private Button buttonNext, buttonPrevious, buttonAddImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.introduction_fourth_images);

        assignLayoutElements();
        attachListener();

    }

    private void assignLayoutElements() {
        buttonAddImage = (Button) findViewById(R.id.buttonAddImage);
        buttonNext = (Button) findViewById(R.id.buttonNext);
        buttonPrevious = (Button) findViewById(R.id.buttonBack);
    }

    private void attachListener () {
        buttonAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do stuff
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), FifthSettings.class);
                startActivity(i);
                finish();
            }
        });

        buttonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ThirdMotivationText.class);
                startActivity(i);
                finish();
            }
        });
    }



}
