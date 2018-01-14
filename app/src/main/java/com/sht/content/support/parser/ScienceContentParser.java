package com.sht.content.support.parser;

import android.util.Log;

/**
 * Created by sht on 2017/3/7.
 */

public class ScienceContentParser {

    private static String STR_CUT_LEFT="<div class=\"container article-page\">";//分割左边界
    private static String STR_CUT_RIGHT="<div class=\"recommend-articles\">";//分割右边界
    private String parserStr = null;
    private String endStr = null;

    public ScienceContentParser(String parserStr){
        this.parserStr = parserStr;
        parserData();
    }

    public String getEndStr(){
        return endStr;
    }

    private void parserData(){
        String first_cut[] = parserStr.split(STR_CUT_LEFT);
        if (first_cut.length > 1){
            String second_cut[] = first_cut[1].split(STR_CUT_RIGHT);
            if (second_cut.length > 1){
                endStr = STR_CUT_LEFT + second_cut[0] +"</div>";
            }else {
                Log.e("ERROR", "第二次解析出错，未找到有效分割元素");
            }
        }else {
            Log.e("ERROR","第一次解析出错，未找到有效分割元素");
        }
    }

}
