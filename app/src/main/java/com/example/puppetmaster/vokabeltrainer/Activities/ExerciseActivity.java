package com.example.puppetmaster.vokabeltrainer.Activities;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import com.example.puppetmaster.vokabeltrainer.Entities.ExerciseLogic;
import com.example.puppetmaster.vokabeltrainer.Fragments.ExerciseFinalScoreFragment;
import com.example.puppetmaster.vokabeltrainer.Fragments.ExerciseInputFragment;
import com.example.puppetmaster.vokabeltrainer.Fragments.ExerciseLearnFragment;
import com.example.puppetmaster.vokabeltrainer.R;
import com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem.SpacedRepititionSystem;
import com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem.Vocab;

import java.util.ArrayList;

/**
 * Die Exercise-Activity wird angezeigt, wenn der Nutzer Wörter einer Unit lernen möchte oder Wörter wiederholt, die vom SRS-Hintergrundservice vorgeschlagen werden. Die Activity zeigt aufeinanderfolgend folgende 4 Fragments an:
 - ExerciseLearnFragment: Neue Wörter werden eingeblendet, damit der Nutzer sie sich einprägen kann, es findet keine Abfrage statt
 - ExerciseInputFragment: Hier muss der Nutzer die richtige Übersetzung eintippen
 - ExerciseSolutionFragment: Ob die Vokabel richtig übersetzt wurde, wird in diesem Fragment angezeigt
 - ExerciseFinalScoreFragment: Zu Ende eines Sets erhält der Nutzer eine Auswertung seiner Gesamtleistung in dieser Runde

 Die rundenbasierte Logik wird durch ExerciseLogic geregelt.
 */


public class ExerciseActivity extends AppCompatActivity {
    ExerciseLogic exercise;
    private boolean fromSRS;
    private static FragmentManager fragmentManager;
    public SpacedRepititionSystem srs;
    private FragmentTransaction ft;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        // SRS wird später für die Auswertung und Aktualisierung in der DB benötigt
        srs = new SpacedRepititionSystem(this);

        if (getIntent().hasExtra("WORD_LIST")) { //Null Checking
            // Wenn der Nutzer (manuell) eine Unit auswählt, die er/sie lernen möchte
            // TODO: Die Unterscheidung zwischen fromSRS müsste man eingentlich löschen können
            fromSRS = false;
            exercise = new ExerciseLogic((ArrayList<Vocab>) getIntent().getSerializableExtra("WORD_LIST"));
        } else {
            // Wenn es sich um eine Wiederholung bereits gelernter Wörter handelt, die durch das Notification-System ausgelöst werden, werden die Liste der abzuarbeitenden Vokabeln vom SRS ausgewählt.
            exercise = new ExerciseLogic(new SpacedRepititionSystem(getApplicationContext()).getCurrentRequestList());
        }

        progressBar = (ProgressBar) findViewById(R.id.pb_exercise);
        progressBar.setMax(exercise.getNumTurns());

        fragmentManager = getFragmentManager();
        startNextTurn();
    }

    /**
     * Wählt passende Ansicht bei einer neuen Runde
     */
    private void startNextTurn() {
        ft = fragmentManager.beginTransaction();
        ft.setCustomAnimations(R.animator.flight_left_in, R.animator.flip_right_out);
        progressBar.setProgress(exercise.getNextTurn() + 2);

        if (exercise.nextVocab().getSrsLevel() == 0) {
            // Ansicht laden, in der der Nutzer neue, unbekannte Vokabeln erlernt (allg. Wörter auf SRS-Level 0)
            ft.replace(R.id.container_exercise, new ExerciseLearnFragment()).commit();
        } else {
            // Ansicht laden, in dem das Wort direkt abgefragt wird
            ft.replace(R.id.container_exercise, new ExerciseInputFragment()).commit();
        }
    }

    /**
     * Steuert Auswertung am Ende einer Runde und wählt die richtige Ansicht für die nächste Runde
     * Diese Funktion wird im ExerciseSolutionFragement aufgerufen
     * @param isCorrect Vokabel wurde richtig übersetzt
     */
    public void evaluateResult(boolean isCorrect) {
        exercise.evaluate(isCorrect);
        if (exercise.hasNextTurn()) {
            startNextTurn();
        } else {
            // Finale Bewertung einblenden, wenn keine weiteren Wörter gelernt/wiederholt werden müssen
            ft = fragmentManager.beginTransaction();
            ft.setCustomAnimations(R.animator.flight_left_in, R.animator.flip_right_out);
            ft.replace(R.id.container_exercise, new ExerciseFinalScoreFragment()).commit();
        }
    }

    /**
     * Sorgt dafür, dass, nachdem ein unbekanntes Wort zum ersten Mal angezeigt wurde, direkt danach eine Abfrage stattfindet
     */
    public void markAsLearned() {
        ft = fragmentManager.beginTransaction();
        ft.setCustomAnimations(R.animator.flight_left_in, R.animator.flip_right_out);
        ft.replace(R.id.container_exercise, new ExerciseInputFragment()).commit();
    }

    // Diverse Getter, die in den zur Activity gehörenden Fragements aufgerufen werden
    public ExerciseLogic getExercise() {
        return exercise;
    }

    public SpacedRepititionSystem getSrs() {
        return srs;
    }

    // Eine AlertBox bittet den Nutzer darum, den Vorzeitigen Abbruch eines Übung-Sets zu bestätigen
    @Override
    public void onBackPressed() {
        showAlertDialogExit();
    }

    private void showAlertDialogExit() {
        // Setup View for AlertDialog
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // Dialog cancelable with back key
        alertDialogBuilder.setCancelable(true);

        // Setup title and message of alertDialog
        alertDialogBuilder.setIcon(R.drawable.ic_exit);
        alertDialogBuilder.setTitle(R.string.exit_title);
        alertDialogBuilder.setMessage(R.string.exit_message);

        // Setup Buttons for dialog
        alertDialogBuilder.setPositiveButton(R.string.exit_title, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        alertDialogBuilder.setNegativeButton(R.string.language_negative_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
