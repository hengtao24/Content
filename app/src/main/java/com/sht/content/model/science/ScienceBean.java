package com.sht.content.model.science;

import java.io.Serializable;

/**
 * Created by sht on 2017/3/2.
 */

public class ScienceBean implements Serializable {

    private ArticleBean[] result;

    public  ArticleBean[] getResult(){
        return result;
    }

    public void setResult(ArticleBean[] mArticleBean){
        this.result = mArticleBean;
    }
}
