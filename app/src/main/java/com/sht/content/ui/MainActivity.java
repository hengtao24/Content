package com.sht.content.ui;

import android.content.Intent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import com.sht.content.support.Settings;
import com.sht.content.support.Utils;
import com.sht.content.ui.About.AboutActivity;
import com.sht.content.ui.daily.DailyFragment;

import com.sht.content.R;
import com.sht.content.ui.douban.DoubanMomentFragment;
import com.sht.content.ui.news.BaseNewsFragment;
import com.sht.content.ui.reading.BaseReadingFragment;
import com.sht.content.ui.science.BaseScienceFragment;
import com.sht.content.ui.sports.BaseSportsFragment;



/**
 * Created by sht on 2017/1/11.
 */

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener{

    //UI Object
    private RadioGroup bottomTabBar;
    private RadioButton bottomTabButton;
    private Toolbar toolbar;
    private AccountHeader header;
    private Drawer drawer;

    //Fragment Object
    private FragmentManager mFragmentManager = getSupportFragmentManager();
    private Fragment mCurrentFragment;
    private FragmentTransaction mFragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        Utils.DLog("mainactivity");
        bottomTabBar = (RadioGroup) findViewById(R.id.rg_tab_bar);
        bottomTabBar.setOnCheckedChangeListener(this);
        // get the first button,set it selected
        bottomTabButton = (RadioButton) findViewById(R.id.rb_daily);
        bottomTabButton.setChecked(true);
        mCurrentFragment = new DailyFragment();

    }

    private void initData(){
        // sht: find the ToolBar <location:activity_main.xml->include->widget_appbar.xml>
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // sht:Set a Toolbar to act as the ActionBar for this Activity window.
        setSupportActionBar(toolbar);

        // sht:use the <com.mikepenz:materialdrawer:4.4.6@aar> library
        // sht:set the header of the Drawer
        header = new AccountHeaderBuilder().withActivity(this)
                .withCompactStyle(false)
                // sht:set the Background
                .withHeaderBackground(R.drawable.header)
                // sht:set the details of the header
                .addProfiles(new ProfileDrawerItem().withIcon(R.drawable.logo2)
                        .withEmail(getString(R.string.author_email))
                        .withName(getString(R.string.author_name)))
                // sht:add callback for AccountHeader Profiles<AboutActivity->ui->about>
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        Intent i = new Intent(MainActivity.this, AboutActivity.class);
                        startActivity(i);
                        return false;
                    }
                })
                .build();
        // sht:set the drawer of the Drawer
        drawer = new DrawerBuilder().withActivity(this)
                // sht:connect to the toolbar
                .withToolbar(toolbar)
                // sht:use the  ActionBarDrawerToggle animation of toolbar
                .withActionBarDrawerToggleAnimated(true)
                .withAccountHeader(header)
                .withSliderBackgroundColor(Settings.isNightMode ? ContextCompat.getColor(this, R.color.night_primary) : ContextCompat.getColor(this, R.color.white))
                // sht:set Items Primary  and Second
    /*            .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.daily).withIcon(R.mipmap.ic_home).withIdentifier(R.mipmap.ic_home)
                                .withTextColor(Settings.isNightMode ? ContextCompat.getColor(this, R.color.white) : ContextCompat.getColor(this, R.color.text_color)),
                        new PrimaryDrawerItem().withName(R.string.science).withIcon(R.mipmap.ic_science).withIdentifier(R.mipmap.ic_science)
                                .withTextColor(Settings.isNightMode ? ContextCompat.getColor(this, R.color.white) : ContextCompat.getColor(this, R.color.text_color)),
                        new PrimaryDrawerItem().withName(R.string.news).withIcon(R.mipmap.ic_news).withIdentifier(R.mipmap.ic_news)
                                .withTextColor(Settings.isNightMode ? ContextCompat.getColor(this, R.color.white) : ContextCompat.getColor(this, R.color.text_color)),
                        new PrimaryDrawerItem().withName(R.string.reading).withIcon(R.mipmap.ic_reading).withIdentifier(R.mipmap.ic_reading)
                                .withTextColor(Settings.isNightMode ? ContextCompat.getColor(this, R.color.white) : ContextCompat.getColor(this, R.color.text_color)),
                        new PrimaryDrawerItem().withName(R.string.collection).withIcon(R.mipmap.ic_collect_grey).withIdentifier(R.mipmap.ic_collect_grey)
                                .withTextColor(Settings.isNightMode ? ContextCompat.getColor(this, R.color.white) : ContextCompat.getColor(this, R.color.text_color)),
                        new SectionDrawerItem().withName(R.string.app_name).withTextColor(Settings.isNightMode ? ContextCompat.getColor(this, R.color.white) : ContextCompat.getColor(this, R.color.text_color)),
                        new SecondaryDrawerItem().withName(Settings.isNightMode == true ? R.string.text_day_mode: R.string.text_night_mode)
                                .withIcon(Settings.isNightMode == true ? R.mipmap.ic_day_white:R.mipmap.ic_night).withIdentifier(R.mipmap.ic_night)
                                .withTextColor(Settings.isNightMode ? ContextCompat.getColor(this, R.color.white) : ContextCompat.getColor(this, R.color.text_light)),
                        new SecondaryDrawerItem().withName(R.string.setting).withIcon(R.mipmap.ic_setting).withIdentifier(R.mipmap.ic_setting)
                                .withTextColor(Settings.isNightMode ? ContextCompat.getColor(this, R.color.white):ContextCompat.getColor(this,R.color.text_light)),
                        new SecondaryDrawerItem().withName(R.string.about).withIcon(R.mipmap.ic_about).withIdentifier(R.mipmap.ic_about)
                                .withTextColor(Settings.isNightMode ? ContextCompat.getColor(this, R.color.white):ContextCompat.getColor(this,R.color.text_light))
                ).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch (drawerItem.getIdentifier()) {
                            case R.mipmap.ic_home:
                                if (currentFragment instanceof DailyFragment) {
                                    return false;
                                }
                                currentFragment = new DailyFragment();
                                break;
                            case R.mipmap.ic_reading:
                                if (currentFragment instanceof BaseReadingFragment) {
                                    return false;
                                }
                                new BaseNewsFragment();
                                currentFragment = new BaseReadingFragment();
                                break;
                            case R.mipmap.ic_news:
                                if (currentFragment instanceof BaseNewsFragment) {
                                    return false;
                                }
                                currentFragment = new BaseNewsFragment();
                                break;
                            case R.mipmap.ic_science:
                                if (currentFragment instanceof BaseScienceFragment) {
                                    return false;
                                }
                                currentFragment = new BaseScienceFragment();
                                break;
                            case R.mipmap.ic_collect_grey:
                                if (currentFragment instanceof BaseCollectionFragment) {
                                    return false;
                                }
                                currentFragment = new BaseCollectionFragment();
                                break;
                            case R.mipmap.ic_night:
                                Settings.isNightMode = !Settings.isNightMode;
                                mSettings.putBoolean(mSettings.NIGHT_MODE, Settings.isNightMode);
                                MainActivity.this.recreate();
                                return false;
                            case R.mipmap.ic_setting:
                                Intent toSetting = new Intent(MainActivity.this, SettingsActivity.class);
                                toSetting.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(toSetting);
                                return false;
                            case R.mipmap.ic_about:
                                Intent toAbout = new Intent(MainActivity.this, AboutActivity.class);
                                startActivity(toAbout);
                                return false;
                        }
                        switchFragment();
                        return false;
                    }
                })*/
               .build();
    }


    @Override
    public void onCheckedChanged(RadioGroup group,int checkdId) {
        switch (checkdId) {
           case R.id.rb_daily:
                if (mCurrentFragment instanceof DailyFragment) {
                    break;
                }
                mCurrentFragment = new DailyFragment();
                break;
            case R.id.rb_reading:
                if (mCurrentFragment instanceof BaseReadingFragment) {
                    break;
                }
                mCurrentFragment = new BaseReadingFragment();
                break;
            case R.id.rb_news:
                if (mCurrentFragment instanceof DoubanMomentFragment) {
                    break;
                }
                mCurrentFragment = new DoubanMomentFragment();
                break;
            case R.id.rb_science:
                if (mCurrentFragment instanceof BaseScienceFragment) {
                    break;
                }
                mCurrentFragment = new BaseScienceFragment();
                break;
            case R.id.rb_sports:
                if (mCurrentFragment instanceof BaseSportsFragment) {
                    break;
                }
                mCurrentFragment = new BaseSportsFragment();
                break;
        }
        switchFragment();
    }

    public void switchFragment(){
       if (mCurrentFragment instanceof DailyFragment){
            switchFragment(mCurrentFragment,getString(R.string.daily));
        }else if(mCurrentFragment instanceof BaseReadingFragment){
            switchFragment(mCurrentFragment,getString(R.string.reading));
        }else if(mCurrentFragment instanceof DoubanMomentFragment){
            switchFragment(mCurrentFragment,getString(R.string.news));
        }else if(mCurrentFragment instanceof BaseScienceFragment){
            switchFragment(mCurrentFragment,getString(R.string.science));
        }else if(mCurrentFragment instanceof BaseSportsFragment){
            switchFragment(mCurrentFragment,getString(R.string.sports));
        }
    }

    public void switchFragment(Fragment fragment,String title){
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.framelayout,fragment);
        mFragmentTransaction.commit();
        getSupportActionBar().setTitle(title);
    }
}
