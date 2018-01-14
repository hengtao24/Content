package com.sht.content.model.doubanmoment;

import java.util.ArrayList;

/**
 * Created by sht on 2017/4/6.
 */

public class DoubanMomentBean {
    private int count;
    private DoubanMomentPosts[] posts;
    private int offset;
    private String date;
    private int total;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public DoubanMomentPosts[] getPosts() {
        return posts;
    }

    public void setPosts(DoubanMomentPosts[] posts) {
        this.posts = posts;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

}
