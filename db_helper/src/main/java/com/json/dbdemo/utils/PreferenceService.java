package com.json.dbdemo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by json on 2016/1/17.
 */
public class PreferenceService {

    private static PreferenceService mInstance = null;
    private SharedPreferences mSharedPreferences = null;
    private Context mContext = null;

    private PreferenceService(Context context) {
        this.mContext = context;
        mSharedPreferences = mContext.getApplicationContext().getSharedPreferences("db_demo", Activity.MODE_PRIVATE);
    }

    public static PreferenceService newInstance(Context context) {

        if (mInstance == null) {
            synchronized (PreferenceService.class) {
                if (mInstance == null) {
                    mInstance = new PreferenceService(context.getApplicationContext());
                }
            }
        }

        return mInstance;
    }

    public void putIntValue(String key, int value) {

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public int getIntValue(String key) {
        return mSharedPreferences.getInt(key, 0);
    }

    public void putBooleanValue(String key, boolean value) {

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean getBooleanValue(String key) {
        return mSharedPreferences.getBoolean(key, false);
    }
}
