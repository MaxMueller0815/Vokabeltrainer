package com.example.puppetmaster.vokabeltrainer;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.puppetmaster.vokabeltrainer.DatabaseCommunication.MyDatabase;

import java.io.IOException;


public class StartScreen extends AppCompatActivity {
    private Cursor testCursor;
    private MyDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        db = new MyDatabase(this);

        String listString = db.getTopics();
        TextView myTopics = (TextView) findViewById(R.id.tv_topics);
        myTopics.setText(listString);
    }
}
