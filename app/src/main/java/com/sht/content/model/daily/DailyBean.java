package com.sht.content.model.daily;

/**
 * Created by sht on 2017/2/11.
 */

public class DailyBean {

    private String date;
    private StoryBean[] stories;

 //   private TopStory[] top_stories;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public StoryBean[] getStories() {
        return stories;
    }

    public void setStories(StoryBean[] stories) {
        this.stories = stories;
    }

//    public TopStory[] getTop_stories() {
//        return top_stories;
//    }
//
//    public void setTop_stories(TopStory[] top_stories) {
//        this.top_stories = top_stories;
//    }
}
