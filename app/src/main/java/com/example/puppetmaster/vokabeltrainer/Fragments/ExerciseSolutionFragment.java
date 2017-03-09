package com.example.puppetmaster.vokabeltrainer.Fragments;


import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.puppetmaster.vokabeltrainer.Activities.ExerciseActivity;
import com.example.puppetmaster.vokabeltrainer.DatabaseCommunication.MyDatabase;
import com.example.puppetmaster.vokabeltrainer.Entities.Unit;
import com.example.puppetmaster.vokabeltrainer.Helper.Declension;
import com.example.puppetmaster.vokabeltrainer.Helper.StringCleaner;
import com.example.puppetmaster.vokabeltrainer.R;
import com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem.Vocab;
import com.github.lzyzsd.circleprogress.Utils;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import static android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE;

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

    public ExerciseSolutionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_exercise_solution, container, false);
        context = getContext();
        currentVocab = ((ExerciseActivity)this.getActivity()).getCurrentVocab();
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
        if(isCorrect) {
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

        TableLayout declensionTable = (TableLayout) view.findViewById(R.id.table_declension);

        if (Declension.checkDeclension(translations.get(0))) {
            ArrayList<String> declinations = Declension.getDeclination(translations.get(0));
            TextView tvSgNom = (TextView) view.findViewById(R.id.tv_sg_nom);
            tvSgNom.setText(declinations.get(0));
            TextView tvSgGen = (TextView) view.findViewById(R.id.tv_sg_gen);
            tvSgGen.setText(declinations.get(1));
            TextView tvSgDat = (TextView) view.findViewById(R.id.tv_sg_dat);
            tvSgDat.setText(declinations.get(2));
            TextView tvSgAkk = (TextView) view.findViewById(R.id.tv_sg_akk);
            tvSgAkk.setText(declinations.get(3));
            TextView tvPlNom = (TextView) view.findViewById(R.id.tv_pl_nom);
            tvPlNom.setText(declinations.get(4));
            TextView tvPlGen = (TextView) view.findViewById(R.id.tv_pl_gen);
            tvPlGen.setText(declinations.get(5));
            TextView tvPlDat = (TextView) view.findViewById(R.id.tv_pl_dat);
            tvPlDat.setText(declinations.get(6));
            TextView tvPlAkk = (TextView) view.findViewById(R.id.tv_pl_akk);
            tvPlAkk.setText(declinations.get(7));
        } else {
            declensionTable.setVisibility(View.INVISIBLE);
        }

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab_exercise_solution);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ExerciseActivity) getActivity()).evaluateResult(isCorrect);
            }
        });

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        tvWiki = (TextView) view.findViewById(R.id.tv_wiktionary);
        FetchWikiEntry fetchWikiEntry = new FetchWikiEntry();
        fetchWikiEntry.execute(translations.get(0));

    }

    // TODO: Sollte man das nicht durch die handleAnswer/checkAnswer Funktion in SRS machen? -> Folge: dauert ewig
    private void compareSolution(){
        translations = currentVocab.getGerman();
        userAnswer = StringCleaner.cleanString(userAnswer);
        for (int i = 0; i < translations.size(); i++) {
            if (userAnswer.equals(StringCleaner.cleanString(translations.get(i)))) {
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

    public class FetchWikiEntry extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String entry = "";
            try {
                Uri.Builder builder = new Uri.Builder();
                builder.scheme("https")
                        .authority("de.wiktionary.org")
                        .appendPath("w")
                        .appendPath("api.php")
                        .appendQueryParameter("action", "query")
                        .appendQueryParameter("format", "json")
                        .appendQueryParameter("prop", "revisions")
                        .appendQueryParameter("rvprop", "content")
                        .appendQueryParameter("titles", strings[0]);

                URL url = new URL(builder.build().toString());
                InputStream inputStream = url.openStream();
                Scanner s = new Scanner(inputStream).useDelimiter("\\A");
                String result = s.hasNext() ? s.next() : "";
                JSONObject response = new JSONObject(result);
                JSONObject query2 = response.getJSONObject("query");
                JSONObject pages = query2.getJSONObject("pages");
                JSONObject page = pages.getJSONObject((String) pages.keys().next());
                JSONArray revisions = page.getJSONArray("revisions");
                JSONObject revision = revisions.getJSONObject(0);
                entry = revision.getString("*");
            }
            catch (JSONException je) {
                Log.e("JSON Error",je.toString());
                return null;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return entry;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            tvWiki.setText(s);
        }
    }
}