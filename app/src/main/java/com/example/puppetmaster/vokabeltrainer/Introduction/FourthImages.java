package com.example.puppetmaster.vokabeltrainer.Introduction;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.puppetmaster.vokabeltrainer.Fragments.ImageFragment;
import com.example.puppetmaster.vokabeltrainer.R;
import com.example.puppetmaster.vokabeltrainer.models.images.GridItem;

import java.util.ArrayList;


public class FourthImages extends AppCompatActivity {

    private Button buttonNext, buttonPrevious;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.introduction_fourth_images);

        assignLayoutElements();
        attachListener();

    }

    private void assignLayoutElements() {
        buttonNext = (Button) findViewById(R.id.buttonNext);
        buttonPrevious = (Button) findViewById(R.id.buttonBack);
    }

    private void attachListener () {

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ImageFragment imageFragment =
                        (ImageFragment) getFragmentManager().findFragmentById(R.id.images_fragment);

                ArrayList<GridItem> gridItems = imageFragment.getGridItems();

                if (gridItems.size() > 1) {
                    // start next activity
                    Intent i = new Intent(getApplicationContext(), FifthSettings.class);
                    startActivity(i);
                    finish();
                } else {
                    Toast.makeText(FourthImages.this, "Add image for motivation", Toast.LENGTH_LONG).show();
                }


            }
        });

        buttonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ThirdMotivationText.class);
                startActivity(i);
                finish();
            }
        });
    }



}
