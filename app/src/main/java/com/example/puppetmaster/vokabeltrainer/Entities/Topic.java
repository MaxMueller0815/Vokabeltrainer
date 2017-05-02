package com.example.puppetmaster.vokabeltrainer.Entities;

import com.example.puppetmaster.vokabeltrainer.R;

import java.util.ArrayList;

/**
 * Repräsentiert ein Themengebiet, das mehrere Teilgebiete (Units) beinhaltet
 */

public class Topic {
    private int id;
    private String title;
    private ArrayList<Unit> unitsOfTopic;

    public Topic(int id, String title, ArrayList<Unit> unitsOfTopic) {
        this.id = id;
        this.title = title;
        this.unitsOfTopic = unitsOfTopic;
    }

    /**
     * Gibt Topic-ID zurück
     * @return Topic-ID
     */
    public int getId() {
        return id;
    }

    /**
     * Gibt Titel des Topics zurück
     * @return Titel des Topics
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gibt zugeordnetes Bild des Topics zurück
     * @return Bild des Topics
     */
    public int getImage() {
        int imageID = -1;
        switch(getId()) {
            case 1:
                imageID = R.drawable.chapter1;
                break;
            case 2:
                imageID = R.drawable.chapter2;
                break;
            case 3:
                imageID = R.drawable.chapter3;
                break;
            case 4:
                imageID = R.drawable.chapter4;
                break;
            case 5:
                imageID = R.drawable.chapter5;
                break;
            case 6:
                imageID = R.drawable.chapter6;
                break;
            case 7:
                imageID = R.drawable.chapter7;
                break;
        }

        return imageID;
    }

    /**
     * Gibt Units des Topics zurück
     * @return Units des Topics
     */
    public ArrayList<Unit> getUnitsOfTopic() {
        return unitsOfTopic;
    }

    /**
     * Gibt Gesamtfortschritt des Topics über alle enthaltenen Units/Vokabeln zurück
     * @return Titel des Topics
     */
    public int getProgress() {
        return getNumOfLearnedVocabs() * 100 / getNumOfAllVocabs();
    }

    /**
     * Gibt Anzahl aller Vokabeln im Topic zurück
     * @return Anzahl aller Vokabeln im Topic
     */
    public int getNumOfAllVocabs() {
        int allVocabs = 0;
        for (Unit unit: unitsOfTopic) {
            allVocabs += unit.getNumOfAllVocabs();
        }
        return allVocabs;
    }

    /**
     * Gibt Anzahl aller gelernten Vokabeln im Topic zurück (SRS > 0)
     * @return Anzahl aller gelernten Vokabeln im Topic
     */
    public int getNumOfLearnedVocabs() {
        int learnedVocabs = 0;
        for (Unit unit: unitsOfTopic) {
            learnedVocabs += unit.getNumOfLearnedVocabs();
        }
        return learnedVocabs;
    }
}
