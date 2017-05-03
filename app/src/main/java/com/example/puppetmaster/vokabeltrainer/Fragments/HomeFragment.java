package com.example.puppetmaster.vokabeltrainer.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.puppetmaster.vokabeltrainer.Activities.ExerciseActivity;
import com.example.puppetmaster.vokabeltrainer.Activities.SRSTesterActivity;
import com.example.puppetmaster.vokabeltrainer.Activities.StartActivity;
import com.example.puppetmaster.vokabeltrainer.DatabaseCommunication.MyDatabase;
import com.example.puppetmaster.vokabeltrainer.Entities.Topic;
import com.example.puppetmaster.vokabeltrainer.Entities.Unit;
import com.example.puppetmaster.vokabeltrainer.R;
import com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem.Vocab;
import com.github.lzyzsd.circleprogress.ArcProgress;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.icu.text.DateFormat.getDateInstance;

/**
 * Fragment stellt die Startseite dar. Hier kann der Nutzer sein Fortschritt sehen
 */
public class HomeFragment extends Fragment {
    private View view;
    private ArrayList<Vocab> vocabs = new ArrayList<Vocab>();
    ArrayList<Topic> topics = new ArrayList<>();
    private final int VISIBLE_DAYS = 7;
    private int counterWeek = 0;
    private int workload = 0;
    DataPoint[] datapointsLearned = new DataPoint[VISIBLE_DAYS];
    DataPoint[] datapointsWorkload = new DataPoint[VISIBLE_DAYS];


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        topics =  ((StartActivity)this.getActivity()).getTopics();

        retrieveWorkload();
        calcStats();
        setupGraph();
        setupProgressBar();

        Button srsButton = (Button) view.findViewById(R.id.btn_srs_test);
        srsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SRSTesterActivity.class));
            }
        });

        Button repetitionButton = (Button) view.findViewById(R.id.btn_srs_repetition);
        Button learnButton = (Button) view.findViewById(R.id.btn_learn);
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getContext());
        int wordsInSRS = settings.getInt("numberOfWordsInWorkload", 0);
        if (wordsInSRS > 0) {
            repetitionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getContext(), ExerciseActivity.class));
                }
            });
            repetitionButton.setVisibility(View.VISIBLE);

        } else {
            if (datapointsLearned[VISIBLE_DAYS - 1].getY() < workload) {
                learnButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((StartActivity) getActivity()).loadFragment(R.id.action_topics);
                    }
                });
                learnButton.setVisibility(View.VISIBLE);
            } else {
                TextView tvDone = (TextView) view.findViewById(R.id.tv_done);
                tvDone.setVisibility(View.VISIBLE);
            }
        }

        return view;
    }

    /**
     * Workload aus DB auslesen, sollte später aus den SharedPrefs gelesen werden
     */
    private void retrieveWorkload() {
        workload = new MyDatabase(getContext()).getWorkload();
        TextView tvWorkload = (TextView) view.findViewById(R.id.tv_workload);
        tvWorkload.setText("" + workload + " words");
    }

    /**
     * Setzt Fortschritt der runden Progessbars (basierend auf com.github.lzyzsd:circleprogress)
     */
    private void setupProgressBar() {
        // Runde ProgressBar für heute
        ArcProgress progressDay = (ArcProgress) view.findViewById(R.id.arc_progress_today);
        TextView tvDay = (TextView) view.findViewById(R.id.tv_day);
        tvDay.setText((int) datapointsLearned[VISIBLE_DAYS - 1].getY() + "");
        double percentDay = datapointsLearned[VISIBLE_DAYS - 1].getY() / workload * 100.0;
        if (percentDay <= 100) {
            progressDay.setProgress((int) percentDay);
        } else {
            progressDay.setProgress(100);
        }

        // Runde ProgressBar für gestern
        ArcProgress progressYesterday = (ArcProgress) view.findViewById(R.id.arc_progress_yesterday);
        TextView tvYesterday = (TextView) view.findViewById(R.id.tv_yesterday);
        tvYesterday.setText((int) datapointsLearned[VISIBLE_DAYS - 2].getY() + "");
        double percentYesterday = datapointsLearned[VISIBLE_DAYS - 2].getY() / workload * 100.0;
        if (percentYesterday <= 100) {
            progressYesterday.setProgress((int) percentYesterday);
        } else {
            progressYesterday.setProgress(100);
        }

        // Runde ProgressBar für die letzen sieben Tage
        ArcProgress progressWeek = (ArcProgress) view.findViewById(R.id.arc_progress_week);
        TextView tvWeek = (TextView) view.findViewById(R.id.tv_week);
        tvWeek.setText(counterWeek + "");
        double percentWeek = counterWeek / 140.0 * 100.0;
        if (percentWeek <= 100) {
            progressWeek.setProgress((int) percentWeek);
        } else {
            progressWeek.setProgress(100);
        }
    }

    /**
     * Zählt Vokabeln die innerhalb eines Tages der letzen Woche bearbeitet wurden
     */
    private void calcStats() {
        MyDatabase db = new MyDatabase(getContext());
        Calendar date = Calendar.getInstance();
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);

        for (int i = VISIBLE_DAYS - 1; i >= 0; i--) {
            double counterDay = 0;
            counterDay = db.getNumVocabsPractisedForDate(date.getTimeInMillis());
            datapointsLearned[i] = new DataPoint(date.getTime(), counterDay);
            datapointsWorkload[i] = new DataPoint(date.getTime(), workload);
            counterWeek = counterWeek + (int) counterDay;
            Log.i("Datapoint added", getDateInstance().format(datapointsLearned[i].getX()) + ", " + datapointsLearned[i].getY());
            date.add(Calendar.DATE, -1);
        }
    }

    /**
     * Setzt Werte im Diagramm, dass die Anzahl der bearbeiteten Wörter in der letzten Woche anzeigt (basierend auf com.jjoe64:graphview:4.2.1)
     */
    private void setupGraph() {
        LineGraphSeries<DataPoint> seriesLearned = new LineGraphSeries<>(datapointsLearned);
        seriesLearned.setColor(ContextCompat.getColor(getContext(), R.color.colorAccent3));
        seriesLearned.setThickness(15);

        LineGraphSeries<DataPoint> seriesWorkload = new LineGraphSeries<>(datapointsWorkload);
        seriesWorkload.setColor(ContextCompat.getColor(getContext(), R.color.md_orange_500));

        GraphView graph = (GraphView) view.findViewById(R.id.graph);

        graph.addSeries(seriesWorkload);
        graph.addSeries(seriesLearned);
        graph.getGridLabelRenderer().setNumHorizontalLabels(VISIBLE_DAYS); // only 4 because of the space

        graph.getViewport().setMinX(datapointsLearned[0].getX());
        graph.getViewport().setMaxX(datapointsLearned[VISIBLE_DAYS-1].getX());
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getGridLabelRenderer().setHumanRounding(false);
        graph.getGridLabelRenderer()
                .setLabelFormatter(new DefaultLabelFormatter() {
                    @Override
                    public String formatLabel(double value, boolean isValueX) {
                        if (isValueX) {
                            long totalSeconds = new Double(value).longValue();
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(new Date(totalSeconds));
                            int day = cal.get(Calendar.DAY_OF_MONTH);
                            int month = cal.get(Calendar.MONTH) + 1;

                            return String.format(day + "." + month);
                        } else {
                            return super.formatLabel(value, isValueX);
                        }
                    }
                });
    }
}
