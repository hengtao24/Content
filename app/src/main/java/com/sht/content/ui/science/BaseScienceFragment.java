package com.sht.content.ui.science;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.sht.content.api.ScienceApi;
import com.sht.content.support.adapter.PagerAdapter;
import com.sht.content.ui.support.AbsTopNavigationFragment;

/**
 * Created by sht on 2017/2/15.
 */

public class BaseScienceFragment extends AbsTopNavigationFragment {

    private PagerAdapter mPagerAdapter;

    @Override
    protected PagerAdapter initPagerAdapter(){
        mPagerAdapter = new PagerAdapter(getChildFragmentManager(), ScienceApi.channel_title){
            @Override
            public Fragment getItem(int position){
                ScienceFragment fragment = new ScienceFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("position",position);
                bundle.putSerializable("category",ScienceApi.channel_tag[position]);
                fragment.setArguments(bundle);
                return fragment;
            }
        };
         return mPagerAdapter;
    }

    @Override
    public void onDetach(){
        super.onDetach();
        if (getChildFragmentManager().getFragments() != null){
            getChildFragmentManager().getFragments().clear();
        }
    }
}
