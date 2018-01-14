package com.sht.content.model.daily;

/**
 * Created by sht on 2017/2/19.
 */

public class DailyDetailsBean {
    private String body;
    private String image;
    private String image_source;
    private String title;
    private String[] ccs;

    public String getBody(){
        return body;
    }

    public void setBody(String body){
        this.body = body;
    }

    public String getImage(){
        return image;
    }

    public void setImage(String image){
        this.image = image;
    }

    public String getImageSource(){
        return image_source;
    }

    public void setImageSource(String image_source){
        this.image_source = image_source;
    }
    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String[] getCCS(){
        return ccs;
    }

    public void setCCS(String[] ccs){
        this.ccs = ccs;
    }
}
