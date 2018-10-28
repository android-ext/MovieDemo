package com.json.dbdemo.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.json.dbdemo.common.Constant;
import com.json.dbdemo.R;
import com.json.dbdemo.adapter.ViewPagerAdapter;
import com.json.dbdemo.common.IViewTool;
import com.json.dbdemo.fragment.DataBaseFragment;
import com.json.dbdemo.view.PagerSlidingTabStrip;

import java.util.ArrayList;


public class NavBarActivity extends FragmentActivity implements IViewTool{

    private PagerSlidingTabStrip mTabStrip = null;
    private ViewPager mViewPager = null;
    private ArrayList<Fragment> fragmentList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_bar);

        initViews();

        initData();
    }

    private void initData() {

        String[] titles = new String[]{"今日推荐", "教案", "视频"};
        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(DataBaseFragment.newInstance(Constant.TYPE_EVERY_RECOMMEND));
        fragmentList.add(DataBaseFragment.newInstance(Constant.TYPE_JIAO_AN));
        fragmentList.add(DataBaseFragment.newInstance(Constant.TYPE_VIDEO));

        mViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), titles, fragmentList));
        mTabStrip.setViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(fragmentList.size() - 1);

    }

    private void initViews() {

        mTabStrip = getViewById(R.id.indicator_view);
        mViewPager = getViewById(R.id.indicator_viewpager);

    }

    public  <T extends View> T getViewById(int resId) {
        return (T) findViewById(resId);
    }

}
