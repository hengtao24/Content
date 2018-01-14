package com.sht.content.database.cache.cache;

import android.database.Cursor;
import android.os.Handler;

import com.google.gson.Gson;
import com.sht.content.api.DailyApi;
import com.sht.content.database.cache.BaseCache;
import com.sht.content.database.table.DailyTable;
import com.sht.content.database.table.ScienceTable;
import com.sht.content.model.daily.DailyBean;
import com.sht.content.model.daily.StoryBean;
import com.sht.content.model.science.ArticleBean;
import com.sht.content.model.science.ScienceBean;
import com.sht.content.support.CONSTANT;
import com.sht.content.support.HttpUtil;
import com.sht.content.support.Utils;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sht on 2017/2/28.
 */

public class ScienceCache extends BaseCache<ArticleBean>{

    public ScienceCache(){
        super();
    }
    @Override
    public void loadToday(long date,boolean isClearing){

    }

    public ScienceCache(Handler handler,String category,String url){
        super(handler,category,url);
    }

    @Override
    public synchronized void loadFromCache(){

        mList.clear();
        String sql = null;

        if (mCategroy == null){
            sql = "select * from "+ScienceTable.NAME;
        }else {
            sql = "select * from "+ScienceTable.NAME+" where "+ScienceTable.CATEGORY+"=\'" +mCategroy+"\'";
        }

        Cursor cursor = query(sql);
        while (cursor.moveToNext()){
            ArticleBean articleBean = new ArticleBean();
            articleBean.setTitle(cursor.getString(ScienceTable.ID_TITLE));
            articleBean.setSummary(cursor.getString(ScienceTable.ID_DESCRIPTION));
            if (articleBean.getImage_info() == null){
                Utils.DLog(" "+articleBean.getImage_info());
            }else {
                articleBean.getImage_info().setUrl(cursor.getString(ScienceTable.ID_IMAGE));
            }
            articleBean.setReplies_count(cursor.getInt(ScienceTable.ID_COMMENT_COUNT));
            articleBean.setInfo(cursor.getString(ScienceTable.ID_INFO));
            articleBean.setIs_collected(cursor.getInt(ScienceTable.ID_IS_COLLECTED));
            articleBean.setUrl(cursor.getString(ScienceTable.ID_URL));
            mList.add(articleBean);
        }
        mHandler.sendEmptyMessage(CONSTANT.ID_FROM_CACHE);
        cursor.close();
    }

    @Override
    public void putData(){

        db.execSQL(mDatabaseHelper.DELETE_TABLE_DATA + ScienceTable.NAME+" where " + ScienceTable.CATEGORY+"=\'" +mCategroy+"\'");

        for(int i=0;i<mList.size();i++){
            ArticleBean articleBean = mList.get(i);
            mValues.put(ScienceTable.TITLE,articleBean.getTitle());
            mValues.put(ScienceTable.DESCRIPTION,articleBean.getSummary());
            mValues.put(ScienceTable.IMAGE,articleBean.getImage_info().getUrl());
            mValues.put(ScienceTable.COMMENT_COUNT, articleBean.getReplies_count());
            mValues.put(ScienceTable.INFO,articleBean.getInfo());
            mValues.put(ScienceTable.URL,articleBean.getUrl());
            mValues.put(ScienceTable.CATEGORY,mCategroy);
            mValues.put(ScienceTable.IS_COLLECTED,articleBean.getIs_collected());
            db.insert(ScienceTable.NAME,null,mValues);
        }
        db.execSQL(ScienceTable.SQL_INIT_COLLECTION_FLAG);
    }

    @Override
    public void putData(ArticleBean articleBean){
        mValues.put(ScienceTable.TITLE,articleBean.getTitle());
        mValues.put(ScienceTable.DESCRIPTION,articleBean.getSummary());
        mValues.put(ScienceTable.IMAGE,articleBean.getImage_info().getUrl());
        mValues.put(ScienceTable.COMMENT_COUNT, articleBean.getReplies_count());
        mValues.put(ScienceTable.URL,articleBean.getUrl());
        mValues.put(ScienceTable.INFO,articleBean.getInfo());
        db.insert(ScienceTable.COLLECTION_NAME,null,mValues);
    }

    @Override
    public void load(){
        Request.Builder builder = new Request.Builder();
        builder.url(mUrl);

        Request request = builder.build();
        HttpUtil.enqueue(request, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                mHandler.sendEmptyMessage(CONSTANT.ID_FAILURE);
            }

            @Override
            public void onResponse(com.squareup.okhttp.Response response) throws IOException {

                if (response.isSuccessful() == false){
                    mHandler.sendEmptyMessage(CONSTANT.ID_FAILURE);
                    return ;
                }

                String res = response.body().string();

                ArrayList<String> CollectionTitles = new ArrayList<String>();
                for(int i=0;i<mList.size();i++){
                    if (mList.get(i).getIs_collected() == 1){
                        CollectionTitles.add(mList.get(i).getTitle());
                    }
                }

                mList.clear();
                // Gson:transform the json data to java object
                Gson gson = new Gson();
                ArticleBean[] articleBeanS = (gson.fromJson(res, ScienceBean.class)).getResult();
                for (ArticleBean articleBean : articleBeanS){
                    mList.add(articleBean);
                }

                for (String title:CollectionTitles){
                    for (int i=0; i<mList.size();i++){
                        if (title.equals(mList.get(i).getTitle())){
                            mList.get(i).setIs_collected(1);
                        }
                    }
                }
                mHandler.sendEmptyMessage(CONSTANT.ID_SUCCESS);
            }
        });

    }
}
