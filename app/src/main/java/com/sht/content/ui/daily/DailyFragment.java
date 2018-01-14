package com.sht.content.ui.daily;


import android.support.v7.widget.RecyclerView;

import com.sht.content.database.cache.cache.DailyCache;
import com.sht.content.support.Utils;
import com.sht.content.support.adapter.DailyAdapter;
import com.sht.content.ui.support.BaseListFragment;

/**
 * Created by sht on 2017/2/7.
 */

public class DailyFragment extends BaseListFragment{

    // all model hava tablayout except Daily
    @Override
    protected boolean setHeaderTab(){
        return false;
    }

    // create different cache(database) for different model
    @Override
    protected void onCreateCache(){
        mICache = new DailyCache(mHandler);
    }

    // empty function
    @Override
    protected void getArgs(){

    }

    @Override
    protected RecyclerView.Adapter bindAdapter(){
        return new DailyAdapter(getContext(),mICache);
    }

    @Override
    protected void loadFromCache(){
        mICache.loadFromCache();
    }

    @Override
    protected void loadFromNet(long date,boolean isClearing){
        Utils.DLog("load from net......");
        mICache.loadToday(date,isClearing);
    }

    @Override
    public void loadMore(long date) {
	    Utils.DLog("load more......");
        mICache.loadToday(date,false);
    }

    @Override
    protected boolean hasData(){
        return mICache.hasData();
    }
}
