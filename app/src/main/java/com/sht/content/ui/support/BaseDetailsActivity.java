package com.sht.content.ui.support;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.facebook.drawee.view.SimpleDraweeView;
import com.sht.content.ContentApplication;
import com.sht.content.R;
import com.sht.content.support.DisplayUtil;
import com.sht.content.support.HttpUtil;
import com.sht.content.support.ImageUtil;
import com.sht.content.support.Settings;
import com.sht.content.support.Utils;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by sht on 2017/2/16.
 */

public abstract class BaseDetailsActivity extends AppCompatActivity {

    private static final String TAG = "BaseDetailsActivity";
    protected ProgressBar mProgressBar;
    protected ProgressBar mProgressBarTopPic;
    protected FrameLayout mainContent;
    protected NestedScrollView mScrollView;
    protected SimpleDraweeView mSimpleDraweeView;
    protected ImageButton mImageButton;
    protected WebView mWebView;
    protected Toolbar mToolbar;
    protected boolean isCollected;

    protected abstract void onDataRefresh();

    protected void loadSettings(){

    }

    @Override
    protected void onCreate(Bundle saveInstanceState){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        super.onCreate(saveInstanceState);
        loadSettings();
        setContentView(getLayoutID());
    }

    protected int getLayoutID(){
        return R.layout.activity_base_details;
    }

    public void hideLoading(){
        if (mProgressBar != null){
            mProgressBar.setVisibility(View.GONE);
            mProgressBarTopPic.setVisibility(View.VISIBLE);
        }
    }

    public void displayNetworkError(){
        if (mImageButton != null){
            mImageButton.setVisibility(View.INVISIBLE);
        }
    }
    protected void initView(){

        // move down the toolbar
        int height = DisplayUtil.getScreenHeight(ContentApplication.getAppContext());
        LinearLayout mLinearLayout = (LinearLayout) findViewById(R.id.stbar);
        LinearLayout.LayoutParams mLayoutParams = (LinearLayout.LayoutParams) mLinearLayout.getLayoutParams();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            mLayoutParams.height = (int) (height*0.03);
            mLinearLayout.setLayoutParams(mLayoutParams);
        }

        mainContent = (FrameLayout) findViewById(R.id.main_content);
        mScrollView = (NestedScrollView) findViewById(R.id.scrollview);
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        mProgressBarTopPic = (ProgressBar) findViewById(R.id.progressToppic);
        mImageButton = (ImageButton) findViewById(R.id.networkbtn);
        mSimpleDraweeView = (SimpleDraweeView) findViewById(R.id.topImage);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mWebView = (WebView) findViewById(R.id.content_view);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onBackPressed();
            }
        });

        getSupportActionBar().setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.top_gradient));

        mWebView.getSettings().setJavaScriptEnabled(true);
        // Open the cache
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setDatabaseEnabled(true);
        // lay over the object WebViewClient of WebView
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url){
                super.onPageFinished(view,url);
                hideLoading();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error){
                displayNetworkError();
            }

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
        mImageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mImageButton.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.VISIBLE);
                onDataRefresh();
            }
        });
        onDataRefresh();
    }

    /*
     * 设置布局背景
     */
    protected void setMainContentBg(final String url){

        if(Utils.hasString(url) == false){
            setDefaultColor();
            return;
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
                            mSimpleDraweeView.setBackground(new BitmapDrawable(getResources(),bitmap));
                        }else {
                            mSimpleDraweeView.setImageURI(Uri.parse(url));
                        }
                        Log.d(TAG,"onResponse: "+url);
                        mainContent.setBackgroundColor(ImageUtil.getImageColor(bitmap));
                        mProgressBarTopPic.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    protected void setDefaultColor(){
        mProgressBarTopPic.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_share,menu);
        updateCollectionMenu(menu.findItem(R.id.menu_collect));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == R.id.menu_share){
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_TEXT,getShareInfo());
            startActivity(Intent.createChooser(sharingIntent,getString(R.string.hint_share_to)));
            return super.onOptionsItemSelected(item);
        }else if(item.getItemId() == R.id.menu_collect){
            if (isCollected){
                removeFromCollection();
                isCollected = false;
                updateCollectionMenu(item);
                Snackbar.make(mainContent,R.string.notify_remove_from_collection,Snackbar.LENGTH_SHORT).show();
            }else {
                addToCollection();
                isCollected = true;
                updateCollectionMenu(item);
                Snackbar.make(mainContent,R.string.notify_add_to_collection,Snackbar.LENGTH_SHORT).show();
            }
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

