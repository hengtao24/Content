package com.sht.content.support.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.sht.content.R;
import com.sht.content.database.cache.ICache;
import com.sht.content.database.table.ReadingTable;
import com.sht.content.model.reading.BookBean;
import com.sht.content.support.HttpUtil;
import com.sht.content.support.Settings;
import com.sht.content.support.Utils;
import com.sht.content.support.adapter.ReadingAdapter.ViewHolder;
import com.sht.content.ui.reading.ReadingDetailsActivity;

/**
 * Created by sht on 2017/3/8.
 */

public class ReadingAdapter extends BaseListAdapter<BookBean,ViewHolder> {

    public ReadingAdapter(Context context, ICache<BookBean> cache){
        super(context,cache);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reading,parent,false);
        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder,final int position){
        final BookBean bookBean = getItem(position);
        holder.title.setText(bookBean.getTitle());
        holder.info.setText(bookBean.getInfo());

        if (Settings.noPicMode && HttpUtil.isWIFI ==false){
            holder.mSimpleDraweeView.setImageURI(null);
        }else {
            holder.mSimpleDraweeView.setImageURI(Uri.parse(bookBean.getImage()));
        }

        holder.parentView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(mContext, ReadingDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("book",bookBean);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });

        //set book
        if (Utils.hasString(bookBean.getEbook_url())){
            holder.ebook.setVisibility(View.VISIBLE);
        }else {
            holder.ebook.setVisibility(View.GONE);
        }

        if (isCollection){
            holder.collect_cb.setVisibility(View.GONE);
            holder.text.setText("remove");
            holder.text.setTextColor(ContextCompat.getColor(mContext,R.color.colorPrimary));
            holder.text.setTextSize(18);
            holder.text.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Snackbar.make(holder.parentView,"Remove it from collection successful!!",Snackbar.LENGTH_SHORT)
                            .setAction("OK",new View.OnClickListener(){

                                @Override
                                public void onClick(View v){
                                    if (mItems.contains(bookBean) == false){
                                        return;
                                    }
                                    mCache.execSQL(ReadingTable.updateCollectionFlag(bookBean.getTitle(),0));
                                    mCache.execSQL(ReadingTable.deleteCollectionFlag(bookBean.getTitle()));
                                    mItems.remove(position);
                                    notifyDataSetChanged();
                                }
                            })
                            .show();
                }
            });
            return;
        }

        holder.collect_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked){
                bookBean.setIs_collected(isChecked ? 1 : 0);
                mCache.execSQL(ReadingTable.updateCollectionFlag(bookBean.getTitle(),isChecked ? 1:0));
                if (isChecked){
                    mCache.addToCollection(bookBean);
                } else {
                    mCache.execSQL(ReadingTable.deleteCollectionFlag(bookBean.getTitle()));
                }
            }
        });

        holder.collect_cb.setChecked(bookBean.getIs_collected() == 1 ? true:false);
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private View parentView;
        private SimpleDraweeView mSimpleDraweeView;
        private TextView title;
        private TextView info;
        private CheckBox collect_cb;
        private TextView text;
        private ImageView ebook;
        public ViewHolder(View itemView){
            super(itemView);
            parentView = itemView;
            mSimpleDraweeView = (SimpleDraweeView) itemView.findViewById(R.id.bookImg);
            title = (TextView) itemView.findViewById(R.id.bookTitle);
            info = (TextView) itemView.findViewById(R.id.bookInfo);
            collect_cb = (CheckBox) itemView.findViewById(R.id.collect_cb);
            ebook = (ImageView) itemView.findViewById(R.id.ebook);
            if(isCollection){
                text = (TextView) parentView.findViewById(R.id.text);
            }
        }
    }
}
