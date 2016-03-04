package com.zxxk.ext.circleimagedemo.entity;

import java.io.Serializable;

/**
 * Created by Ext on 2015/10/9.
 */
public class ItemBean implements Serializable {

    private int id;
    private String title;

    public ItemBean(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public ItemBean() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
