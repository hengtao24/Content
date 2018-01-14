package com.sht.content.ui.douban;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.NestedScrollView;
import android.view.View;

import com.sht.content.R;
import com.sht.content.api.DoubanMomentApi;
import com.sht.content.database.cache.cache.DailyCache;
import com.sht.content.database.cache.cache.DoubanMomentCache;
import com.sht.content.database.table.DoubanMomentTable;
import com.sht.content.model.daily.StoryBean;
import com.sht.content.model.doubanmoment.DoubanMomentBean;
import com.sht.content.model.doubanmoment.DoubanMomentStory;
import com.sht.content.support.CONSTANT;
import com.sht.content.support.HttpUtil;
import com.sht.content.support.Settings;
import com.sht.content.ui.support.BaseDetails;

/**
 * Created by sht on 2017/4/6.
 */

public class DoubanMomentDetails extends BaseDetails {

	private DoubanMomentBean mDoubanMomBean;
	private String url;
	private String title;
	private String body;
	private String imageUrl;
	private int id;
	private DoubanMomentCache doubanMomentCache;
	private String image_source;
	private DoubanMomentStory mDoubanMomStory;

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
		mDoubanMomBean = new DoubanMomentBean();
		// get data from the click story
		url = getIntent().getStringExtra("url");
		id = getIntent().getIntExtra("id",0);
		title = getIntent().getStringExtra("title");
		body = getIntent().getStringExtra("body");
		imageUrl = getIntent().getStringExtra("imageUrl");
		isCollected = getIntent().getBooleanExtra("Collection",false);
		// set the object of Daily
	/*	mDoubanMomBean.setID(id);
		mDoubanMomBean.setBody(body);
		mDoubanMomBean.setTitle(title);
		mDoubanMomBean.setLargepic(imageUrl);
		mDoubanMomBean.setImages(new String[]{getIntent().getStringExtra("id_small_image")});*/
	}

	protected void initView(){
		super.initView();
		doubanMomentCache = new DoubanMomentCache(mHandler);
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

	}

	@Override
	protected String getShareInfo(){
		return "["+title+"]:"+ DoubanMomentApi.DOUBAN_ARTICLE_DETAIL+id+"("
				+getString(R.string.text_share_from)+getString(R.string.app_name)+")";
	}

	@Override
	protected void addToCollection(){
		//doubanMomentCache.execSQL(DoubanMomentTable.updateCollectionFlag(mDoubanMomBean.getTitle(),1));
		//doubanMomentCache.addToCollection(mDoubanMomBean.getPosts());
	}

	@Override
	protected void removeFromCollection(){
		//doubanMomentCache.execSQL(DoubanMomentTable.updateCollectionFlag(mDoubanMomBean.getTitle(),0));
		//doubanMomentCache.execSQL(DoubanMomentTable.deleteCollectionFlag(mDoubanMomBean.getTitle()));
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

