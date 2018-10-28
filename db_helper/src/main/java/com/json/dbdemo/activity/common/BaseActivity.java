package com.json.dbdemo.activity.common;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

/**
 * activity基类
 */
public abstract class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayout());

        initViews();

        setListener();

        initData();
    }

    /**
     * 返回视图布局文件资源ID
     * @return
     */
    protected abstract int getLayout();

    /**
     * 控件的绑定
     */
    protected abstract void initViews();

    /**
     * 事件绑定
     */
    protected abstract void setListener();

    /**
     * 数据初始化
     */
    protected abstract void initData();

    protected abstract  <T extends View> T findView(int resId);

    /**
     * 吐司提示
     * @param context
     * @param text
     */
    protected void showToast(final Activity context, String text) {

        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }
}
