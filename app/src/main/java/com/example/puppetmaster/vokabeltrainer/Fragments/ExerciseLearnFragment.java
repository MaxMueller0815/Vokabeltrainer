package com.example.puppetmaster.vokabeltrainer.Fragments;


import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.puppetmaster.vokabeltrainer.Activities.ExerciseActivity;
import com.example.puppetmaster.vokabeltrainer.Helper.WiktionaryHelper;
import com.example.puppetmaster.vokabeltrainer.Helper.WordHelper;
import com.example.puppetmaster.vokabeltrainer.R;
import com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem.Vocab;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.Locale;

/**
 * Fragment wird angezeigt, wenn das Wort sich auf dem SRS-Level 0 befindet, so dass der Nutzer die neue Vokabel lernen kann. Hier findet keine Abfrage statt.
 */

public class ExerciseLearnFragment extends Fragment {
    private View view;
    private Vocab currentVocab;
    Context context;
    WebView webView;
    TextToSpeech tts;


    public ExerciseLearnFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_learn, container, false);
        context = getContext();
        currentVocab = ((ExerciseActivity) this.getActivity()).getExercise().getCurrentVocab();

        setupUI();
        initTTS();
        searchWiktionary();

        return view;
    }

    /**
     * Baut UI auf
     */
    private void setupUI() {
        TextView tvBestSolution = (TextView) view.findViewById(R.id.tv_best_solution);
        String germanTranslation = currentVocab.getGerman().get(0);
        if (WordHelper.isNoun(germanTranslation)) {
            SpannableString text = new SpannableString(germanTranslation);
            text.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.md_cyan_600, null)), germanTranslation.indexOf("("), germanTranslation.indexOf(")") + 1, 0);
            tvBestSolution.setText(text);
        } else {
            tvBestSolution.setText(germanTranslation);
        }


        TextView tvTerm = (TextView) view.findViewById(R.id.tv_term);
        tvTerm.setText(currentVocab.getEnglish());

        FloatingActionButton fabNext = (FloatingActionButton) view.findViewById(R.id.fab_exercise_solution);
        fabNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ExerciseActivity) getActivity()).markAsLearned();
            }
        });

        FloatingActionButton fabAudio = (FloatingActionButton) view.findViewById(R.id.fab_audio);
        fabAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playTTS();
            }
        });
    }

    /**
     * Initialisiert Text-to-Speech-Funktion
     */
    private void initTTS() {
        tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.GERMANY);
                }
            }
        });
    }

    /**
     * Beginnt AsyncTask, um Wiktionary-Website abzurufen
     */
    private void searchWiktionary() {
        try {
            WikiFetcher sf = new WikiFetcher();
            sf.execute(currentVocab.getGerman().get(0));
        } catch (Exception e) {
            System.out.print(e);
        }
    }

    /**
     * Text-to-Speech-Funktion spricht dt. Wort aus
     */
    public void playTTS() {
        String phrase = currentVocab.getGerman().get(0);

        if (WordHelper.isNoun(phrase)) {
            // Setzt Artikel vor Nomen ("die Lehrerin" statt "Lehrerin (die, -nen)
            phrase = WordHelper.getArticle(phrase) + " " + WordHelper.getNoun(phrase);
        } else {
            phrase = WordHelper.cleanString(phrase);
        }
        tts.speak(phrase, TextToSpeech.QUEUE_FLUSH, null, this.hashCode() + "");
    }

    /**
     * AsyncTask, der die Wiktionary-Seite abrufen soll
     */
    private class WikiFetcher extends AsyncTask<String, Void, WiktionaryHelper> {
        @Override
        protected WiktionaryHelper doInBackground(String... strings) {
            try {
                Document doc = Jsoup.connect(WiktionaryHelper.makeUrl(strings[0]))
                        .get();
                WiktionaryHelper wikiHelper = new WiktionaryHelper(doc);
                return wikiHelper;
            } catch (Exception e) {
                System.out.print(e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(WiktionaryHelper wikiHelper) {
            super.onPostExecute(wikiHelper);
            ProgressBar pbLoader = (ProgressBar) view.findViewById(R.id.pb_wiktionary_loader);
            pbLoader.setVisibility(View.GONE);

            if (wikiHelper != null && wikiHelper.hasBeenFound()) {
                webView = (WebView) view.findViewById(R.id.wv_wiki);
                webView.getSettings().setJavaScriptEnabled(false);
                webView.setWebViewClient(new WebViewClient() {
                    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                        return true;
                    }
                });
                webView.loadDataWithBaseURL("file:///android_asset/.", wikiHelper.toString(), "text/html", "UTF-8", null);
                webView.setVisibility(View.VISIBLE);
            } else {
                TextView noEntry = (TextView) view.findViewById(R.id.tv_no_wiki_found);
                noEntry.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * Erweitert, damit Text-to-Speech ordnungsgemäß beendet wird
     */
    @Override
    public void onDestroy() {
        if(tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
}
