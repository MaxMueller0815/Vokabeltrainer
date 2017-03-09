package com.example.puppetmaster.vokabeltrainer.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.puppetmaster.vokabeltrainer.Activities.ExerciseActivity;
import com.example.puppetmaster.vokabeltrainer.R;
import com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem.Vocab;

import java.util.ArrayList;

public class ExerciseResultFragment extends Fragment {
    View view;
    int counterCorrect;
    ArrayList<Vocab> allVocab;
    public ExerciseResultFragment() {
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
        view = inflater.inflate(R.layout.fragment_exercise_result, container, false);
        counterCorrect = ((ExerciseActivity)this.getActivity()).getCounterCorrect();
        allVocab = ((ExerciseActivity)this.getActivity()).getAllVocab();

        ImageView ivSolution = (ImageView) view.findViewById(R.id.iv_solution);
        int listSize = allVocab.size();
        double result = (double) counterCorrect / (double) listSize ;
        String title = "";
        String description = "Du hast " + counterCorrect +  " von "  + listSize +  " Vokabeln richtig!";

        if(result >= 0.90){
            ivSolution.setImageResource(R.drawable.smiley_cool);
            title = "Super!";

        }else if(result < 0.90 && result >= 0.60){
            ivSolution.setImageResource(R.drawable.smiley_happy);
            title = "Glückwunsch!";
        }
        else if(result < 0.60 && result >= 0.20){
            ivSolution.setImageResource(R.drawable.smiley_question);
            title = "Das geht besser!";
        }
        else if(result < 0.20 && result > 0.00){
            ivSolution.setImageResource(R.drawable.smiley_sad);
            title = "Schade!";
        }else {
            ivSolution.setImageResource(R.drawable.smiley_sad);
            title = "Schade!";
            description = "Du hast keine einzige Vokabel richtig!";
        }
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_solution_title);
        tvTitle.setText(title);
        TextView tvDescription = (TextView) view.findViewById(R.id.tv_solution_description);
        tvDescription.setText(description);
        return view;
    }


}
