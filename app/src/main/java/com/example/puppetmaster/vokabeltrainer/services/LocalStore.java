package com.example.puppetmaster.vokabeltrainer.services;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.puppetmaster.vokabeltrainer.R;


import java.util.HashSet;
import java.util.Set;

/**
 * Service for work with SharedPreferences.
 * Saves and loads fields from local store.
 */
public class LocalStore {

    private SharedPreferences sharedPref;
    private Context context;

    public LocalStore(Context context) {
        this.context = context;
        String preferencesName = context.getString(R.string.preferences_settings_file);
        sharedPref = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
    }

    public void saveIsRegistered(boolean isRegistered) {
    //    String isRegisteredKey = context.getString(R.string.is_registered_key);
    //    SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
    //    sharedPreferencesEditor.putBoolean(isRegisteredKey, isRegistered);
    //    sharedPreferencesEditor.apply();
    }

    public boolean loadIsRegistered() {
    //    String isRegisteredKey = context.getString(R.string.is_registered_key);
    //    return sharedPreferences.getBoolean(isRegisteredKey, isRegisteredValue);
        return false;
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

    public void saveMotivation(String motivation) {
    //    String motivationKey = context.getString(R.string.motivation_key);
    //    SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
    //    sharedPreferencesEditor.putString(motivationKey, motivation);
    //    sharedPreferencesEditor.apply();
    }

    public String loadMotivation() {
    //    String motivationKey = context.getString(R.string.motivation_key);
    //    String motivationValue = context.getString(R.string.motivation_value);
    //    return sharedPreferences.getString(motivationKey, motivationValue);
        return "a";
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

    public void saveWordsPerDay(int wordsPerDay) {
    //    String wordsPerDayKey = context.getString(R.string.words_per_day_key);
    //    SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
    //    sharedPreferencesEditor.putInt(wordsPerDayKey, wordsPerDay);
    //    sharedPreferencesEditor.apply();
    }

    public int loadWordsPerDay() {
    //    String wordsPerDayKey = context.getString(R.string.words_per_day_key);
    //    int wordsPerDayValue = Integer.parseInt(context.getString(R.string.words_per_day_value));
    //    return sharedPreferences.getInt(wordsPerDayKey, wordsPerDayValue);
        return 1;
    }


}