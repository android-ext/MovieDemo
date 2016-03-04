package com.zhy.quickdev.adapter.sample.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.zhy.quickdev.adapter.CommonAdapter;
import com.zhy.quickdev.adapter.R;
import com.zhy.quickdev.adapter.ViewHolder;
import com.zhy.quickdev.adapter.scroll.entity.ItemBean;

import java.util.ArrayList;

/**
 * Created by Ext on 2015/9/30.
 */
public class SingleItemAdapter extends CommonAdapter<ItemBean> {

    private boolean isScroll;
    private IOnCheckedChangeListener mListener;

    public SingleItemAdapter(Context context, ArrayList<ItemBean> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, final ItemBean bean) {

        if (isScroll) return;

        holder.updateLayout(bean, 60, R.id.id_state, R.id.id_content_layout);

        holder.setText(R.id.id_title, bean.getTitle())
                .setText(R.id.id_desc, bean.getDesc())
                .setText(R.id.id_time, bean.getTime())
                .setText(R.id.id_phone, bean.getPhone());
        holder.getView(R.id.id_state).setTag(bean);
        holder.setOnClickListener(R.id.id_state, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = ((CheckBox) view).isChecked();
                ItemBean bean = (ItemBean) view.getTag();
                mListener.onCheckedChanged(bean, checked);
            }
        });
    }

    public void setIsScroll(boolean isScroll) {
        this.isScroll = isScroll;
    }

    public void setmListener(IOnCheckedChangeListener mListener) {
        this.mListener = mListener;
    }

    public interface IOnCheckedChangeListener {
        public void onCheckedChanged(ItemBean bean, boolean isChecked);
    }
}
