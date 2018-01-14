package com.sht.content.ui.reading;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sht.content.R;
import com.sht.content.api.ReadingApi;

/**
 * Created by sht on 2017/3/13.
 */

public class ReadingTabFragment extends Fragment {
    private View parentView;
    private TextView mTextView;
    private int pos;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        parentView = View.inflate(getContext(), R.layout.layout_reading_tab,null);
        initView();
        return parentView;
    }

    private void initView(){
        pos = getArguments().getInt("position");
        mTextView = (TextView) parentView.findViewById(R.id.text);
        mTextView.setText(ReadingApi.getBookInfo(pos,ReadingDetailsActivity.mBookBean));
    }
}
