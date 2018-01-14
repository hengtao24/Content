package com.sht.content.model.daily;

/**
 * Created by sht on 2017/2/11.
 */

public class StoryBean {

    private String title;
    private int id;
    private String []images;
    private String body;
    private String largepic;
    private boolean isCollected;
    private boolean isRead = false;

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public int getID(){
        return id;
    }

    public void setID(int id){
        this.id = id;
    }

    public String[] getImages(){
        return images;
    }

    public void setImages(String[] images){
        this.images = images;
    }

    public String getBody(){
        return body;
    }

    public void setBody(String body){
        this.body = body;
    }

    public String getLargepic(){
        return largepic;
    }

    public void setLargepic(String largepic){
        this.largepic = largepic;
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
        this.isRead = (read == 1?true:false);
    }
}
