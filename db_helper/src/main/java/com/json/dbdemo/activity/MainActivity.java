package com.json.dbdemo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.json.dbdemo.R;
import com.json.dbdemo.entity.ContactEntity;
import com.json.dbdemo.entity.NewsEntity;
import com.json.dbdemo.service.ContactServiceImpl;
import com.json.dbdemo.service.IBaseService;
import com.json.dbdemo.service.NewsServiceImpl;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MainActivity extends Activity implements View.OnClickListener {

    private TextView titleTv;
    private TextView mRecyclerTv = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newsDBTest();

        initView();

        contactDaoTest();

    }

    /**
     * 数据库操作在子线程中操作
     */
    private void contactDaoTest() {

        ExecutorService singleThreadService = Executors.newSingleThreadExecutor();
        singleThreadService.execute(new Runnable() {
            @Override
            public void run() {
                ContactServiceImpl service = new ContactServiceImpl(MainActivity.this);
                List<ContactEntity> list = new ArrayList<>();
                ContactEntity entity = new ContactEntity(1, "中关村在线", "13549490909", "湖北");
                list.add(entity);
                ContactEntity entity2 = new ContactEntity(2, "国美在线", "13549490904", "江苏");
                list.add(entity2);
                service.insertObjects(list);

                List<ContactEntity> contactEntities =  service.getObjects();

                mHandler.sendMessage(mHandler.obtainMessage(MSG_UPDATE_INTERFACE, contactEntities));
            }
        });
    }

    private void newsDBTest() {

        IBaseService service = new NewsServiceImpl(this);
        NewsEntity entity = new NewsEntity(1, "中关村在线", "2016-01-08");
        service.insertObject(entity);
        NewsEntity entity2 = new NewsEntity(2, "国美在线", "2016-01-16");
        service.insertObject(entity2);

        service.getObjects();
    }

    private void initView() {

        titleTv = (TextView) findViewById(R.id.main_click_tv);
        mRecyclerTv = (TextView) findViewById(R.id.main_recycler_tv);

        titleTv.setOnClickListener(this);
        mRecyclerTv.setOnClickListener(this);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.main_click_tv:

                Intent intent = new Intent(this, NavBarActivity.class);
                startActivity(intent);
                break;
            case R.id.main_recycler_tv:

                Intent recyclerIntent = new Intent(this, RecyclerViewActivity.class);
                startActivity(recyclerIntent);
                break;
        }
    }


    private ThreadHandler mHandler = new ThreadHandler(this);

    public static final int MSG_UPDATE_INTERFACE = 1;

    static class ThreadHandler extends Handler {

        WeakReference<Activity > mActivityReference;

        ThreadHandler(Activity activity) {
            mActivityReference= new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final Activity activity = mActivityReference.get();
            if (activity != null) {
//                mImageView.setImageBitmap(mBitmap);
                switch (msg.what) {
                    case MSG_UPDATE_INTERFACE:

                        ArrayList<ContactEntity> contactEntities = (ArrayList<ContactEntity>)msg.obj;
                        for (ContactEntity obj :contactEntities) {
                            Log.i("@@@", obj.toString());
                        }
                        break;
                    default:
                        break;
                }
            }
        }

    }
}
