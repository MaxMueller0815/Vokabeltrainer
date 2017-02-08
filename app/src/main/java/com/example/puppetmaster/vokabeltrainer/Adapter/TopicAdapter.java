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

import com.example.puppetmaster.vokabeltrainer.R;
import com.example.puppetmaster.vokabeltrainer.Topic;
import com.example.puppetmaster.vokabeltrainer.UnitsActivity;

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
        Topic ti = topicList.get(i);
        final int selectedTopic = topicList.get(i).getId();
        topicViewHolder.vCard.setTag(selectedTopic);
        topicViewHolder.vImage.setImageResource(ti.getImage());
        topicViewHolder.vTitle.setText(ti.getTitle());

        topicViewHolder.vCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),UnitsActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putInt("SELECTED_TOPIC", selectedTopic);
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

        public TopicViewHolder(View v) {
            super(v);
            vCard = (CardView) v.findViewById(R.id.card_topic);
            vImage =  (ImageView) v.findViewById(R.id.iv_topic);
            vTitle = (TextView)  v.findViewById(R.id.tv_topic_title);
        }
    }
}