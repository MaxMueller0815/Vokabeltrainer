package com.example.puppetmaster.vokabeltrainer;

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
import com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem.Vocab;
import com.google.gson.Gson;
import com.google.gson.annotations.Until;

import java.util.ArrayList;

public class UnitsActivity extends AppCompatActivity {
    private Topic topic;
    ArrayList<Unit> listOfUnits;
    private CarouselLayoutManager layoutManager;
    private int scrollPosition = 0;

    private int topicID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_units);
        readIntent();
        calcStats();
        initCarousel();
    }

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

            /*if (extras.containsKey("FINISHED_UNIT")) {
                MyDatabase db = new MyDatabase(this);
                listOfUnits = db.getTopics().get(topicID).getUnitsOfTopic();
                int finishedUnitID = intent.getIntExtra("FINISHED_UNIT", -1);
                int i = 0;
                while (i < listOfUnits.size() - 1) {
                    if (listOfUnits.get(i).getId() == finishedUnitID) {
                        layoutManager.scrollToPosition(i);
                        break;
                    }
                    i++;
                }
                calcStats();
                initCarousel();
            }*/
        }
    }

    private void calcStats() {
        int vocabsInTopic = 0;
        int practicedVocabs = 0;
        for (Unit unit : listOfUnits) {
            ArrayList<Vocab> vocabsOfUnit = unit.getVocabsOfUnit();
            vocabsInTopic = vocabsInTopic + vocabsOfUnit.size();
            for (Vocab vocab : vocabsOfUnit) {
                if(vocab.isPracticed()) {
                    practicedVocabs++;
                }
            }
        }
        TextView tvCount = (TextView) findViewById(R.id.tv_count);
        tvCount.setText("" + vocabsInTopic);
        TextView tvLearned = (TextView) findViewById(R.id.tv_learned);
        tvLearned.setText("" + practicedVocabs);
        TextView tvNumLeft = (TextView) findViewById(R.id.tv_num_left);
        tvNumLeft.setText("" + (vocabsInTopic - practicedVocabs));
        TextView tvPercent = (TextView) findViewById(R.id.tv_percent);
        tvPercent.setText((practicedVocabs*100/vocabsInTopic) + "%");
    }

    private void initCarousel() {
        layoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_units);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener(new CenterScrollListener());
        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
        recyclerView.setAdapter(new UnitAdapter(listOfUnits));
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        MyDatabase db = new MyDatabase(this);
//        listOfUnits = db.getTopics().get(topicID).getUnitsOfTopic();
//
//        int i = 0;
//        while (i < listOfUnits.size() - 1) {
//            if (listOfUnits.get(i).getId() == ) {
//                layoutManager.scrollToPosition(i);
//                break;
//            }
//            i++;
//        }
//        calcStats();
//        initCarousel();
//    }
}
