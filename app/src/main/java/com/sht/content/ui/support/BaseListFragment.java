package com.sht.content.ui.support;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.sht.content.ContentApplication;
import com.sht.content.database.cache.ICache;
import com.sht.content.R;
import com.sht.content.support.CONSTANT;
import com.sht.content.support.HttpUtil;
import com.sht.content.support.Settings;
import com.sht.content.support.Utils;

import java.util.Calendar;

/**
 * Created by sht on 2017/2/7.
 */

public abstract class BaseListFragment extends Fragment{

    protected View parentView;
    protected ProgressBar mProgressBar;
    protected ImageView placeHolder;
    protected RecyclerView mRecyclerView;
    protected RecyclerView.Adapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected SwipeRefreshLayout mRefreshView;

    protected ICache mICache;

    // the ID of <layout_common_list.xml>
    protected int mLayout = 0;

    protected boolean withHeaderTab = true;
    protected boolean withRefreshView =true;
    protected boolean needCache = true;

    protected abstract void getArgs();
    protected abstract void onCreateCache();
    protected abstract RecyclerView.Adapter bindAdapter();
    protected abstract void loadFromNet(long date,boolean isClearing);
    protected abstract void loadFromCache();
    protected abstract void loadMore(long date);
    protected abstract boolean hasData();

    // sht:单列获取时间
    private int mYear = Calendar.getInstance().get(Calendar.YEAR);
    private int mMonth = Calendar.getInstance().get(Calendar.MONTH);
    private int mDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        setLayout();
        needCache = setCache();
        getArgs();

        parentView = inflater.inflate(mLayout,container,false);

        withHeaderTab = setHeaderTab();
        withRefreshView = setRefreshView();

        mProgressBar = (ProgressBar) parentView.findViewById(R.id.progressbar);
        placeHolder = (ImageView) parentView.findViewById(R.id.placeholder);
        mRecyclerView = (RecyclerView) parentView.findViewById(R.id.recyclerView);

        onCreateCache();
        mAdapter = bindAdapter();

        mLayoutManager = new LinearLayoutManager(ContentApplication.getAppContext());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRefreshView = (SwipeRefreshLayout) parentView.findViewById(R.id.pull_to_refresh);

        mRefreshView.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

      /*  mRecyclerView.addOnScrollListener(new HidingScrollListener(){
            @Override
            public void onHide() {
                hideViews();
            }
            @Override
            public void onShow() {
                showViews();
            }
        });*/

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            boolean isSlidingToLast = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                // 当不滚动时
                if( newState == RecyclerView.SCROLL_STATE_IDLE ){
                    // 获取最后一个完全显示的item
                    int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                    int totalItemCount = manager.getItemCount();
                    Utils.DLog("mList个数:"+totalItemCount);
                    // 判断是否滑到底部且是继续向下滑动
                    if (lastVisibleItem == (totalItemCount -1 ) && isSlidingToLast) {
                        Calendar c = Calendar.getInstance();
                        c.set(mYear, mMonth, --mDay);
                        loadMore(c.getTimeInMillis());
                    }

                }
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                isSlidingToLast = dy > 0;
            }
        });

        View tabLayout = getActivity().findViewById(R.id.tab_layout);
        if(withHeaderTab){
            tabLayout.setVisibility(View.VISIBLE);
        }else {
            if (tabLayout != null){
                tabLayout.setVisibility(View.GONE);
            }
        }

        // add for display the loading process
    /*    mRefreshView.post(new Runnable() {
            @Override
            public void run() {
                mRefreshView.setRefreshing(true);
            }
        });
   */
        if (withRefreshView){
            mRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
                @Override
                public void onRefresh(){
                    loadFromNet(Calendar.getInstance().getTimeInMillis(),true);
                }
            });

            placeHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    placeHolder.setVisibility(View.GONE);
                    loadFromNet(Calendar.getInstance().getTimeInMillis(),true);
                }
            });
        }else {
            mRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
                @Override
                public void onRefresh(){
                    mRefreshView.setRefreshing(false);
                }
            });
        }

        HttpUtil.readNetworkState();

        // load from the cache
        new Thread(new Runnable() {
            @Override
            public void run() {
                loadFromCache();
            }
        }).start();

        return parentView;
    }


    // sht:2017.3.17
    // recyclerview ScrollListener
    private void hideViews() {
        Toolbar mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        mToolbar.animate().translationY(-mToolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));
    }

    private void showViews() {
        Toolbar mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        mToolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
    }

    protected void setLayout(){
        mLayout = R.layout.layout_common_list;
    }

    protected boolean setCache(){
       return true;
    }

    protected boolean setHeaderTab(){
        return true;
    }

    protected boolean setRefreshView(){
        return true;
    }

    protected Handler mHandler = new Handler(new Handler.Callback(){
        @Override
        public boolean handleMessage(Message msg){
            switch (msg.what){
                case CONSTANT.ID_FAILURE:
                    if(isAdded()) {
                        Utils.DLog(getString(R.string.Text_Net_Exception));
                    }
                    break;
                case CONSTANT.ID_SUCCESS:
                    if(isAdded()){
                        Utils.DLog(getString(R.string.text_refresh_success));
                    }
                    if(needCache){
                        mICache.cache();
                    }
                    break;
                case CONSTANT.ID_FROM_CACHE:
                    if(withRefreshView && hasData() == false){
                        loadFromNet(Calendar.getInstance().getTimeInMillis(),false);
                        return false;
                    }else if(Settings.isAutoRefresh && HttpUtil.isWIFI){
                        loadFromNet(Calendar.getInstance().getTimeInMillis(),false);
                        return false;
                    }
                    break;
            }

            if (withRefreshView){
                mRefreshView.setRefreshing(false);
            }

            if (hasData()){
                placeHolder.setVisibility(View.GONE);
            }else {
                placeHolder.setVisibility(View.VISIBLE);
            }

            mProgressBar.setVisibility(View.GONE);
            if (hasData()) {
                mAdapter.notifyDataSetChanged();
            }
            return false;
        }
    });
}
