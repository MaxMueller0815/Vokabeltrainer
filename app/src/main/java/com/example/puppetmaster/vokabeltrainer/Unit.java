package com.example.puppetmaster.vokabeltrainer;

/**
 * Created by Benedikt on 20.01.17.
 */

public class Unit {
    private int id;
    private String title;
    private int topicID;

    public Unit(int id, String title, int topicID) {
        this.id = id;
        this.title = title;
        this.topicID = topicID;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getTopicID() {
        return topicID;
    }
}
