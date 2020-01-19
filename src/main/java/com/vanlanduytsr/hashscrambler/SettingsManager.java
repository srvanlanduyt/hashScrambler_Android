package com.vanlanduytsr.hashscrambler;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsManager {

    Context context;

    SettingsManager(Context context) {
        this.context = context;
    }

    public void saveSettings(boolean scrambleAll, boolean paragraphSpacing) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("ScrambleAll", scrambleAll);
        editor.putBoolean("ParagraphSpacing", paragraphSpacing);
        editor.commit();
    }

    public boolean getScrambleAll() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("ScrambleAll", false);
    }

    public boolean getParagraphSpacing() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("ParagraphSpacing", false);
    }

}
