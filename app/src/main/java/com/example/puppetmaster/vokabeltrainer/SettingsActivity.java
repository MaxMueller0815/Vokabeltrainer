package com.example.puppetmaster.vokabeltrainer;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {
    private Spinner spinnerStart;
    private Spinner spinnerEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        spinnerStart = (Spinner) findViewById(R.id.spinner_start);
        spinnerEnd = (Spinner) findViewById(R.id.spinner_end);
        ArrayAdapter<String> adapter;
        List<String> list = new ArrayList<String>();
        list.add("00:00");
        list.add("02:00");
        list.add("04:00");
        list.add("06:00");
        list.add("08:00");
        list.add("10:00");
        list.add("12:00");
        list.add("14:00");
        list.add("16:00");
        list.add("18:00");
        list.add("20:00");
        list.add("22:00");
        list.add("24:00");
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStart.setAdapter(adapter);
        spinnerEnd.setAdapter(adapter);
    }
}
