package com.sht.content.database.cache.cache;

import android.database.Cursor;
import android.os.Handler;

import com.google.gson.Gson;
import com.sht.content.database.cache.BaseCache;
import com.sht.content.database.table.ReadingTable;
import com.sht.content.database.table.ScienceTable;
import com.sht.content.model.reading.BookBean;
import com.sht.content.model.reading.ReadingBean;
import com.sht.content.support.CONSTANT;
import com.sht.content.support.HttpUtil;
import com.sht.content.support.Utils;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by sht on 2017/3/8.
 */

public class ReadingCache extends BaseCache<BookBean>{

    public ReadingCache(Handler handler,String category,String[] urls){
        super(handler,category,urls);
    }
    @Override
    public void loadToday(long date,boolean isClearing){

    }
    @Override
    public void putData(){

        db.execSQL(mDatabaseHelper.DELETE_TABLE_DATA + ReadingTable.NAME+" where " + ReadingTable.CATEGORY+"=\'" +mCategroy+"\'");

        for(int i=0;i<mList.size();i++){
            BookBean bookBean = mList.get(i);
            mValues.put(ReadingTable.TITLE,bookBean.getTitle());
            mValues.put(ReadingTable.INFO,bookBean.getInfo());
            mValues.put(ReadingTable.IMAGE, bookBean.getImage());
            mValues.put(ReadingTable.AUTHOR_INTRO,bookBean.getAuthor_intro() ==null ? "":bookBean.getAuthor_intro());
            mValues.put(ReadingTable.CATALOG,bookBean.getCatalog() == null? "":bookBean.getCatalog());
            mValues.put(ReadingTable.EBOOK_URL,bookBean.getEbook_url() == null?"":bookBean.getEbook_url());
            mValues.put(ReadingTable.CATEGORY,mCategroy);
            mValues.put(ReadingTable.SUMMARY,bookBean.getSummary() == null?"":bookBean.getSummary());
            mValues.put(ReadingTable.IS_COLLECTED,bookBean.getIs_collected());
            db.insert(ReadingTable.NAME,null,mValues);
        }
        db.execSQL(ScienceTable.SQL_INIT_COLLECTION_FLAG);
    }

    @Override
    public void putData(BookBean bookBean){
        mValues.put(ReadingTable.TITLE,bookBean.getTitle());
        mValues.put(ReadingTable.INFO,bookBean.getInfo());
        mValues.put(ReadingTable.IMAGE,bookBean.getImage());
        mValues.put(ReadingTable.AUTHOR_INTRO,bookBean.getAuthor_intro() ==null ? "":bookBean.getAuthor_intro());
        mValues.put(ReadingTable.CATALOG,bookBean.getCatalog() == null? "":bookBean.getCatalog());
        mValues.put(ReadingTable.EBOOK_URL,bookBean.getEbook_url() == null?"":bookBean.getEbook_url());
        mValues.put(ReadingTable.SUMMARY,bookBean.getSummary() == null?"":bookBean.getSummary());
        db.insert (ReadingTable.COLLECTION_NAME,null,mValues);
    }

    @Override
    public synchronized void loadFromCache(){
        mList.clear();
        String sql = null;

        if (mCategroy == null){
            sql = "select * from "+ ReadingTable.NAME;
        }else {
            sql = "select * from "+ ReadingTable.NAME+" where "+ReadingTable.CATEGORY+"=\'" +mCategroy+"\'";
        }

        Cursor cursor = query(sql);
        while (cursor.moveToNext()){
            BookBean bookBean = new BookBean();
            bookBean.setTitle(cursor.getString(ReadingTable.ID_TITLE));
            bookBean.setImage(cursor.getString(ReadingTable.ID_IMAGE));
            bookBean.setInfo(cursor.getString(ReadingTable.ID_INFO));
            bookBean.setAuthor_intro(cursor.getString(ReadingTable.ID_AUTHOR_INTRO));
            bookBean.setCatalog(cursor.getString(ReadingTable.ID_CATALOG));
            bookBean.setEbook_url(cursor.getString(ReadingTable.ID_EBOOK_URL));
            bookBean.setSummary(cursor.getString(ReadingTable.ID_SUMMARY));
            bookBean.setIs_collected(cursor.getInt(ReadingTable.ID_IS_COLLECTED));
            mList.add(bookBean);
        }
        mHandler.sendEmptyMessage(CONSTANT.ID_FROM_CACHE);
        cursor.close();
    }

    @Override
    public void load(){

        for(int i = 0 ; i<mUrls.length; i++) {

            String url = mUrls[i];
            Request.Builder builder = new Request.Builder();
            builder.url(url);
            Utils.DLog(url);
            Request request = builder.build();
            HttpUtil.enqueue(request, new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    mHandler.sendEmptyMessage(CONSTANT.ID_FAILURE);
                }

                @Override
                public void onResponse(com.squareup.okhttp.Response response) throws IOException {

                    if (response.isSuccessful() == false) {
                        mHandler.sendEmptyMessage(CONSTANT.ID_FAILURE);
                        return;
                    }

                //    String res = response.body().string();

                    ArrayList<String> CollectionTitles = new ArrayList<String>();
                    for (int i = 0; i < mList.size(); i++) {
                        if (mList.get(i).getIs_collected() == 1) {
                            CollectionTitles.add(mList.get(i).getTitle());
                        }
                    }

                    mList.clear();
                    // Gson:transform the json data to java object
                    Gson gson = new Gson();
                    BookBean[] bookBeanS = (gson.fromJson( response.body().string(), ReadingBean.class)).getBooks();

                    for (BookBean bookBean:bookBeanS) {
                        mList.add(bookBean);
                    }

                    for (String title : CollectionTitles) {
                        for (int i = 0; i < mList.size(); i++) {
                            if (title.equals(mList.get(i).getTitle())) {
                                mList.get(i).setIs_collected(1);
                            }
                        }
                    }
                    mHandler.sendEmptyMessage(CONSTANT.ID_SUCCESS);
                }
            });
        }
    }
}
