package com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem;

import java.util.Date;

/**
 * Created by florian on 11.01.17.
 */

public class Vocab {

    private int id;
    private String english;
    private String german;
    private int unitId;
    private int srsLevel;
    private Date lastRevision;
    private Date nextRevision;
    private int countCorrect;
    private int countFalse;

    public Vocab(int id, String english, String german, int unitId, int srsLevel,
                      Date lastRevision, Date nextRevision, int countCorrect, int countFalse){

        this.id = id;
        this.english = english;
        this.german = german;
        this.unitId = unitId;
        this.srsLevel = srsLevel;
        this.lastRevision = lastRevision;
        this.nextRevision = nextRevision;
        this.countCorrect = countCorrect;
        this.countFalse = countFalse;

    }
    public void increaseCountCorrect(){
        countCorrect += 1;
    }
    public void increaseCountFalse(){
        countFalse += 1;
    }
    public void increaseSrsLevel(){
        srsLevel += 1;
    }

    public void decreaseSrsLevel(){
        if(srsLevel > 0){
            srsLevel -= 1;
        }else{
            srsLevel = 0;
        }
    }

    public String getEnglish(){
        return english;
    }

    public String getGerman(){
        return german;
    }

    public int getSrsLevel(){
        return srsLevel;
    }

    public int getId(){
        return id;
    }

    public int getRevisionDifference(){
        return 0;
    }

}
