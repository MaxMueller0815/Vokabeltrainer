package com.example.puppetmaster.vokabeltrainer.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.puppetmaster.vokabeltrainer.DatabaseCommunication.MyDatabase;
import com.example.puppetmaster.vokabeltrainer.R;
import com.example.puppetmaster.vokabeltrainer.SRSTesterActivity;
import com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem.Vocab;
import com.example.puppetmaster.vokabeltrainer.Topic;
import com.example.puppetmaster.vokabeltrainer.Unit;
import com.github.lzyzsd.circleprogress.ArcProgress;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);


        readBundle();
        retrieveWorkload();
        calcStats();
        initGraph();
        initProgressBar();

        Button srsButton = (Button) view.findViewById(R.id.btn_srs_test);
        srsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SRSTesterActivity.class));
            }
        });

        return view;
    }

    private void retrieveWorkload() {
        workload = new MyDatabase(getContext()).getWorkload();
        TextView tvWorkload = (TextView) view.findViewById(R.id.tv_workload);
        tvWorkload.setText(""+ workload);
    }

    private void readBundle() {
        Gson gson = new Gson();
        String getArgument = getArguments().getString("topics");
        Type baseType = new TypeToken<List<Topic>>() {
        }.getType();
        topics = gson.fromJson(getArgument, baseType);

        for (Topic topic : topics) {
            ArrayList<Unit> units = topic.getUnitsOfTopic();
            for (Unit unit : units) {
                ArrayList<Vocab> vocabsOfUnit = unit.getVocabsOfUnit();
                for (Vocab vocab : vocabsOfUnit) {
                    vocabs.add(vocab);
                }
            }
        }

        Log.i("Length of vocabs",vocabs.size()+" items");
    }




    private void initProgressBar() {
        // ProgressBar for today
        ArcProgress progressDay = (ArcProgress) view.findViewById(R.id.arc_progress_today);
        TextView tvDay = (TextView) view.findViewById(R.id.tv_day);
        tvDay.setText((int) datapointsLearned[6].getY() + "");
        double percentDay = datapointsLearned[6].getY() / workload * 100.0;
        if (percentDay <= 100) {
            progressDay.setProgress((int) percentDay);
        } else {
            progressDay.setProgress(100);
        }
        //Log.i("counterToday", percentDay + " in ProgressBar");

        // ProgressBar for yesterday
        ArcProgress progressYesterday = (ArcProgress) view.findViewById(R.id.arc_progress_yesterday);
        TextView tvYesterday = (TextView) view.findViewById(R.id.tv_yesterday);
        tvYesterday.setText((int) datapointsLearned[5].getY() + "");
        double percentYesterday = datapointsLearned[5].getY() / workload * 100.0;
        if (percentYesterday <= 100) {
            progressYesterday.setProgress((int) percentYesterday);
        } else {
            progressYesterday.setProgress(100);
        }
        //Log.i("counterYesterday", percentYesterday + " in ProgressBar");

        // ProgressBar for last 7 days
        ArcProgress progressWeek = (ArcProgress) view.findViewById(R.id.arc_progress_week);
        TextView tvWeek = (TextView) view.findViewById(R.id.tv_week);
        tvWeek.setText(counterWeek + "");
        double percentWeek = counterWeek / 140.0 * 100.0;
        if (percentWeek <= 100) {
            progressWeek.setProgress((int) percentWeek);
        } else {
            progressWeek.setProgress(100);
        }
        //Log.i("counterWeek", percentWeek + " in ProgressBar");
    }

    private void calcStats() {
        Calendar calendarPast = Calendar.getInstance();
        calendarPast.setTime(new Date());
        for (int i = VISIBLE_DAYS - 1; i >= 0; i--) {
            double counterDay = 0;
            for (Vocab vocab : vocabs) {
                    Calendar calenderOfVocab = Calendar.getInstance();
                    calenderOfVocab.setTime(vocab.getLastRevision());
                    if (calenderOfVocab.get(Calendar.YEAR) == calendarPast.get(Calendar.YEAR) &&
                            calenderOfVocab.get(Calendar.DAY_OF_YEAR) == calendarPast.get(Calendar.DAY_OF_YEAR)) {
                        counterDay++;
                        counterWeek++;
                    }
            }
            datapointsLearned[i] = new DataPoint(calendarPast.getTime(), counterDay);
            datapointsWorkload[i] = new DataPoint(calendarPast.getTime(), workload);
            //datapointsLearned[i] = new DataPoint(i, counter);
            Log.i("Datapoint added", new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(datapointsLearned[i].getX()) + ", " + datapointsLearned[i].getY());
            calendarPast.add(Calendar.DATE, -1);
        }
    }

    private void initGraph() {
        LineGraphSeries<DataPoint> seriesLearned = new LineGraphSeries<>(datapointsLearned);
        seriesLearned.setColor(ContextCompat.getColor(getContext(), R.color.colorAccent3));
        //seriesLearned.setDrawDataPoints(true);
        //seriesLearned.setDataPointsRadius(15);
        seriesLearned.setThickness(15);

        LineGraphSeries<DataPoint> seriesWorkload = new LineGraphSeries<>(datapointsWorkload);
        //seriesWorkload.setDrawBackground(true);
        //seriesWorkload.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent3Light));
        seriesWorkload.setColor(ContextCompat.getColor(getContext(), R.color.md_orange_500));
        seriesWorkload.setThickness(15);

        GraphView graph = (GraphView) view.findViewById(R.id.graph);
        Calendar calendar1wAgo = Calendar.getInstance();
        calendar1wAgo.setTime(new Date());
        calendar1wAgo.add(Calendar.DATE, -6);
        graph.getViewport().setMinX(calendar1wAgo.getTime().getTime());
        graph.getViewport().setMaxX(new Date().getTime());
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getGridLabelRenderer().setHumanRounding(false);
        graph.getGridLabelRenderer().setNumHorizontalLabels(VISIBLE_DAYS);
        graph.getGridLabelRenderer()
                .setLabelFormatter(new DefaultLabelFormatter() {
                    @Override
                    public String formatLabel(double value, boolean isValueX) {
                        if (isValueX) {
                            long totalSeconds = new Double(value).longValue();
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(new Date(totalSeconds));
                            int day = cal.get(Calendar.DAY_OF_MONTH);

                            return String.format(day + ".");
                        } else {
                            return super.formatLabel(value, isValueX);
                        }

                    }
                });
        graph.addSeries(seriesWorkload);
        graph.addSeries(seriesLearned);

    }
}
