package com.sht.content.support;

import android.content.Context;
import android.content.SharedPreferences;

import com.sht.content.ContentApplication;

/**
 * Created by sht on 2017/2/14.
 */

public class Settings {

    public static boolean isAutoRefresh = false;
    public static boolean noPicMode = false;
    public static boolean isNightMode = false;

    private static Settings mInstance;
    private SharedPreferences mPrefs;

    public static final String XML_NAME = "settings";
    public static final String NO_PIC_MODE = "no_pic_mode";

    private  Settings(Context context){
        mPrefs = context.getSharedPreferences(XML_NAME,Context.MODE_PRIVATE);
    }

    public static Settings getInstance(){
        if (mInstance == null){
            mInstance = new Settings(ContentApplication.getAppContext());
        }
        return mInstance;
    }

    public Settings putBoolean(String key,boolean value){
        mPrefs.edit().putBoolean(key,value).commit();
        return this;
    }
    public boolean getBoolean(String key,boolean def){
        return mPrefs.getBoolean(key,def);
    }
}
