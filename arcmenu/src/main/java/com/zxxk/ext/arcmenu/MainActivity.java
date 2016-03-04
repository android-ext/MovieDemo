package com.zxxk.ext.arcmenu;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.zxxk.ext.arcmenu.view.ArcMenu;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private ListView mListView;
    private List<String> mDatas;
    private ArcMenu mArcMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initView();
        mListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mDatas));
        initEvent();
    }

    private void initEvent() {
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (mArcMenu.isOpen()) {
                    mArcMenu.toggleMenu(600);
                }
            }
        });

        mArcMenu.setmMenuItemClickListener(new ArcMenu.OnMenuItemClickListener() {
            @Override
            public void onClick(View view, int pos) {
                Toast.makeText(MainActivity.this, pos + ":" + view.getTag(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.id_listview);
        mArcMenu = (ArcMenu) findViewById(R.id.id_menu);
    }

    private void initData() {

        mDatas = new ArrayList<String>();

        for (int i = 'A'; i <= 'Z'; i ++) {
            mDatas.add((char)i + "");
        }
    }

}
