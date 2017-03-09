package com.example.puppetmaster.vokabeltrainer.Helper;

/**
 * Created by Benedikt on 08.03.17.
 */

public class StringCleaner {
    public static String cleanString(String term) {
        term = term.replaceAll("\\(.*?\\)","");
        term = term.toLowerCase().replaceAll("[^a-z]", "");
        return term.trim();
    }
}
