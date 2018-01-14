package com.sht.content.ui.science;

import android.support.v7.widget.RecyclerView;

import com.sht.content.api.ScienceApi;
import com.sht.content.database.cache.cache.ScienceCache;
import com.sht.content.support.Utils;
import com.sht.content.support.adapter.ScienceAdapter;
import com.sht.content.ui.support.BaseListFragment;

/**
 * Created by sht on 2017/2/27.
 */

public class ScienceFragment extends BaseListFragment{

    private String mUrl;
    private String mCategory;

    @Override
    protected void onCreateCache(){
        mICache = new ScienceCache(mHandler,mCategory,mUrl);
    }

    @Override
    protected RecyclerView.Adapter bindAdapter(){
        return new ScienceAdapter(getContext(),mICache);
    }

    @Override
    protected void loadMore(long date){
        Utils.DLog("load more......");
        mICache.loadToday(date,false);
    }

    @Override
    protected void loadFromNet(long date,boolean isClearing){
        Utils.DLog("load from net......");
        mICache.load();
    }

    @Override
    protected void loadFromCache(){
        mICache.loadFromCache();
    }

    @Override
    protected boolean hasData(){
        return mICache.hasData();
    }

    @Override
    protected void getArgs(){
        mUrl = ScienceApi.science_channel_url + ScienceApi.channel_tag[getArguments().getInt("position")];
        mCategory = getArguments().getString("category");
        Utils.DLog(mUrl);
    }
}
