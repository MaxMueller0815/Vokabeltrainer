package com.example.puppetmaster.vokabeltrainer.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.puppetmaster.vokabeltrainer.Adapter.TopicAdapter;
import com.example.puppetmaster.vokabeltrainer.DatabaseCommunication.MyDatabase;
import com.example.puppetmaster.vokabeltrainer.R;
import com.example.puppetmaster.vokabeltrainer.Topic;
import com.example.puppetmaster.vokabeltrainer.UnitsActivity;

import java.util.ArrayList;

import static com.example.puppetmaster.vokabeltrainer.UIHelper.convertDpToPixel;

public class TopicsFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_topics, container, false);
        MyDatabase db = new MyDatabase(getContext());

        RecyclerView recList = (RecyclerView) view.findViewById(R.id.recycler_topics);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        TopicAdapter ta = new TopicAdapter(new MyDatabase(getContext()).getTopics());
        recList.setAdapter(ta);
        return view;
    }
}
