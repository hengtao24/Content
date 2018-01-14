package com.sht.content.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sht.content.database.table.DailyTable;
import com.sht.content.database.table.DoubanMomentTable;
import com.sht.content.database.table.ReadingTable;
import com.sht.content.database.table.ScienceTable;

/**
 * Created by sht on 2017/2/9.
 */

public class DatabaseHelper extends SQLiteOpenHelper{

    private static final String DB_NAME = "Content";
    private static DatabaseHelper instance = null;
    private static final int DB_VERSION = 2;
    public static final String DELETE_TABLE_DATA = "delete from ";
    public static final String DROP_TABLE = "drop table if exists ";

    private DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory cursorFactory,int version){
        super(context,name,cursorFactory,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db){

        // create table
        db.execSQL(DailyTable.CREATE_TABLE);
        db.execSQL(DailyTable.CREATE_COLLECTION_TABLE);

        db.execSQL(ScienceTable.CREATE_TABLE);
        db.execSQL(ScienceTable.CREATE_COLLECTION_TABLE);

        db.execSQL(ReadingTable.CREATE_TABLE);
        db.execSQL(ReadingTable.CREATE_COLLECTION_TABLE);

        db.execSQL(DoubanMomentTable.CREATE_TABLE);
        db.execSQL(DoubanMomentTable.CREATE_COLLECTION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
        if (oldVersion == 1){
            db.execSQL(DROP_TABLE+DailyTable.NAME);
            db.execSQL(DROP_TABLE+DailyTable.COLLECTION_NAME);
            db.execSQL(DailyTable.CREATE_TABLE);
            db.execSQL(DailyTable.CREATE_COLLECTION_TABLE);
        }
    }

    public static synchronized DatabaseHelper instance(Context context){
        if(instance == null){
            instance = new DatabaseHelper(context,DB_NAME,null,DB_VERSION);
        }
        return instance;
    }

}
