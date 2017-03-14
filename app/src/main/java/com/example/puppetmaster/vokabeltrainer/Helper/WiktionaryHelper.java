package com.example.puppetmaster.vokabeltrainer.Helper;

import android.util.Log;

import org.jsoup.nodes.Document;

/**
 * Created by Benedikt on 10.03.17.
 */

public class WiktionaryHelper {
    private Document doc;
    private String searchTerm;

    public WiktionaryHelper(Document doc) {
        this.doc = doc;
        removeUnneededSegments();
        addCustomCSS("main");
    }

    public Document addCustomCSS(String cssFileName) {
        doc.head().appendElement("link").attr("rel", "stylesheet").attr("type", "text/css").attr("href", cssFileName+".css");
        return doc;
    }
//    TODO: Funktioniert nicht so ganz
    public void removeUnneededSegments() {
        doc.getElementsByTag("script").remove();
        doc.body().getElementsByClass("thumb").remove();
        doc.body().getElementsByClass("header-container").remove();
        doc.body().getElementsByClass("pre-content").remove();
        doc.body().getElementsByClass("section-heading").remove();
        doc.body().getElementsByClass("edit-page").remove();
        doc.body().getElementsByClass("NavFrame").remove();
        doc.body().getElementsByClass("references").remove();
        doc.body().getElementsByClass("toc-mobile").remove();
        doc.body().getElementsByClass("printfooter").remove();
        doc.body().getElementsByClass("noprint").remove();
        doc.body().getElementById(".C3.9Cbersetzungen").remove();
        doc.body().select("#Vorlage_Uberarbeiten").remove();
        //doc.getElementById("Vorlage_Erweitern").remove();
        //doc.body().select("div#Vorlage_Erweitern").remove();

        doc.body().select("[title=Akronyme und Kürzel] + dl").remove();
        doc.body().select("[title=Akronyme und Kürzel]").remove();

        doc.body().select("[title=Etymologie und Morphologie] + dl").remove();
        doc.body().select("[title=Etymologie und Morphologie]").remove();

        doc.body().select("[title=bedeutungsgleich gebrauchte Wörter] + dl").remove();
        doc.body().select("[title=bedeutungsgleich gebrauchte Wörter]").remove();

        doc.body().select("[title=Sinnverwandte Wörter] + dl").remove();
        doc.body().select("[title=Sinnverwandte Wörter]").remove();

        doc.body().select("[title=Antonyme] + dl").remove();
        doc.body().select("[title=Antonyme]").remove();

        doc.body().select("[title=Hyperonyme] + dl").remove();
        doc.body().select("[title=Hyperonyme]").remove();

        doc.body().select("[title=Hyponyme] + dl").remove();
        doc.body().select("[title=Hyponyme]").remove();

        doc.body().select("[title=Derivate, Komposita und Konversionen] + dl").remove();
        doc.body().select("[title=Derivate, Komposita und Konversionen]").remove();

        doc.body().select("[title=Referenzen und weiterführende Informationen] + dl + p").remove();
        doc.body().select("[title=Referenzen und weiterführende Informationen] + dl").remove();
        doc.body().select("[title=Referenzen und weiterführende Informationen]").remove();

        doc.body().select("[title=Ähnlich geschriebene oder gleich klingende Wörter] + dl").remove();
        doc.body().select("[title=Ähnlich geschriebene oder gleich klingende Wörter]").remove();

        doc.body().select("[title=Ähnliche Wörter (Deutsch)] + dl").remove();
        doc.body().select("[title=Ähnliche Wörter (Deutsch)]").remove();

        doc.body().select("[title=Das Gesuchte nicht gefunden? Ähnliche Wörter aus allen Sprachen] + dl").remove();
        doc.body().select("[title=Das Gesuchte nicht gefunden? Ähnliche Wörter aus allen Sprachen]").remove();

        doc.body().select("[title=Ähnlich geschriebene oder gleich klingende Wörter] + dl").remove();
        doc.body().select("[title=Ähnlich geschriebene oder gleich klingende Wörter]").remove();

        doc.body().select("[title=Signifikante Kollokationen] + dl").remove();
        doc.body().select("[title=Signifikante Kollokationen]").remove();

        doc.body().select("footer").remove();
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
