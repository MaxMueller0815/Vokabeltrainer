package com.example.puppetmaster.vokabeltrainer.Helper;

import android.util.Log;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.util.ArrayList;

/**
 * Helper für den Abruf von Wiktionary-Artikeln
 * Anmerkung: Wikitionary stellt eine API zur Verfügung mit diversen Ausgabeformaten, wie bspw. JSON (https://de.wiktionary.org/w/api.php?action=query&format=json&prop=revisions&rvprop=content&indexpageids&titles=Kartoffel).
 * Der eigentliche Seiteninhalt ist jedoch immer im schwer zu parsenden WikiText-Markup. Einfachheitahalber wird deshalb die Mobil-Website aufgerufen und mit JSoup geparst, um unnötige Inhalte zu entfernen, wie bspw. Übersetzungen/Dialekt.
 */

public class WiktionaryHelper {
    private Document doc;

    public WiktionaryHelper(Document doc) {
        this.doc = doc;
        removeUnneededSegments();
        addCustomCSS("main");
    }

    /**
     * Genannte CSS-Datei wird an das HTML-Doc angefügt
     * @param cssFileName Gewünscht CSS-Datei
     * @return Document, auf das die CSS-Datei angewendet wurde
     */
    public Document addCustomCSS(String cssFileName) {
        doc.head().appendElement("link").attr("rel", "stylesheet").attr("type", "text/css").attr("href", cssFileName + ".css");
        return doc;
    }

    /**
     * Unnötige Absätze und Knoten werden entfernt, so dass der Eintrag übersichtlich bleibt
     */
    public void removeUnneededSegments() {
        doc.select("script, link, meta").remove();
        ArrayList<Element> segments = new ArrayList<>();
        Element firstSegment = doc.select(".mf-section-1").first();
        firstSegment.select(".mw-ui-icon").remove();
        firstSegment.select("[title=Phonetik]+dl  > dd:gt(0)").remove();
        segments.add(firstSegment.select("h3").first());
        segments.add(firstSegment.select(".wikitable").first());

        String[] relevantSegments = {"Trennungsmöglichkeiten am Zeilenumbruch", "Phonetik", "Sinn und Bezeichnetes (Semantik)", "Verwendungsbeispielsätze"};

        for (String relevantSegment : relevantSegments) {
            segments.add(firstSegment.select("[title=" + relevantSegment + "]").first());
            segments.add(firstSegment.select("[title=" + relevantSegment + "] + dl").first());
        }

        doc.body().select("body>div").remove();
        for (Element segment : segments) {
            if (segment != null) {
                doc.body().appendChild(segment);
            }
        }
    }

    /**
     * Konvertiert JSoup Dokument zu String (HTML)
     * @return HTML im String-Format
     */
    public String toString() {
        return doc.outerHtml().replaceAll("(\n)", "");
    }

    /**
     * Enfternt unnötige Angaben im Suchbegriff und gibt eine Wiktionary-URL zurück
     * @param searchTerm Ursprünglicher Begriff, wie bspw. "Lehrerin (die, -nen)"
     * @return URL, mit bereinigtem Begriff, wie bspw. "https://de.m.wiktionary.org/wiki/Lehrerin"
     */
    public static String makeUrl(String searchTerm) {
        /*URL Encoding*/
        searchTerm = WordHelper.cleanString(searchTerm);
        searchTerm = searchTerm.replace(" ", "_");

        String url = "https://de.m.wiktionary.org/wiki/" + searchTerm;
        Log.i("Trying to connect to", url + "(encoding by jSoup in next step)");
        return url;
    }


    /**
     *     Wiktionary liefert zu vielen Einträgen eine Audio-Datei zur Aussprache. Standardmäßig wird jedoch die Google TTS-Bibliothek zur Aussprache verwendet, da diese für jedes Wort/Phrase eine Vorlesefunktion bereitstellen kann (auch offline).
     */
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

    /**
     * Gibt an, ob das Wort/Phrase in der Wiktionary enthalten ist
     * @return Passender Wiki-Eintrag wurde gefunden
     */
    public boolean hasBeenFound() {
        if (doc.toString().contains("noarticletext")) {
            return false;
        } else {
            return true;
        }
    }
}
