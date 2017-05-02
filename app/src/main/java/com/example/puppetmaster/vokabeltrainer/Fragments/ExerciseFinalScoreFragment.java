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

/**
 * Fragment, das am Ende eines Abfrage-Sets angezeigt wird. Der Ansicht kann die Gesamtleistung, sowie eine Liste der richtig bzw. falsch beantworteten Fragen entnommen werden
 */

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
        view = inflater.inflate(R.layout.fragment_exercise_final_score, container, false);
        ExerciseLogic exercise = ((ExerciseActivity)this.getActivity()).getExercise();

        // Tab-Ansicht initialisieren
        TabHost host = (TabHost) view.findViewById(R.id.tabHost);
        host.setup();

        //Tab Overview (Gesamtleistung)
        TabHost.TabSpec spec = host.newTabSpec("Overview");
        spec.setContent(R.id.tab_overview);
        spec.setIndicator("Overview");
        host.addTab(spec);
        int counterCorrect = exercise.getVocabsCorrect().size();
        int counterSet = ((ExerciseActivity)this.getActivity()).getExercise().getNumTurns();
        double result = (double) counterCorrect / (double) counterSet ;
        String title = "";
        String description = "You translated " + counterCorrect +  " out of "  + counterSet +  " vocabs correctly.";
        ImageView ivSolution = (ImageView) view.findViewById(R.id.iv_solution);
        // Bild-Ressource auswählen
        if(result >= 0.90){
            ivSolution.setImageResource(R.drawable.smiley_1);
            title = "Excellent!";
        }else if(result < 0.90 && result >= 0.60){
            ivSolution.setImageResource(R.drawable.smiley_2);
            title = "Good job!";
        } else if(result < 0.60 && result >= 0.20){
            ivSolution.setImageResource(R.drawable.smiley_3);
            title = "There is room for improvement.";
        } else if(result < 0.20 && result > 0.00){
            ivSolution.setImageResource(R.drawable.smiley_4);
            title = "Celebrate every tiny victory!";
        }else {
            ivSolution.setImageResource(R.drawable.smiley_5);
            title = "At least you tried";
            description = "But you should learn more regularly!";
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


        //Tab mit Liste der falsch übersetzen Wörter
        spec = host.newTabSpec("False");
        spec.setContent(R.id.tab_false);
        spec.setIndicator("False");
        host.addTab(spec);

        ListView listViewFalse = (ListView) view.findViewById(R.id.list_false_vocabs);
        if (exercise.getVocabsCorrect().size() > 0) {
            VocabAdapter adapterFalse = new VocabAdapter(getContext(), exercise.getVocabsFalse());
            listViewFalse.setAdapter(adapterFalse);
        } else {
            listViewFalse.setVisibility(View.GONE);
        }

        //Tab mit Liste der richtig übersetzen Wörter
        spec = host.newTabSpec("Correct");
        spec.setContent(R.id.tab_correct);
        spec.setIndicator("Correct");
        host.addTab(spec);

        ListView listViewCorrect = (ListView) view.findViewById(R.id.list_correct_vocabs);
        if (exercise.getVocabsCorrect().size() > 0) {
            VocabAdapter adapterCorrect = new VocabAdapter(getContext(), exercise.getVocabsCorrect());
            listViewCorrect.setAdapter(adapterCorrect);
        } else {
            listViewCorrect.setVisibility(View.GONE);
        }

        return view;
    }


}
