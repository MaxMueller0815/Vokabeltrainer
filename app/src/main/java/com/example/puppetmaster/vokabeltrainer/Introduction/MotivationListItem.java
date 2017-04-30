package com.example.puppetmaster.vokabeltrainer.Introduction;

public class MotivationListItem{

    private String motivationText;
    private int id;

    public MotivationListItem(String motivationText, int id) {
        this.motivationText = motivationText;
        this.id = id;
    }


    public String getMotivationText() {
        return motivationText;
    }

    public int getMotivationID() {
        return id;
    }


}
