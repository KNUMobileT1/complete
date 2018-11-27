package com.bmh.trackchild.Tools;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.bmh.trackchild.R;

public class SharedPrefs {
    Context context;
    SharedPreferences sharedPreferences;

    public SharedPrefs(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.shared_prefrences), Activity.MODE_PRIVATE);

    }

    public void savePreferences(int key, String Value) {
        Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(key), Value);
        editor.commit();
    }

    public void savePreferences(int key, boolean Value) {
        Editor editor = sharedPreferences.edit();
        editor.putBoolean(context.getResources().getString(key), Value);
        editor.commit();
    }

    public String getPreferences(int key, String defValue) {
        return sharedPreferences.getString(context.getResources().getString(key), defValue);
    }

    public boolean getPreferences(int key, boolean defValue) {
        return sharedPreferences.getBoolean(context.getResources().getString(key), defValue);
    }

    public void clearPreferences() {
        sharedPreferences.edit().clear().apply();
    }
}
