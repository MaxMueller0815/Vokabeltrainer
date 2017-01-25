package com.example.puppetmaster.vokabeltrainer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.puppetmaster.vokabeltrainer.DatabaseCommunication.MyDatabase;
import java.util.ArrayList;
import static com.example.puppetmaster.vokabeltrainer.UIHelper.convertDpToPixel;

public class TopicsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topics);

        MyDatabase db = new MyDatabase(this);
        setupCards(db.getTopics(), (LinearLayout) findViewById(R.id.ll_topics_layout));
    }

    private void setupCards(ArrayList<Topic> listOfTopics, LinearLayout parentElement) {
        for (int i = 0; i < listOfTopics.size(); i++) {
            CardView card = new CardView(this);
            card.setPreventCornerOverlap(false);
            CardView.LayoutParams cardParams = new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT);
            int margin = convertDpToPixel(16, getApplicationContext());
            cardParams.setMargins(0, 0, 0, margin);
            card.setLayoutParams(cardParams);
            card.setUseCompatPadding(true);

            card.setTag((Integer) listOfTopics.get(i).getId());
            card.setOnClickListener(myListener);

            LinearLayout cardLayout = new LinearLayout(this);
            cardLayout.setOrientation(LinearLayout.VERTICAL);

            ImageView topicImage = new ImageView(this);
            LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT);
            topicImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            topicImage.setLayoutParams(imgParams);
            switch(listOfTopics.get(i).getId()) {
                case 1:
                    topicImage.setImageResource(R.drawable.chapter1);
                    break;
                case 2:
                    topicImage.setImageResource(R.drawable.chapter2);;
                    break;
                case 3:
                    topicImage.setImageResource(R.drawable.chapter3);;
                    break;
                case 4:
                    topicImage.setImageResource(R.drawable.chapter4);;
                    break;
                case 5:
                    topicImage.setImageResource(R.drawable.chapter5);;
                    break;
                case 6:
                    topicImage.setImageResource(R.drawable.chapter6);;
                    break;
                case 7:
                    topicImage.setImageResource(R.drawable.chapter7);;
                    break;
            }

            TextView topicTitle = new TextView(this);
            topicTitle.setText(listOfTopics.get(i).getId() + ". " + listOfTopics.get(i).getTitle());
            int padding = convertDpToPixel(16, getApplicationContext());
            topicTitle.setPadding(padding, padding, padding, padding);

            cardLayout.addView(topicImage);
            cardLayout.addView(topicTitle);
            card.addView(cardLayout);
            parentElement.addView(card);
        }
    }

    private View.OnClickListener myListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent nextAction = new Intent(v.getContext(), UnitsActivity.class);
            Bundle mBundle = new Bundle();
            mBundle.putInt("SELECTED_TOPIC", (int) v.getTag());
            nextAction.putExtras(mBundle);
            System.out.println(v.getTag());
            startActivity(nextAction);
        }
    };
}
