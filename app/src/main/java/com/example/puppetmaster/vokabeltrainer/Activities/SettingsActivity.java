package com.example.puppetmaster.vokabeltrainer.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import com.example.puppetmaster.vokabeltrainer.DatabaseCommunication.MyDatabase;
import com.example.puppetmaster.vokabeltrainer.R;

import java.lang.reflect.Array;
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
    Switch switchArticle;
    Switch switchCapitalisation;
    private int inputRequiresArticle;
    private int inputRequiresCapitalisation;

    private SharedPreferences prefs;


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
//                Snackbar.make(view, "Settings have been saved", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });


    }

    //TODO würde ich in die startscreen activity machen
    private void loadSettings() {
        // TODO Vermutlich ist es schöner wenn man die Uhrzeiten als Int speichert
        ArrayList<Object> settings = db.getSettings();
        workload = (int) settings.get(0);
        strStart = settings.get(1).toString();
        strEnd = settings.get(2).toString();
        inputRequiresArticle = (int) settings.get(3);
        inputRequiresCapitalisation = (int) settings.get(4);
        initSpinner();
        initWorkload();
        initInputMode();

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
        int i;
        for (i = 0; i < 24; i = i + 2) {
            list.add(String.format("%02d:00", i));
        }
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStart.setAdapter(adapter);
        spinnerEnd.setAdapter(adapter);

        setSpinnerValue(spinnerStart, strStart);
        setSpinnerValue(spinnerEnd, strEnd);
    }

    private void initInputMode() {
        switchArticle = (Switch) findViewById(R.id.switch_article);
        switchCapitalisation = (Switch) findViewById(R.id.switch_case_sensitive);

        if (inputRequiresArticle == 1) {
            switchArticle.setChecked(true);
        } else {
            switchArticle.setChecked(false);
        }

        if (inputRequiresCapitalisation == 1) {
            switchCapitalisation.setChecked(true);
        } else {
            switchCapitalisation.setChecked(false);
        }
    }

    private void saveSettings() {

        this.prefs = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);

        System.out.println("####### ALTER WORKLOAD:  " + prefs.getInt("workload", 0));
        System.out.println("####### ALTER HOURSTART:  " + prefs.getInt("hourStart", 0));
        System.out.println("####### ALTER HOUREND:  " + prefs.getInt("hourEnd", 0));

        workload = Integer.parseInt(etWorkload.getText().toString());
        int start = Integer.parseInt(spinnerStart.getSelectedItem().toString().split(":")[0]);
        int end = Integer.parseInt(spinnerEnd.getSelectedItem().toString().split(":")[0]);

        db.saveSettings(workload, start, end, switchArticle.isChecked(), switchCapitalisation.isChecked());

        // save to shared preferences
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("workload", workload);
        editor.putInt("hourStart", start);
        editor.putInt("hourEnd", end);
        editor.apply();

        System.out.println("####### WORKLOAD:  " + prefs.getInt("workload", 0));
        System.out.println("####### HOURSTART:  " + prefs.getInt("hourStart", 0));
        System.out.println("####### HOUREND:  " + prefs.getInt("hourEnd", 0));

        }

    private void setSpinnerValue(Spinner spinner, String myString) {
        int index = 0;
        for(int i = 0; i < spinner.getCount(); i++){
            if(spinner.getItemAtPosition(i).toString().equals(myString)){
                spinner.setSelection(i);
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        saveSettings();
        finish();
    }
}
