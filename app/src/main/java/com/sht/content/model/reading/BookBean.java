package com.sht.content.model.reading;

import android.content.Context;

import com.sht.content.ContentApplication;

import java.io.Serializable;

/**
 * Created by sht on 2017/3/8.
 */

public class BookBean implements Serializable{
    String[] author;
    String pubdate;
    String image;
    String catalog;
    String ebook_url;
    String pages;
    String id;
    String title;
    String author_intro;
    String summary;
    String price;

    //self define
    private String Info;
    private int is_collected = 0;

    public String[] getAuthor(){
        return author;
    }

    public void setAuthor(String[] author){
        this.author = author;
    }

    public String getPubDate(){
        return pubdate;
    }

    public void setPubdate(String pubdate){
        this.pubdate = pubdate;
    }

    public String getImage(){
        return image;
    }

    public void setImage(String image){
        this.image = image;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getEbook_url(){
        return ebook_url;
    }

    public void setEbook_url(String ebook_url){
        this.ebook_url = ebook_url;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getAuthor_intro() {
        return author_intro;
    }

    public void setAuthor_intro(String author_intro) {
        this.author_intro = author_intro;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getInfo(){
        if (Info == null)
            return toString();
        return Info;
    }

    public void setInfo(String info){
        this.Info = info;
    }

    public int getIs_collected() {
        return is_collected;
    }

    public void setIs_collected(int is_collected) {
        this.is_collected = is_collected;
    }

    @Override
    public String toString(){
        Context mContext = ContentApplication.getAppContext();
        StringBuffer sb = new StringBuffer();
        if (getAuthor() != null)
        for (String s:getAuthor()){
            sb.append(" "+s);
        }
        return "Author" + sb +"\n"+
                "Publish Date "+getPubDate()+"\n"+
                "Page"+getPages()+"\n"+
                "Price" + getPrice();
    }
}
