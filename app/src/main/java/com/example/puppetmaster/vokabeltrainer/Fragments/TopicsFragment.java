package com.example.puppetmaster.vokabeltrainer.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.puppetmaster.vokabeltrainer.Adapter.TopicAdapter;
import com.example.puppetmaster.vokabeltrainer.DatabaseCommunication.MyDatabase;
import com.example.puppetmaster.vokabeltrainer.R;
import com.example.puppetmaster.vokabeltrainer.Topic;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class TopicsFragment extends Fragment {
    ArrayList<Topic> topics = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_topics, container, false);
        readBundle();
        RecyclerView recList = (RecyclerView) view.findViewById(R.id.recycler_topics);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        TopicAdapter ta = new TopicAdapter(topics);
        recList.setAdapter(ta);
        return view;
    }

    private void readBundle() {
        Gson gson = new Gson();
        String getArgument = getArguments().getString("topics");
        Type baseType = new TypeToken<List<Topic>>() {
        }.getType();
        topics = gson.fromJson(getArgument, baseType);
    }
}
