package com.sht.content.support.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.sht.content.database.cache.BaseCollectionCache;
import com.sht.content.database.cache.ICache;
import com.sht.content.support.HttpUtil;

import java.util.List;

/**
 * Created by sht on 2017/2/14.
 */

public abstract class BaseListAdapter<M,VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    // sht:2017.3.17
    // add viewType
    protected static final int TYPE_ITEM = 1;
    protected static final int TYPE_HEADER = 2;
    protected static final int TYPE_FOOTER = 3;

    protected List<M> mItems;
    protected Context mContext;
    protected ICache<M> mCache;

    protected boolean isCollection = false;

    public BaseListAdapter(Context context, ICache<M> cache){

        mContext = context;
        mCache = cache;
        mItems = cache.getmList();

        if (cache instanceof BaseCollectionCache){
            isCollection = true;
        }

        HttpUtil.readNetworkState();
    }

    @Override
    public int getItemViewType(int position){
       /* if (isPositionHeader(position)) {
            return TYPE_HEADER;
        }*/
        if (isPositionFooter(position)){
	        return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount(){
        return mItems.size() + 1;
    }

    protected M getItem(int position){
        return mItems.get(position);
    }

    // sht:2017.3.17
    // added a method to check if given position is a header
    protected boolean isPositionHeader(int position) {
        return position == 0 ;
    }

    protected  boolean isPositionFooter(int position) {
        return position == mItems.size();
    }
}
