package com.json.dbdemo.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.json.dbdemo.R;
import com.json.dbdemo.activity.common.BaseActivity;
import com.json.dbdemo.adapter.RecyclerAdapter;
import com.json.dbdemo.adapter.RecyclerHolder;
import com.json.dbdemo.entity.RecyclerEntity;

import java.util.ArrayList;

public class RecyclerViewActivity extends BaseActivity {

    /**
     * 列表展示控件
     */
    private RecyclerView mRecycleView = null;
    /**
     * 数据源
     */
    private ArrayList<RecyclerEntity> mDataList = null;
    private RecyclerAdapter<RecyclerEntity> mAdapter = null;


    @Override
    protected int getLayout() {
        return R.layout.activity_recycler_view;
    }

    @Override
    protected void initViews() {

        initRecyclerView();
    }

    private void initRecyclerView() {

        mRecycleView = findView(R.id.recycler_view);
        // 设置mRecyclerView的item向下排列
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new RecyclerAdapter<RecyclerEntity>(this, R.layout.layout_recycler_item) {
            @Override
            public void onBindView(RecyclerHolder holder, int position) {

                RecyclerEntity entity = mDataList.get(position);
                holder.setText(R.id.recycler_item_title, entity.getTitle())
                .setText(R.id.recycler_item_time, entity.getTime())
                .setText(R.id.recycler_item_count, entity.getCount());
            }
        };
        mRecycleView.setAdapter(mAdapter);
    }

    @Override
    protected void setListener() {

        mAdapter.setmOnItemClickListener(new RecyclerAdapter.OnItemClickListener(){

            @Override
            public void onItemClick(View view, int position) {
                showToast(RecyclerViewActivity.this, mDataList.get(position).getTitle());
            }
        });
    }

    @Override
    protected void initData() {


        mDataList = new ArrayList<RecyclerEntity>();
        for (int i = 0; i < 10; i++) {
            mDataList.add(new RecyclerEntity(i , "标题-" + i, "发布时间:" + System.currentTimeMillis(), "浏览数量:" + i * 5));
        }

        mAdapter.setmDataList(mDataList);
    }

    @Override
    protected <T extends View> T findView(int resId) {
        return (T) findViewById(resId);
    }
}
