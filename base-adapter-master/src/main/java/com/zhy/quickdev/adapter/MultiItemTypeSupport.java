package com.zhy.quickdev.adapter;

/**
 * 多个条目类型支持接口
 * @param <T>
 */
public interface MultiItemTypeSupport<T>
{
	int getLayoutId(int position, T t);

	int getViewTypeCount();

	int getItemViewType(int postion, T t);
}