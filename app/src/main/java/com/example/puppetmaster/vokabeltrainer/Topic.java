package com.example.puppetmaster.vokabeltrainer;

/**
 * Created by Benedikt on 20.01.17.
 */

public class Topic {
    private int id;
    private String title;
    private int topicID;

    public Topic(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
