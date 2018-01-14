package com.sht.content.database.cache;

import java.util.List;

/**
 * Created by sht on 2017/2/9.
 * Cache interface.All cache class must implement this interface.
 * @author SHTT
 * @version  Content 1.0
 */

public interface ICache<T> {
    void addToCollection(T object);
    void execSQL(String sql);
    List<T> getmList();
    boolean hasData();
    void load();
    void loadFromCache();
    void cache();
    void loadToday(long date,boolean isClearing);
}
