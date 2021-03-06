package com.example.puppetmaster.vokabeltrainer.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.puppetmaster.vokabeltrainer.DatabaseCommunication.MyDatabase;
import com.example.puppetmaster.vokabeltrainer.R;
import com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem.SpacedRepititionSystem;
import com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem.Vocab;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SRSTesterActivity extends AppCompatActivity {
    private Vocab currentVocab;

    private EditText etLevel;
    private EditText etNextRevision;
    private EditText etLastRevision;
    private EditText etCountCorrect;
    private EditText etCountFalse;
    private long lastRevision;
    private long nextRevision;
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.ENGLISH);

    private SpacedRepititionSystem srs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.srs = new SpacedRepititionSystem(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_srstester);
        currentVocab = srs.getVocabRequest();

        showVocab();

        final Button updateBtn = (Button) findViewById(R.id.btn_update_vocab);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                currentVocab.setSrsLevel(Integer.parseInt(etLevel.getText().toString()));
                /*currentVocab.setLastRevision(lastRevision);
                currentVocab.setNextRevision(nextRevision);*/
                currentVocab.setCountCorrect(Integer.parseInt(etCountCorrect.getText().toString()));
                currentVocab.setCountFalse(Integer.parseInt(etCountFalse.getText().toString()));
                MyDatabase db = new MyDatabase(getApplicationContext());
                db.updateSingleVocab(currentVocab);
            }
        });
    }

    private void showVocab() {

        if (currentVocab == null) {

            TextView tvId = (TextView) findViewById(R.id.tv_id);
            tvId.setText("0");

            TextView tvEnglish = (TextView) findViewById(R.id.tv_english);
            tvEnglish.setText("keine Vokabel vorhanden");

            TextView tvGerman = (TextView) findViewById(R.id.tv_german);
            tvGerman.setText("keine Vokabel vorhanden");

            TextView tvLastRevision = (TextView) findViewById(R.id.tv_revision);
            tvLastRevision.setText("1");

            etLevel = (EditText) findViewById(R.id.et_level);
            etLevel.setText("keine Vokabel vorhanden");

            lastRevision = Calendar.getInstance().getTimeInMillis();
            String strLastRevision = dateFormat.format(lastRevision);
            etLastRevision = (EditText) findViewById(R.id.et_last_revision);
            etLastRevision.setText(strLastRevision);

            nextRevision = Calendar.getInstance().getTimeInMillis();
            String strNextRevision = dateFormat.format(nextRevision);
            etNextRevision = (EditText) findViewById(R.id.et_next_revision);
            etNextRevision.setText(strNextRevision);

            etCountCorrect = (EditText) findViewById(R.id.et_correct);
            etCountCorrect.setText("keine Vokabel vorhanden");

            etCountFalse = (EditText) findViewById(R.id.et_false);
            etCountFalse.setText("keine Vokabel vorhanden");


        } else {

            TextView tvId = (TextView) findViewById(R.id.tv_id);
            tvId.setText("" + currentVocab.getId());

            TextView tvEnglish = (TextView) findViewById(R.id.tv_english);
            tvEnglish.setText(currentVocab.getEnglish());

            TextView tvGerman = (TextView) findViewById(R.id.tv_german);
            tvGerman.setText(currentVocab.getGerman().get(0));

            TextView tvLastRevision = (TextView) findViewById(R.id.tv_revision);
            tvLastRevision.setText("" + currentVocab.getRevisionDifference());

            etLevel = (EditText) findViewById(R.id.et_level);
            etLevel.setText(currentVocab.getSrsLevel() + "");

            lastRevision = currentVocab.getLastRevision();
            String strLastRevision = dateFormat.format(lastRevision);
            etLastRevision = (EditText) findViewById(R.id.et_last_revision);
            etLastRevision.setText(strLastRevision);

            nextRevision = currentVocab.getNextRevision();
            String strNextRevision = dateFormat.format(nextRevision);
            etNextRevision = (EditText) findViewById(R.id.et_next_revision);
            etNextRevision.setText(strNextRevision);

            etCountCorrect = (EditText) findViewById(R.id.et_correct);
            etCountCorrect.setText(currentVocab.getCountCorrect() + "");

            etCountFalse = (EditText) findViewById(R.id.et_false);
            etCountFalse.setText(currentVocab.getCountFalse() + "");
        }
    }


}
