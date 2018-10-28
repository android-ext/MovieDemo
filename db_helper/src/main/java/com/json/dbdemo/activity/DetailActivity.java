package com.json.dbdemo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.json.dbdemo.R;
import com.json.dbdemo.common.Constant;
import com.json.dbdemo.common.IViewTool;
import com.json.dbdemo.utils.PreferenceService;

public class DetailActivity extends Activity implements IViewTool, View.OnClickListener {

    private TextView mResetDataBtn = null;
    private TextView mChangeDataBtn = null;
    private TextView mReturnBtn = null;
    private PreferenceService mService = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initViews();
    }

    private void initViews() {

        mResetDataBtn = getViewById(R.id.reset_data_btn);
        mChangeDataBtn = getViewById(R.id.change_data_btn);
        mReturnBtn = getViewById(R.id.return_btn);

        mResetDataBtn.setOnClickListener(this);
        mChangeDataBtn.setOnClickListener(this);
        mReturnBtn.setOnClickListener(this);

        mService = PreferenceService.newInstance(this);
    }

    @Override
    public <T extends View> T getViewById(int resId) {

        return (T) findViewById(resId);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.reset_data_btn:
                mService.putBooleanValue(Constant.REFRESH_TAG, false);
                break;
            case R.id.change_data_btn:
                mService.putBooleanValue(Constant.REFRESH_TAG, true);
                break;
            case R.id.return_btn:
                finish();
                break;
        }
    }
}
