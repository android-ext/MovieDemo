package com.json.dbdemo.entity;

/**
 * Created by Ext on 2016/1/25.
 */
public class RecyclerEntity extends BaseEntity {

    private int id;
    private String title;
    private String time;
    private String count;

    public RecyclerEntity() {
    }

    public RecyclerEntity(int id, String title, String time, String count) {
        this.id = id;
        this.title = title;
        this.time = time;
        this.count = count;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
