package com.example.puppetmaster.vokabeltrainer.Fragments;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
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
 * Fragment, in dem lediglich der englische Begriff angezeigt wird. Der Nutzer kann über ein Eingabefeld die deutsche Übersetzung eintippen
 */
public class ExerciseInputFragment extends Fragment {
    private View view;
    private Vocab currentVocab;
    EditText etUserAnswer;

    public ExerciseInputFragment() {
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_exercise_input, container, false);

        currentVocab = ((ExerciseActivity) this.getActivity()).getExercise().getCurrentVocab();

        // Englischer Begriff
        TextView tvQuestion = (TextView) view.findViewById(R.id.tv_question);
        tvQuestion.setText(currentVocab.getEnglish());

        // Eingabefeld für dt. Begriff
        etUserAnswer = (EditText) view.findViewById(R.id.et_user_answer);

        // Next-Button
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab_exercise_input);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeKeyboard();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                Fragment exerciseSolutionFragment = new ExerciseSolutionFragment();
                Bundle inputBundle = new Bundle();
                inputBundle.putString("user_answer", String.valueOf(etUserAnswer.getText()));
                exerciseSolutionFragment.setArguments(inputBundle);
                ft.setCustomAnimations(R.animator.flight_left_in, R.animator.flip_right_out);
                ft.replace(R.id.container_exercise, exerciseSolutionFragment);
                ft.commit();
            }
        });
        return view;
    }

    /**
     * Sobald die Flip-Animation zu Ende ist, wird automatisch die Tastatur ausgeklappt
     */
    @Override
    public void onResume() {
        super.onResume();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showKeyboard();
            }
        }, 500);

    }

    /**
     * Klappt Tastatur ein
     */
    private void removeKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etUserAnswer.getWindowToken(), 0);
    }

    /**
     * Klappt Tastatur aus
     */
    private void showKeyboard() {
        etUserAnswer.requestFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(etUserAnswer, InputMethodManager.SHOW_IMPLICIT);
    }
}
