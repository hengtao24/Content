package com.sht.content.model.moment;

import com.sht.content.model.doubanmoment.DoubanMomentPosts;

/**
 * Created by user on 2017/4/28.
 */

public class MomentBean {
	private int count;
	private MomentPosts[] posts;
	private int offset;
	private String date;
	private int total;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public MomentPosts[] getPosts() {
		return posts;
	}

	public void setPosts(MomentPosts[] posts) {
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
