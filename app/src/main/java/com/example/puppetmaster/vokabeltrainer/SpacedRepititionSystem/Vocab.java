package com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by florian on 11.01.17.
 */

public class Vocab {

    private String dateFormatString = "yyyy-MM-dd'T'HH:mm";
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
                      String lastRevision, String nextRevision, int countCorrect, int countFalse) throws ParseException {

        DateFormat format = new SimpleDateFormat(dateFormatString, Locale.ENGLISH);

        this.id = id;
        this.english = english;
        this.german = german;
        this.unitId = unitId;
        this.srsLevel = srsLevel;
        this.lastRevision = format.parse(lastRevision);
        this.nextRevision = format.parse(nextRevision);
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

    public void setLastRevision(Date currentdate){
        this.lastRevision = currentdate;
    }

    public void setNextRevision(Date nextRevision){
        this.nextRevision = nextRevision;
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

    public long getRevisionDifference(){

        long nextRevisionAsLong = nextRevision.getTime();
        long currentTime = new Date().getTime();
        return currentTime - nextRevisionAsLong;

    }

    public boolean isPracticed() {
        if(getSrsLevel() >= 1) {
            return true;
        } else {
            return false;
        }
    }

    //TODO: Funktion löschen, nur für Testing
    public Date getLastRevision() {
        return lastRevision;
    }

    //TODO: Funktion löschen, nur für Testing
    public Date getNextRevision() {
        return nextRevision;
    }

    //TODO: Funktion löschen, nur für Testing
    public int getCountCorrect() {
        return countCorrect;
    }

    //TODO: Funktion löschen, nur für Testing
    public int getCountFalse() {
        return countFalse;
    }

    //TODO: Funktion löschen, nur für Testing
    public void setSrsLevel(int srsLevel) {
        this.srsLevel = srsLevel;
    }

    //TODO: Funktion löschen, nur für Testing
    public void setCountCorrect(int countCorrect) {
        this.countCorrect = countCorrect;
    }

    //TODO: Funktion löschen, nur für Testing
    public void setCountFalse(int countFalse) {
        this.countFalse = countFalse;
    }
}
