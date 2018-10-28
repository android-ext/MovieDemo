package com.json.dbdemo.database;

import com.json.dbdemo.database.utils.OrderType;

import java.util.List;

/**
 * Created by Ext on 2016/1/22.
 */
public interface IBaseDao<T> {

    void insertObject(T t);

    boolean insertObjects(List<T> tList);

    void deleteObjects(int[] ids);

    void updateObject(T t);

    void updateObjects(List<T> tList);

    T getObjectById(int key);

    List<T> getObjects(@OrderType String order, int offset, int limit);

    boolean isObjectExist(T t);

}
