package com.example.puppetmaster.vokabeltrainer;

import android.content.Context;
import android.database.SQLException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.puppetmaster.vokabeltrainer.DatabaseCommunication.DataBaseHelper;

import java.io.IOException;

public class StartScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        DataBaseHelper myDbHelper = new DataBaseHelper(this);
        myDbHelper = new DataBaseHelper(this);

        try {

            myDbHelper.createDataBase();

        } catch (IOException ioe) {

            throw new Error("Unable to create database");

        }

        try {

            myDbHelper.openDataBase();

        }catch(SQLException sqle){

            throw sqle;

        }
    }
}
