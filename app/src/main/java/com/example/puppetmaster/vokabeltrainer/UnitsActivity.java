package com.example.puppetmaster.vokabeltrainer;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.puppetmaster.vokabeltrainer.DatabaseCommunication.MyDatabase;
import com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem.Vocab;

import java.util.ArrayList;

import static com.example.puppetmaster.vokabeltrainer.UIHelper.convertDpToPixel;


public class UnitsActivity extends AppCompatActivity {
    ArrayList<Unit> listOfUnits = new ArrayList<Unit>();
    ArrayList<Vocab> listOfVocabs = new ArrayList<Vocab>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_units);


        MyDatabase db = new MyDatabase(this);


        Intent intent = getIntent();
        if (null != intent) { //Null Checking
            int selectedTopic = intent.getIntExtra("SELECTED_TOPIC", -1);
            Log.d("Ausgewähltes Topic", Integer.toString(selectedTopic));
            listOfUnits = db.getUnitsOfTopic(selectedTopic);
        }


        for (int i = 0; i < listOfUnits.size(); i++) {
            final LinearLayout ll = (LinearLayout) findViewById(R.id.ll_list_of_units);

            CardView card = new CardView(this);
            int width = (int) (getDisplayWidth() - 2 * convertDpToPixel(16, getApplicationContext()));
            CardView.LayoutParams cardParams = new CardView.LayoutParams(width, CardView.LayoutParams.MATCH_PARENT);
            int margin = convertDpToPixel(16, getApplicationContext());
            cardParams.setMargins(margin, 0, 0, margin);
            card.setUseCompatPadding(true);
            card.setLayoutParams(cardParams);

            LinearLayout cardLayout = new LinearLayout(this);
            cardLayout.setOrientation(LinearLayout.VERTICAL);
            int padding = convertDpToPixel(48, getApplicationContext());
            cardLayout.setPadding(padding, padding, padding, padding);
            card.addView(cardLayout);

            TextView numberOfUnit = new TextView(this);
            numberOfUnit.setText("Lektion " + (i + 1) + " von " + listOfUnits.size());
            cardLayout.addView(numberOfUnit);

            TextView titleOfUnit = new TextView(this);
            titleOfUnit.setText(listOfUnits.get(i).getTitle());
            titleOfUnit.setTextAppearance(android.R.style.TextAppearance_Material_Title);
            cardLayout.addView(titleOfUnit);





            listOfVocabs = db.getVocabOfUnit(listOfUnits.get(i).getId());
            final TextView[] listOfTextViews = new TextView[listOfVocabs.size()];
            int numOfPreview = 10;
            for (int j=0; j < listOfVocabs.size() && j < numOfPreview; j++) {
                listOfTextViews[j] = new TextView(this);
                listOfTextViews[j].setText(listOfVocabs.get(j).getEnglish() + "  <->  " + listOfVocabs.get(j).getEnglish());
                cardLayout.addView(listOfTextViews[j]);
            }

            Button exerciseButton = new Button(this);
            exerciseButton.setText("Lernen");
            cardLayout.addView(exerciseButton);

            ll.addView(card);
        }
    }

    private View.OnClickListener myListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent mIntent = new Intent(v.getContext(), UnitsActivity.class);
            Bundle mBundle = new Bundle();
            mBundle.putString("SELECTED_UNIT", v.getTag().toString());
            Log.d("Unit", v.getTag().toString());
            mIntent.putExtras(mBundle);
            startActivity(mIntent);
        }
    };

    public int getDisplayWidth() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }
}