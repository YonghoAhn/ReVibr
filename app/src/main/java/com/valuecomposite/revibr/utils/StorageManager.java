package com.valuecomposite.revibr.utils;

import android.content.Context;
import android.content.SharedPreferences;

class StorageManager {
    static void putDataString(Context context, String key, String value) {
        SharedPreferences prefs = context.getSharedPreferences("SMSCAPTUREHELPER", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    static void putDataInt(Context context, String key, int value) {
        SharedPreferences prefs = context.getSharedPreferences("SMSCAPTUREHELPER", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    static String getDataString(Context context, String key, String def) {
        SharedPreferences prefs = context.getSharedPreferences("SMSCAPTUREHELPER", Context.MODE_PRIVATE);
        return prefs.getString(key, def);
    }

    static int getDataInt(Context context, String key, int def) {
        SharedPreferences prefs = context.getSharedPreferences("SMSCAPTUREHELPER", Context.MODE_PRIVATE);
        return prefs.getInt(key, def);

    }

}