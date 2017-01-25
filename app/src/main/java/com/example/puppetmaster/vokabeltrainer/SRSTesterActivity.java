package com.example.puppetmaster.vokabeltrainer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem.SpacedRepititionSystem;
import com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem.Vocab;

public class SRSTesterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_srstester);
        showVocab();
    }

    private void showVocab() {
        SpacedRepititionSystem srs = new SpacedRepititionSystem(this);
        Vocab currentVocab = srs.getVocabRequest();
        TextView tvId = (TextView) findViewById(R.id.tv_id);
        tvId.setText("ID: " + currentVocab.getId());

        TextView tvEnglish = (TextView) findViewById(R.id.tv_english);
        tvEnglish.setText("Englisch: " + currentVocab.getEnglish());

        TextView tvGerman = (TextView) findViewById(R.id.tv_german);
        tvGerman.setText("German: " + currentVocab.getGerman());

        TextView tvLevel = (TextView) findViewById(R.id.tv_level);
        tvLevel.setText("Level: " + currentVocab.getSrsLevel());

        TextView tvLastRevision = (TextView) findViewById(R.id.tv_revision);
        tvLastRevision.setText("Revision Difference: " + currentVocab.getRevisionDifference());
    }


}
