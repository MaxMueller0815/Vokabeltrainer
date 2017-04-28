package com.example.puppetmaster.vokabeltrainer.Introduction;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.puppetmaster.vokabeltrainer.Activities.StartScreen;
import com.example.puppetmaster.vokabeltrainer.services.LocalStore;
import com.example.puppetmaster.vokabeltrainer.R;


public class FirstName extends AppCompatActivity {

    private SharedPreferences prefs;
    private Button buttonStart;
    private EditText editTextName;
    private int startIntro = 0;

    private LocalStore localStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.introduction_first_name);

        initLayout();
        initClickListener();
        startServices();
        checkPrefs();
        setUserName();

    }

    private void initLayout() {
        buttonStart = (Button) findViewById(R.id.buttonStartIntro);
        editTextName = (EditText) findViewById(R.id.editTextName);
    }

    private void initClickListener() {
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkName()) {
                    Intent i = new Intent(getApplicationContext(), SecondIntroMotivation1.class);
                    startActivity(i);
                    finish();
                }

            }
        });
    }

    private void checkPrefs(){

        this.prefs = PreferenceManager.getDefaultSharedPreferences(FirstName.this);

        int introStarted = prefs.getInt("startedIntro",0);

        if (introStarted == 1) {
            Intent i = new Intent(getApplicationContext(), StartScreen.class);
            startActivity(i);
            finish();
        }

    }

    private void setUserName() {
        if (localStore.loadName()!= null) {
            String userName = localStore.loadName();
            Log.d("intro","##### loaded name: " + userName);
            editTextName.setText(userName);
        }

    }


    private boolean checkName() {
        String userName = editTextName.getText().toString();
        if (userName.length() > 0) {

            // save to shared preferences via localStore class
            localStore.saveName(userName);
            Log.d("intro","#####saved name: " + userName);


            return true;
        }
        else {
            sendToast();
            return false;
        }
    }

    private void sendToast() {
        Context context = getApplicationContext();
        CharSequence text = "Please enter your name.";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    private void startServices (){
        localStore = new LocalStore(getApplication());
    }
}
