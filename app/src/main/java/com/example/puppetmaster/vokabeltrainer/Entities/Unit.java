package com.example.puppetmaster.vokabeltrainer.Entities;

import com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem.Vocab;

import java.util.ArrayList;

/**
 * Repräsentiert ein Teilgebiet, das einem Themenbereich (Topic) zugeordnet ist
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

    /**
     * Gibt ID der Unit zurück
     * @return ID der Unit
     */
    public int getId() {
        return id;
    }

    /**
     * Gibt Titel der Unit zurück
     * @return Titel der Unit
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gibt ID des Topics zurück, dem die aktuelle Unit zugeordnet ist
     * @return ID des Topics
     */
    public int getTopicId() {
        return topicId;
    }

    /**
     * Gibt Liste aller Vokabeln der Unit zurück
     * @return Liste aller Vokabeln der Unit
     */
    public ArrayList<Vocab> getVocabsOfUnit() {
        return vocabsOfUnit;
    }

    /**
     * Gibt Anzahl aller Vokabeln in Unit zurück
     * @return Anzahl aller Vokabeln in Unit
     */
    public int getNumOfAllVocabs() {
        return vocabsOfUnit.size();
    }

    /**
     * Gibt Anzahl aller bereits gelernten Vokabeln in Unit zurück
     * @return Anzahl aller bereits gelernten Vokabeln in Unit
     */
    public int getNumOfLearnedVocabs() {
        int learnedVocabs = 0;
        for (Vocab vocab : vocabsOfUnit) {
            if(vocab.isPracticed()) {
                learnedVocabs++;
            }
        }
        return learnedVocabs;
    }
}
