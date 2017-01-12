package com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem;

import java.util.Date;

/**
 * Created by florian on 11.01.17.
 */

public class Vocab {

    private int id;
    private String term;
    private String translation;
    private int unitId;
    private int srsLevel;
    private Date lastRevision;
    private Date nextRevision;
    private int countCorrect;
    private int countFalse;

    public void Vocab(int id, String term, String translation, int unitId, int srsLevel,
                      Date lastRevision, Date nextRevision, int countCorrect, int countFalse){

        this.id = id;
        this.term = term;
        this.translation = translation;
        this.unitId = unitId;
        this.srsLevel = srsLevel;
        this.lastRevision = lastRevision;
        this.nextRevision = nextRevision;
        this.countCorrect = countCorrect;
        this.countFalse = countFalse;

    }

    public int getId(){
        return id;
    }

    public int getRevisionDifference(){
        return 0;
    }

}
