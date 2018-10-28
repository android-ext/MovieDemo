package com.json.dbdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * RecyclerView万能适配器
 * Created by Ext on 2016/1/25.
 */
public abstract class RecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerHolder> {


    protected Context mContext = null;
    protected int mLayout = 0;
    protected List<T> mDataList = null;

    public RecyclerAdapter(Context mContext, int mLayout, List<T> mDataList) {
        this.mContext = mContext;
        this.mDataList = mDataList;
        this.mLayout = mLayout;
    }

    public RecyclerAdapter(Context mContext, int mLayout) {
        this(mContext, mLayout, null);
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(mLayout, parent, false);
        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerHolder holder, final int position) {

        /** 事件绑定外传 */
        holder.getmItemView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    int layoutPosition = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(v, layoutPosition);
                }
            }
        });

        onBindView(holder, position);
    }

    public abstract void onBindView(RecyclerHolder holder, int position);

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    /**
     * 指定位置添加数据
     * @param pos
     * @param t
     */
    public void addData(int pos, T t) {
        mDataList.add(pos, t);
        notifyItemInserted(pos);
    }

    /**
     * 重新设置数据源
     * @param dataList
     */
    public void setmDataList(List<T> dataList) {
        mDataList = dataList;
        notifyDataSetChanged();
    }

    /**
     * 尾部追加数据集合
     * @param dataList
     */
    public void addData(List<T> dataList) {
        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    /**
     * 添加一条数据记录
     * @param t
     */
    public void addData(T t) {
        mDataList.add(t);
        notifyDataSetChanged();
    }

    /**
     * 移除指定位置的数据
     * @param pos
     */
    public void removeData(int pos) {
        mDataList.remove(pos);
        notifyItemRemoved(pos);
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    private OnItemClickListener mOnItemClickListener;
}
