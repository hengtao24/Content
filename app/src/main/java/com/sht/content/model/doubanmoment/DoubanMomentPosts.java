package com.sht.content.model.doubanmoment;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by sht on 2017/4/6.
 */

public class DoubanMomentPosts {


    private String published_time;
    private String url;
    private String short_url;
    private author author;
    private String column;
    @SerializedName("abstract")
    private String date;
    private ArrayList<thumbs> thumbs = new ArrayList<>();
    private String created_time;
    private String title;
    private String share_pic_url;
    private String type;
    private int id;

    private String content;
    private boolean isCollected;
    private boolean isRead = false;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int isCollected(){
        return isCollected ? 1:0;
    }

    public void setCollected(int collected){
        this.isCollected = (collected == 1?true:false);
    }

    public int isRead() {
        return isRead? 1:0;
    }

    public void setRead(int read) {
        this.isRead = (read == 1?true:false);;
    }

    public String getPublished_time() {
        return published_time;
    }

    public void setPublished_time(String published_time) {
        this.published_time = published_time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getShort_url() {
        return short_url;
    }

    public void setShort_url(String short_url) {
        this.short_url = short_url;
    }


    public DoubanMomentPosts.author getAuthor() {
        return author;
    }

    public void setAuthor(DoubanMomentPosts.author author) {
        this.author = author;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }



    public ArrayList<thumbs> getThumbs() {
       // if (thumbs.size() == 0)
         //   return null;
        return thumbs;
    }

    public void setThumbs(ArrayList<thumbs> thumbs) {
        this.thumbs = thumbs;
    }

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShare_pic_url() {
        return share_pic_url;
    }

    public void setShare_pic_url(String share_pic_url) {
        this.share_pic_url = share_pic_url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public class thumbs {
        medium medium;
        String description;
        large large;
        String tag_name;
        small small;
        int id;

        public DoubanMomentPosts.medium getMedium() {
            return medium;
        }

        public void setMedium(DoubanMomentPosts.medium medium) {
            this.medium = medium;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public DoubanMomentPosts.large getLarge() {
            return large;
        }

        public void setLarge(DoubanMomentPosts.large large) {
            this.large = large;
        }

        public String getTag_name() {
            return tag_name;
        }

        public void setTag_name(String tag_name) {
            this.tag_name = tag_name;
        }

        public DoubanMomentPosts.small getSmall() {
            return small;
        }

        public void setSmall(DoubanMomentPosts.small small) {
            this.small = small;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    public class small {
        String url;
        int width;
        int height;

        public String getUrl() {
            return url;
        }
    }

    public  class medium {
        String url;
        int width;
        int height;
        public  String getUrl() {
            return url;
        }

    }

    public class large {
        String url;
        int width;
        int height;

        public String getUrl() {
            return url;
        }
    }

    private class author {
        boolean is_followed;
        String uid;
        String url;
        String avatar;
        String name;
        boolean is_special_user;
        int n_posts;
        String alt;
        String large_avatar;
        String id;
        boolean is_auth_author;
    }

  /*  public String getImage(){
        return medium.getUrl();
    }
    public void setImage(String url){
        medium.url = url;
    }*/
}
