package com.example.puppetmaster.vokabeltrainer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

    }
}
