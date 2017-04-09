package com.example.puppetmaster.vokabeltrainer.Adapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.puppetmaster.vokabeltrainer.Activities.UnitsActivity;
import com.example.puppetmaster.vokabeltrainer.Entities.Topic;
import com.example.puppetmaster.vokabeltrainer.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicViewHolder> {

    private List<Topic> topicList;

    public TopicAdapter(ArrayList<Topic> topicList) {
        this.topicList = topicList;
    }


    @Override
    public int getItemCount() {
        return topicList.size();
    }

    @Override
    public void onBindViewHolder(TopicViewHolder topicViewHolder, int i) {
        final Topic topic = topicList.get(i);
        final int selectedTopic = topicList.get(i).getId();
        topicViewHolder.vCard.setTag(topic);
        topicViewHolder.vImage.setImageResource(topic.getImage());
        topicViewHolder.vTitle.setText(topic.getTitle());
        topicViewHolder.vProgress.setText(topic.getProgress() + "%");

        topicViewHolder.vCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),UnitsActivity.class);
                Bundle mBundle = new Bundle();
                Gson gson = new Gson();
                String TopicAsGson = gson.toJson(topic);
                mBundle.putString("SELECTED_TOPIC", TopicAsGson);
                intent.putExtras(mBundle);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public TopicViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.topic_item, viewGroup, false);

        return new TopicViewHolder(itemView);
    }


    public static class TopicViewHolder extends RecyclerView.ViewHolder {
        protected CardView vCard;
        protected ImageView vImage;
        protected TextView vTitle;
        protected TextView vProgress;

        public TopicViewHolder(View v) {
            super(v);
            vCard = (CardView) v.findViewById(R.id.card_topic);
            vImage =  (ImageView) v.findViewById(R.id.iv_topic);
            vTitle = (TextView)  v.findViewById(R.id.tv_topic_title);
            vProgress = (TextView) v.findViewById(R.id.tv_progress);
        }
    }
}