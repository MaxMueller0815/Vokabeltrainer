package com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by florian on 11.01.17.
 */

public class Vocab {

    private String dateFormatString = "yyyy-MM-dd'T'HH:mm";
    private int id;
    private String english;
    private ArrayList<String> german;
    private int unitId;
    private int srsLevel;
    private Date lastRevision;
    private Date nextRevision;
    private int countCorrect;
    private int countFalse;

    public Vocab(int id, String english, ArrayList<String> german, int unitId, int srsLevel,
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
    public void pushIntoWorkload(){
        this.srsLevel = 1;

        Date currentDate = new Date();
        this.nextRevision = new Date(currentDate.getTime() - 2 * 24 * 3600 * 1000);

    }

    public boolean isNewVocab(){
        return srsLevel == 1 && countCorrect == 0 && countFalse == 0;
    }

    public void setLastRevision(){
        this.lastRevision = new Date();
    }

    public void setNextRevision(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getLastRevision());

        if (srsLevel == 1) {
            calendar.add(Calendar.HOUR, 5);
        } else if (srsLevel == 2) {
            calendar.add(Calendar.HOUR, 12);
        } else if (srsLevel == 3) {
            calendar.add(Calendar.HOUR, 24);
        } else if (srsLevel == 4) {
            calendar.add(Calendar.DATE, 2);
        } else if (srsLevel == 5) {
            calendar.add(Calendar.DATE, 5);
        } else if (srsLevel == 6) {
            calendar.add(Calendar.DATE, 14);
        } else if (srsLevel == 7) {
            calendar.add(Calendar.MONTH, 1);
        } else if (srsLevel == 8) {
            calendar.add(Calendar.MONTH, 2);
        } else if (srsLevel == 9) {
            calendar.add(Calendar.MONTH, 3);
        }

        this.nextRevision = calendar.getTime();
    }

    public String getEnglish(){
        return english;
    }

    public ArrayList<String> getGerman(){
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
        return getSrsLevel() >= 1;
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
