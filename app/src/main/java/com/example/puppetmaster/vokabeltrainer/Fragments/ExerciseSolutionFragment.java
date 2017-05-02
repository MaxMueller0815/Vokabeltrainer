package com.example.puppetmaster.vokabeltrainer.Fragments;


import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.puppetmaster.vokabeltrainer.Activities.ExerciseActivity;
import com.example.puppetmaster.vokabeltrainer.Helper.WiktionaryHelper;
import com.example.puppetmaster.vokabeltrainer.Helper.WordHelper;
import com.example.puppetmaster.vokabeltrainer.R;
import com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem.Vocab;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Fragment zeigt dem Nutzer an, ob die Vokabel korrekt übersetzt wurde
 */

public class ExerciseSolutionFragment extends Fragment {
    private View view;
    private Vocab currentVocab;
    private ArrayList<String> translations;
    private String userAnswer = "";
    private String alternativeSolution = "";
    private boolean isCorrect = false;
    ImageView ivIndicator;
    Context context;
    WebView webView;
    TextToSpeech tts;

    public ExerciseSolutionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_exercise_solution, container, false);
        context = getContext();
        currentVocab = ((ExerciseActivity) this.getActivity()).getExercise().getCurrentVocab();
        translations = currentVocab.getGerman();
        Bundle inputBundle = this.getArguments();
        if (inputBundle != null) {
            userAnswer = inputBundle.getString("user_answer");
        }

        compareSolution();
        setupUI();
        searchWiktionary();
        initTTS();

        return view;
    }

    /**
     * Vokabel und Antwort des Nutzers wird an das SRS übergeben, wo es ausgewertet wird
     */
    private void compareSolution() {
        isCorrect = ((ExerciseActivity)this.getActivity()).getSrs().handleAnswer(2, currentVocab, userAnswer);
    }

    /**
     * UI
     */
    private void setupUI() {
        TextView tvUserAnswer = (TextView) view.findViewById(R.id.tv_user_answer);
        ivIndicator = (ImageView) view.findViewById(R.id.iv_solution);
        if (isCorrect) {
            ivIndicator.setImageResource(R.drawable.correct);
            tvUserAnswer.setVisibility(View.GONE);
        } else {
            ivIndicator.setImageResource(R.drawable.error);
            tvUserAnswer.setText("Your answer: " + userAnswer);
        }

        TextView tvBestSolution = (TextView) view.findViewById(R.id.tv_best_solution);
        tvBestSolution.setText(translations.get(0));

        TextView tvTerm = (TextView) view.findViewById(R.id.tv_term);
        tvTerm.setText(currentVocab.getEnglish());

        TextView tvAlternativeSolution = (TextView) view.findViewById(R.id.tv_alternative_solution);
        if (alternativeSolution.equals("")) {
            tvAlternativeSolution.setVisibility(View.GONE);
        } else {
            tvAlternativeSolution.setText("Rarer: " + alternativeSolution);
        }

        // Audio-Button
        FloatingActionButton fabAudio = (FloatingActionButton) view.findViewById(R.id.fab_audio);
        fabAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playTTS();
            }
        });

        // Next-Button
        FloatingActionButton fabNext = (FloatingActionButton) view.findViewById(R.id.fab_exercise_solution);
        fabNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ExerciseActivity) getActivity()).evaluateResult(isCorrect);
            }
        });
    }

    /**
     * Beginnt AsyncTask, um Wiktionary-Website abzurufen
     */
    private void searchWiktionary() {
        try {
            WikiFetcher sf = new WikiFetcher();
            sf.execute(translations.get(0));
        } catch (Exception e) {
            System.out.print(e);
        }
    }

    /**
     * Initialisiert TTS
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
     * Spielt Sound mithilfe der TTS-Funktion
     */
    public void playTTS() {
        String phrase = currentVocab.getGerman().get(0);

        if (WordHelper.isNoun(phrase)) {
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
     * Die onDestroy-Methode soll zusätzlich noch das TTS beenden
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