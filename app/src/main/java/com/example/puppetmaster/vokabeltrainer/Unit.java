package com.example.puppetmaster.vokabeltrainer;

import com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem.Vocab;

import java.util.ArrayList;

/**
 * Created by Benedikt on 20.01.17.
 */

public class Unit {
    private int id;
    private String title;
    private ArrayList<Vocab> vocabsOfUnit;

    public Unit(int id, String title, ArrayList<Vocab> vocabsOfUnit) {
        this.id = id;
        this.title = title;
        this.vocabsOfUnit = vocabsOfUnit;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<Vocab> getVocabsOfUnit() {
        return vocabsOfUnit;
    }
}
