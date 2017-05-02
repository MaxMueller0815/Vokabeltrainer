package com.example.puppetmaster.vokabeltrainer.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.puppetmaster.vokabeltrainer.Adapter.VocabAdapter;
import com.example.puppetmaster.vokabeltrainer.DatabaseCommunication.SRSDataBaseCommunicator;
import com.example.puppetmaster.vokabeltrainer.R;
import com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem.Vocab;

import java.util.ArrayList;

/**
 * Activity zeigt sämtliche Vokabeln inklusive SRS-Level an, die in der Datenbank abgespeichert sind. So kann der Nutzer seinen Fortschritt überprüfen
 */
public class AllVocabsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_vocabs);

        // Vokabeln aus DB lesen
        SRSDataBaseCommunicator dbCommunicator = new SRSDataBaseCommunicator(this);
        ArrayList<Vocab> listOfAllVocab = dbCommunicator.getAllVocab();

        // Arraylist an Adapter anbinden
        VocabAdapter adapter = new VocabAdapter(this, listOfAllVocab);
        ListView listView = (ListView) findViewById(R.id.list_all_vocabs);
        listView.setAdapter(adapter);
    }
}
