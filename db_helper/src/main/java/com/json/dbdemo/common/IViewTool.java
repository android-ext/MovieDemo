package com.json.dbdemo.common;

import android.view.View;

/**
 * Created by json on 2016/1/17.
 */
public interface IViewTool {

    <T extends View> T getViewById(int resId);
}
