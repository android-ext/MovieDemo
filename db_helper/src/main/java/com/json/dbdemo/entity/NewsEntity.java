package com.json.dbdemo.entity;

/**
 * Created by json on 2016/1/17.
 */
public class NewsEntity extends BaseEntity {

    private int id;
    private String title;
    private String publishTime;

    public NewsEntity(int id, String title, String publishTime) {
        this.id = id;
        this.title = title;
        this.publishTime = publishTime;
    }

    public NewsEntity() {
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

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }
}
