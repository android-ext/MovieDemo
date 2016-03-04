package com.zhy.quickdev.adapter.scroll.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;

import com.zhy.quickdev.adapter.R;
import com.zhy.quickdev.adapter.scroll.adapter.ScrollAdapter;
import com.zhy.quickdev.adapter.scroll.entity.ItemBean;

import java.util.ArrayList;

public class SctrollActivity extends ActionBarActivity implements View.OnClickListener, ScrollAdapter.IOnCheckedChangeListener {

    private ArrayList<ItemBean> mDatas;
    private ListView mListView;
    private ScrollAdapter mAdapter;
    private boolean isVisibleCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sctroll);
        initView();
        initData();
    }

    private void initData() {
        initDatas();
        mAdapter = new ScrollAdapter(this, mDatas);
        mAdapter.setmListener(this);
        mListView.setAdapter(mAdapter);
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.listView);
        ((Button)findViewById(R.id.id_select_all)).setOnClickListener(this);

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case SCROLL_STATE_FLING:
                    case SCROLL_STATE_TOUCH_SCROLL:
                        mAdapter.setIsScroll(true);
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        mAdapter.setIsScroll(false);
                        mAdapter.notifyDataSetChanged();
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_select_all:
                updateDatas();
                mAdapter.notifyDataSetChanged();
                break;
        }
    }

    private void updateDatas() {
        isVisibleCheckBox = !isVisibleCheckBox;
        for (ItemBean bean : mDatas) {
            bean.setIsVisible(isVisibleCheckBox);
        }
    }

    private void initDatas()
    {
        mDatas = new ArrayList<ItemBean>();

        ItemBean bean = new ItemBean("Android新技能Get 1",
                "Android打造万能的ListView和GridView适配器", "2014-12-12", "10086");
        mDatas.add(bean);
        bean = new ItemBean("Android新技能Get 2", "Android打造万能的ListView和GridView适配器",
                "2014-12-12", "10086");
        mDatas.add(bean);
        bean = new ItemBean("Android新技能Get 3", "Android打造万能的ListView和GridView适配器",
                "2014-12-12", "10086");
        mDatas.add(bean);
        bean = new ItemBean("Android新技能Get 4", "Android打造万能的ListView和GridView适配器",
                "2014-12-12", "10086");
        mDatas.add(bean);
        bean = new ItemBean("Android新技能Get 5", "Android打造万能的ListView和GridView适配器",
                "2014-12-12", "10086");
        mDatas.add(bean);
        bean = new ItemBean("Android新技能Get 6", "Android打造万能的ListView和GridView适配器",
                "2014-12-12", "10086");
        mDatas.add(bean);
        bean = new ItemBean("Android新技能Get 7", "Android打造万能的ListView和GridView适配器",
                "2014-12-12", "10086");
        mDatas.add(bean);
        bean = new ItemBean("Android新技能Get 8", "Android打造万能的ListView和GridView适配器",
                "2014-12-12", "10086");
        mDatas.add(bean);
        bean = new ItemBean("Android新技能Get 9", "Android打造万能的ListView和GridView适配器",
                "2014-12-12", "10086");
        mDatas.add(bean);

    }

    /**
     * 代理方法
     * @param itemBean
     * @param isChecked
     */
    @Override
    public void onCheckedChanged(ItemBean itemBean, boolean isChecked) {

        for (ItemBean bean : mDatas) {
            if (bean.getTitle().equals(itemBean.getTitle())) {
                bean.setChecked(isChecked);
                break;
            }
        }
        mAdapter.notifyDataSetChanged();
    }

}
