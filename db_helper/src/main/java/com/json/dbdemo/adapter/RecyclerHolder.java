package com.json.dbdemo.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Ext on 2016/1/25.
 */
public class RecyclerHolder extends RecyclerView.ViewHolder {

    private final SparseArray<View> mViews;

    private View mItemView = null;

    public RecyclerHolder(View itemView) {
        super(itemView);

        this.mItemView = itemView;
        this.mViews = new SparseArray<View>();
    }

    public View getmItemView() {
        return mItemView;
    }


    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mItemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 设置文本数据
     * @param resId
     * @param text
     * @return
     */
    public RecyclerHolder setText(int resId, String text) {

        TextView view = getView(resId);
        if (!TextUtils.isEmpty(text)) {
            view.setText(text);
        }
        return this;
    }
}
