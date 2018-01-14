package com.sht.content.database.cache.cache;

import android.database.Cursor;
import android.os.Handler;

import com.google.gson.Gson;
import com.sht.content.api.DailyApi;
import com.sht.content.database.cache.BaseCache;
import com.sht.content.database.table.DailyTable;
import com.sht.content.model.daily.DailyBean;
import com.sht.content.model.daily.StoryBean;
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
 * Created by sht on 2017/2/11.
 */

public class DailyCache extends BaseCache<StoryBean>{

    public DailyCache (Handler handler){
        super(handler);
    }

    // 可以重写在BaseCache<T>中
    public void setIsRead(StoryBean object){
        object.setRead(1);
        for (int i=0;i<mList.size();i++){
            if (mList.get(i).getID() == object.getID()){
                mList.get(i).setRead(1);
            }
        }
    }

    @Override
    public synchronized void loadFromCache(){
        mList.clear();
        String sql = "select * from " + DailyTable.NAME + " order by " + DailyTable.ID + " desc";
        Cursor cursor = query(sql);
        while (cursor.moveToNext()){
            StoryBean storyBean = new StoryBean();
            storyBean.setTitle(cursor.getString(DailyTable.ID_TITLE));
            storyBean.setID(cursor.getInt(DailyTable.ID_ID));
            storyBean.setImages(new String[]{cursor.getString(DailyTable.ID_IMAGE)});
            storyBean.setBody(cursor.getString(DailyTable.ID_BODY));
            storyBean.setLargepic(cursor.getString(DailyTable.ID_LARGEPIC));
            storyBean.setCollected(cursor.getInt(DailyTable.ID_IS_COLLECTED));
            storyBean.setRead(cursor.getInt(DailyTable.ID_IS_READ));
            mList.add(storyBean);
        }
        mHandler.sendEmptyMessage(CONSTANT.ID_FROM_CACHE);
        cursor.close();
    }

    @Override
    public void putData(){

      //  db.execSQL(mDatabaseHelper.DELETE_TABLE_DATA + DailyTable.NAME);

        for(int i=0;i<mList.size();i++){
            StoryBean storyBean = mList.get(i);
            mValues.put(DailyTable.TITLE,storyBean.getTitle());
            mValues.put(DailyTable.ID,storyBean.getID());
            mValues.put(DailyTable.IMAGE,storyBean.getImages()[0]);
            mValues.put(DailyTable.BODY,storyBean.getBody());
            mValues.put(DailyTable.LARGEPIC,storyBean.getLargepic());
            mValues.put(DailyTable.IS_COLLECTED,storyBean.isCollected());
            if ( queryIsRead(storyBean.getID()) == 1){
                Utils.DLog("story的ID:"+storyBean.getID());
                storyBean.setRead(1);
            }
            mValues.put(DailyTable.IS_READ,storyBean.isRead());
            Utils.DLog("story的ID:"+storyBean.isRead());
            if(!queryIfIDExists(storyBean.getID())) {
                db.insert(DailyTable.NAME, null, mValues);
            }
        }
        db.execSQL(DailyTable.SQL_INIT_COLLECTION_FLAG);
    }

    @Override
    public void putData(StoryBean storyBean){
        mValues.put(DailyTable.TITLE,storyBean.getTitle());
        mValues.put(DailyTable.ID,storyBean.getID());
        mValues.put(DailyTable.IMAGE,storyBean.getImages()[0]);
        mValues.put(DailyTable.BODY,storyBean.getBody() == null ?"":storyBean.getBody());
        mValues.put(DailyTable.LARGEPIC,storyBean.getLargepic());
        db.insert(DailyTable.COLLECTION_NAME,null,mValues);
    }
    // sht:2017-4-19
    // load the latest date
    @Override
    public void loadToday(long date,final boolean isClearing){

        Request.Builder builder = new Request.Builder();

        builder.url(DailyApi.daily_old_url + formatter.ZhihuDailyDateFormat(date));

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
                DailyBean dailyBean = gson.fromJson(res,DailyBean.class);
                StoryBean[] storyBeens = dailyBean.getStories();

                for (StoryBean storyBean:storyBeens){
                    mList.add(storyBean);
                }
                mHandler.sendEmptyMessage(CONSTANT.ID_SUCCESS);
            }
        });
    }

    // load today's data
    @Override
    public void load(){
        Request.Builder builder = new Request.Builder();
        builder.url(DailyApi.daily_url);
        Request request = builder.build();
        HttpUtil.enqueue(request, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                mHandler.sendEmptyMessage(CONSTANT.ID_FAILURE);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful() == false){
                    mHandler.sendEmptyMessage(CONSTANT.ID_FAILURE);
                    return ;
                }
                String res = response.body().string();

                ArrayList<String> CollectionTitles = new ArrayList<String>();
                for(int i=0;i<mList.size();i++){
                    if (mList.get(i).isCollected() == 1){
                        CollectionTitles.add(mList.get(i).getTitle());
                    }
                }

                List<StoryBean> oldList = new ArrayList<StoryBean>();
                List<StoryBean> newList = new ArrayList<StoryBean>();

                for (StoryBean storyBean:mList){
                    oldList.add(storyBean);
                }
                mList.clear();
                // Gson:transform the json data to java object
                Gson gson = new Gson();
                DailyBean dailyBean = gson.fromJson(res,DailyBean.class);
                StoryBean[] storyBeens = dailyBean.getStories();
                for (StoryBean storyBean:storyBeens){
                    mList.add(storyBean);
                }

                loadOld(dailyBean.getDate(),oldList,newList);
            }
        });
    }

    // load yesterday's data
    private void loadOld(String date,final List<StoryBean> oldList,final List<StoryBean> newList) {
        Request.Builder builder = new Request.Builder();
        builder.url(DailyApi.daily_old_url + date);
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
                        CollectionIDs.add(oldList.get(i).getID());
                    }
                    if (oldList.get(i).isRead() == 1) {
                        isReadIDs.add(oldList.get(i).getID());
                    }
                }

                mList.clear();

                Gson gson = new Gson();

                DailyBean dailyBean = gson.fromJson(res, DailyBean.class);
                StoryBean[] storyBeens = dailyBean.getStories();

                for (StoryBean storyBean : storyBeens) {
                    newList.add(storyBean);
                }

                for(StoryBean storyBean: newList){
                    mList.add(storyBean);
                }

                for(Integer id:CollectionIDs){
                    for(int i=0;i<mList.size();i++){
                        if(id.equals(mList.get(i).getID())){
                            mList.get(i).setCollected(1);
                        }
                    }
                }

                for(Integer id:isReadIDs){
                    for(int i=0;i<mList.size();i++){
                        if(id.equals(mList.get(i).getID())){
                            mList.get(i).setRead(1);
                        }
                    }
                }

                // notify
                mHandler.sendEmptyMessage(CONSTANT.ID_SUCCESS);
            }
        });
    }

    // 查询对应ID是否存在于db之中
    private boolean queryIfIDExists(int id){

        Cursor cursor = db.query(DailyTable.NAME,null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do {
                if (id == cursor.getInt(DailyTable.ID_ID) ){
                    return true;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return false;
    }

    // 查询is_read对应ID
    private int queryIsRead(int id){

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
