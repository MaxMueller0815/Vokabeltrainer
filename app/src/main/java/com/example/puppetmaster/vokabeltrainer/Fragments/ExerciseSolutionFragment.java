package com.example.puppetmaster.vokabeltrainer.Fragments;


import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
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
    TextView tvWiki;
    String htmlDoc = "";
    WebView webView;

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
        initUI();
        return view;
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

        try {
            SiteFetcher sf = new SiteFetcher();
            sf.execute(translations.get(0));
            webView = (WebView) view.findViewById(R.id.wv_wiki);
            webView.getSettings().setJavaScriptEnabled(false);
            webView.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    return true;
                }
            });
        } catch (Exception e) {
            System.out.print(e);
        }
    }

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

    private class SiteFetcher extends AsyncTask<String, Void, String> {
        Document doc;

        //HERE DECLARE THE VARIABLES YOU USE FOR PARSING
        @Override
        protected String doInBackground(String... strings) {
            try {
                String searchTerm = StringCleaner.cleanString(strings[0]);
                String url = "https://de.m.wiktionary.org/wiki/" + searchTerm;
                Log.i("Trying to connect to", url + "(encoding by jSoup in next step)");
                doc = Jsoup.connect(url)
                        .get();
                doc = WiktionaryHelper.removeUnneededSegments(doc);
                //doc = WiktionaryHelper.addCustomCSS(doc, "materialize");
                doc = WiktionaryHelper.addCustomCSS(doc, "main");
                htmlDoc = WiktionaryHelper.docToHTML(doc);
            } catch (Exception e) {
                System.out.print(e);
            }
            return htmlDoc;
        }

        @Override
        protected void onPostExecute(String htmlDoc) {
            super.onPostExecute(htmlDoc);
            webView.loadDataWithBaseURL("file:///android_asset/.", htmlDoc, "text/html", "UTF-8", null);
        }
    }
}