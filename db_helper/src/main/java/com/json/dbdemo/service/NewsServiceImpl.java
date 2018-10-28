package com.json.dbdemo.service;

import android.content.Context;
import android.util.Log;

import com.json.dbdemo.database.NewsDaoImpl;
import com.json.dbdemo.database.utils.OrderType;
import com.json.dbdemo.entity.NewsEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by json on 2016/1/16.
 */
public class NewsServiceImpl implements IBaseService<NewsEntity> {

    private NewsDaoImpl newsDao;

    public NewsServiceImpl(Context context) {

        this.newsDao = NewsDaoImpl.newInstance(context);
    }

    @Override
    public void insertObject(NewsEntity entity) {

        newsDao.insertObject(entity);
    }

    @Override
    public void insertObjects(List<NewsEntity> t) {

    }

    @Override
    public void updateObject(NewsEntity entity) {

        newsDao.updateObject(entity);
    }



    @Override
    public void deleteObject(NewsEntity entity) {

        newsDao.deleteObjects(new int[] {entity.getId()});
    }

    @Override
    public List<NewsEntity> getObjects() {
        List<NewsEntity> News = newsDao.getObjects(OrderType.ASC, 0, 20);
        for (NewsEntity entity : News) {
            Log.i("NewsServiceImpl", entity.getId() + " , " + entity.getTitle() + " , " + entity.getPublishTime());
        }
        return News;
    }

    @Override
    public NewsEntity getObject(int key) {
        return newsDao.getObjectById(key);
    }
}
