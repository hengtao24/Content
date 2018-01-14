package com.sht.content.model.moment;

/**
 * Created by user on 2017/4/28.
 */

public class thumbs {

	medium medium;
	String description;
	String tag_name;
	int id;

	public medium getMedium() {
		return medium;
	}
	public void setMedium(medium medium) {
		this.medium = medium;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public String getTag_name() {
		return tag_name;
	}
	public void setTag_name(String tag_name) {
		this.tag_name = tag_name;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}


	public  class medium {
		String url;
		int width;
		int height;
		public  String getUrl() {
			return url;
		}

	}

}
