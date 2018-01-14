package com.sht.content.support;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.sht.content.ContentApplication;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by sht on 2017/2/8.
 * Utils for Post Data via network and get network state of cell phone.
 * @author SHTT
 * @version Content 1.0
 */

public class HttpUtil {

    public static boolean isWIFI = true;
    private static final OkHttpClient mOKHttpClient = new OkHttpClient();
    static {
        mOKHttpClient.setConnectTimeout(30, TimeUnit.SECONDS);
        mOKHttpClient.setReadTimeout(30,TimeUnit.SECONDS);
        mOKHttpClient.setWriteTimeout(30,TimeUnit.SECONDS);
    }
    /**
     * this step start asynchronous thread
     * @param request
     * @return Response
     * @throws IOException
     */
    public static Response execute(Request request) throws IOException {
        return mOKHttpClient.newCall(request).execute();
    }

    /**
     * this step access the network by startting asynchronous thread
     *@param request
     *@param responseCallback
     */
    public static void enqueue(Request request, Callback responseCallback){
        mOKHttpClient.newCall(request).enqueue(responseCallback);
    }

    /**
     * this step access the network by startting asynchronous thread, and achieve empty callback
     * @param request
     */
    public static void enqueue(Request request){
        mOKHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {

            }
        });
    }

    public static boolean readNetworkState(){
        ConnectivityManager mConManager = (ConnectivityManager) ContentApplication.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        // my code for test net state
        /*NetworkInfo mNetworkInfo = mConManager.getActiveNetworkInfo();
        boolean isAvailable = mNetworkInfo.isAvailable();
        if (isAvailable){
            return true;
        }else
            return false;*/
        if (mConManager != null && mConManager.getActiveNetworkInfo() != null && mConManager.getActiveNetworkInfo().isConnected()) {
            isWIFI = (mConManager.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI);
            return true;
        } else {
            return false;
        }
    }
}
