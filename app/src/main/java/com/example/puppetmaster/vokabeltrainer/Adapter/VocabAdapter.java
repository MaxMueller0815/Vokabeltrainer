package com.example.puppetmaster.vokabeltrainer.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.puppetmaster.vokabeltrainer.R;
import com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem.Vocab;

import java.util.ArrayList;

/**
 * Created by Benedikt on 06.02.17.
 */

public class VocabAdapter extends ArrayAdapter<Vocab> {
    private final int MAX_LEVEL = 12;

    public VocabAdapter(Context context, ArrayList<Vocab> vocabs) {
        super(context, 0, vocabs);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.vocab_item, parent, false);
        }

        Vocab vocab = getItem(position);

        TextView german = (TextView) listItemView.findViewById(R.id.tv_german);
        german.setText(vocab.getGerman());

        TextView defaultTranslation = (TextView) listItemView.findViewById(R.id.tv_default);
        defaultTranslation.setText(vocab.getEnglish());

        ProgressBar progressBar = (ProgressBar) listItemView.findViewById(R.id.progress_level);
        progressBar.setProgress(vocab.getSrsLevel());
        return listItemView;
    }
}
