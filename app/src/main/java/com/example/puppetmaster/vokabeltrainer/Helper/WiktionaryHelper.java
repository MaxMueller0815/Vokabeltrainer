package com.example.puppetmaster.vokabeltrainer.Helper;

import android.util.Log;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Benedikt on 10.03.17.
 */

public class WiktionaryHelper {
    private Document doc;

    public WiktionaryHelper(Document doc) {
        this.doc = doc;
        removeUnneededSegments();
        addCustomCSS("main");
    }

    public Document addCustomCSS(String cssFileName) {
        doc.head().appendElement("link").attr("rel", "stylesheet").attr("type", "text/css").attr("href", cssFileName+".css");
        return doc;
    }

    public void removeUnneededSegments() {
        doc.select("script, link, meta").remove();
        doc.getElementsByTag("script").remove();
        ArrayList<Element> segments = new ArrayList<>();
        Element firstSegment = doc.select(".mf-section-1").first();
        firstSegment.select(".mw-ui-icon").remove();
        firstSegment.select("[title=Phonetik]+dl  > dd:gt(0)").remove();
        segments.add(firstSegment.select("h3").first());
        segments.add(firstSegment.select(".wikitable").first());

        String[] relevantSegments = {"Trennungsmöglichkeiten am Zeilenumbruch", "Phonetik", "Sinn und Bezeichnetes (Semantik)", "Verwendungsbeispielsätze"};

        for (String relevantSegment : relevantSegments) {
            segments.add(firstSegment.select("[title=" + relevantSegment+ "]").first());
            segments.add(firstSegment.select("[title=" + relevantSegment+ "] + dl").first());
        }

        doc.body().select("body>div").remove();
        for (Element segment : segments) {
            doc.body().appendChild(segment);
        }
    }

    public String toString() {
        return doc.outerHtml().replaceAll("(\n)", "");
    }

    public static String makeUrl(String searchTerm) {
        searchTerm = WordHelper.cleanString(searchTerm);

//        Improves capitalisation
        int countBlankSpaces = 0;
        for(int i = 0; i < searchTerm.length(); i++) {
            if(Character.isWhitespace(searchTerm.charAt(i))) countBlankSpaces++;
        }
        if (countBlankSpaces > 0) {
            char c[] = searchTerm.toCharArray();
            c[0] = Character.toLowerCase(c[0]);
            searchTerm = new String(c);
        }

        /*URL Encoding*/
        searchTerm = searchTerm.replace(" ", "_");

        String url = "https://de.m.wiktionary.org/wiki/" + searchTerm;
        Log.i("Trying to connect to", url + "(encoding by jSoup in next step)");
        return url;
    }



    /*private void playWikiSoundFile() {
        Element urlLink = doc.select("[href$=.ogg]").first();
        if (urlLink != null) {
            String soundUrl = urlLink.attr("href");
            soundUrl = "https:" + soundUrl;
            System.out.println(soundUrl);

            MediaPlayer mp = new MediaPlayer();
            try {
                mp.setDataSource(soundUrl);
                mp.prepare();
                mp.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/

    public boolean hasBeenFound() {
        if (doc.toString().contains("noarticletext")) {
            return false;
        } else {
            return true;
        }
    }
}
