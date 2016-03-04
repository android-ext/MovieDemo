package com.zhy.quickdev.adapter.scroll.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhy.quickdev.adapter.R;
import com.zhy.quickdev.adapter.scroll.entity.ItemBean;

import java.util.ArrayList;

/**
 * Created by Ext on 2015/9/30.
 */
public class ScrollAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<ItemBean> mDatas;
    private IOnCheckedChangeListener mListener;
    private boolean isScroll;

    public ScrollAdapter(Context mContext) {
        this(mContext, null);
    }

    public ScrollAdapter(Context mContext, ArrayList<ItemBean> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_single_listview, null);
            holder = new ViewHolder(convertView);
            holder.state_cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean checked = ((CheckBox) v).isChecked();
                    ItemBean bean = (ItemBean) v.getTag();
                    mListener.onCheckedChanged(bean, checked);
                }
            });
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (!isScroll) {
            ItemBean bean = (ItemBean) getItem(position);
            holder.setData(bean);
        }
        return convertView;
    }

    static class ViewHolder {

        TextView title_tv;
        TextView desc_tv;
        TextView time_tv;
        TextView phone_tv;
        CheckBox state_cb;
        RelativeLayout id_content_layout;

        public ViewHolder (View view) {
            title_tv = (TextView) view.findViewById(R.id.id_title);
            desc_tv = (TextView) view.findViewById(R.id.id_desc);
            time_tv = (TextView) view.findViewById(R.id.id_time);
            phone_tv = (TextView) view.findViewById(R.id.id_phone);
            state_cb = (CheckBox) view.findViewById(R.id.id_state);
            id_content_layout = (RelativeLayout) view.findViewById(R.id.id_content_layout);
        }

        public void setData(ItemBean bean) {
            title_tv.setText(bean.getTitle());
            desc_tv.setText(bean.getDesc());
            time_tv.setText(bean.getTime());
            phone_tv.setText(bean.getPhone());

            RelativeLayout.LayoutParams viewParams = (RelativeLayout.LayoutParams)state_cb.getLayoutParams();
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)id_content_layout.getLayoutParams();
            if (bean.isVisible()) {
                if ( viewParams.width <= 0) {
                    viewParams.width += 60;
                    params.width -=  60;
                }
            } else {
                if ( viewParams.width >= 60) {
                    viewParams.width -= 60;
                    params.width +=  60;
                }
            }
            state_cb.setTag(bean);
            state_cb.setLayoutParams(viewParams);
            state_cb.setChecked(bean.isChecked());

            id_content_layout.setLayoutParams(params);
        }

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
