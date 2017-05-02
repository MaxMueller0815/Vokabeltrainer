package com.example.puppetmaster.vokabeltrainer.Helper;

/**
 * Created by Benedikt on 08.03.17.
 */

/**
 * Klasse stellt diverse Funktionen zur Bereinigung von Strings bereit
 */
public class WordHelper {

    /**
     * Entfernt Sonderzeichen, sowie den Inhalt zwischen den Klammern
     * @param term String, der bereinigt werden soll, bspw. "Lehrerin (die, -nen)
     * @return String ohne Sonderzeichen, Inhalt zwischen Klammern, bspw. "Lehrerin)
     */
    public static String cleanString(String term) {
        term = term.replaceAll("\\(.*?\\)","");
        term = term.replaceAll("[^A-ZÄÖÜa-zäöüß ]", "");
        return term.trim();
    }

    /**
     * Bestimmt, ob Parameter ein Nomen ist
     * @param term Begriff, zu dem man wissen möchte, ob es ein String ist
     * @return Begriff ist ein Nomen
     */
    public static boolean isNoun(String term) {
        return getArticle(term) != null;
    }


    public static boolean isArticle(String term) {
        if (term.equals("der") || term.equals("die") || term.equals("das")) {
            return true;
        }

        return false;
    }

    /**
     * Extrahiert Artikel aus Parameter
     * @param term Begriff, aus dem der Artikel extrahiert werden soll
     * @return Artikel als String
     */
    public static String getArticle(String term) {

        int idx = term.indexOf('(');
        if (idx == -1) {
            return null;
        }

        return term.substring(idx + 1, idx + 4);
    }

    /**
     * Liefert das Nomen ohne Zusatzangaben, wie bspw. Artikel
     * @param term Begriff, der u.U. Zusatzinfos enthalten kann
     * @return Nomen
     */
    public static String getNoun(String term) {

        if (!isNoun(term)) {
            return null;
        }

        return term.substring(0 , term.indexOf('(') - 1);
    }
}
