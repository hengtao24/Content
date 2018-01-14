package com.sht.content.database.cache.cache;

import android.database.Cursor;
import android.os.Handler;

import com.google.gson.Gson;
import com.sht.content.api.DoubanMomentApi;
import com.sht.content.database.cache.BaseCache;
import com.sht.content.database.table.DoubanMomentTable;
import com.sht.content.model.doubanmoment.DoubanMomentBean;
import com.sht.content.model.doubanmoment.DoubanMomentPosts;
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
 * Created by sht on 2017/4/5.
 */

public class DoubanMomentCache extends BaseCache<DoubanMomentPosts>{
    
    public DoubanMomentCache(Handler mHandler){
        super(mHandler);
    }

   /* public void setIsRead(DoubanMomentNews object){
        object.setRead(1);
        for (int i=0;i<mList.size();i++){
            if (mList.get(i).getId() == object.getId()){
                mList.get(i).setRead(1);
            }
        }
    }*/

    @Override
    public void putData(){
    //    db.execSQL(mDatabaseHelper.DELETE_TABLE_DATA + DoubanMomentTable.NAME);
        for(int i=0;i<mList.size();i++){
            DoubanMomentPosts dbMomentBean = mList.get(i);
            mValues.put(DoubanMomentTable.TITLE,dbMomentBean.getTitle());
            mValues.put(DoubanMomentTable.ID,dbMomentBean.getId());
            mValues.put(DoubanMomentTable.CONTENT,dbMomentBean.getContent());
           /* if (dbMomentBean.getThumbs().size() != 0) {
                mValues.put(DoubanMomentTable.IMAGE, dbMomentBean.getThumbs().get(0).getMedium().getUrl());
                Utils.DLog(dbMomentBean.getThumbs().get(0).getMedium().getUrl());
            }*/
            if(!queryIfIDExists(dbMomentBean.getId())) {
                db.insert(DoubanMomentTable.NAME, null, mValues);
            }
        }
        db.execSQL(DoubanMomentTable.SQL_INIT_COLLECTION_FLAG);
    }

    @Override
    public void putData(DoubanMomentPosts dbMomentBean){
        mValues.put(DoubanMomentTable.TITLE,dbMomentBean.getTitle());
        mValues.put(DoubanMomentTable.ID,dbMomentBean.getId());
        mValues.put(DoubanMomentTable.CONTENT,dbMomentBean.getContent() == null? " ":dbMomentBean.getContent());
        if (dbMomentBean.getThumbs().size() != 0)
            mValues.put(DoubanMomentTable.IMAGE,dbMomentBean.getThumbs().get(0).getMedium().getUrl());
        db.insert(DoubanMomentTable.COLLECTION_NAME,null,mValues);
    }



    @Override
    public void loadFromCache(){
        mList.clear();
        String sql = "select * from "+ DoubanMomentTable.NAME+" order by " + DoubanMomentTable.ID +" desc";
        Cursor cursor = query(sql);
        while (cursor.moveToNext()){
            DoubanMomentPosts dbMomentBean = new DoubanMomentPosts();
            dbMomentBean.setTitle(cursor.getString(DoubanMomentTable.ID_TITLE));
            dbMomentBean.setId(cursor.getInt(DoubanMomentTable.ID_ID));
            dbMomentBean.setContent(cursor.getString(DoubanMomentTable.ID_CONTENT));

         /*   if (cursor.getString(DoubanMomentTable.ID_IMAGE) != null) {
                dbMomentBean.setImage(cursor.getString(DoubanMomentTable.ID_IMAGE));
            }
            else
                dbMomentBean.setImage(null);*/
            mList.add(dbMomentBean);
        }
        mHandler.sendEmptyMessage(CONSTANT.ID_FROM_CACHE);
        cursor.close();
    }

    @Override
    public void loadToday(long date,final boolean isClearing){
        Request.Builder builder = new Request.Builder();

        builder.url(DoubanMomentApi.DOUBAN_MOMENT+ formatter.DoubanDateFormat(date));

        Request request = builder.build();

        HttpUtil.enqueue(request, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                mHandler.sendEmptyMessage(CONSTANT.ID_FAILURE);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful()){
                    mHandler.sendEmptyMessage(CONSTANT.ID_FAILURE);
                    return ;
                }
                if (isClearing){
                    mList.clear();
                }
                //mList.clear();
                String res = response.body().string();

                // Gson:transform the json data to java object
                Gson gson = new Gson();
                DoubanMomentPosts[] posts = gson.fromJson(res,DoubanMomentBean.class).getPosts();

                for (DoubanMomentPosts item : posts){
                    mList.add(item);
                }
                mHandler.sendEmptyMessage(CONSTANT.ID_SUCCESS);
            }
        });
    }

    @Override
    public void load(){

        Request.Builder builder = new Request.Builder();
        builder.url(DoubanMomentApi.DOUBAN_MOMENT+"2017-04-13");
        Request request = builder.build();

        HttpUtil.enqueue(request, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                mHandler.sendEmptyMessage(CONSTANT.ID_FAILURE);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful() == false) {
                    mHandler.sendEmptyMessage(CONSTANT.ID_FAILURE);
                    return;
                }
                String res = response.body().string();

                ArrayList<String> CollectionTitles = new ArrayList<String>();
                for (int i = 0; i < mList.size(); i++) {
                    if (mList.get(i).isCollected() == 1) {
                        CollectionTitles.add(mList.get(i).getTitle());
                    }
                }

                List<DoubanMomentPosts> oldList = new ArrayList<DoubanMomentPosts>();
                List<DoubanMomentPosts> newList = new ArrayList<DoubanMomentPosts>();

                for (DoubanMomentPosts bean : mList) {
                    oldList.add(bean);
                }

                // Gson:transform the json data to java object
                Gson gson = new Gson();
                DoubanMomentBean dbMomentBean = gson.fromJson(res, DoubanMomentBean.class);

                List<DoubanMomentPosts> PostsBean = new ArrayList<DoubanMomentPosts>();
             //   PostsBean = dbMomentBean.getPosts();

                for (DoubanMomentPosts item : PostsBean) {
                    newList.add(item);
                }

             //   loadOld(dbMomentBean.getDate(), oldList, newList);
            }
        });
    }

    // 查询对应ID是否存在于db之中
    private boolean queryIfIDExists(int id){

        Cursor cursor = db.query(DoubanMomentTable.NAME,null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do {
                if (id == cursor.getInt(DoubanMomentTable.ID_ID) ){
                    return true;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return false;
    }
  /*  private void loadOld(String date,final List<DoubanMomentNews> oldList,final List<DoubanMomentPosts> newList){

        Request.Builder builder = new Request.Builder();
        builder.url(DoubanMomentApi.DOUBAN_MOMENT + date);
        Request request = builder.build();
        HttpUtil.enqueue(request, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                mHandler.sendEmptyMessage(CONSTANT.ID_FAILURE);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful() == false) {
                    mHandler.sendEmptyMessage(CONSTANT.ID_FAILURE);
                    return;
                }

                String res = response.body().string();

                ArrayList<Integer> CollectionIDs = new ArrayList<Integer>();
                ArrayList<Integer> isReadIDs = new ArrayList<Integer>();

                for (int i = 0; i < oldList.size(); i++) {
                    if (oldList.get(i).isCollected() == 1) {
                        CollectionIDs.add(oldList.get(i).getId());
                    }
                    if (oldList.get(i).isRead() == 1) {
                        isReadIDs.add(oldList.get(i).getId());
                    }
                }

                mList.clear();

                Gson gson = new Gson();

                DoubanMomentBean dbMomentBean = gson.fromJson(res, DoubanMomentBean.class);
                List<DoubanMomentPosts> PostsBeans = dbMomentBean.getPosts();

                for (DoubanMomentPosts item : PostsBeans) {
                    newList.add(item);
                }

                for(DoubanMomentPosts item: newList){
                    mList.add(item);
                }

                for(Integer id:CollectionIDs){
                    for(int i=0;i<mList.size();i++){
                        if(id.equals(mList.get(i).getId())){
                            mList.get(i).setCollected(1);
                        }
                    }
                }

                for(Integer id:isReadIDs){
                    for(int i=0;i<mList.size();i++){
                        if(id.equals(mList.get(i).getId())){
                            mList.get(i).setRead(1);
                        }
                    }
                }

                // notify
                mHandler.sendEmptyMessage(CONSTANT.ID_SUCCESS);
            }
        });
    }*/
}
