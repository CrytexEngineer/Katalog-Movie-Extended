package com.example.aqil.katalogfilmuiux.pref;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.aqil.katalogfilmuiux.R;

public class SettingPreference {
    private String KEY_REMINDER = "reminder";
    private String KEY_RELEASE = "release";
    private SharedPreferences preferences;

    public SettingPreference(Context context) {
        String PREFS_NAME = "preference";
        preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void setHasChecked(boolean hasChecked, int checboxId) {
        SharedPreferences.Editor editor = preferences.edit();

        if (checboxId == R.id.checkbox_reminder) {
            editor.putBoolean(KEY_REMINDER, hasChecked);
        } else {
            editor.putBoolean(KEY_RELEASE, hasChecked);
        }
        editor.apply();
    }

    public boolean getHasChcked(int checboxId) {

        if (checboxId == R.id.checkbox_reminder) {
            return preferences.getBoolean(KEY_REMINDER, false);
        } else {
            return preferences.getBoolean(KEY_RELEASE, false);
        }

    }


}
