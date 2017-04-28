package com.example.puppetmaster.vokabeltrainer.models.way;

/**
 * @author Alexander Eveler, alexander.eveler@gmail.com
 * @since 4/3/17.
 */

public class WayByVisit implements Way {

    private int wordsPerVisit;

    public WayByVisit(int wordsPerVisit) {
        this.wordsPerVisit = wordsPerVisit;
    }

    @Override
    public int getWayCode() {
        return 1;
    }

    public int getWordsPerVisit() {
        return wordsPerVisit;
    }

    public void setWordsPerVisit(int wordsPerVisit) {
        this.wordsPerVisit = wordsPerVisit;
    }

    @Override
    public String toString() {
        return "WayByVisit{" +
                "wordsPerVisit=" + wordsPerVisit +
                '}';
    }
}
