package com.sht.content.support.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.sht.content.api.DoubanMomentApi;
import com.sht.content.database.cache.ICache;
import com.sht.content.database.cache.cache.DoubanMomentCache;
import com.sht.content.database.table.DoubanMomentTable;
import com.sht.content.model.doubanmoment.DoubanMomentPosts;
import com.sht.content.model.doubanmoment.DoubanMomentStory;
import com.sht.content.support.HttpUtil;
import com.sht.content.support.Settings;
import com.sht.content.ui.douban.DoubanMomentDetails;

/**
 * Created by sht on 2017/4/6.
 */

public class DoubanMomentAdapter extends BaseListAdapter<DoubanMomentPosts,RecyclerView.ViewHolder> {

    private DoubanMomentCache mDoubanMomentCache;

    public DoubanMomentAdapter(Context context, ICache<DoubanMomentPosts> cache){
        super(context,cache);
        mDoubanMomentCache= new DoubanMomentCache(mHandler);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        switch(viewType){
            case TYPE_ITEM:
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_daily,parent,false);
                ViewHolder vh = new ViewHolder(itemView);
                return vh;
            case TYPE_FOOTER:
                View footerView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_footer,parent,false);
                FooterViewHolder fvh = new FooterViewHolder(footerView);
                return fvh;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder,final int position){

        if (viewHolder instanceof ViewHolder) {
            final DoubanMomentPosts item = getItem(position);
            final ViewHolder holder = (ViewHolder) viewHolder;
            holder.title.setText(item.getTitle());


            if (item.isRead() == 0) {
                holder.title.setTextColor(ContextCompat.getColor(mContext, R.color.textColorFirst_Day));
            } else {
                holder.title.setTextColor(ContextCompat.getColor(mContext, R.color.color_read));
            }


            if (Settings.noPicMode && ! HttpUtil.isWIFI) {
                holder.image.setImageURI(null);
            } else {
                if (item.getThumbs().size() == 0) {
                    holder.image.setImageURI(null);
                } else
                holder.image.setImageURI(Uri.parse(item.getThumbs().get(0).getMedium().getUrl()));
            }

            holder.parentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.isRead() == 0) {
                        mDoubanMomentCache.execSQL(DoubanMomentTable.updateReadFlag(item.getTitle(), 1));
                       // mDoubanMomentCache.setIsRead(item);
                        holder.title.setTextColor(ContextCompat.getColor(mContext, R.color.color_read));
                    }
                    Intent intent = new Intent(mContext, DoubanMomentDetails.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("url", DoubanMomentApi.DOUBAN_ARTICLE_DETAIL + item.getId());
                    bundle.putString("title", item.getTitle());
                    bundle.putString("content", item.getContent());
                    bundle.putInt("isRead", item.isRead());

                    if (isCollection) {
                        bundle.putBoolean("Collection", true);
                    } else {
                        bundle.putBoolean("Collection", item.isCollected() == 1 );
                    }

                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            });
        }
    }

    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            return false;
        }
    });

    private class ViewHolder extends RecyclerView.ViewHolder{
        private View parentView;
        private TextView title;
        private SimpleDraweeView image;
        private ViewHolder(View itemView){
            super(itemView);
            parentView = itemView;
            title = (TextView) parentView.findViewById(R.id.title);
            image = (SimpleDraweeView) parentView.findViewById(R.id.image);
        }
    }

    // header viewHolder
    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    // footer viewHolder
    private class FooterViewHolder extends RecyclerView.ViewHolder{
        private FooterViewHolder(View itemView) {
            super(itemView);
        }
    }
}
