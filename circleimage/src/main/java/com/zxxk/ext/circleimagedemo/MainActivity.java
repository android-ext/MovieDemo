package com.zxxk.ext.circleimagedemo;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.zxxk.ext.circleimagedemo.adapter.ItemAdapter;
import com.zxxk.ext.circleimagedemo.entity.ItemBean;
import com.zxxk.ext.circleimagedemo.utils.DisplayUtil;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements View.OnClickListener, AdapterView.OnItemClickListener, PopupWindow.OnDismissListener {

    private TextView mNavTitleTv;
    private PopupWindow mPopupWindow;
    private View mPopView;
    private GridView mPopGridView;
    private ItemAdapter mAdapter;
    private ArrayList<ItemBean> mDatas;
    private View mRootView;
    private boolean isMenuShowing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
        setContentView(mRootView);

        initView();
    }

    private void initView() {

        mNavTitleTv = (TextView) findViewById(R.id.nav_title_tv);
        mNavTitleTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_title_tv:

                if (!isMenuShowing) {
                    isMenuShowing = true;
                    showMenu();
                }  else {
                    isMenuShowing = false;
                }
                mNavTitleTv.setSelected(isMenuShowing);

                break;
        }
    }

    private void showMenu() {
        int[] location = new int[2];
        mNavTitleTv.getLocationInWindow(location);
        int height = getNavLayoutHeight();
        getmPopupWindow().showAtLocation(mRootView, Gravity.CENTER_HORIZONTAL + Gravity.TOP, 0, location[1] + height + 10);
    }

    private int getNavLayoutHeight() {
        int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        mNavTitleTv.measure(w, h);
        return DisplayUtil.dip2px(this, mNavTitleTv.getMeasuredHeight());
    }

    public PopupWindow getmPopupWindow() {
        if (mPopupWindow == null) {
            mPopupWindow = new PopupWindow(getmPopView(), ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            mPopupWindow.setFocusable(true);//  如果设置为false就会出现点击titleNav后会出现先关闭再打开的现象，onDismiss()比其他方法都先执行
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            // 点击弹出框之外的区域能够获得焦点
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setOnDismissListener(this);
        }
        return mPopupWindow;
    }

    public View getmPopView() {
        if (mPopView == null) {
            mPopView = getLayoutInflater().inflate(R.layout.nav_pop_view, null);
            mPopGridView = (GridView) mPopView.findViewById(R.id.nav_pop_gridview);
            mAdapter = new ItemAdapter(this, R.layout.item_view);
            mPopGridView.setAdapter(mAdapter);
            mPopGridView.setOnItemClickListener(this);
            initData();
            mAdapter.setmDatas(mDatas);
        }
        return mPopView;
    }

    private void initData() {
        mDatas = new ArrayList<ItemBean>();
        for (int i = 0; i < 10; i++) {
            ItemBean itemBean = new ItemBean(i, "课程-"+i);
            mDatas.add(itemBean);
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, "点击了" + mDatas.get(position).getTitle(), Toast.LENGTH_LONG).show();
        mPopupWindow.dismiss();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        if (null != getmPopupWindow() && getmPopupWindow().isShowing()) {
            getmPopupWindow().dismiss();
            isMenuShowing = false;
            mNavTitleTv.setSelected(isMenuShowing);
            return true;
        }

        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onDismiss() {
        isMenuShowing = false;
        mNavTitleTv.setSelected(isMenuShowing);
    }
}
