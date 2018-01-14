package com.sht.content.ui.science;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.sht.content.R;
import com.sht.content.database.cache.cache.ScienceCache;
import com.sht.content.database.table.DailyTable;
import com.sht.content.database.table.ScienceTable;
import com.sht.content.model.science.ArticleBean;
import com.sht.content.support.HttpUtil;
import com.sht.content.support.Settings;
import com.sht.content.support.Utils;
import com.sht.content.support.parser.ScienceContentParser;
import com.sht.content.ui.support.BaseDetails;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by sht on 2017/2/28.
 */

public class ScienceDetailsActivity extends BaseDetails {

    private static final String TAG = "ScienceDetailsActivity";
    private ScienceCache mScienceCache;
    private ArticleBean mArticleBean;

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        mArticleBean = (ArticleBean) getIntent().getSerializableExtra("id_science");
        mScienceCache = new ScienceCache();
        isCollected = (mArticleBean.getIs_collected() == 1?true:false);
        initView();
    }

    // sht:2017-3-27
    // add mSwipeBackLayout for Slide back
    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        mSwipeBackLayout.setEdgeDp(120); //设置滑动返回触发范围
    }

    @Override
    protected void initView(){
        super.initView();
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }
        });
    }

    @Override
    protected void onDataRefresh() {

        Utils.getRawHtmlFromUrl(mArticleBean.getUrl(), new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                final String rawData = response.body().string();
                Handler mHandler = new Handler(Looper.getMainLooper());
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        ScienceContentParser mParser = new ScienceContentParser(rawData);
                        String data = mParser.getEndStr();
                        mWebView.loadDataWithBaseURL("file:///android_asset/", "<link rel=\"stylesheet\" type=\"text/css\" href=\"guokr.css\" />" + data,"text/html", "utf-8", null);
                    }
                });
            }
        });
        if(HttpUtil.isWIFI == true || Settings.getInstance().getBoolean(Settings.NO_PIC_MODE, false) == false) {
            setMainContentBg(mArticleBean.getImage_info().getUrl(),mArticleBean.getTitle(),null);
        }
    }

    @Override
    protected String getShareInfo(){
        return "["+mArticleBean.getTitle()+"]:"+ mArticleBean.getUrl()+"("
                +getString(R.string.text_share_from)+getString(R.string.app_name)+")";
    }

    @Override
    protected void addToCollection(){
        mScienceCache.execSQL(ScienceTable.updateCollectionFlag(mArticleBean.getTitle(),1));
        mScienceCache.addToCollection(mArticleBean);
    }

    @Override
    protected void removeFromCollection(){
        mScienceCache.execSQL(DailyTable.updateCollectionFlag(mArticleBean.getTitle(),0));
        mScienceCache.execSQL(DailyTable.deleteCollectionFlag(mArticleBean.getTitle()));
    }


}
