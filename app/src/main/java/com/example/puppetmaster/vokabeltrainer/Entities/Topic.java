package com.example.puppetmaster.vokabeltrainer.Entities;

import com.example.puppetmaster.vokabeltrainer.R;

import java.util.ArrayList;

/**
 * Created by Benedikt on 20.01.17.
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

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

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

    public ArrayList<Unit> getUnitsOfTopic() {
        return unitsOfTopic;
    }
}
