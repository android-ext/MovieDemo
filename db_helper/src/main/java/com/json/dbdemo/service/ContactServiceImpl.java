package com.json.dbdemo.service;

import android.content.Context;

import com.json.dbdemo.database.ContactDaoImpl;
import com.json.dbdemo.database.IBaseDao;
import com.json.dbdemo.database.utils.OrderType;
import com.json.dbdemo.entity.ContactEntity;

import java.util.List;

/**
 * Created by Ext on 2016/1/22.
 */
public class ContactServiceImpl implements IBaseService<ContactEntity> {

    private IBaseDao contactDao;

    public ContactServiceImpl(Context context) {

        this.contactDao = ContactDaoImpl.newInstance(context);
    }

    @Override
    public void insertObject(ContactEntity contactEntity) {
        contactDao.insertObject(contactEntity);
    }

    @Override
    public void insertObjects(List<ContactEntity> t) {
        contactDao.insertObjects(t);
    }

    @Override
    public void updateObject(ContactEntity contactEntity) {
        contactDao.updateObject(contactEntity);
    }

    @Override
    public void deleteObject(ContactEntity contactEntity) {
        contactDao.deleteObjects(new int[] {contactEntity.getId()});
    }

    @Override
    public List<ContactEntity> getObjects() {
        return contactDao.getObjects(OrderType.ASC, 0, 20);
    }

    @Override
    public ContactEntity getObject(int key) {
        return (ContactEntity) contactDao.getObjectById(key);
    }
}
