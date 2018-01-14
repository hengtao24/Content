package com.sht.content.support.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.sht.content.R;
import com.sht.content.database.cache.ICache;
import com.sht.content.model.science.ArticleBean;
import com.sht.content.support.HttpUtil;
import com.sht.content.support.Settings;
import com.sht.content.ui.science.ScienceDetailsActivity;

/**
 * Created by sht on 2017/2/28.
 */

public class ScienceAdapter extends BaseListAdapter<ArticleBean,RecyclerView.ViewHolder> {

    public ScienceAdapter(Context context, ICache<ArticleBean> cache){
        super(context,cache);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        switch(viewType){
            case TYPE_ITEM:
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_science,parent,false);
                ViewHolder vh = new ViewHolder(itemView);
                return vh;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder,final int position){
        if (viewHolder instanceof ViewHolder) {
            final ArticleBean articleBean = getItem(position);
            final ViewHolder holder = (ViewHolder) viewHolder;
            holder.title.setText(articleBean.getTitle());

            if (Settings.noPicMode && HttpUtil.isWIFI == false) {
                holder.image.setImageURI(null);
            } else {
                holder.image.setImageURI(Uri.parse(articleBean.getImage_info().getUrl()));
            }

            holder.comment.setText(" " + articleBean.getReplies_count());

            holder.parentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(mContext, ScienceDetailsActivity.class);
                    Bundle bundle = new Bundle();

                    if (isCollection) {
                        articleBean.setIs_collected(1);
                    }
                    bundle.putSerializable("id_science", articleBean);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            });
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private View parentView;
        private TextView title;
        private TextView comment;
        private SimpleDraweeView image;
        public ViewHolder(View itemView){
            super(itemView);
            parentView = itemView;
            title = (TextView) parentView.findViewById(R.id.title);
            image = (SimpleDraweeView) parentView.findViewById(R.id.image);
            comment = (TextView) parentView.findViewById(R.id.comment);
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

    @Override
    public int getItemViewType(int position){
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount(){
        return mItems.size();
    }
}