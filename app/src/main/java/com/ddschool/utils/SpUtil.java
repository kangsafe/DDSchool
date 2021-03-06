package com.ddschool.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SpUtil {
    private static final String NAME = "QQ";
    private static SpUtil instance;

    static {
        instance = new SpUtil();
    }

    public static SpUtil getInstance() {
        if (instance == null) {
            instance = new SpUtil();
        }
        return instance;
    }

    public static SharedPreferences getSharePerference(Context context) {
        return getSharePerference(NAME, context);
    }

    public static SharedPreferences getSharePerference(String key, Context context) {
        return context.getSharedPreferences(key, Context.MODE_PRIVATE);
    }

    public static boolean isFirst(SharedPreferences sp) {

        return sp.getBoolean("isFirst", false);
    }

    public static void setStringSharedPerference(SharedPreferences sp, String key, String value) {
        Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void setBooleanSharedPerference(SharedPreferences sp, String key, boolean value) {
        Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static void setLongSharedPerference(SharedPreferences sp, String key, long value) {
        Editor editor = sp.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public static void setIntSharedPerference(SharedPreferences sp, String key, int value) {
        Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.commit();
    }
}
