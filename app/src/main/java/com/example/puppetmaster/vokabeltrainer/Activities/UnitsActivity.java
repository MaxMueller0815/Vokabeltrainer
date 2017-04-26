package com.example.puppetmaster.vokabeltrainer.Activities;

import android.app.Fragment;
import android.app.FragmentManager;
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
import com.example.puppetmaster.vokabeltrainer.Fragments.TopicsFragment;
import com.example.puppetmaster.vokabeltrainer.R;
import com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem.Vocab;
import com.google.gson.Gson;

import java.util.ArrayList;

public class UnitsActivity extends AppCompatActivity {
    private Topic topic;
    ArrayList<Unit> listOfUnits;
    private CarouselLayoutManager layoutManager;
    private RecyclerView recyclerView;

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
        }
    }

    private void calcStats() {
        int vocabsInTopic = topic.getNumOfAllVocabs();
        int practicedVocabs = topic.getNumOfLearnedVocabs();

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
        recyclerView = (RecyclerView) findViewById(R.id.recycler_units);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener(new CenterScrollListener());
        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
        recyclerView.setAdapter(new UnitAdapter(listOfUnits, topicID));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        MyDatabase db = new MyDatabase(this);
        Log.d("Topic", topicID + "");
        topic = db.getTopics().get(topicID - 1);
        listOfUnits = topic.getUnitsOfTopic();
        calcStats();
        recyclerView.setAdapter(new UnitAdapter(listOfUnits, topicID));
    }

//    @Override
//    public void onBackPressed() {
//        Intent topicsIntent = new Intent(getApplicationContext(), StartScreen.class);
//        topicsIntent.putExtra("frgToLoad", R.id.action_topics);
//        startActivity(topicsIntent);
//    }
}