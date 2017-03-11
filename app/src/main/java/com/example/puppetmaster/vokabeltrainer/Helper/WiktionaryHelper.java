package com.example.puppetmaster.vokabeltrainer.Helper;

import org.jsoup.nodes.Document;

/**
 * Created by Benedikt on 10.03.17.
 */

public class WiktionaryHelper {
    private Document doc;
    private String searchTerm;

    public static Document addCustomCSS(Document doc, String cssFileName) {
        doc.head().appendElement("link").attr("rel", "stylesheet").attr("type", "text/css").attr("href", cssFileName+".css");
        return doc;
    }
//    TODO: Funktioniert nicht so ganz
    public static Document removeUnneededSegments(Document doc) {
        doc.head().getElementsByTag("script").remove();
        doc.head().getElementsByTag("img").remove();
        doc.body().getElementsByClass("header-container").remove();
        doc.body().getElementsByClass("pre-content").remove();
        doc.body().getElementsByClass("section-heading").remove();
        doc.body().getElementsByClass("edit-page").remove();
        doc.body().getElementsByClass("NavFrame").remove();
        doc.body().getElementsByClass("references").remove();
        doc.body().getElementsByClass("toc-mobile").remove();

        doc.body().select("[title=Akronyme und Kürzel]>dl").remove();
        doc.body().select("[title=Akronyme und Kürzel]").remove();

        doc.body().select("[title=Etymologie und Morphologie]>dl").remove();
        doc.body().select("[title=Etymologie und Morphologie]").remove();

        doc.body().select("[title=bedeutungsgleich gebrauchte Wörter]>dl").remove();
        doc.body().select("[title=bedeutungsgleich gebrauchte Wörter]").remove();

        doc.body().select("[title=Sinnverwandte Wörter]>dl").remove();
        doc.body().select("[title=Sinnverwandte Wörter]").remove();

        doc.body().select("[title=Antonyme]>dl").remove();
        doc.body().select("[title=Antonyme]").remove();
/*______*/
        doc.body().select("[title=Hyperonyme]>dl").remove();
        doc.body().select("[title=Hyperonyme]").remove();

        doc.body().select("[title=Hyponyme]>dl").remove();
        doc.body().select("[title=Hyponyme]").remove();

        doc.body().select("[title=Derivate, Komposita und Konversionen]>dl").remove();
        doc.body().select("[title=Derivate, Komposita und Konversionen]").remove();

        doc.body().select("[title=Referenzen und weiterführende Informationen]>dl").remove();
        doc.body().select("[title=Referenzen und weiterführende Informationen]").remove();

        doc.body().select("[title=Ähnlich geschriebene oder gleich klingende Wörter]>dl").remove();
        doc.body().select("[title=Ähnlich geschriebene oder gleich klingende Wörter]").remove();

        doc.body().select("footer").remove();

        return doc;
    }

    public static String docToHTML(Document doc) {
        return doc.outerHtml().replaceAll("(\n)", "");
    }

    public static String prepareSearchTerm(String searchTerm) {
        searchTerm = StringCleaner.cleanString(searchTerm);
        searchTerm = improveCapitalizationRules(searchTerm);
        searchTerm = searchTerm.replace(" ", "_");
        return searchTerm;
    }

    private static String improveCapitalizationRules(String searchTerm) {
        int countBlankSpaces = 0;
        for(int i = 0; i < searchTerm.length(); i++) {
            if(Character.isWhitespace(searchTerm.charAt(i))) countBlankSpaces++;
        }
        if (countBlankSpaces > 0) {
            char c[] = searchTerm.toCharArray();
            c[0] = Character.toLowerCase(c[0]);
            searchTerm = new String(c);
        }
        return searchTerm;
    }
}
