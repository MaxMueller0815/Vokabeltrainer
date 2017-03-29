package com.example.puppetmaster.vokabeltrainer.Activities;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import com.example.puppetmaster.vokabeltrainer.Entities.Unit;
import com.example.puppetmaster.vokabeltrainer.Fragments.ExerciseFinalScoreFragment;
import com.example.puppetmaster.vokabeltrainer.Fragments.ExerciseInputFragment;
import com.example.puppetmaster.vokabeltrainer.Fragments.ExerciseLearnFragment;
import com.example.puppetmaster.vokabeltrainer.R;
import com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem.SpacedRepititionSystem;
import com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem.Vocab;
import com.google.gson.Gson;

import java.util.ArrayList;

public class ExerciseActivity extends AppCompatActivity {
    private ArrayList<Vocab> allVocabs = new ArrayList<>();
    private ArrayList<Vocab> newVocabs = new ArrayList<>();
    private static FragmentManager fragmentManager;
    private int turn = 0;
    private int counterCorrect = 0;
    public SpacedRepititionSystem srs;
    private FragmentTransaction ft;

    public SpacedRepititionSystem getSrs() {
        return srs;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        srs = new SpacedRepititionSystem(this);
        fragmentManager = getFragmentManager();
        readIntent();
        startNextTurn();

    }

    private void readIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra("SELECTED_UNIT")) { //Null Checking
            Gson gson = new Gson();
            Unit unit = gson.fromJson(intent.getStringExtra("SELECTED_UNIT"), Unit.class);
            for (Vocab vocab : unit.getVocabsOfUnit()) {
                if (vocab.getSrsLevel() == 0) {
                    newVocabs.add(vocab);
                } else {
                    allVocabs.add(vocab);
                }
            }
        } else {
            allVocabs.add(srs.getVocabRequest());
        }
    }

    private void startNextTurn() {
        ft = fragmentManager.beginTransaction();
        ft.setCustomAnimations(R.animator.flight_left_in, R.animator.flip_right_out);

        if (newVocabs.size() > 0) {
            ft.replace(R.id.container_exercise, new ExerciseLearnFragment()).commit();
        } else if(turn < allVocabs.size()) {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.pb_exercise);
            progressBar.setMax(allVocabs.size() + newVocabs.size());
            progressBar.setProgress(turn + 1);
            ft.replace(R.id.container_exercise, new ExerciseInputFragment()).commit();
        } else {
            ft.replace(R.id.container_exercise, new ExerciseFinalScoreFragment()).commit();
        }
    }

    public void evaluateResult(boolean isCorrect) {
        if (isCorrect) {
            counterCorrect++;
        }
        turn++;
        startNextTurn();
    }

    public Vocab getCurrentVocab() {
        if (newVocabs.size() > 0) {
            return newVocabs.get(turn);
        } else if (allVocabs.size() > 0){
            return allVocabs.get(turn);
        } else {
            return null;
        }
    }

    public int getCounterCorrect() {
        return counterCorrect;
    }

    public ArrayList<Vocab> getAllVocab() {
        return allVocabs;
    }

    @Override
    public void onBackPressed() {
        showAlertDialogExit();
    }

    private void showAlertDialogExit() {
        // Setup View for AlertDialog
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // Dialog cancelable with back key
        alertDialogBuilder.setCancelable(true);

        // Setup title and message of alertDialog
        alertDialogBuilder.setIcon(R.drawable.ic_exit);
        alertDialogBuilder.setTitle(R.string.exit_title);
        alertDialogBuilder.setMessage(R.string.exit_message);

        // Setup Buttons for dialog
        alertDialogBuilder.setPositiveButton(R.string.exit_title, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        alertDialogBuilder.setNegativeButton(R.string.language_negative_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    public void markAsLearned() {

        if (newVocabs.size() > 0) {
            allVocabs.add(newVocabs.remove(0));
        }
        startNextTurn();
    }
}
