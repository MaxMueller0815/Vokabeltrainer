package com.example.puppetmaster.vokabeltrainer.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.puppetmaster.vokabeltrainer.R;
import com.example.puppetmaster.vokabeltrainer.SpacedRepititionSystem.Vocab;
import com.example.puppetmaster.vokabeltrainer.Unit;

import java.util.ArrayList;

/**
 * Created by Benedikt on 06.02.17.
 */

public class VocabAdapter extends ArrayAdapter<Vocab> {

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
        return listItemView;
    }
}
