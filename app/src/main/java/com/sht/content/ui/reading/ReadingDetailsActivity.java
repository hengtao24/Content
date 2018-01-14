package com.sht.content.ui.reading;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.sht.content.R;
import com.sht.content.api.ReadingApi;
import com.sht.content.model.reading.BookBean;
import com.sht.content.support.Utils;
import com.sht.content.support.adapter.PagerAdapter;
import com.sht.content.ui.support.WebViewUrlActivity;


/**
 * Created by sht on 2017/3/9.
 */

public class ReadingDetailsActivity extends AppCompatActivity{

    public static BookBean mBookBean;
    private ViewPager mViewPager;
    private PagerAdapter mAdapter;
    private Toolbar mToolbar;
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_reading_details);
        initView();
    }

    private void initView(){
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mTabLayout = (TabLayout) findViewById(R.id.slide_tabs);
        setSupportActionBar(mToolbar);
        for (String title: ReadingApi.bookTab_Titles){
            mTabLayout.addTab(mTabLayout.newTab().setText(title));
        }
        mBookBean = (BookBean) getIntent().getSerializableExtra("book");
        getSupportActionBar().setTitle(mBookBean.getTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mAdapter = new PagerAdapter(getSupportFragmentManager(),ReadingApi.bookTab_Titles){
            @Override
            public Fragment getItem(int position){
                ReadingTabFragment fragment = new ReadingTabFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("position",position);
                fragment.setArguments(bundle);
                return fragment;
            }
        };
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_ebook,menu);
        if (Utils.hasString(mBookBean.getEbook_url()) == false )
            menu.getItem(0).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_ebook:
                Intent intent = new Intent(ReadingDetailsActivity.this, WebViewUrlActivity.class);
                intent.putExtra("url",ReadingApi.readEBook);
                startActivity(intent);
                break;
        }
        return true;
    }

}
