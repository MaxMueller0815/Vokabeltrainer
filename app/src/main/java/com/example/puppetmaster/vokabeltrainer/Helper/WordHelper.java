package com.example.puppetmaster.vokabeltrainer.Helper;

/**
 * Created by Benedikt on 08.03.17.
 */

public class WordHelper {
    public static String cleanString(String term) {
        term = term.replaceAll("\\(.*?\\)","");
        term = term.replaceAll("[^A-ZÄÖÜa-zäöüß ]", "");
        return term.trim();
    }

    public static boolean isNoun(String term) {
        String article = "";
        boolean isNoun = false;
        try {
            article = term.substring(term.indexOf("(")+1,term.indexOf(","));
            if (article.equals("der") || article.equals("die") || article.equals("das")) {
                isNoun = true;
            }
            } catch (Exception e) {}
        return isNoun;
    }

    public static String getArticle(String term) {
        String word = term.replaceAll(" \\(.*\\)", "");
        String[] parts = term.split("[\\(\\)]");
        String[] declinationInfo = new String[2];
        for (String part : parts) {
            declinationInfo = part.split(",");
        }
        String article = declinationInfo[0].trim();
        if (article == null) {
            article = declinationInfo[0];
        }
        return article;
    }
}
