package com.sht.content.support.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.sht.content.R;
import com.sht.content.api.DailyApi;
import com.sht.content.database.DatabaseHelper;
import com.sht.content.database.cache.ICache;
import com.sht.content.database.cache.cache.DailyCache;
import com.sht.content.database.table.DailyTable;
import com.sht.content.model.daily.StoryBean;
import com.sht.content.support.HttpUtil;
import com.sht.content.support.Settings;
import com.sht.content.ui.daily.DailyDetails;

/**
 * Created by sht on 2017/2/14.
 */

public class DailyAdapter extends BaseListAdapter<StoryBean,RecyclerView.ViewHolder> {


    // sht:2017-3-27
    // add for change the color of title
    private DailyCache mDailyCache;
    protected SQLiteDatabase db;
    protected DatabaseHelper mDatabaseHelper;

    public DailyAdapter(Context context, ICache<StoryBean> cache){
        super(context,cache);
        mDailyCache = new DailyCache(mHandler);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        // sht:2017.3.17
        // judge the viewtype
   /*     if (viewType == TYPE_ITEM){
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_daily,parent,false);
            return  new ViewHolder(itemView);
        } else if (viewType == TYPE_HEADER){
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_header,parent,false);
            return new RecyclerHeaderViewHolder(itemView);
        }

        throw new RuntimeException("There is no type that matches the type " + viewType + " + make sure your using types correctly");
     */
        /*
            View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_daily,parent,false);
        ViewHolder vh = new ViewHolder(itemView);
        return vh;
        */
        switch(viewType){
            case TYPE_ITEM:
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_daily,parent,false);
                ViewHolder vh = new ViewHolder(itemView);
                return vh;
            case TYPE_FOOTER:
                View footerView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_footer, parent, false);
                FooterViewHolder fvh = new FooterViewHolder(footerView);
                return fvh;

        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder,final int position){

        // sht:2017.3.17
        // judge isPosition
     //   if (!isPositionHeader(position)) {
            if (viewHolder instanceof ViewHolder){
                final StoryBean storyBean = getItem(position);
                final ViewHolder holder = (ViewHolder) viewHolder;
                holder.title.setText(storyBean.getTitle());

                // sht:2017-3-27
                // add for change the color of title

                if (queryIsRead(storyBean.getID()) == 0) {
                    holder.title.setTextColor(ContextCompat.getColor(mContext, R.color.textColorFirst_Day));
                } else {
                    holder.title.setTextColor(ContextCompat.getColor(mContext, R.color.color_read));
                }

                if (Settings.noPicMode && HttpUtil.isWIFI == false) {
                    holder.image.setImageURI(null);
                } else {
                    holder.image.setImageURI(Uri.parse(storyBean.getImages()[0]));
                }
                holder.parentView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // sht:2017-3-27
                        // add for change the color of title
                        if (storyBean.isRead() == 0) {
                            mDailyCache.execSQL(DailyTable.updateReadFlag(storyBean.getTitle(),1));
                           // mDailyCache.setIsRead(storyBean);
                            holder.title.setTextColor(ContextCompat.getColor(mContext,R.color.color_read));
                        }
                        // debug for new details layout
                        //       Intent intent = new Intent(mContext,DailyDetailsActivity.class);
                        Intent intent = new Intent(mContext, DailyDetails.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("url", DailyApi.daily_details_url + storyBean.getID());
                        bundle.putString("title", storyBean.getTitle());
                        bundle.putString("body", storyBean.getBody());
                        bundle.putString("imageUrl", storyBean.getLargepic());
                        bundle.putString("id_small_image", storyBean.getImages()[0]);
                        bundle.putInt("id", storyBean.getID());
                        bundle.putInt("isRead",storyBean.isRead());

                        if (isCollection) {
                            bundle.putBoolean("Collection", true);
                        } else {
                            bundle.putBoolean("Collection", storyBean.isCollected() == 1 ? true : false);
                        }

                        intent.putExtras(bundle);
                        mContext.startActivity(intent);
                    }
                });
            }

       // }
    }


    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
           return false;
        }
    });

    // item viewHolder
    class ViewHolder extends RecyclerView.ViewHolder{
        private View parentView;
        private TextView title;
        private SimpleDraweeView image;
        public ViewHolder(View itemView){
            super(itemView);
            parentView = itemView;
            title = (TextView) parentView.findViewById(R.id.title);
            image = (SimpleDraweeView) parentView.findViewById(R.id.image);
        }
    }

    // header viewHolder
    class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    // footer viewHolder
    class FooterViewHolder extends RecyclerView.ViewHolder{
        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }


    private int queryIsRead(int id){
        mDatabaseHelper = DatabaseHelper.instance(mContext);
        db = mDatabaseHelper.getWritableDatabase();
        Cursor cursor = db.query(DailyTable.NAME,null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do {
                if (id == cursor.getInt(DailyTable.ID_ID) ){
                    return cursor.getInt(DailyTable.ID_IS_READ);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return 0;
    }
}
