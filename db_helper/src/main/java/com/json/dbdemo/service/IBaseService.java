package com.json.dbdemo.service;

import java.util.List;

/**
 * Created by Ext on 2016/1/22.
 */
public interface IBaseService<T> {

    void insertObject(T t);

    void insertObjects(List<T> t);

    void updateObject(T t);

    void deleteObject(T t);

    List<T> getObjects();

    T getObject(int key);
}
