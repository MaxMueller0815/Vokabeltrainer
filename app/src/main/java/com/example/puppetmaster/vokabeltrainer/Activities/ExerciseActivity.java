package com.example.puppetmaster.vokabeltrainer.Activities;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
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
    private ArrayList<Vocab> learnedVocabs = new ArrayList<>();
    private ArrayList<Vocab> newVocabs = new ArrayList<>();
    private Vocab currentVocab;
    private boolean fromSRS;
    private static FragmentManager fragmentManager;
    private int turn = 0;
    final int MAX_TURNS = 10;
    private int counterCorrect = 0;
    public SpacedRepititionSystem srs;
    private FragmentTransaction ft;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        progressBar = (ProgressBar) findViewById(R.id.pb_exercise);
        fromSRS = checkSource();
        if (!fromSRS) {
            readIntent();
        }
        srs = new SpacedRepititionSystem(this);
        fragmentManager = getFragmentManager();
        startNextTurn();
    }

    public void startNextTurn() {
        ft = fragmentManager.beginTransaction();
        ft.setCustomAnimations(R.animator.flight_left_in, R.animator.flip_right_out);
        if (hasNextTurn()) {
            //currentVocab = pickANewVocab();
            if (fromSRS) {
                if (turn <= MAX_TURNS) {
                    progressBar.setMax(MAX_TURNS);
                    progressBar.setProgress(turn + 1);
                    currentVocab = srs.getVocabRequest();
                    ft.replace(R.id.container_exercise, new ExerciseInputFragment()).commit();
                    turn++;
                }
            } else if (!fromSRS) {
                progressBar.setMax(getMaxTurns());
                if (newVocabs.size() > 0)  {
                    currentVocab = newVocabs.remove(0);
                    learnedVocabs.add(currentVocab);
                    ft.replace(R.id.container_exercise, new ExerciseLearnFragment()).commit();
                } else {
                    currentVocab = learnedVocabs.get(turn);
                    turn++;
                    progressBar.setProgress(turn);
                    ft.replace(R.id.container_exercise, new ExerciseInputFragment()).commit();
                }
            }
        } else {
            ft.replace(R.id.container_exercise, new ExerciseFinalScoreFragment()).commit();
        }

    }

    public void evaluateResult(boolean isCorrect) {
        if (isCorrect) {
            counterCorrect++;
        }
        startNextTurn();
    }

    public Vocab getCurrentVocab() {
        return currentVocab;
    }

    private boolean hasNextTurn() {
        if (fromSRS && turn < getMaxTurns()) {
            return true;
        } else if (turn < getMaxTurns()) {
            return true;
        } else {
            return false;
        }
    }

    public int getCounterCorrect() {
        return counterCorrect;
    }

  /*  public ArrayList<Vocab> getAllVocab() {
        return learnedVocabs;
    }*/

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

    public SpacedRepititionSystem getSrs() {
        return srs;
    }


    private boolean checkSource() {
        if (getIntent().hasExtra("SELECTED_UNIT")) { //Null Checking
            return false;
        } else {
            return true;
        }
    }

    public int getMaxTurns() {
        if (fromSRS) {
            return MAX_TURNS;
        } else {
            return newVocabs.size() + learnedVocabs.size();
        }
    }

    private void readIntent() {
        Gson gson = new Gson();
        Unit unit = gson.fromJson(getIntent().getStringExtra("SELECTED_UNIT"), Unit.class);
        for (Vocab vocab : unit.getVocabsOfUnit()) {
            if (vocab.getSrsLevel() == 0) {
                newVocabs.add(vocab);
            } else {
                learnedVocabs.add(vocab);
            }
        }
    }
}
