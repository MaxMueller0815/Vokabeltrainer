package com.example.puppetmaster.vokabeltrainer.Fragments;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.puppetmaster.vokabeltrainer.Activities.ExerciseActivity;
import com.example.puppetmaster.vokabeltrainer.R;
import com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem.Vocab;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExerciseInputFragment extends Fragment {
    private View view;
    private Vocab currentVocab;
    private String userAnswer = "";
    EditText solutionInput;

    public ExerciseInputFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_exercise_input, container, false);
        currentVocab = ((ExerciseActivity)this.getActivity()).getCurrentVocab();
        TextView tvQuestion = (TextView) view.findViewById(R.id.tv_question);
        tvQuestion.setText(currentVocab.getEnglish());
        solutionInput = (EditText) view.findViewById(R.id.et_user_answer);


        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab_exercise_input);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeKeyboard();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                Fragment exerciseSolutionFragment = new ExerciseSolutionFragment();//Get Fragment Instance
                Bundle inputBundle = new Bundle();
                inputBundle.putString("user_answer", String.valueOf(solutionInput.getText()));
                exerciseSolutionFragment.setArguments(inputBundle);//Finally set argument bundle to fragment
                ft.setCustomAnimations(R.animator.flight_left_in, R.animator.flip_right_out);
                ft.replace(R.id.container_exercise, exerciseSolutionFragment);
                ft.commit();
            }
        });
        return view;
    }

    private void removeKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(solutionInput.getWindowToken(), 0);
    }

//    private void showKeyboard() {
//        InputMethodManager imm =  (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.showSoftInput(solutionInput, InputMethodManager.SHOW_IMPLICIT);
//    }



}
