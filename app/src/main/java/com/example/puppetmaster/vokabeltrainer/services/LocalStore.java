package com.example.puppetmaster.vokabeltrainer.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.puppetmaster.vokabeltrainer.Introduction.MotivationListItem;
import com.example.puppetmaster.vokabeltrainer.R;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Service for work with SharedPreferences.
 * Saves and loads fields from local storage.
 */
public class LocalStore {

    String log = "LocalStorage";


    private SharedPreferences sharedPref;
    private Context context;
    private int motivationCounter;

    public LocalStore(Context context) {
        this.context = context;
        String preferencesName = context.getString(R.string.preferences_settings_file);
        sharedPref = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);

        // deletes all localStorage
        //sharedPref.edit().clear().apply();
    }


    public void saveName(String name) {
        String nameKey = context.getString(R.string.key_user_name);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(nameKey, name);
        editor.apply();
    }

    public String loadName() {
        String keyName= context.getString(R.string.key_user_name);
        return sharedPref.getString(keyName, "");
    }

    public int addMotivation(String motivation) {

        String motivationIDKey = context.getString(R.string.key_motivation_id);
        String motivationIDValue = sharedPref.getString(motivationIDKey,"");
        motivationCounter = checkMotivationID(motivationIDValue);

        motivationCounter++;
        Log.d("LocalStorage", "##### motiID: " + motivationCounter);


        SharedPreferences.Editor shPrEditor = sharedPref.edit();
        shPrEditor.putString(String.valueOf(motivationCounter), motivation);
        shPrEditor.putString(motivationIDKey, String.valueOf(motivationCounter));
        shPrEditor.apply();


        return motivationCounter;
    }

    public void removeMotivationText(int id) {
        Log.d(log,"###### DELETE ID: " + id);
        sharedPref.edit().remove(String.valueOf(id)).apply();
    }

    public ArrayList<MotivationListItem> getAllMotivation() {
        ArrayList<MotivationListItem> items = new ArrayList<MotivationListItem>();

        String motivationIDKey = context.getString(R.string.key_motivation_id);
        String motivationIDValue = sharedPref.getString(motivationIDKey,"");
        Log.d(log, "###### motivationIDValue: " + motivationIDValue);
        int motivationID = checkMotivationID(motivationIDValue);

        Log.d(log, "###### motivationID: " + motivationID);

        for (int i = 0; i <= motivationID; i++) {

            String motivationText = sharedPref.getString(String.valueOf(i), "");

            if (motivationText.length() > 0) {
                items.add(new MotivationListItem(motivationText, i));
            }
        }

        return items;
    }

    private int checkMotivationID(String value) {
        if (value == "") {
            return 0;
        } else {
            return Integer.valueOf(value);
        }
    }

    public void saveImages(Set<String> set) {
        String imagesKey = context.getString(R.string.key_images);
        SharedPreferences.Editor sharedPreferencesEditor = sharedPref.edit();
        sharedPreferencesEditor.putStringSet(imagesKey, set);
        sharedPreferencesEditor.apply();
    }

    public Set<String> loadImages() {
        String imagesKey = context.getString(R.string.key_images);
        return sharedPref.getStringSet(imagesKey, new HashSet<String>());
    }

}