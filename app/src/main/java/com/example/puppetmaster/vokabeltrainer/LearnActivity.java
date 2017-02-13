package com.example.puppetmaster.vokabeltrainer;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.puppetmaster.vokabeltrainer.DatabaseCommunication.MyDatabase;
import com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem.Vocab;
import com.google.gson.Gson;

import java.util.ArrayList;

public class LearnActivity extends AppCompatActivity {


    private RelativeLayout vocab_knowing_language;
    private TextView vocab_learning_language_text_view;
    private TextView vocab_knowing_language_text_view;
    private Button vocab_counter_button;
    private ImageButton imageButton_exit;
    private boolean isBackVisible = false;
    private Handler handler;
    private ArrayList<Vocab> allVocab;
    private int counter_vocab = 0;
    private int counter_vocab_num = 1;
    private int listSize;
    private FloatingActionButton vocab_fab;
    private EditText editTextInput;
    private String textInput;
    private ImageView imageView;
    private int click_counter_fab = 0;
    private TextView textViewInput;
    private int counter_correct_answer = 0;
    private String frontCard = "";
    private String backCard ="";
    private double randomNum;
    private double result = 0.0;

    private boolean learnList = false;

    private AlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);
        readIntent();
        learnDirection();
        initUIElements();
        setupVocabAnimation();
        setupExitButton();
    }

    private void readIntent() {
        Intent intent = getIntent();
        if (null != intent) { //Null Checking
            Gson gson = new Gson();
            Unit unit = gson.fromJson(intent.getStringExtra("SELECTED_UNIT"), Unit.class);
            allVocab = unit.getVocabsOfUnit();
            listSize = allVocab.size();
        }
    }

    private void learnDirection() {
        frontCard = allVocab.get(counter_vocab).getGerman();
        backCard = allVocab.get(counter_vocab).getEnglish();
        /*Bundle bundle = getIntent().getExtras();
        boolean knowing_to_learning = bundle.getBoolean("knowingToLearning");
        boolean learning_to_knowing = bundle.getBoolean("learningToKnowing");
        boolean mixed = bundle.getBoolean("mixed");

        String listName = bundle.getString("listName");


        if (listName != null){
            learnList = true;
            repopulateVocabArrayWithList(listName);
        }

        if (knowing_to_learning){
            frontCard = allVocab.get(counter_vocab).getSourceVocab();
            backCard = allVocab.get(counter_vocab).getTargetVocab();
        }

        if (learning_to_knowing){
            backCard = allVocab.get(counter_vocab).getSourceVocab();
            frontCard = allVocab.get(counter_vocab).getTargetVocab();
        }

        if (mixed){

            randomNum =  Math.random();

            if(randomNum < 0.5) {
                backCard = allVocab.get(counter_vocab).getSourceVocab();
                frontCard = allVocab.get(counter_vocab).getTargetVocab();
            }
            if(randomNum >= 0.5) {
                frontCard = allVocab.get(counter_vocab).getSourceVocab();
                backCard = allVocab.get(counter_vocab).getTargetVocab();
            }
        }*/
    }

    // TODO: Sollte man das nicht durch die handleAnswer/checkAnswer Funktion in SRS machen?
    private void compareSolution(){
        textInput = editTextInput.getText().toString();
        String currentSolution = backCard;
        //textInput.trim();
        //currentSolution.trim();
        //allVocab.get(counter_vocab).increaseAsked();

        if(currentSolution.trim().equalsIgnoreCase(textInput.trim())) {
            imageView.setImageResource(R.drawable.smiley_happy);
            imageView.setVisibility(View.VISIBLE);

            allVocab.get(counter_vocab).increaseCountCorrect();
            //dbAdmin.updateAskedAndKnown(allVocab.get(counter_vocab));
            counter_correct_answer++;
        } else {
            imageView.setImageResource(R.drawable.smiley_question);
            imageView.setVisibility(View.VISIBLE);

            //allVocab.get(counter_vocab).resetKnown();
            //dbAdmin.updateAskedAndKnown(allVocab.get(counter_vocab));
        }
    }

    private void updateNumCounter() {
        counter_vocab_num++;
        vocab_counter_button.setText(counter_vocab_num + " / " + listSize);
    }

    /*private void fillListFromDB(){
        allVocab = new ArrayList<>();
        allVocab.addAll(NavigationFragmentThree.getVocItems());

        listSize = allVocab.size();
        Collections.shuffle(allVocab);
        Collections.sort(allVocab, new CustomComparator());
    }

    private void initDB(){
        dbAdmin = new DBAdmin(this);
        dbAdmin.open();
    }

    protected void onDestroy() {
        dbAdmin.close();
        super.onDestroy();
    }*/

    private void initUIElements() {
        vocab_knowing_language = (RelativeLayout)findViewById(R.id.vocab_knowing_language);
        vocab_learning_language_text_view = (TextView)findViewById(R.id.vocab_learning_language_text_view);
        vocab_knowing_language_text_view =(TextView)findViewById(R.id.vocab_knowing_language_text_view);

        vocab_knowing_language_text_view.setText(frontCard);
        vocab_learning_language_text_view.setText(backCard);

        vocab_counter_button = (Button) findViewById(R.id.vocab_counter);
        vocab_counter_button.setText(counter_vocab_num + " / " + listSize);
        vocab_fab = (FloatingActionButton) findViewById(R.id.vocab_fab_next);
        editTextInput = (EditText)findViewById(R.id.vocab_edit_text);
        imageView = (ImageView) findViewById(R.id.image_view_correct);
        imageView.setVisibility(View.INVISIBLE);

        textViewInput = (TextView) findViewById(R.id.vocab_text_view);

        imageButton_exit = (ImageButton) findViewById(R.id.imageButton_exit);
    }

    private void setupVocabAnimation(){

        final AnimatorSet setRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(),
                R.animator.flip_right_out);

        final AnimatorSet setLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(),
                R.animator.flight_left_in);


        vocab_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isBackVisible) {

                    vocab_fab.setClickable(false);

                    setRightOut.setTarget(vocab_knowing_language_text_view);
                    setLeftIn.setTarget(vocab_learning_language_text_view);
                    setRightOut.start();
                    setLeftIn.start();
                    isBackVisible = true;

                    handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            vocab_fab.setClickable(true);
                        }
                    }, 500);
                }

                if (click_counter_fab == 0) {
                    removeKeyboard();
                    editTextToTextView();
                    vocab_fab.setImageResource(R.drawable.ic_arrow_forward);
                    compareSolution();
                    click_counter_fab++;
                } else {
                    counter_vocab++;

                    if (counter_vocab < listSize) {
                        vocab_fab.setClickable(false);

                        setRightOut.setTarget(vocab_learning_language_text_view);
                        setLeftIn.setTarget(vocab_knowing_language_text_view);
                        setRightOut.start();
                        setLeftIn.start();
                        isBackVisible = false;

                        handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                learnDirection();
                                vocab_knowing_language_text_view.setText(frontCard);
                                vocab_learning_language_text_view.setText(backCard);
                                click_counter_fab--;

                                textViewToEditText();

                                vocab_fab.setImageResource(R.drawable.ic_done);
                                imageView.setVisibility(View.INVISIBLE);

                                updateNumCounter();
                            }
                        }, 250);

                    } else {
                        showAlertDialog();
                    }
                }


                handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        vocab_fab.setClickable(true);
                    }
                }, 500);
            }
        });
    }

    private void showAlertDialog() {
        // Setup View for AlertDialog
        final View view = LayoutInflater.from(LearnActivity.this).inflate(R.layout.result_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LearnActivity.this);
        alertDialogBuilder.setView(view);

        // Dialog cancelable with back key
        alertDialogBuilder.setCancelable(false);

        // Setup title and message of alertDialog
        alertDialogBuilder.setIcon(R.drawable.ic_trophies);
        alertDialogBuilder.setTitle(R.string.result);

        // Setup Buttons for dialog
        alertDialogBuilder.setPositiveButton(R.string.finished, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });


        alertDialog = alertDialogBuilder.create();
        alertDialog.show();


        // Edit Design alertDialog
        Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        //positiveButton.setTextColor(getResources().getColor(R.color.colorPrimary));

        ImageView smileyIV = (ImageView) alertDialog.findViewById(R.id.image_view_smiley);
        String sourceString = "";
        result= (double) counter_correct_answer / (double)listSize;

        if(result >= 0.90){
            smileyIV.setImageResource(R.drawable.smiley_cool);
            sourceString = "Super!" + "<br>" + "Du hast " + "<b>" + counter_correct_answer +  " / "  + listSize +  "</b> " + " Vokabeln richtig!"+ "</br>";
        }else if(result < 0.90 && result >= 0.60){
            smileyIV.setImageResource(R.drawable.smiley_happy);
            sourceString = "Glückwunsch!" + "<br>" + "Du hast " + "<b>" + counter_correct_answer +  " / "  + listSize +  "</b> " + " Vokabeln richtig!"+ "</br>";
        }
        else if(result < 0.60 && result >= 0.20){
            smileyIV.setImageResource(R.drawable.smiley_question);
            sourceString = "Das geht besser!" + "<br>" + "Du hast nur " + "<b>" + counter_correct_answer +  " / "  + listSize +  "</b> " + " Vokabeln richtig!"+ "</br>";
        }
        else if(result < 0.20 && result > 0.00){
            smileyIV.setImageResource(R.drawable.smiley_sad);
            sourceString = "Schade!" + "<br>" + "Du hast nur " + "<b>" + counter_correct_answer +  " / "  + listSize +  "</b> " + " Vokabeln richtig!"+ "</br>";
        }else {
            smileyIV.setImageResource(R.drawable.smiley_sad);
            sourceString = "Schade!" + "<br>" + "Du hast " + "<b>" + "keine einzige" + "</b> " + " Vokabel richtig!"+ "</br>";
        }
        TextView textView_result = (TextView) alertDialog.findViewById(R.id.textView_result);
        textView_result.setText(Html.fromHtml(sourceString));

    }


    private void setupExitButton() {
        imageButton_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialogExit();

            }
        });
    }

    private void showAlertDialogExit() {
        // Setup View for AlertDialog
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LearnActivity.this);

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

        alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        // Edit Design alertDialog
        Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        //positiveButton.setTextColor(getResources().getColor(R.color.colorPrimary));

        Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        //negativeButton.setTextColor(getResources().getColor(R.color.colorPrimary));
    }

    private void textViewToEditText() {
        textViewInput.setVisibility(View.GONE);
        editTextInput.setVisibility(View.VISIBLE);
        editTextInput.setText("");
    }

    private void editTextToTextView() {
        editTextInput.setVisibility(View.GONE);
        textViewInput.setVisibility(View.VISIBLE);
        textViewInput.setText(editTextInput.getText().toString());
    }

    private void removeKeyboard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editTextInput.getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {
        showAlertDialogExit();
    }

    /*private void repopulateVocabArrayWithList(String listName) {
        allVocab = dbAdmin.getAllVocabForList(listName);
        listSize = allVocab.size();
    }*/
}
