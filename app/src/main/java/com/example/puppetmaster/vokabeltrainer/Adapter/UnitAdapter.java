package com.example.puppetmaster.vokabeltrainer.Adapter;

/**
 * Created by Benedikt on 08.02.17.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.puppetmaster.vokabeltrainer.Activities.ExerciseActivity;
import com.example.puppetmaster.vokabeltrainer.Entities.Unit;
import com.example.puppetmaster.vokabeltrainer.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class UnitAdapter extends RecyclerView.Adapter<UnitAdapter.UnitViewHolder> {
    private Context context;
    private List<Unit> unitList;
    private int topicID;

    public UnitAdapter(ArrayList<Unit> unitList, int topicID) {
        this.unitList = unitList;
        this.topicID = topicID;
    }


    @Override
    public int getItemCount() {
        return unitList.size();
    }

    @Override
    public void onBindViewHolder(UnitViewHolder unitViewHolder, int i) {
        final Unit unit = unitList.get(i);
        unitViewHolder.vNumberOfUnit.setText("Unit " + (i + 1) + " of " + unitList.size());
        unitViewHolder.vTitle.setText(unit.getTitle());
        VocabAdapter vocabAdapter = new VocabAdapter(context, unit.getVocabsOfUnit());
        unitViewHolder.vList.setAdapter(vocabAdapter);

        unitViewHolder.vButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ExerciseActivity.class);
                intent.putExtra("WORD_LIST", unit.getVocabsOfUnit());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public UnitViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.unit_item, viewGroup, false);
        context = viewGroup.getContext();

        return new UnitViewHolder(itemView);
    }



    public static class UnitViewHolder extends RecyclerView.ViewHolder {
        protected CardView vCard;
        protected TextView vNumberOfUnit;
        protected TextView vTitle;
        protected ListView vList;
        protected Button vButton;

        public UnitViewHolder(View v) {
            super(v);
            vCard = (CardView) v.findViewById(R.id.card_unit);
            vNumberOfUnit =  (TextView) v.findViewById(R.id.tv_unit_number);
            vTitle = (TextView)  v.findViewById(R.id.tv_unit_title);
            vList = (ListView) v.findViewById(R.id.list_unit_vocabs);
            vButton = (Button) v.findViewById(R.id.btn_unit_learn);
        }
    }
}