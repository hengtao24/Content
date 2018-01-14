package com.sht.content.database.table;

import android.media.Image;

/**
 * Created by sht on 2017/2/9.
 */

public class DailyTable {

    public static final String NAME = "daily_table";
    public static final String COLLECTION_NAME = "collection_daily_table";

    public static final String TITLE = "title";
    public static final String ID = "id";
    public static final String IMAGE = "image";
    public static final String BODY = "body";
    public static final String LARGEPIC = "largepic";
    public static final String IS_COLLECTED = "is_collected";

    // sht:2017-3-27
    // add for change title color
    public static final String IS_READ = "is_read";

    public static final int ID_TITLE = 0;
    public static final int ID_ID = 1;
    public static final int ID_IMAGE = 2;
    public static final int ID_BODY = 3;
    public static final int ID_LARGEPIC = 4;
    public static final int ID_IS_COLLECTED = 5;
    // sht:2017-3-27
    // add for change title color
    public static final int ID_IS_READ = 6;

    public static final String SELECT_ALL_FROM_COLLECTION = "select * from " + COLLECTION_NAME;

    public static final String CREATE_TABLE = "create table " + NAME + "("+
            TITLE+" text ,"+
            ID+" integer primary key ,"+
            IMAGE+" text ,"+
            BODY+" text ,"+
            LARGEPIC+" text ,"+
            IS_COLLECTED+" integer ,"+
            IS_READ+" integer)";

    public static final String CREATE_COLLECTION_TABLE = "create table "+COLLECTION_NAME+
            "("+TITLE+" text,"+
            ID+" integer promary key,"+
            IMAGE+" text,"+
            BODY+" text,"+
            LARGEPIC+" text)";

    public static final String SQL_INIT_COLLECTION_FLAG = "update "+NAME+
            " set "+IS_COLLECTED+" =1 where " +TITLE+" in ( select " + TITLE+
            " from "+COLLECTION_NAME+")";

    public static  String updateCollectionFlag(String title,int flag){
        return "update "+NAME+" set "+IS_COLLECTED+" ="+flag+" where "+
                TITLE+"="+"\'"+title+"\'";
    }

    // sht:2017-3-27
    // add for change title color
    public static String updateReadFlag(String title,int flag){
        return "update "+NAME+" set "+IS_READ+" ="+flag+" where "+
                TITLE+"="+"\'"+title+"\'";
    }

    public static String updateBodyContent(String tableName,String title,String body){
        body = body.replaceAll("'","`");
        return "update " + tableName + " set " + BODY + " =\'"+body+"\' where "+
                TITLE+"="+"\'"+title+"\'";
    }

    public static  String updateLargepic(String tableName,String title,String imageUrl){
        return "update " + tableName + " set " + LARGEPIC+ " =\'"+imageUrl+"\' where "+
                TITLE+"="+"\'"+title+"\'";
    }

    public static  String deleteCollectionFlag(String title){
        return "delete from " + COLLECTION_NAME +" where +" + TITLE + "=" +"\'" + title+"\'";
    }
}
