package com.zxxk.ext.bannercarousel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zxxk.ext.bannercarousel.utils.CommonUtil;
import com.zxxk.ext.bannercarousel.view.RollViewPager;

import java.util.ArrayList;

public class RollViewActivity extends AppCompatActivity {

    /**
     * 整个轮播图布局
     */
    private LinearLayout mRollViewLayout;
    /**
     * ViewPager外部容器
     */
    private LinearLayout mViewpagerLayout;
    /**
     * 标题
     */
    private TextView mTitleTv;
    private RollViewPager mViewPager;
    /**
     * 圆点容器
     */
    private LinearLayout dotLl;
    /**
     * 圆点View
     */
    private ArrayList<View> dotList;
    private ArrayList<String> titleList, urlList;
    /**
     * 轮播视图View
     */
    private View mRollRootView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roll_view);


        initViews();
    }

    private void initViews() {

        mRollRootView = LayoutInflater.from(this).inflate(R.layout.layout_roll_view, null);
        mRollViewLayout = (LinearLayout) findViewById(R.id.rollview_layout);

        mViewpagerLayout = (LinearLayout) mRollRootView.findViewById(R.id.top_news_viewpager);
        dotLl = (LinearLayout) mRollRootView.findViewById(R.id.dots_ll);
        mTitleTv = (TextView) mRollRootView.findViewById(R.id.top_news_title);

        // 室使用本地图片
        final int[] resArray = new int[]{R.mipmap.poster1, R.mipmap.poster2, R.mipmap.poster3};
        // 初始化标题和网络图片URL
        titleList = new ArrayList<String>();
        urlList = new ArrayList<String>();
        for (int i = 0; i < 5; i++) {
            titleList.add("title--" + i);
            urlList.add("---" + i);
        }

        initDot(3);

        mViewPager = new RollViewPager(this, dotList,
                R.mipmap.dot_focus, R.mipmap.dot_normal,
                new RollViewPager.OnPagerClickCallback() {
                    @Override
                    public void onPagerClick(int position) {

                    }
                });
        mViewPager.setResImageIds(resArray);
        mViewPager.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
//        mViewPager.setUriList(urlList);
        mViewPager.setTitle(mTitleTv, titleList);
        mViewPager.startRoll();
        mViewpagerLayout.removeAllViews();
        mViewpagerLayout.addView(mViewPager);

        mRollViewLayout.addView(mRollRootView);

    }

    /**
     * 初始化小圆点
     *
     * @param size
     */
    private void initDot(int size) {
        dotList = new ArrayList<View>();
        dotLl.removeAllViews();
        for (int i = 0; i < size; i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    CommonUtil.dip2px(this, 6), CommonUtil.dip2px(this, 6));
            params.setMargins(5, 0, 5, 0);
            View m = new View(this);
            if (i == 0) {
                m.setBackgroundResource(R.mipmap.dot_focus);
            } else {
                m.setBackgroundResource(R.mipmap.dot_normal);
            }
            m.setLayoutParams(params);
            dotLl.addView(m);
            dotList.add(m);
        }
    }
}
