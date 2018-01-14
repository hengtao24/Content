package com.sht.content.ui.support;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.sht.content.R;

/**
 * Created by sht on 2017/2/27.
 */

public abstract class AbsTopNavigationFragment extends Fragment {

    protected View parentView;
    protected ViewPager mViewPager;
    protected PagerAdapter mPagerAdapter;
    private SmartTabLayout mSmartTabLayout;

    protected abstract PagerAdapter initPagerAdapter();

    @Override
    public void onCreate(@Nullable Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle saveInstanceState){
        parentView = View.inflate(getContext(), R.layout.layout_top_navigation,null);
        mViewPager = (ViewPager) parentView.findViewById(R.id.inner_viewpager);
        mSmartTabLayout = (SmartTabLayout) getActivity().findViewById(R.id.tab_layout);
        mSmartTabLayout.setVisibility(View.VISIBLE);
        mPagerAdapter = initPagerAdapter();
        mViewPager.setAdapter(mPagerAdapter);
        mSmartTabLayout.setViewPager(mViewPager);
        return parentView;
    }
}
