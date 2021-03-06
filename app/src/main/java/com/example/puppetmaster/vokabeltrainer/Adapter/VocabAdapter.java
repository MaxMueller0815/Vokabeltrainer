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
 * Listenansicht für eine Vokabel, beinhaltet Deutsch/Englisch und SRS-Level
 */
public class VocabAdapter extends ArrayAdapter<Vocab> {

    public VocabAdapter(Context context, ArrayList<Vocab> vocabs) {
        super(context, 0, vocabs);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.vocab_item, parent, false);
        }

        Vocab vocab = getItem(position);

        TextView german = (TextView) listItemView.findViewById(R.id.tv_german);

        String germanTranslation = "";
        if (vocab.getGerman().size() > 0) {
            germanTranslation = vocab.getGerman().get(0);
        }

        german.setText(germanTranslation);
        TextView defaultTranslation = (TextView) listItemView.findViewById(R.id.tv_default);
        defaultTranslation.setText(vocab.getEnglish());
        ProgressBar progressBar = (ProgressBar) listItemView.findViewById(R.id.progress_level);
        progressBar.setProgress(vocab.getSrsLevel());
        TextView tvLevel = (TextView) listItemView.findViewById(R.id.tv_level);
        tvLevel.setText(vocab.getExternalSrsLevel());
        return listItemView;
    }
}
