package com.sht.content.database.cache;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;

import com.sht.content.ContentApplication;
import com.sht.content.database.DatabaseHelper;
import com.sht.content.support.DateFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sht on 2017/2/9.
 */

public abstract class BaseCache<T> implements ICache<T>{

    protected Context mContext = ContentApplication.getAppContext();
    protected DatabaseHelper mDatabaseHelper;
    protected SQLiteDatabase db;

    protected Handler mHandler;
    protected String mCategroy;
    protected String mUrl;
    protected String[] mUrls;

    protected ContentValues mValues;
    protected List<T> mList = new ArrayList<>();

    protected DateFormatter formatter = new DateFormatter();

    public BaseCache(){
        mDatabaseHelper = DatabaseHelper.instance(mContext);
    }

    public BaseCache(Handler handler,String categroy){
        mDatabaseHelper = DatabaseHelper.instance(mContext);
        mHandler = handler;
        mCategroy = categroy;
    }

    public BaseCache(Handler handler,String categroy,String[] urls){
        this(handler,categroy);
        mUrls = urls;
    }

    public BaseCache(Handler handler,String categroy,String url){
        this(handler,categroy);
        mUrl = url;
    }

    protected BaseCache(Handler handler){
        this(handler,null);
    }

    protected abstract void putData();
    protected abstract void putData(T object);

    public synchronized void cache(){
        db = mDatabaseHelper.getWritableDatabase();
        db.beginTransaction();
        mValues = new ContentValues();
        putData();
        db.setTransactionSuccessful();
        db.endTransaction();
    }
    public synchronized void addToCollection(T object){
        db = mDatabaseHelper.getWritableDatabase();
        db.beginTransaction();
        mValues = new ContentValues();
        putData(object);
        db.setTransactionSuccessful();
        db.endTransaction();
    }
    public synchronized void execSQL(String sql){
        db = mDatabaseHelper.getWritableDatabase();
        db.execSQL(sql);
    }
    public List<T> getmList(){
        return mList;
    }
    public boolean hasData(){
        return !mList.isEmpty();
    }

    public abstract void loadFromCache();

    protected Cursor query(String sql){
        return mDatabaseHelper.getReadableDatabase().rawQuery(sql,null);
    }


}
