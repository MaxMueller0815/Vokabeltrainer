package com.example.puppetmaster.vokabeltrainer.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.puppetmaster.vokabeltrainer.Introduction.MotivationListItem;
import com.example.puppetmaster.vokabeltrainer.services.LocalStore;
import com.example.puppetmaster.vokabeltrainer.R;

import java.util.ArrayList;


public class MotivationListAdapter extends ArrayAdapter<MotivationListItem> {
    private ArrayList<MotivationListItem> motivationList;
    private Context context;
    private LocalStore localStore;

    public MotivationListAdapter(Context context, ArrayList<MotivationListItem> listItems) {
        super(context, R.layout.listitem_motivation, listItems);

        this.context = context;
        this.motivationList = listItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.listitem_motivation, null);
        }

        final MotivationListItem motivationItem = motivationList.get(position);

        if (motivationItem != null) {
            TextView textViewMotivation = (TextView) v.findViewById(R.id.motivation_text);

            textViewMotivation.setText(motivationItem.getMotivationText());

            //Button buttonDelete = (Button)v.findViewById(R.id.delete_button);
            //buttonDelete.setOnClickListener(new View.OnClickListener() {
            //    @Override
            //    public void onClick(View v) {
            //        localStore = new LocalStore(context);
            //        localStore.removeMotivationText(motivationItem.getMotivationID());
            //    }
            //});
        }

        return v;
    }

}
