package com.example.puppetmaster.vokabeltrainer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.puppetmaster.vokabeltrainer.Adapter.VocabAdapter;
import com.example.puppetmaster.vokabeltrainer.DatabaseCommunication.MyDatabase;
import com.example.puppetmaster.vokabeltrainer.DatabaseCommunication.SRSDataBaseCommunicator;
import com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem.Vocab;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AllVocabsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_vocabs);
        SRSDataBaseCommunicator dbCommunicator = new SRSDataBaseCommunicator(this);
        ArrayList<Vocab> listOfAllVocab = dbCommunicator.getAllVocab();
        VocabAdapter adapter = new VocabAdapter(this, listOfAllVocab);
        ListView listView = (ListView) findViewById(R.id.list_all_vocabs);
        listView.setAdapter(adapter);
    }
}
