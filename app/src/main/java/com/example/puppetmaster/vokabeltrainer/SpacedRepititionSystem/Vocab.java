package com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem;

import java.io.Serializable;
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

public class Vocab implements Serializable {

  //  private String dateFormatString = "yyyy-MM-dd'T'HH:mm";
    private int id;
    private String english;
    private ArrayList<String> german;
    private int unitId;
    private int srsLevel;
    private long lastRevision;
    private long nextRevision;
    private int countCorrect;
    private int countFalse;

    public Vocab(int id, String english, ArrayList<String> german, int unitId, int srsLevel,
                      long lastRevision, long nextRevision, int countCorrect, int countFalse) throws ParseException {

       // DateFormat format = new SimpleDateFormat(dateFormatString, Locale.ENGLISH);

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
        if(srsLevel >= 9) {
            srsLevel = 9;
        }
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

        Calendar calendar = Calendar.getInstance();

        Date currentDate = new Date();
        calendar.add(Calendar.HOUR, 5);
      //  this.nextRevision = new Date(currentDate.getTime() - 2 * 24 * 3600 * 1000);
        this.nextRevision = calendar.getTimeInMillis();

    }

    public boolean isNewVocab(){
        return srsLevel == 1 && countCorrect == 0 && countFalse == 0;
    }

    public void setLastRevision(){
        Calendar calendar = Calendar.getInstance();
        this.lastRevision = calendar.getTimeInMillis();
    }

    public void setNextRevision(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(getLastRevision());
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


        /*// TEST VALUES
        if (srsLevel == 1) {
            calendar.add(Calendar.MINUTE, 1);
        } else if (srsLevel == 2) {
            calendar.add(Calendar.MINUTE, 3);
        } else if (srsLevel == 3) {
            calendar.add(Calendar.MINUTE, 5);
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
        }*/

        this.nextRevision = calendar.getTimeInMillis();
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

    public String getExternalSrsLevel() {
        switch (srsLevel) {
            case 1: return "A";
            case 2: return "A";
            case 3: return "A";
            case 4: return "B";
            case 5: return "B";
            case 6: return "B";
            case 7: return "C";
            case 8: return "D";
            case 9: return "E";
            default: return "-";
        }
    }

    public int getId(){
        return id;
    }

    public long getRevisionDifference(){

        Calendar calendar = Calendar.getInstance();

      //  long nextRevisionAsLong = nextRevision.getTime();
        long currentTime = calendar.getTimeInMillis();
        return currentTime - nextRevision;

    }

    public boolean isPracticed() {
        return getSrsLevel() >= 1;
    }

    //TODO: Funktion löschen, nur für Testing
    public long getLastRevision() {
        return lastRevision;
    }

    //TODO: Funktion löschen, nur für Testing
    public long getNextRevision() {
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
