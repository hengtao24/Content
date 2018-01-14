package com.sht.content.model.moment;

import com.google.gson.annotations.SerializedName;



/**
 * Created by user on 2017/4/28.
 */

public class MomentPosts {

	private String published_time;
	private String url;
	private String short_url;
	private String column;
	@SerializedName("abstract")
	private String date;
	private thumbs[] thumbs;
	private String created_time;
	private String title;
	private String share_pic_url;
	private String type;
	private int id;

/*	private String content;
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
*/
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

	public thumbs[] getThumbs() {
		return thumbs;
	}

	public void setThumbs(thumbs[] thumbs) {
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

}
