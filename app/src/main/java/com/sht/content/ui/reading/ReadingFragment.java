package com.sht.content.ui.reading;

import android.support.v7.widget.RecyclerView;

import com.sht.content.api.ReadingApi;
import com.sht.content.api.ScienceApi;
import com.sht.content.database.cache.cache.ReadingCache;
import com.sht.content.support.Utils;
import com.sht.content.support.adapter.ReadingAdapter;
import com.sht.content.support.adapter.ScienceAdapter;
import com.sht.content.ui.support.BaseListFragment;

/**
 * Created by sht on 2017/3/8.
 */

public class ReadingFragment extends BaseListFragment {

    private int pos;
    private String[] mUrls;
    private String mCategory;

    @Override
    protected void onCreateCache(){
        mICache = new ReadingCache(mHandler,mCategory,mUrls);
    }

    @Override
    protected RecyclerView.Adapter bindAdapter(){
        return new ReadingAdapter(getContext(),mICache);
    }

    @Override
    protected void loadMore(long date){

    }

    @Override
    protected void loadFromNet(long date,boolean is){
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
        pos = getArguments().getInt("position");
        mCategory = getArguments().getString("category");
        final String[] tags = ReadingApi.getTags(ReadingApi.getApiTag(pos));
        mUrls = new String[tags.length];
        for (int i=0;i<tags.length;i++){
            mUrls[i] = ReadingApi.searchByTag+tags[i];
        }
    }
}
