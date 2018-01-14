package com.sht.content.database.table;

/**
 * Created by sht on 2017/4/6.
 */

public class DoubanMomentTable {

    public static final String NAME = "doubenmoment_table";
    public static final String COLLECTION_NAME = "collection_ddoubanmoment_table";

    public static final String TITLE = "title";
    public static final String ID = "id";
    public static final String CONTENT = "content";
    public static final String IMAGE = "image";
    public static final String IS_READ = "is_read";
    public static final String IS_COLLECTED = "is_collected";

    public static final int ID_TITLE = 0;
    public static final int ID_ID = 1;
    public static final int ID_CONTENT = 2;
    public static final int ID_IMAGE = 3;
    public static final int ID_IS_READ = 4;
    public static final int ID_IS_COLLECTED = 5;

    public static final String SQL_INIT_COLLECTION_FLAG = "update "+NAME+
            " set "+IS_COLLECTED+" =1 where " +TITLE+" in ( select " + TITLE+
            " from "+COLLECTION_NAME+")";

    public static final String CREATE_TABLE = "create table " + NAME + "("+
            TITLE+" text ,"+
            ID+" integer primary key ,"+
            CONTENT+" text ,"+
            IMAGE + " text ,"+
            IS_COLLECTED+" integer ,"+
            IS_READ+" integer)";

    public static final String CREATE_COLLECTION_TABLE = "create table "+COLLECTION_NAME+
            "("+TITLE+" text,"+
            ID+" integer promary key,"+
            CONTENT+" text,"+
            IMAGE + " text ,"+
            IS_READ+" text)";

    public static String updateBodyContent(String tableName,String title,String body){
        body = body.replaceAll("'","`");
        return "update " + tableName + " set " + CONTENT + " =\'"+body+"\' where "+
                TITLE+"="+"\'"+title+"\'";
    }

    public static  String updateCollectionFlag(String title,int flag){
        return "update "+NAME+" set "+IS_COLLECTED+" ="+flag+" where "+
                TITLE+"="+"\'"+title+"\'";
    }

    public static String updateReadFlag(String title,int flag){
        return "update "+NAME+" set "+IS_READ+" ="+flag+" where "+
                TITLE+"="+"\'"+title+"\'";
    }

    public static  String deleteCollectionFlag(String title){
        return "delete from " + COLLECTION_NAME +" where +" + TITLE + "=" +"\'" + title+"\'";
    }
}
