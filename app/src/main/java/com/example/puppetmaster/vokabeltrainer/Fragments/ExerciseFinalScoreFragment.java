package com.example.puppetmaster.vokabeltrainer.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.puppetmaster.vokabeltrainer.Activities.ExerciseActivity;
import com.example.puppetmaster.vokabeltrainer.Adapter.VocabAdapter;
import com.example.puppetmaster.vokabeltrainer.Entities.ExerciseLogic;
import com.example.puppetmaster.vokabeltrainer.R;
import com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem.Vocab;

import java.util.ArrayList;

public class ExerciseFinalScoreFragment extends Fragment {
    View view;
    public ExerciseFinalScoreFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_exercise_final_score, container, false);
        ExerciseLogic exercise = ((ExerciseActivity)this.getActivity()).getExercise();
        int counterCorrect = exercise.getVocabsCorrect().size();
        int listSize = ((ExerciseActivity)this.getActivity()).getExercise().getVocabsAll().size();

        ImageView ivSolution = (ImageView) view.findViewById(R.id.iv_solution);
        double result = (double) counterCorrect / (double) listSize ;
        String title = "";
        String description = "Du hast " + counterCorrect +  " von "  + listSize +  " Vokabeln richtig!";

        if(result >= 0.90){
            ivSolution.setImageResource(R.drawable.smiley_1);
            title = "Super!";
        }else if(result < 0.90 && result >= 0.60){
            ivSolution.setImageResource(R.drawable.smiley_2);
            title = "Glückwunsch!";
        } else if(result < 0.60 && result >= 0.20){
            ivSolution.setImageResource(R.drawable.smiley_3);
            title = "Das geht besser!";
        } else if(result < 0.20 && result > 0.00){
            ivSolution.setImageResource(R.drawable.smiley_4);
            title = "Schade!";
        }else {
            ivSolution.setImageResource(R.drawable.smiley_5);
            title = "Schade!";
            description = "Du hast keine einzige Vokabel richtig!";
        }
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_solution_title);
        tvTitle.setText(title);
        TextView tvDescription = (TextView) view.findViewById(R.id.tv_solution_description);
        tvDescription.setText(description);

        Button returnButton = (Button) view.findViewById(R.id.btn_continue);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        ListView listViewFalse = (ListView) view.findViewById(R.id.list_false_vocabs);
        if (exercise.getVocabsCorrect().size() > 0) {
            VocabAdapter adapterFalse = new VocabAdapter(getContext(), exercise.getVocabsFalse());
            listViewFalse.setAdapter(adapterFalse);
        } else {
            listViewFalse.setVisibility(View.GONE);
        }

        ListView listViewCorrect = (ListView) view.findViewById(R.id.list_correct_vocabs);
        if (exercise.getVocabsCorrect().size() > 0) {
            VocabAdapter adapterCorrect = new VocabAdapter(getContext(), exercise.getVocabsCorrect());
            listViewCorrect.setAdapter(adapterCorrect);
        } else {
            listViewCorrect.setVisibility(View.GONE);
        }

        TabHost host = (TabHost) view.findViewById(R.id.tabHost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Overview");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Overview");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("False");
        spec.setContent(R.id.tab2);
        spec.setIndicator("False");
        host.addTab(spec);

        //Tab 3
        spec = host.newTabSpec("Correct");
        spec.setContent(R.id.tab3);
        spec.setIndicator("Correct");
        host.addTab(spec);
        return view;
    }


}
