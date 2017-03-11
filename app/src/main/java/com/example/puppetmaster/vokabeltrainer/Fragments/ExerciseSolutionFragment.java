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
import com.example.puppetmaster.vokabeltrainer.DatabaseCommunication.MyDatabase;
import com.example.puppetmaster.vokabeltrainer.Helper.StringCleaner;
import com.example.puppetmaster.vokabeltrainer.Helper.WiktionaryHelper;
import com.example.puppetmaster.vokabeltrainer.R;
import com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem.Vocab;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
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
        currentVocab = ((ExerciseActivity) this.getActivity()).getCurrentVocab();
        Bundle inputBundle = this.getArguments();
        if (inputBundle != null) {
            userAnswer = inputBundle.getString("user_answer");
        }

        compareSolution();
        playTTS();
        initUI();
        searchWiktionary();
        return view;
    }

    private void searchWiktionary() {
        try {
            SiteFetcher sf = new SiteFetcher();
            sf.execute(translations.get(0));
        } catch (Exception e) {
            System.out.print(e);
        }
    }

    private void initUI() {
        TextView tvUserAnswer = (TextView) view.findViewById(R.id.tv_user_answer);
        ivIndicator = (ImageView) view.findViewById(R.id.iv_solution);
        if (isCorrect) {
            ivIndicator.setImageResource(R.drawable.smiley_happy);
            tvUserAnswer.setVisibility(View.GONE);
            /*if (!alternativeSolution.equals("")) {

            }*/
        } else {
            ivIndicator.setImageResource(R.drawable.smiley_question);
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

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab_exercise_solution);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ExerciseActivity) getActivity()).evaluateResult(isCorrect);
            }
        });
    }
//TODO: Gehört das hier hin?
    private void compareSolution() {
        translations = currentVocab.getGerman();
        userAnswer = StringCleaner.cleanString(userAnswer.toLowerCase());
        for (int i = 0; i < translations.size(); i++) {
            if (userAnswer.equals(StringCleaner.cleanString(translations.get(i).toLowerCase()))) {
                isCorrect = true;
                if (isCorrect && i > 0) {
                    alternativeSolution = translations.get(i);
                }
            }
        }
        if (isCorrect) {
            currentVocab.increaseCountCorrect();
            currentVocab.increaseSrsLevel();
        } else {
            currentVocab.increaseCountFalse();
            currentVocab.decreaseSrsLevel();
        }
        MyDatabase db = new MyDatabase(context);
        db.updateSingleVocab(currentVocab);
        db.close();
    }

    private void playTTS() {
        tts = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.GERMANY);
                    String utteranceId = this.hashCode() + "";
                    tts.speak(StringCleaner.cleanString(translations.get(0)), TextToSpeech.QUEUE_FLUSH, null, utteranceId);
                }
            }
        });
    }

    private class SiteFetcher extends AsyncTask<String, Void, WiktionaryHelper> {
        @Override
        protected WiktionaryHelper doInBackground(String... strings) {
            try {
                Document doc = Jsoup.connect(WiktionaryHelper.makeUrl(strings[0]))
                        .get();
                WiktionaryHelper wikiHelper = new WiktionaryHelper(doc);
                return wikiHelper;
            } catch (Exception e) {
                System.out.print(e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(WiktionaryHelper wikiHelper) {
            super.onPostExecute(wikiHelper);
            ProgressBar pbLoader = (ProgressBar) view.findViewById(R.id.pb_wiktionary_loader);
            pbLoader.setVisibility(View.GONE);

            webView = (WebView) view.findViewById(R.id.wv_wiki);
            webView.getSettings().setJavaScriptEnabled(false);
            webView.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    return true;
                }
            });
            if (wikiHelper.hasBeenFound()) {
                webView.loadDataWithBaseURL("file:///android_asset/.", wikiHelper.toString(), "text/html", "UTF-8", null);
                webView.setVisibility(View.VISIBLE);
            } else {
                TextView noEntry = (TextView) view.findViewById(R.id.tv_no_wiki_found);
                noEntry.setVisibility(View.VISIBLE);
            }
        }
    }
}