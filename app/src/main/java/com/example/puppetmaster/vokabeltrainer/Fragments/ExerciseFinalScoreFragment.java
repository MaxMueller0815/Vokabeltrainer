package com.example.puppetmaster.vokabeltrainer.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.puppetmaster.vokabeltrainer.Activities.ExerciseActivity;
import com.example.puppetmaster.vokabeltrainer.R;
import com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem.Vocab;

import java.util.ArrayList;

public class ExerciseFinalScoreFragment extends Fragment {
    View view;
    int counterCorrect;
    ArrayList<Vocab> allVocab;
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
        counterCorrect = ((ExerciseActivity)this.getActivity()).getCounterCorrect();
        allVocab = ((ExerciseActivity)this.getActivity()).getAllVocab();

        ImageView ivSolution = (ImageView) view.findViewById(R.id.iv_solution);
        int listSize = allVocab.size();
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
        return view;
    }


}
