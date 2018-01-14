package com.sht.content;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;


/**
 * Created by sht on 2017/2/8.
 */

public class ContentApplication extends Application{
    private static Context AppContext = null;

    @Override
    public void onCreate(){
        super.onCreate();
        AppContext = getApplicationContext();
        Fresco.initialize(AppContext);
    }

    public static Context getAppContext(){
        return AppContext;
    }
}
