package com.zxxk.ext.circleimagedemo.adapter;

import android.content.Context;

import com.zxxk.ext.circleimagedemo.R;
import com.zxxk.ext.circleimagedemo.entity.ItemBean;

import java.util.ArrayList;

/**
 * Created by Ext on 2015/9/30.
 */
public class ItemAdapter extends CommonAdapter<ItemBean> {

    private boolean isScroll;
    private IOnItemClickListener mListener;

    public ItemAdapter(Context context, ArrayList<ItemBean> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    public ItemAdapter(Context context, int layoutId) {
        this(context, null, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, final ItemBean bean) {

//        if (isScroll) return;

        holder.setText(R.id.item_title, bean.getTitle());
    }

    public void setIsScroll(boolean isScroll) {
        this.isScroll = isScroll;
    }

    public void setmListener(IOnItemClickListener mListener) {
        this.mListener = mListener;
    }

    public interface IOnItemClickListener {
        public void onItemClick(ItemBean bean);
    }
}
