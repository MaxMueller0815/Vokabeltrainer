package com.example.puppetmaster.vokabeltrainer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ListView;

import com.example.puppetmaster.vokabeltrainer.Adapter.UnitAdapter;
import com.example.puppetmaster.vokabeltrainer.Adapter.VocabAdapter;
import com.example.puppetmaster.vokabeltrainer.DatabaseCommunication.MyDatabase;

import java.util.ArrayList;

public class UnitsActivity extends AppCompatActivity {
    ArrayList<Unit> listOfUnits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_units);
        getListOfUnits();

        RecyclerView recList = (RecyclerView) findViewById(R.id.recycler_units);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        recList.setLayoutManager(llm);

        UnitAdapter unitAdapter = new UnitAdapter(listOfUnits);
        recList.setAdapter(unitAdapter);
        //setupCards();
    }

    private void getListOfUnits() {
        Intent intent = getIntent();
        if (null != intent) { //Null Checking
            int selectedTopic = intent.getIntExtra("SELECTED_TOPIC", -1);
            Log.d("Ausgewähltes Topic", Integer.toString(selectedTopic));
            MyDatabase db = new MyDatabase(this);
            listOfUnits = db.getUnitsOfTopic(selectedTopic);
            Log.d("Num Cards", "" + listOfUnits.size());

        }
    }

//    public int getDisplayWidth() {
//        Display display = getWindowManager().getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//        return size.x;
//    }
//
//    public void setupCards() {
//        final LinearLayout ll = (LinearLayout) findViewById(R.id.ll_list_of_units);
//        for (int i = 0; i < listOfUnits.size(); i++) {
//            // Card
//            CardView card = new CardView(this);
//            int idCard = View.generateViewId();
//            card.setId(idCard);
//            int width = (int) (getDisplayWidth() - 2 * convertDpToPixel(36, getApplicationContext()));
//            CardView.LayoutParams cardParams = new CardView.LayoutParams(width, CardView.LayoutParams.MATCH_PARENT);
//            int margin = convertDpToPixel(16, getApplicationContext());
//            cardParams.setMargins(margin, 0, 0, margin);
//            card.setUseCompatPadding(true);
//            card.setLayoutParams(cardParams);
//            ll.addView(card);
//
//            // RelativeLayout in Card
//            LinearLayout cardLayout = new LinearLayout(this);
//            cardLayout.setOrientation(LinearLayout.VERTICAL);
//            //RelativeLayout.LayoutParams relParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
//            int padding = convertDpToPixel(48, getApplicationContext());
//            cardLayout.setPadding(padding, padding, padding, padding);
//            //cardLayout.setLayoutParams(relParams);
//            card.addView(cardLayout);
//
//            // Header-Texte
//            TextView numberOfUnit = new TextView(this);
//            numberOfUnit.setText("Lektion " + (i + 1) + " von " + listOfUnits.size());
//            int idNumberOfUnit = View.generateViewId();
//            numberOfUnit.setId(idNumberOfUnit);
//            cardLayout.addView(numberOfUnit);
//
//            TextView titleOfUnit = new TextView(this);
//            titleOfUnit.setTextAppearance(android.R.style.TextAppearance_Material_Title);
//            RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//            titleOfUnit.setText(listOfUnits.get(i).getTitle());
//            //textParams.addRule(RelativeLayout.BELOW, idNumberOfUnit);
//            //titleOfUnit.setLayoutParams(textParams);
//            //int idTitleOfUnit = View.generateViewId();
//            //numberOfUnit.setId(idTitleOfUnit);
//            cardLayout.addView(titleOfUnit);
//
//            listOfVocabs = db.getVocabOfUnit(listOfUnits.get(i).getId());
//            final TextView[] listOfTextViews = new TextView[listOfVocabs.size()];
//            for (int j=0; j < listOfVocabs.size(); j++) {
//                listOfTextViews[j] = new TextView(this);
//                listOfTextViews[j].setText("" + listOfVocabs.get(j).getGerman());
//                cardLayout.addView(listOfTextViews[j]);
//            }
//
//            Button exerciseButton = new Button(this);
//            exerciseButton.setText("Lernen");
//            RelativeLayout.LayoutParams buttonParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//            buttonParams.addRule(RelativeLayout.ALIGN_BOTTOM, idCard);
//            exerciseButton.setLayoutParams(buttonParams);
//            cardLayout.addView(exerciseButton);
//
//
//            Log.d("Card", "Neue Card angelegt" + i);
//        }
//    }
}
