package vion.logtracks.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {

    public static final String PREFERENCE_NAME = "LOG_TRACKS";
    private static SharedPreferences sharedPreferences;

    public static void putString(String key, String val) {
        init();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, val);
        editor.apply();
    }

    public static String getString(String key) {
        init();
        return sharedPreferences.getString(key, "");
    }

    public static void putBoolean(String key, boolean val) {
        init();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, val);
        editor.apply();
    }

    public static boolean getBoolean(String key) {
        init();
        return sharedPreferences.getBoolean(key, false);
    }

    private static void init() {
        sharedPreferences = App.getInstance().getSharedPreferences(PREFERENCE_NAME,Context.MODE_PRIVATE);
    }

    public static void clear() {
        init();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

}
