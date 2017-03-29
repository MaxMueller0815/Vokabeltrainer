package com.example.puppetmaster.vokabeltrainer.Fragments;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
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
 * A simple {@link Fragment} subclass.
 */
public class ExerciseLearnFragment extends Fragment {
    private View view;
    private Vocab currentVocab;
    private ArrayList<String> translations;
    private String userAnswer = "";
    private String alternativeSolution = "";
    ImageView ivIndicator;
    Context context;
    WebView webView;
    TextToSpeech tts;


    public ExerciseLearnFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_learn, container, false);
        context = getContext();
        currentVocab = ((ExerciseActivity) this.getActivity()).getCurrentVocab();
        translations = currentVocab.getGerman();

        initUI();
        initTTS();
        searchWiktionary();

        return view;
    }

    private void searchWiktionary() {
        try {
            ExerciseLearnFragment.SiteFetcher sf = new ExerciseLearnFragment.SiteFetcher();
            sf.execute(translations.get(0));
        } catch (Exception e) {
            System.out.print(e);
        }
    }

    private void initUI() {
        TextView tvUserAnswer = (TextView) view.findViewById(R.id.tv_user_answer);
        ivIndicator = (ImageView) view.findViewById(R.id.iv_solution);

        TextView tvBestSolution = (TextView) view.findViewById(R.id.tv_best_solution);
        tvBestSolution.setText(translations.get(0));

        TextView tvTerm = (TextView) view.findViewById(R.id.tv_term);
        tvTerm.setText(currentVocab.getEnglish());

        FloatingActionButton fabNext = (FloatingActionButton) view.findViewById(R.id.fab_exercise_solution);
        fabNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ExerciseActivity) getActivity()).startNextTurn();
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

    public void playTTS() {
        String phrase = currentVocab.getGerman().get(0);

        if (WordHelper.isNoun(phrase)) {
            phrase = WordHelper.getArticle(phrase) + " " + WordHelper.getNoun(phrase);
        } else {
            phrase = WordHelper.cleanString(phrase);
        }
        tts.speak(phrase, TextToSpeech.QUEUE_FLUSH, null, this.hashCode() + "");
    }

    private class SiteFetcher extends AsyncTask<String, Void, WiktionaryHelper> {
        @Override
        protected WiktionaryHelper doInBackground(String... strings) {
            try {
                Document doc = Jsoup.connect(WiktionaryHelper.makeUrl(strings[0]))
                        .get();
                WiktionaryHelper wikiHelper = new WiktionaryHelper(doc);
                Log.i("wiki", wikiHelper.toString());
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

    @Override
    public void onDestroy() {
        //Close the Text to Speech Library
        if(tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
}
