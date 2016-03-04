package com.zxxk.ext.moviedemo;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.zxxk.ext.moviedemo.view.SurfaceViewTemplate;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private SurfaceViewTemplate mTemplateView;
    private ImageView mStartBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTemplateView = (SurfaceViewTemplate) findViewById(R.id.luckypan);
        mStartBtn = (ImageView) findViewById(R.id.id_start_btn);
        mStartBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (!mTemplateView.isStart()) { // 还没有开始旋转
            mTemplateView.luckyStart(1);
            mStartBtn.setImageResource(R.mipmap.stop);
        } else {
            if (!mTemplateView.isShouldEnd()) { // 停止按钮还没有被点击
                mTemplateView.luckyEnd();
                mStartBtn.setImageResource(R.mipmap.start);
            }
        }
    }
}
