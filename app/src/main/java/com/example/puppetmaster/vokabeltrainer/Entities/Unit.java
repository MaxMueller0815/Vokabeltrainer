package com.example.puppetmaster.vokabeltrainer.Entities;

import com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem.Vocab;

import java.util.ArrayList;

/**
 * Created by Benedikt on 20.01.17.
 */

public class Unit {
    private int id;
    private String title;
    private ArrayList<Vocab> vocabsOfUnit;
    private int topicId;

    public Unit(int id, String title, ArrayList<Vocab> vocabsOfUnit, int topicId) {
        this.id = id;
        this.title = title;
        this.vocabsOfUnit = vocabsOfUnit;
        this.topicId = topicId;
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

    public int getNumOfAllVocabs() {
        return vocabsOfUnit.size();
    }

    public int getNumOfLearnedVocabs() {
        int learnedVocabs = 0;
        for (Vocab vocab : vocabsOfUnit) {
            if(vocab.isPracticed()) {
                learnedVocabs++;
            }
        }
        return learnedVocabs;
    }

    public int getTopicId() {
        return topicId;
    }
}
