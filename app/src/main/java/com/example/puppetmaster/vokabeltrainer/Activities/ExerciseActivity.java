package com.example.puppetmaster.vokabeltrainer.Activities;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import com.example.puppetmaster.vokabeltrainer.Entities.ExerciseLogic;
import com.example.puppetmaster.vokabeltrainer.Fragments.ExerciseFinalScoreFragment;
import com.example.puppetmaster.vokabeltrainer.Fragments.ExerciseInputFragment;
import com.example.puppetmaster.vokabeltrainer.Fragments.ExerciseLearnFragment;
import com.example.puppetmaster.vokabeltrainer.R;
import com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem.SpacedRepititionSystem;
import com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem.Vocab;

import java.util.ArrayList;

public class ExerciseActivity extends AppCompatActivity {
    ExerciseLogic exercise;
    private boolean fromSRS;
    private static FragmentManager fragmentManager;
    public SpacedRepititionSystem srs;
    private FragmentTransaction ft;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        srs = new SpacedRepititionSystem(this);
        progressBar = (ProgressBar) findViewById(R.id.pb_exercise);

        if (getIntent().hasExtra("WORD_LIST")) { //Null Checking
            fromSRS = false;
            exercise = new ExerciseLogic((ArrayList<Vocab>) getIntent().getSerializableExtra("WORD_LIST"));
        } else {
            exercise = new ExerciseLogic(new SpacedRepititionSystem(getApplicationContext()).getCurrentRequestList());
        }

        fragmentManager = getFragmentManager();
        startNextTurn();
    }

    private void startNextTurn() {
        ft = fragmentManager.beginTransaction();
        ft.setCustomAnimations(R.animator.flight_left_in, R.animator.flip_right_out);

        if (exercise.nextVocab().getSrsLevel() == 0) {
            ft.replace(R.id.container_exercise, new ExerciseLearnFragment()).commit();
        } else {
            progressBar.setProgress(exercise.getNextTurn());
            ft.replace(R.id.container_exercise, new ExerciseInputFragment()).commit();
        }
    }

    public void evaluateResult(boolean isCorrect) {
        exercise.evaluate(isCorrect);
        if (exercise.hasNextTurn()) {
            startNextTurn();
        } else {
            // Show final score
            ft = fragmentManager.beginTransaction();
            ft.setCustomAnimations(R.animator.flight_left_in, R.animator.flip_right_out);
            ft.replace(R.id.container_exercise, new ExerciseFinalScoreFragment()).commit();
        }
    }

    public ExerciseLogic getExercise() {
        return exercise;
    }

    public void markAsLearned() {
        ft = fragmentManager.beginTransaction();
        ft.setCustomAnimations(R.animator.flight_left_in, R.animator.flip_right_out);
        ft.replace(R.id.container_exercise, new ExerciseInputFragment()).commit();
    }

    public SpacedRepititionSystem getSrs() {
        return srs;
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
}
