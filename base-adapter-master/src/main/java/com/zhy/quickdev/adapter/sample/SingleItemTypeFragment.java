package com.zhy.quickdev.adapter.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.zhy.quickdev.adapter.R;
import com.zhy.quickdev.adapter.sample.adapter.SingleItemAdapter;
import com.zhy.quickdev.adapter.scroll.adapter.ScrollAdapter;
import com.zhy.quickdev.adapter.scroll.entity.ItemBean;

import java.util.ArrayList;

/**
 * Created by zhy on 15/9/4.
 */
public class SingleItemTypeFragment extends Fragment implements View.OnClickListener, SingleItemAdapter.IOnCheckedChangeListener
{
    private ArrayList<ItemBean> mDatas;
    private ListView mListView;
    private SingleItemAdapter mAdapter;
    private boolean isVisibleCheckBox;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initDatas();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_single_main, null);
        initView(view);
       return  view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        mAdapter = new SingleItemAdapter(getActivity(), mDatas,R.layout.item_single_listview);
        mAdapter.setmListener(SingleItemTypeFragment.this);
        mListView.setAdapter(mAdapter);

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

    private void initView(View view) {

        mListView = (ListView) view.findViewById(R.id.listView);
        ((Button)view.findViewById(R.id.id_select_all)).setOnClickListener(this);
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
