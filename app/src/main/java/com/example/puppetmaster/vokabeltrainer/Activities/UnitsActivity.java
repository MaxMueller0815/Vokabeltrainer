package com.example.puppetmaster.vokabeltrainer.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;
import com.example.puppetmaster.vokabeltrainer.Adapter.UnitAdapter;
import com.example.puppetmaster.vokabeltrainer.DatabaseCommunication.MyDatabase;
import com.example.puppetmaster.vokabeltrainer.Entities.Topic;
import com.example.puppetmaster.vokabeltrainer.Entities.Unit;
import com.example.puppetmaster.vokabeltrainer.R;
import com.google.gson.Gson;

import java.util.ArrayList;

public class UnitsActivity extends AppCompatActivity {
    private Topic topic;
    ArrayList<Unit> listOfUnits;
    private CarouselLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private boolean datasetChanged;
    private int topicID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_units);
        readIntent();
        calcStats();
        setupCarousel();
        setTitle(topic.getTitle());
    }

    /**
     * Lädt ausgewähltes Topic aus Intent
     */
    private void readIntent() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey("SELECTED_TOPIC")) {
                Gson gson = new Gson();
                topic = gson.fromJson(intent.getStringExtra("SELECTED_TOPIC"), Topic.class);
                topicID = topic.getId();
                listOfUnits = topic.getUnitsOfTopic();
            }
        }
    }

    /**
     * Berechnet die Statistik, die am oberen Bildrand angezeigt wird
     */
    private void calcStats() {
        int vocabsInTopic = topic.getNumOfAllVocabs();
        int practicedVocabs = topic.getNumOfLearnedVocabs();

        //Anzahl der Vokabeln in Topic
        TextView tvCount = (TextView) findViewById(R.id.tv_count);
        tvCount.setText("" + vocabsInTopic);

        //Anzahl der bereits gelernten Vokabeln in Topic (SRS > 0)
        TextView tvLearned = (TextView) findViewById(R.id.tv_learned);
        tvLearned.setText("" + practicedVocabs);

        //Anzahl der noch ausstehenden Vokabeln in Topic (SRS = 0)
        TextView tvNumLeft = (TextView) findViewById(R.id.tv_num_left);
        tvNumLeft.setText("" + (vocabsInTopic - practicedVocabs));

        // Prozentsatz
        TextView tvPercent = (TextView) findViewById(R.id.tv_percent);
        tvPercent.setText((practicedVocabs*100/vocabsInTopic) + "%");
    }

    /**
     * Generiert Kartenansicht für jede Unit eines Topics, durch die der Nutzer swipen kann
     */
    private void setupCarousel() {
        layoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_units);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener(new CenterScrollListener());
        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
        recyclerView.setAdapter(new UnitAdapter(listOfUnits, topicID));
    }

    /**
     * Aktualisiert Daten, nachdem man bspw. eine Unit komplett gelernt hat und dann zur Übersicht zurückkehrt
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        MyDatabase db = new MyDatabase(this);
        Log.d("Topic", topicID + "");
        topic = db.getTopics().get(topicID - 1);
        listOfUnits = topic.getUnitsOfTopic();
        calcStats();
        recyclerView.setAdapter(new UnitAdapter(listOfUnits, topicID));
        datasetChanged = true;
    }

    /**
     * onBackPressed muss überschrieben werden, damit voriges Fragement aktualsiert wird
     */
    @Override
    public void onBackPressed() {
        if (datasetChanged) {
            Intent topicsIntent = new Intent(getApplicationContext(), StartActivity.class);
            topicsIntent.putExtra("frgToLoad", R.id.action_topics);
            startActivity(topicsIntent);
        } else {
            finish();
        }

    }
}