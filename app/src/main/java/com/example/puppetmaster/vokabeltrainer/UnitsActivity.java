package com.example.puppetmaster.vokabeltrainer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;
import com.example.puppetmaster.vokabeltrainer.Adapter.UnitAdapter;
import com.example.puppetmaster.vokabeltrainer.DatabaseCommunication.MyDatabase;

import java.util.ArrayList;

public class UnitsActivity extends AppCompatActivity {
    ArrayList<Unit> listOfUnits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_units);
        getListOfUnits();

        final CarouselLayoutManager layoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_units);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener(new CenterScrollListener());
        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
        recyclerView.setAdapter(new UnitAdapter(listOfUnits));
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
}
