package com.example.puppetmaster.vokabeltrainer.Activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.puppetmaster.vokabeltrainer.Entities.Unit;
import com.example.puppetmaster.vokabeltrainer.Fragments.ExerciseInputFragment;
import com.example.puppetmaster.vokabeltrainer.Fragments.ExerciseResultFragment;
import com.example.puppetmaster.vokabeltrainer.Fragments.ExerciseSolutionFragment;
import com.example.puppetmaster.vokabeltrainer.R;
import com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem.Vocab;
import com.google.gson.Gson;

import java.util.ArrayList;

public class ExerciseActivity extends AppCompatActivity {
    private ArrayList<Vocab> allVocab;
    private static FragmentManager fragmentManager;
    private int turn = 0;
    private int counterCorrect = 0;
    public Vocab currentVocab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        fragmentManager = getFragmentManager();
        readIntent();
        startNextTurn();
    }

    private void readIntent() {
        Intent intent = getIntent();
        if (null != intent) { //Null Checking
            Gson gson = new Gson();
            Unit unit = gson.fromJson(intent.getStringExtra("SELECTED_UNIT"), Unit.class);
            allVocab = unit.getVocabsOfUnit();
        }
    }

    private void startNextTurn() {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if(turn > 0) {
            ft.setCustomAnimations(R.animator.flight_left_in, R.animator.flip_right_out);
        }

        if (turn < allVocab.size()) {
            Fragment exerciseInputFragment = new ExerciseInputFragment();//Get Fragment Instance
            ft.replace(R.id.container_exercise, exerciseInputFragment).commit();
        } else {
            Fragment exerciseResultFragment= new ExerciseResultFragment();
            ft.replace(R.id.container_exercise, exerciseResultFragment).commit();
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
        return allVocab.get(turn);
    }

    public int getCounterCorrect() {
        return counterCorrect;
    }

    public ArrayList<Vocab> getAllVocab() {
        return allVocab;
    }



}
