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
        return getArticle(term) != null;
    }

    public static boolean isArticle(String term) {
        if (term.equals("der") || term.equals("die") || term.equals("das")) {
            return true;
        }

        return false;
    }

    public static String getArticle(String term) {

        int idx = term.indexOf('(');
        if (idx == -1) {
            return null;
        }

        return term.substring(idx + 1, idx + 4);
    }

    public static String getNoun(String term) {

        if (!isNoun(term)) {
            return null;
        }

        return term.substring(0 , term.indexOf('(') - 1);
    }
}
