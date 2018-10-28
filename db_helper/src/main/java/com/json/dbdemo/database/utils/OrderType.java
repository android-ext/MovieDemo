package com.json.dbdemo.database.utils;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.CLASS)
@StringDef({OrderType.DESC, OrderType.ASC})
public @interface OrderType {
   String DESC = "DESC";
   String ASC = "ASC";
}
