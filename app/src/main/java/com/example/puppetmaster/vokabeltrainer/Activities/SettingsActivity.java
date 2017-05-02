package com.example.puppetmaster.vokabeltrainer.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import com.example.puppetmaster.vokabeltrainer.DatabaseCommunication.MyDatabase;
import com.example.puppetmaster.vokabeltrainer.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity zeigt Einstellungsoptionen an
 */
public class SettingsActivity extends AppCompatActivity {
    private MyDatabase db;
    private SharedPreferences prefs;


    private final int TIMESLOT_DURATION = 2;
    private int startTime;
    private int endTime;
    private int workload;
    // Int als Ersatz für Booleans, die von SQLite nicht unterstützt werden
    private int inputRequiresArticle;
    private int inputRequiresCapitalisation;


    // UI Elemente
    private Spinner spinnerStart;
    private Spinner spinnerEnd;
    private EditText etWorkload;
    private Switch switchArticle;
    private Switch switchCapitalisation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        // TODO: Die DB-Anbindung braucht man gar nicht mehr,
        db = new MyDatabase(this);
        loadSettings();
        setupUI();
    }

    /**
     * Einstellungen werden aus der Datenbank gelesen
     */
    private void loadSettings() {
        ArrayList<Object> settings = db.getSettings();
        workload = (int) settings.get(0);
        startTime = (int) settings.get(1);
        endTime = (int) settings.get(2) + TIMESLOT_DURATION;
        inputRequiresArticle = (int) settings.get(3);
        inputRequiresCapitalisation = (int) settings.get(4);
    }

    /**
     * UI wird aufgebaut
     */
    private void setupUI() {
        // Workload
        etWorkload = (EditText) findViewById(R.id.et_workload);
        etWorkload.setText(workload + "");

        //Spinner
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

        //Aktueller Wert als Auswahl setzen
        setSpinnerValue(spinnerStart, startTime);
        setSpinnerValue(spinnerEnd, endTime);

        // Eingabe erfordert Artikel / korrekte Groß-/Kleinschriebung
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

    /**
     * Wählt den momentanen Wert im Spinner aus
     * @param spinner Spinner, bei dem der momentane Wert (hour) gesetzt werden soll
     * @param hour Die Stunde, die als Selection angezeigt werden soll
     */
    private void setSpinnerValue(Spinner spinner, int hour) {
        int index = 0;
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equals(String.format("%02d:00", hour))) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    /**
     * Liest Einstellungsfelder aus und speichert sie in den SharedPreferences und der DB gespeichert.
     */
    private void saveSettings() {
        this.prefs = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);

        System.out.println("####### ALTER WORKLOAD:  " + prefs.getInt("workload", 0));
        System.out.println("####### ALTER HOURSTART:  " + prefs.getInt("hourStart", 0));
        System.out.println("####### ALTER HOUREND:  " + prefs.getInt("hourEnd", 0));

        workload = Integer.parseInt(etWorkload.getText().toString());
        int start = Integer.parseInt(spinnerStart.getSelectedItem().toString().split(":")[0]);
        int end = Integer.parseInt(spinnerEnd.getSelectedItem().toString().split(":")[0]) - TIMESLOT_DURATION;

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


    /**
     * Einstellungen werden beim Drücken des Zurück-Buttons gespeichert
     */
    @Override
    public void onBackPressed() {
        saveSettings();
        finish();
    }
}