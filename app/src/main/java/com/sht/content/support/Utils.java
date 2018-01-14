package com.sht.content.support;

import android.text.TextUtils;
import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;

/**
 * Created by sht on 2017/2/14.
 */

public class Utils {

    public static boolean DEBUG = true;

    public static void DLog(String text){
        if(DEBUG){
            Log.e("Debug data",text);
        }
    }

    public static boolean hasString(String str){
        if (str == null || str.equals("")){
            return false;
        }
        return true;
    }

    public static void getRawHtmlFromUrl(String url, Callback callback){

        if (callback == null || TextUtils.isEmpty(url)){
            return ;
        }

        Request.Builder builder = new Request.Builder();
        builder.url(url);
        Request request = builder.build();
        HttpUtil.enqueue(request,callback);
    }
}
