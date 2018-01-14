package com.sht.content.ui.douban;

import android.support.v7.widget.RecyclerView;

import com.sht.content.database.cache.cache.DoubanMomentCache;
import com.sht.content.support.Utils;
import com.sht.content.support.adapter.DailyAdapter;
import com.sht.content.support.adapter.DoubanMomentAdapter;
import com.sht.content.ui.support.BaseListFragment;

import java.util.Calendar;

/**
 * Created by sht on 2017/4/5.
 */

public class DoubanMomentFragment extends BaseListFragment {

    // all model hava tablayout except Daily & DouBanMoment

    @Override
    protected boolean setHeaderTab(){
        return false;
    }

    // create different cache(database) for different model
    @Override
    protected void onCreateCache(){
        mICache = new DoubanMomentCache(mHandler);
    }

    // empty function
    @Override
    protected void getArgs(){

    }

    @Override
    protected RecyclerView.Adapter bindAdapter(){
        return new DoubanMomentAdapter(getContext(),mICache);
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
    protected void loadMore(long date){
        Utils.DLog("load more......");
        mICache.loadToday(date,false);
    }
    @Override
    protected boolean hasData(){
        return mICache.hasData();
    }
}
