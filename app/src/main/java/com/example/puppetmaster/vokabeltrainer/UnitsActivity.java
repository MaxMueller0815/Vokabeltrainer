package com.example.puppetmaster.vokabeltrainer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;
import com.example.puppetmaster.vokabeltrainer.Adapter.UnitAdapter;
import com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem.Vocab;
import com.google.gson.Gson;

import java.util.ArrayList;

public class UnitsActivity extends AppCompatActivity {
    ArrayList<Unit> listOfUnits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_units);
        readIntent();
        calcStats();
        initCarousel();
    }

    private void initCarousel() {
        final CarouselLayoutManager layoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_units);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener(new CenterScrollListener());
        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
        recyclerView.setAdapter(new UnitAdapter(listOfUnits));
    }

    private void readIntent() {
        Intent intent = getIntent();
        if (null != intent) { //Null Checking
            Gson gson = new Gson();
            Topic topic = gson.fromJson(intent.getStringExtra("SELECTED_TOPIC"), Topic.class);
            listOfUnits = topic.getUnitsOfTopic();
        }
    }

    private void calcStats() {
        int vocabsInTopic = 0;
        int practicedVocabs = 0;
        for (Unit unit : listOfUnits) {
            ArrayList<Vocab> vocabsOfUnit = unit.getVocabsOfUnit();
            vocabsInTopic = vocabsOfUnit.size();
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
        tvPercent.setText((practicedVocabs/vocabsInTopic) + "%");
    }
}
