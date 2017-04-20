package com.example.puppetmaster.vokabeltrainer.Entities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem.SpacedRepititionSystem;
import com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem.Vocab;

import java.util.ArrayList;

/**
 * Created by Benedikt on 18.04.17.
 */

public class ExerciseLogic {
    private int MAX_TURNS = 20;
    private ArrayList<Vocab> vocabsAll;
    private ArrayList<Vocab> vocabsCorrect;
    private ArrayList<Vocab> vocabsFalse;
    private int nextTurn;
    private boolean fromSRS;
    private SpacedRepititionSystem srs;
    private Context context;

    public ExerciseLogic(ArrayList<Vocab> vocabsAll) {
        this.vocabsAll = vocabsAll;
        vocabsCorrect = new ArrayList<>();
        vocabsFalse = new ArrayList<>();
        this.nextTurn = -1;
        this.fromSRS = false;
    }

    // FromSRS
    public ExerciseLogic(Context context) {
        this.vocabsAll = new ArrayList<>();
        vocabsCorrect = new ArrayList<>();
        vocabsFalse = new ArrayList<>();
        this.nextTurn = -1;
        this.fromSRS = true;
        srs = new SpacedRepititionSystem(context);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        MAX_TURNS = prefs.getInt("numberOfWordsInWorkload", 0);
    }

    public Vocab nextVocab() {
        if (fromSRS) {
            Vocab vocab = srs.getVocabRequest();
            vocabsAll.add(vocab);
            nextTurn++;
            return vocab;
        } else {
            return vocabsAll.get(++nextTurn);
        }
    }

    public Vocab getCurrentVocab() {
        if (fromSRS) {
            return vocabsAll.get(nextTurn);
        } else {
            return vocabsAll.get(nextTurn);
        }

    }

    public boolean hasNextTurn() {
        if (fromSRS) {
            /*Vocab vocab;
            vocab = srs.getVocabRequest();*/
            if (nextTurn < MAX_TURNS - 1 && srs.getVocabRequest() != null) {
                return true;
            } else {
                return false;
            }
        } else {
            if (nextTurn < vocabsAll.size() - 1) {
                return true;
            } else {
                return false;
            }
        }
    }

    public int getNextTurn() {
        return nextTurn;
    }

    public void evaluate(boolean isCorrect) {
        if (isCorrect) {
            vocabsCorrect.add(getCurrentVocab());
        } else {
            vocabsFalse.add(getCurrentVocab());
        }
    }

    public ArrayList<Vocab> getVocabsAll() {
        return vocabsAll;
    }

    public ArrayList<Vocab> getVocabsCorrect() {
        return vocabsCorrect;
    }

    public ArrayList<Vocab> getVocabsFalse() {
        return vocabsFalse;
    }
}
