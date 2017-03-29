package com.example.puppetmaster.vokabeltrainer.Introduction;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import com.example.puppetmaster.vokabeltrainer.R;


public class FirstName extends AppCompatActivity {

    private Button buttonStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.introduction_first_name);

        buttonStart = (Button) findViewById(R.id.buttonStartIntro);

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SecondIntroMotivation1.class);
                startActivity(i);
                finish();
            }
        });
    }



}
