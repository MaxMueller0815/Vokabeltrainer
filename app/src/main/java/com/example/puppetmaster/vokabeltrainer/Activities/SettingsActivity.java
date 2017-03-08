package com.example.puppetmaster.vokabeltrainer.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.puppetmaster.vokabeltrainer.DatabaseCommunication.MyDatabase;
import com.example.puppetmaster.vokabeltrainer.R;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {
    private MyDatabase db;
    private Spinner spinnerStart;
    private String strStart;
    private Spinner spinnerEnd;
    private String strEnd;
    private EditText etWorkload;
    private int workload;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        db = new MyDatabase(this);
        loadSettings();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSettings();
                Snackbar.make(view, "Settings have been saved", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


    }

    private void loadSettings() {
        // TODO Vermutlich ist es schöner wenn man die Uhrzeiten als Int speichert
        ArrayList<Object> settings = db.getSettings();
        workload = (int) settings.get(0);
        strStart = settings.get(1).toString();
        strEnd = settings.get(2).toString();
        initSpinner();
        initWorkload();
    }

    private void initWorkload() {
        etWorkload = (EditText) findViewById(R.id.et_workload);
        etWorkload.setText(workload + "");
    }

    private void initSpinner() {
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

        setSpinnerValue(spinnerStart, strStart);
        setSpinnerValue(spinnerEnd, strEnd);
    }

    private void saveSettings() {
        workload = Integer.parseInt(etWorkload.getText().toString());
        strStart = spinnerStart.getSelectedItem().toString();
        strEnd = spinnerEnd.getSelectedItem().toString();
        db.saveSettings(workload, strStart, strEnd);
        }

    private void setSpinnerValue(Spinner spinner, String myString)
    {
        int index = 0;
        for(int i = 0; i < spinner.getCount(); i++){
            if(spinner.getItemAtPosition(i).toString().equals(myString)){
                spinner.setSelection(i);
                break;
            }
        }
    }
}
