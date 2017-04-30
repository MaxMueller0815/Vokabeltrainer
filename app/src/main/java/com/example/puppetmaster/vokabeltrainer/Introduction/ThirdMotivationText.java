package com.example.puppetmaster.vokabeltrainer.Introduction;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.puppetmaster.vokabeltrainer.Adapter.MotivationListAdapter;
import com.example.puppetmaster.vokabeltrainer.R;
import com.example.puppetmaster.vokabeltrainer.services.LocalStore;

import java.util.ArrayList;


public class ThirdMotivationText extends AppCompatActivity {

    private String log = "ThirdMotivation";

    private Button buttonNext, buttonPrevious, buttonAdd;
    private ArrayList<MotivationListItem> motiList = new ArrayList<MotivationListItem>();
    private MotivationListAdapter motiListAdapter;
    private LocalStore localStore;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.introduction_third_motivation);

        initService();
        initUI();
        buttonsOnClick();
        updateList();
    }

    private void initService () {
        localStore = new LocalStore(this);
    }

    private void initUI () {
        initButtons();
        initListView();
        initListAdapter();

    }

    private void initButtons() {
        buttonNext = (Button) findViewById(R.id.buttonNext);
        buttonPrevious = (Button) findViewById(R.id.buttonBack);
        buttonAdd = (Button) findViewById(R.id.button_add_motivation);
    }

    private void initListView() {
        list = (ListView) findViewById(R.id.motivation_list);
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                removeMotivationAtPosition(position);
                Log.d(log, "###### long Clicked!");
                return false;
            }
        });
        //list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        //    public void onItemClick(AdapterView<?> parent, View view,
        //                            final int position, long id) {
        //        Button buttonDelete = (Button)view.findViewById(R.id.delete_button);
        //        buttonDelete.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //                localStore.removeMotivationText(motiList.get(position).getMotivationID());
        //                updateList();
        //                Log.d(log,"###### Listview Clicked!");
        //            }
        //        });
        //
        //    }
        //});
    }

    private void removeMotivationAtPosition(int position) {
        int id = motiList.get(position).getMotivationID();
        localStore.removeMotivationText(id);
        updateList();
    }

    private void initListAdapter() {
        motiListAdapter = new MotivationListAdapter(this, motiList);
        list.setAdapter(motiListAdapter);
    }

    private void buttonsOnClick () {
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (motiList.size() == 0) {
                    Toast.makeText(ThirdMotivationText.this, "Please add at least one motivational text.", Toast.LENGTH_LONG).show();
                }
                else {
                    Intent i = new Intent(getApplicationContext(), FourthImages.class);
                    startActivity(i);
                    finish();
                }
            }
        });

        buttonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SecondIntroMotivation1.class);
                startActivity(i);
                finish();
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edit = (EditText) findViewById(R.id.editText_motivation);
                String motivation = edit.getText().toString();

                if (motivation.equals("")) {
                    Toast.makeText(ThirdMotivationText.this, "Please, write your motivation first", Toast.LENGTH_LONG).show();
                } else {
                    edit.setText("");
                    int id = localStore.addMotivation(motivation);
                    Log.d(log, "##### id = "+id);
                    MotivationListItem item = new MotivationListItem(motivation, id);
                    motiList.add(item);
                }
                updateList();
            }
        });
    }


    private void updateList() {
        motiList.clear();
        motiList.addAll(localStore.getAllMotivation());
        motiListAdapter.notifyDataSetChanged();
    }


}
