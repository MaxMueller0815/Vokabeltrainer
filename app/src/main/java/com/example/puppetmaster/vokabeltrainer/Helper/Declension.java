package com.example.puppetmaster.vokabeltrainer.Helper;

import java.util.ArrayList;

/**
 * Created by Benedikt on 08.03.17.
 */

public class Declension {
    public static boolean checkDeclension(String term) {
        String article = "";
        boolean isNoun = false;
        try {
            article = term.substring(term.indexOf("(")+1,term.indexOf(","));
            if (article.equals("der") || article.equals("die") || article.equals("das")) {
                isNoun = true;
            }
        } catch (Exception e) {
        }
        return isNoun;
    }
    public static ArrayList<String> getDeclination(String term) {
        String sgNominativ = "";
        String sgGenitiv = "";
        String sgDativ = "";
        String sgAkkusativ = "";
        String plNominativ = "";
        String plGenitiv = "";
        String plDativ = "";
        String plAkkusativ = "";

        String word = term.replaceAll(" \\(.*\\)", "");
        String[] parts = term.split("[\\(\\)]");
        String[] declinationInfo = new String[2];
        for (String part : parts) {
            declinationInfo = part.split(",");
        }
        String article = declinationInfo[0].trim();
        String pluralEnding = declinationInfo[1].trim();

        System.out.println(article + " " + word + "\n" +  pluralEnding);

        if (article.equals("der")) {
            sgNominativ = article + " " + word;

            if(pluralEnding.equals("-e") || pluralEnding.equals("~e")) {
                pluralEnding = pluralEnding.replaceAll("-", "");
                pluralEnding = pluralEnding.replaceAll("~", "");
                sgGenitiv = "des " + word + "es";
                sgDativ = "dem " + word;
                sgAkkusativ = "den " + word;

                if (pluralEnding.equals("~e")) {word = replaceUmlaut(word);}
                plNominativ = "die " + word + pluralEnding;
                plGenitiv = "der " + word + "e";
                plDativ = "den " + word + "en";
                plAkkusativ = "die " + word + "e";
            }

            if (pluralEnding.equals("-") || pluralEnding.equals("~")) {
                sgGenitiv = "des " + word + "s";
                sgDativ = "dem " + word;
                sgAkkusativ = "den " + word;

                if (pluralEnding.equals("~")) {word = replaceUmlaut(word);}
                plNominativ = "die " + word;
                plGenitiv = "der " + word;
                plDativ = "den " + word + "n";
                plAkkusativ = "die " + word;
            }


            if (pluralEnding.equals("~er")) {
                pluralEnding = pluralEnding.replace("~", "");

                sgGenitiv = "des " + word + "es";
                sgDativ = "dem " + word;
                sgAkkusativ = "den " + word;

                word = replaceUmlaut(word);
                plNominativ = "die " + word + pluralEnding;
                plGenitiv = "der " + word + pluralEnding;
                plDativ = "den " + word + pluralEnding + "n";
                plAkkusativ = "die " + word + pluralEnding;
            }

            if (pluralEnding.equals("-s")) {
                pluralEnding = pluralEnding.replace("-", "");
                sgGenitiv = "des " + word + "s";
                sgDativ = "dem " + word;
                sgAkkusativ = "den " + word;

                plNominativ = "die " + word + pluralEnding;
                plGenitiv = "der " + word + "s";
                plDativ = "den " + word + "s";
                plAkkusativ = "die " + word + "s";
            }

            if (pluralEnding.equals("-en") || pluralEnding.equals("-n")) {
                pluralEnding = pluralEnding.replace("-", "");
                sgGenitiv = "des " + word + pluralEnding;
                sgDativ = "dem " + word + pluralEnding;
                sgAkkusativ = "den " + word + pluralEnding;

                plNominativ = "die " + word + pluralEnding;
                plGenitiv = "der " + word + pluralEnding;
                plDativ = "den " + word + pluralEnding;
                plAkkusativ = "die " + word + pluralEnding;
            }

            if(pluralEnding.equals("-se")) {
                pluralEnding = pluralEnding.replaceAll("-", "");
                sgGenitiv = "des " + word + "ses";
                sgDativ = "dem " + word;
                sgAkkusativ = "den " + word;

                plNominativ = "die " + word + pluralEnding;
                plGenitiv = "der " + word + pluralEnding;
                plDativ = "den " + word + pluralEnding + "n";
                plAkkusativ = "die " + word + pluralEnding;
            }
        }


        else if (article.equals("die")) {
            sgNominativ = article + " " + word;
            sgGenitiv = "der " + word;
            sgDativ = "der " + word;
            sgAkkusativ = "die " + word;

            if (pluralEnding.equals("-n") || pluralEnding.equals("-en") || pluralEnding.equals("-nen")) {
                pluralEnding = pluralEnding.replaceAll("-", "");
                plNominativ = "die " + word + pluralEnding;
                plGenitiv = "der " + word + pluralEnding;
                plDativ = "den " + word + pluralEnding;
                plAkkusativ = "die " + word + pluralEnding;
            }

            if (pluralEnding.equals("-")) {
                plNominativ = "die " + word + "en";
                plGenitiv = "der " + word + "en";
                plDativ = "den " + word + "en";
                plAkkusativ = "die " + word + "en";
            }
            if (pluralEnding.equals("~e")) {
                pluralEnding = pluralEnding.replace("~", "");
                word = replaceUmlaut(word);
                plNominativ = "die " + word + pluralEnding;
                plGenitiv = "der " + word + "e";
                plDativ = "den " + word + "en";
                plAkkusativ = "die " + word + "e";
            }

            if (pluralEnding.equals("~")) {
                word = replaceUmlaut(word);
                plNominativ = "die " + word;
                plGenitiv = "der " + word;
                plDativ = "den " + word + "n";
                plAkkusativ = "die " + word;
            }

            if (pluralEnding.equals("-s")) {
                pluralEnding = pluralEnding.replace("-", "");
                plNominativ = "die " + word + pluralEnding;
                plGenitiv = "der " + word + "s";
                plDativ = "den " + word + "s";
                plAkkusativ = "die " + word + "s";
            }
        }

        else if (article.equals("das")) {
            if(pluralEnding.equals("-e")) {
                pluralEnding = pluralEnding.replaceAll("-", "");
                sgGenitiv = "des " + word + "es";
                sgDativ = "dem " + word;
                sgAkkusativ = "den " + word;

                if (pluralEnding.equals("~e")) {word = replaceUmlaut(word);}
                plNominativ = "die " + word + pluralEnding;
                plGenitiv = "der " + word + "e";
                plDativ = "den " + word + "en";
                plAkkusativ = "die " + word + "e";
            }
        }


        ArrayList<String> declensions = new ArrayList<>();
        declensions.add(sgNominativ);
        declensions.add(sgGenitiv);
        declensions.add(sgDativ);
        declensions.add(sgAkkusativ);
        declensions.add(plNominativ);
        declensions.add(plGenitiv);
        declensions.add(plDativ);
        declensions.add(plAkkusativ);
        return declensions;

    }

    // TODO Funktioniert wohl noch nicht so sauber (Bsp: Sohnemann -> Söhnemänn)
    private static String replaceUmlaut(String word) {
        word = word.replaceAll("a", "ä");
        word = word.replaceAll("u", "ü");
        word = word.replaceAll("o", "ö");
        word = word.replaceAll("A", "Ä");
        word = word.replaceAll("U", "Ü");
        word = word.replaceAll("O", "Ö");
        return word;
    }
}
