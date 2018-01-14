package com.sht.content.ui.daily;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.NestedScrollView;
import android.view.View;

import com.google.gson.Gson;
import com.sht.content.R;
import com.sht.content.api.DailyApi;
import com.sht.content.database.cache.cache.DailyCache;
import com.sht.content.database.table.DailyTable;
import com.sht.content.model.daily.DailyDetailsBean;
import com.sht.content.model.daily.StoryBean;
import com.sht.content.support.CONSTANT;
import com.sht.content.support.DisplayUtil;
import com.sht.content.support.HttpUtil;
import com.sht.content.support.Settings;
import com.sht.content.ui.support.BaseDetails;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by sht on 2017/2/22.
 */

public class DailyDetails extends BaseDetails{

    private StoryBean mStoryBean;
    private String url;
    private String title;
    private String body;
    private String imageUrl;
    private int id;
    private DailyCache mDailyCache;
    private String image_source;
    private DailyDetailsBean mDailyDetailsBean;


    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        getData();
        initView();
    }

    // sht:2017-3-27
    // add mSwipeBackLayout for Slide back
    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        mSwipeBackLayout.setEdgeDp(120); //设置滑动返回触发范围
    }

    private void getData(){

        // create object of Daily
        mStoryBean = new StoryBean();
        // get data from the click story
        url = getIntent().getStringExtra("url");
        id = getIntent().getIntExtra("id",0);
        title = getIntent().getStringExtra("title");
        body = getIntent().getStringExtra("body");
        imageUrl = getIntent().getStringExtra("imageUrl");
        isCollected = getIntent().getBooleanExtra("Collection",false);
        // set the object of Daily
        mStoryBean.setID(id);
        mStoryBean.setBody(body);
        mStoryBean.setTitle(title);
        mStoryBean.setLargepic(imageUrl);
        mStoryBean.setImages(new String[]{getIntent().getStringExtra("id_small_image")});
    }

    protected void initView(){
        super.initView();
        mDailyCache = new DailyCache(mHandler);
		// body在daily_list_url中并不存在，在这里判断是否是第一次加载，
		// 第一次加载则请求网络，否则从数据库中读取
		// imgaeUrl同理
        if (body == null || body == "" || imageUrl == null || imageUrl == ""){
            onDataRefresh();
        }else {
            mHandler.sendEmptyMessage(CONSTANT.ID_FROM_CACHE);
        }
    }

    @Override
    protected void onDataRefresh(){
        Request.Builder builder = new Request.Builder();
		// 这里的url对应Details
        builder.url(url);
        Request request = builder.build();
        HttpUtil.enqueue(request, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                mHandler.sendEmptyMessage(CONSTANT.ID_FAILURE);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful() == false){
                    mHandler.sendEmptyMessage(CONSTANT.ID_FAILURE);
                    return ;
                }

                String res = response.body().string();
                Gson gson = new Gson();
				// 更新DailyTable的body
                mDailyDetailsBean = gson.fromJson(res,DailyDetailsBean.class);
                mDailyCache.execSQL(DailyTable.updateBodyContent(DailyTable.NAME,
                        mDailyDetailsBean.getTitle(),mDailyDetailsBean.getBody()));
                mDailyCache.execSQL(DailyTable.updateBodyContent(DailyTable.COLLECTION_NAME,
                        mDailyDetailsBean.getTitle(),mDailyDetailsBean.getBody()));
				// 更新DailyTable的大图
                mDailyCache.execSQL(DailyTable.updateLargepic(DailyTable.NAME,
                        mDailyDetailsBean.getTitle(),mDailyDetailsBean.getImage()));
                mDailyCache.execSQL(DailyTable.updateLargepic(DailyTable.COLLECTION_NAME,
                        mDailyDetailsBean.getTitle(),mDailyDetailsBean.getImage()));

                imageUrl = mDailyDetailsBean.getImage();
                body = mDailyDetailsBean.getBody();
                image_source = mDailyDetailsBean.getImageSource();

                mHandler.sendEmptyMessage(CONSTANT.ID_SUCCESS);
            }
        });
    }

    @Override
    protected String getShareInfo(){
        return "["+title+"]:"+ DailyApi.daily_story_base_url+id+"("
                +getString(R.string.text_share_from)+getString(R.string.app_name)+")";
    }

    @Override
    protected void addToCollection(){
        mDailyCache.execSQL(DailyTable.updateCollectionFlag(mStoryBean.getTitle(),1));
        mDailyCache.addToCollection(mStoryBean);
    }

    @Override
    protected void removeFromCollection(){
        mDailyCache.execSQL(DailyTable.updateCollectionFlag(mStoryBean.getTitle(),0));
        mDailyCache.execSQL(DailyTable.deleteCollectionFlag(mStoryBean.getTitle()));
    }

    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case CONSTANT.ID_FAILURE:
                    hideLoading();
                    displayNetworkError("HTTP");
                    break;
                case CONSTANT.ID_SUCCESS:
                case CONSTANT.ID_FROM_CACHE:
                    if (HttpUtil.isWIFI == true || Settings.getInstance().getBoolean(Settings.NO_PIC_MODE,false) == false){
                        setMainContentBg(imageUrl,title,image_source);
                    }
                    isLoadingSuccessful = true;
                    mScrollView.setVisibility(View.VISIBLE);
                     mScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                        @Override
                        public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                      //      mTvHeader.setTranslationY(Math.max(-scrollY/2,
                      //              -DisplayUtil.dip2px(getBaseContext(),170)));
                        }
                    });
                    mWebView.loadDataWithBaseURL("file:///android_asset/", "<link rel=\"stylesheet\" type=\"text/css\" href=\"dailycss.css\" />"+body, "text/html", "utf-8", null);
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    protected void onStop(){
        super.onStop();
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

}
