package com.zxxk.ext.bannercarousel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zxxk.ext.bannercarousel.view.AutoPlayManager;
import com.zxxk.ext.bannercarousel.view.ImageIndicatorView;
import com.zxxk.ext.bannercarousel.view.ImageIndicatorView.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity implements OnItemClickListener, View.OnClickListener {

    private ImageIndicatorView autoImageIndicatorView;
    private TextView mRollViewTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.autoImageIndicatorView = (ImageIndicatorView) findViewById(R.id.indicate_view);
        this.autoImageIndicatorView.setOnItemChangeListener(new ImageIndicatorView.OnItemChangeListener() {
            @Override
            public void onPosition(int position, int totalCount) {

            }
        });

        initView();
    }

    private void initView() {
        final Integer[] resArray = new Integer[]{R.mipmap.poster1, R.mipmap.poster2, R.mipmap.poster3};
        List<String> titles = new ArrayList<String>();
        for (int i = 0; i < 3; i++) {
            titles.add("高考视频--" + i);
        }
        this.autoImageIndicatorView.setTitleList(titles);
        this.autoImageIndicatorView.setupLayoutByDrawable(resArray);
        this.autoImageIndicatorView.show();
        this.autoImageIndicatorView.setOnItemClickListener(this);
        AutoPlayManager autoBroadcastManager = new AutoPlayManager(this.autoImageIndicatorView);
        autoBroadcastManager.setBroadcastEnable(true);
//        autoBroadcastManager.setBroadCastTimes(5);//循环次数  默认是无限循环
        autoBroadcastManager.setBroadcastTimeIntevel(2 * 1000, 2 * 1000);//首次启动时间及间隔
        autoBroadcastManager.loop();

        mRollViewTv = (TextView) findViewById(R.id.rollview_tv);
        mRollViewTv.setOnClickListener(this);
    }

    @Override
    public void OnItemClick(View view, int position) {
        Toast.makeText(MainActivity.this, "" + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.rollview_tv:
                Intent intent = new Intent(this, RollViewActivity.class);
                startActivity(intent);
                break;
        }
    }

//    private void initView() {
//        List<String> urlList= new ArrayList<String>();
//        urlList.add("https://github.com/winfirm/android-image-indicator/blob/master/AndroidImageIndicatorSample/screenshot/guider_00.jpg");
//        urlList.add("https://github.com/winfirm/android-image-indicator/blob/master/AndroidImageIndicatorSample/screenshot/guider_01.jpg");
//
//        this.imageIndicatorView.setupLayoutByImageUrl(urlList);
//        this.imageIndicatorView.show();
//    }
}
