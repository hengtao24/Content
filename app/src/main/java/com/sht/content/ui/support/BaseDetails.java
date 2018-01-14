package com.sht.content.ui.support;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sht.content.R;
import com.sht.content.support.HttpUtil;
import com.sht.content.support.Settings;
import com.sht.content.support.SwipeBack.SwipeBackLayout;
import com.sht.content.support.SwipeBack.SwipeBackActivity;
import com.sht.content.support.Utils;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.w3c.dom.Text;

import java.io.IOException;

/**
 * Created by sht on 2017/2/21.
 */

public abstract  class BaseDetails extends SwipeBackActivity {

    // sht:2017-3-27
    // add mSwipeBackLayout for Slide back
    protected SwipeBackLayout mSwipeBackLayout;
    protected abstract void afterCreate(Bundle savedInstanceState);

    // sht:2017-4-4
    // add Variable for judge the network
    protected boolean isNetVariable = false;

    // sht:2017-4-5
    // add Variable for judge weather load successful
    protected boolean isLoadingSuccessful;

    private static final String TAG = "BaseDetails";

    // container object
    protected CoordinatorLayout container;

    // layout object
    protected ImageView mTvHeader;
    protected TextView mTvTitle;
    protected TextView mTvSource;
    protected WebView mWebView;
    protected Toolbar mToolbar;
    protected CollapsingToolbarLayout mCollapsingToolbarLayout;
    protected NestedScrollView mScrollView;
    protected TextView mTvLoadEmpty;
    protected ImageView mTvError;
    protected ContentLoadingProgressBar mPBLoading;
    protected FrameLayout include;

    // sht:2017-4-5
    // add for load error view
    protected View mErrorView;

    // isCollected
    protected boolean isCollected;

    protected abstract void onDataRefresh();

    protected void loadSettings(){

    }

    // sht:2017-3-27
    // add the function onKeyDown
  /*  @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            scrollToFinishActivity();
        }
        return true;
    }*/

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        loadSettings();
        readNetState();
        setContentView(getLayoutID());

        // sht:2017-3-27
        // add mSwipeBackLayout for Slide back
        mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
    }

    public boolean readNetState(){
        return isNetVariable = HttpUtil.readNetworkState();
    }

    protected int getLayoutID(){
   //     if (isNetVariable){
        return R.layout.test_layout;
     //   }
     //   else return R.layout.activity_details_net_error;
    }

    public void hideLoading(){
        if (mPBLoading != null){
            mPBLoading.setVisibility(View.GONE);
        }
    }

    public void displayNetworkError(final String type){

        if (mTvError != null){
            mTvError.setVisibility(View.VISIBLE);
        }

      /*  if (mErrorView == null) {
            mErrorView = View.inflate(this, R.layout.activity_details_net_error, null);
            RelativeLayout button = (RelativeLayout)mErrorView.findViewById(R.id.online_error_btn_retry);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (type.equals("WebView")){
                        mWebView.reload();
                    } else {
                        onDataRefresh();
                    }
                }
            });
            mErrorView.setOnClickListener(null);
        }

        LinearLayout webParentView = (LinearLayout) mWebView.getParent();

        while (webParentView.getChildCount() > 1) {
            webParentView.removeViewAt(0);
        }
        webParentView.addView(mErrorView,0);
*/
    }
    protected void initView(){

         container =(CoordinatorLayout) findViewById(R.id.BaseDetails);
         mTvHeader = (ImageView) findViewById(R.id.iv_header);
         mTvTitle = (TextView) findViewById(R.id.tv_title);
         mTvSource = (TextView) findViewById(R.id.tv_source);
         mWebView = (WebView) findViewById(R.id.webview);
         mToolbar = (Toolbar) findViewById(R.id.toolbar);
         mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
         mScrollView = (NestedScrollView) findViewById(R.id.nested_view);
         mTvError = (ImageView) findViewById(R.id.sad_face);
         mPBLoading = (ContentLoadingProgressBar) findViewById(R.id.pb_loading);
         include = (FrameLayout) findViewById(R.id.include);
//        // some set for ToolBar
//        setSupportActionBar(mToolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle("");
//        mToolbar.setNavigationOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                onBackPressed();
//            }
//        });
//        getSupportActionBar().setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.top_gradient));

        // new code for set toolbar and CollapsingToolbarLayout
         setSupportActionBar(mToolbar);
         getSupportActionBar().setDisplayHomeAsUpEnabled(true);
         mCollapsingToolbarLayout.setTitleEnabled(true);
         mToolbar.setNavigationOnClickListener(new View.OnClickListener(){
             @Override
             public void onClick(View v){
                 onBackPressed();
             }
         });


        // WebView set
        mWebView.getSettings().setJavaScriptEnabled(true);
        // Open the cache
    /*    mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setDatabaseEnabled(true);
      */  // lay over the object WebViewClient of WebView
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url){
                super.onPageFinished(view,url);
                Utils.DLog("1111111");
                hideLoading();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error){
                displayNetworkError("WebView");
            }

            // In the click of the request is the link will be called,
            // rewrite this method returns true that click on the page inside the link or
            // jump in the current webview, do not jump to the browser side.
            @Override
            public boolean shouldOverrideUrlLoading(WebView view,String url){
                mWebView.loadUrl(url);
                return false;
            }
        });

        if (HttpUtil.isWIFI == false){
            mWebView.getSettings().setBlockNetworkImage(Settings.getInstance().getBoolean(Settings.NO_PIC_MODE,false));
        }else {
            mWebView.getSettings().setBlockNetworkImage(false);
        }

        // wrong network
//        mImageButton.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                mImageButton.setVisibility(View.GONE);
//                mProgressBar.setVisibility(View.VISIBLE);
//                onDataRefresh();
//            }
//        });
        onDataRefresh();
    }

    /*
     * 设置布局背景
     */
    protected void setMainContentBg(final String url,final String title,@Nullable final String imageSource){

        if(Utils.hasString(url) == false){
            setDefaultColor();
            return;
        }

        mCollapsingToolbarLayout.setTitle(title);
        mTvTitle.setText(title);
        if (imageSource != null) {
            mTvSource.setText(imageSource);
        }

        Request.Builder builder = new Request.Builder();
        builder.url(url);
        Request request = builder.build();

        HttpUtil.enqueue(request, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG,"onFailure:"+url);
                        setDefaultColor();
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
               final Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (bitmap == null){
                            setDefaultColor();
                            Log.d(TAG,"onResponse bitmap null:" + url);
                            return;
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                            mTvHeader.setBackground(new BitmapDrawable(getResources(),bitmap));
                        }else {
                            mTvHeader.setImageURI(Uri.parse(url));
                        }
                        Log.d(TAG,"onResponse: "+url);
                        mPBLoading.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    protected void setDefaultColor(){
        mPBLoading.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_share,menu);
        updateCollectionMenu(menu.findItem(R.id.menu_collect));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (isLoadingSuccessful == true) {
            if (item.getItemId() == R.id.menu_share) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, getShareInfo());
                startActivity(Intent.createChooser(sharingIntent, getString(R.string.hint_share_to)));
                return super.onOptionsItemSelected(item);
            } else if (item.getItemId() == R.id.menu_collect) {
                if (isCollected) {
                    removeFromCollection();
                    isCollected = false;
                    updateCollectionMenu(item);
                    Snackbar.make(container, R.string.notify_remove_from_collection, Snackbar.LENGTH_SHORT).show();
                } else {
                    addToCollection();
                    isCollected = true;
                    updateCollectionMenu(item);
                    Snackbar.make(container, R.string.notify_add_to_collection, Snackbar.LENGTH_SHORT).show();
                }
            }
            return true;
        }
        return true;
    }

    protected void updateCollectionMenu(MenuItem item){
        if (isCollected){
            item.setIcon(R.mipmap.ic_star_black);
        }else {
            item.setIcon(R.mipmap.ic_star_white);
        }
    }

    protected abstract String getShareInfo();
    protected abstract void removeFromCollection();
    protected abstract void addToCollection();


}
