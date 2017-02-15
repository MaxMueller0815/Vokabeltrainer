package com.example.puppetmaster.vokabeltrainer.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.puppetmaster.vokabeltrainer.DatabaseCommunication.MyDatabase;
import com.example.puppetmaster.vokabeltrainer.R;
import com.example.puppetmaster.vokabeltrainer.SRSTesterActivity;
import com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem.Vocab;
import com.example.puppetmaster.vokabeltrainer.Topic;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.lang.reflect.Array;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HomeFragment extends Fragment {
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        initGraph();

        Button srsButton = (Button) view.findViewById(R.id.btn_srs_test);
        srsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SRSTesterActivity.class));
            }
        });


        return view;
    }

    private void initGraph() {
        final int VISIBLE_DAYS = 7;

        Calendar calendarPast = Calendar.getInstance();
        calendarPast.setTime(new Date());

        ArrayList<Vocab> vocabs = new MyDatabase(getContext()).getListOfAllVocab();
        DataPoint[] datapoints = new DataPoint[VISIBLE_DAYS];

        for (int i = VISIBLE_DAYS - 1; i >= 0; i--) {
            double counter = 0;
            for (Vocab vocab : vocabs) {
                Calendar calenderOfVocab = Calendar.getInstance();
                calenderOfVocab.setTime(vocab.getLastRevision());

                if (calenderOfVocab.get(Calendar.YEAR) == calendarPast.get(Calendar.YEAR) &&
                        calenderOfVocab.get(Calendar.DAY_OF_YEAR) == calendarPast.get(Calendar.DAY_OF_YEAR)) {
                    counter++;
                }
            }
            datapoints[i] = new DataPoint(calendarPast.getTime(), counter);
            //datapoints[i] = new DataPoint(i, counter);
            Log.i("Datapoint added", new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(datapoints[i].getX()) + ", " + datapoints[i].getY());
            calendarPast.add(Calendar.DATE, -1);
        }


        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(datapoints);
        series.setColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(15);
        series.setThickness(15);

        GraphView graph = (GraphView) view.findViewById(R.id.graph);
        calendarPast.add(Calendar.DATE, 1);
        graph.getViewport().setMinX(calendarPast.getTime().getTime());
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
        graph.addSeries(series);
    }


}
