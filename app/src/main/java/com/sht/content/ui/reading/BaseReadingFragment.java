package com.sht.content.ui.reading;


import android.os.Bundle;
import android.support.v4.app.Fragment;


import com.sht.content.api.ReadingApi;
import com.sht.content.support.adapter.PagerAdapter;
import com.sht.content.ui.support.AbsTopNavigationFragment;

/**
 * Created by sht on 2017/2/15.
 */

public class BaseReadingFragment extends AbsTopNavigationFragment{

    private PagerAdapter mPagerAdapter;

    @Override
    protected PagerAdapter initPagerAdapter(){
        mPagerAdapter = new PagerAdapter(getChildFragmentManager(), ReadingApi.Tag_Titles) {
            @Override
            public Fragment getItem(int position){
               ReadingFragment fragment = new ReadingFragment();
               Bundle bundle = new Bundle();
               bundle.putInt("position",position);
               bundle.putSerializable("category",ReadingApi.Tag_Titles[position]);
               fragment.setArguments(bundle);
               return fragment;
           }
        };
        return mPagerAdapter;
    }

    @Override
    public void onDetach(){
        super.onDetach();
        if (getChildFragmentManager().getFragments()!=null){
            getChildFragmentManager().getFragments().clear();
        }
    }
}
