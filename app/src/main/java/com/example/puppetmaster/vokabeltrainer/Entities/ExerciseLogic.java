package com.example.puppetmaster.vokabeltrainer.Entities;

import com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem.Vocab;

import java.util.ArrayList;

/**
 * Created by Benedikt on 18.04.17.
 */

/**
 * Stellt die Logik für ein Abfrage-Set bereit
 */
public class ExerciseLogic {
    private ArrayList<Vocab> vocabsAll;
    private ArrayList<Vocab> vocabsCorrect;
    private ArrayList<Vocab> vocabsFalse;
    private int nextTurn;
    private int numTurns;

    public ExerciseLogic(ArrayList<Vocab> vocabsAll) {
        this.vocabsAll = vocabsAll;
        vocabsCorrect = new ArrayList<>();
        vocabsFalse = new ArrayList<>();
        this.nextTurn = -1;
        numTurns = vocabsAll.size();
    }

    /**
     * Abhängig vom Ergebnis wird die Vokabel in die Richtig/Falsch-Liste aufgenommen (für finale Übersicht)
     * @param isCorrect Vokabel wurde richtig übersetzt
     */
    public void evaluate(boolean isCorrect) {
        if (isCorrect) {
            vocabsCorrect.add(getCurrentVocab());
        } else {
            vocabsFalse.add(getCurrentVocab());
        }
    }


    public Vocab nextVocab() {
        return vocabsAll.get(++nextTurn);
    }

    /**
     * Liefert die Vokabel der aktuellen Runde aus VocabsAll
     * @return Aktuelle Vokabel
     */
    public Vocab getCurrentVocab() {
        return vocabsAll.get(nextTurn);
    }

    /**
     * Bestimmt, ob noch eine weitere Runde im Abfrageset möglich ist
     * @return Weitere Runde ist möglich
     */
    public boolean hasNextTurn() {
        if (nextTurn < vocabsAll.size() - 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Gibt Nummer der nächsten Runde zurück
     * @return Aktuelle Runde + 1
     */
    public int getNextTurn() {
        return nextTurn;
    }

    /**
     * Gibt Liste aller Vokabeln im aktuellen Vokabelset zurück
     * @return Liste aller Vokabel im aktuellen Vokabelset
     */
    public ArrayList<Vocab> getVocabsAll() {
        return vocabsAll;
    }

    /**
     * Gibt Liste aller richtig beantworteten Vokabeln im aktuellen Vokabelset zurück
     * @return Liste aller richtig beantworteten Vokabeln im aktuellen Vokabelset
     */
    public ArrayList<Vocab> getVocabsCorrect() {
        return vocabsCorrect;
    }

    /**
     * Gibt Liste aller falsch beantworteten Vokabeln im aktuellen Vokabelset zurück
     * @return Liste aller falsch beantworteten Vokabeln im aktuellen Vokabelset
     */
    public ArrayList<Vocab> getVocabsFalse() {
        return vocabsFalse;
    }

    /**
     * Gibt Anzahl der maximal möglichen Runden im Set zurück
     * @return Maximal möglichen Runden im Set
     */
    public int getNumTurns() {
        return numTurns;
    }
}
