package com.json.dbdemo.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.json.dbdemo.R;
import com.json.dbdemo.activity.DetailActivity;
import com.json.dbdemo.common.Constant;
import com.json.dbdemo.utils.PreferenceService;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DataBaseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DataBaseFragment extends LazyFragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_TYPE = "type";

    // TODO: Rename and change types of parameters
    private final String TAG = this.getClass().getName();
    private int type;
    private boolean isPrepared = false;
    private TextView mTitleTv = null;
    private TextView mClickBtn = null;
    private PreferenceService mService = null;
    private boolean isFirstEntry = true;
    private boolean refreshFlag = false;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment DataBaseFragment.
     */
    public static DataBaseFragment newInstance(int type) {
        DataBaseFragment fragment = new DataBaseFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    public DataBaseFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getInt(ARG_TYPE);
        }

        mService = PreferenceService.newInstance(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_data_base, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {

        mTitleTv = getViewById(view, R.id.data_base_title_tv);
        mClickBtn = getViewById(view, R.id.data_base_btn);
        mClickBtn.setOnClickListener(this);

        isPrepared = true;
    }


    @Override
    public void onStart() {
        super.onStart();

        refreshFlag = mService.getBooleanValue(Constant.REFRESH_TAG);
        Log.i(TAG, "onStart");

        lazyLoad();
    }

    /**
     * 延迟加载
     * 子类必须重写此方法
     */
    @Override
    protected void lazyLoad() {


        Log.i(TAG, "lazyLoad before");
        if (!isPrepared || !isVisible) return;

        // 首次进来不限制
        if ((!isFirstEntry && !refreshFlag)) return;

        Log.i(TAG, "lazyLoad over");

        mTitleTv.setText("fragment: " + type);
        isFirstEntry = false;
        if (refreshFlag)
            refreshFlag = false;
    }

    public <T extends View> T getViewById(View view, int resId) {
        return (T) view.findViewById(resId);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.data_base_btn:
        Intent intent = new Intent(this.getActivity(), DetailActivity.class);
                startActivity(intent);
                break;
        }
    }
}
