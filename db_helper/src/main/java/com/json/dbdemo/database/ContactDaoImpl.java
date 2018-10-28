package com.json.dbdemo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.text.TextUtils;

import com.json.dbdemo.database.utils.CLog;
import com.json.dbdemo.database.utils.CollectionUtil;
import com.json.dbdemo.database.utils.IOUtil;
import com.json.dbdemo.database.utils.OrderType;
import com.json.dbdemo.entity.ContactEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by Ext on 2016/1/22.
 */
public class ContactDaoImpl implements IBaseDao<ContactEntity> {

    private DBHelper mDBHelper;
    public static final String TABLE_NAME = "tb_contact";
    public static final String COLUMN_ID = "c_id";
    public static final String COLUMN_PHONE = "c_phone";
    public static final String COLUMN_NAME = "c_name";
    public static final String COLUMN_ADDRESS = "c_address";
    public static final int INDEX_ID = 0;
    public static final int INDEX_PHONE = 1;
    public static final int INDEX_NAME = 2;
    public static final int INDEX_ADDRESS = 3;

    public static final String CREATE_TABLE_SQL;
    public static final String INSERT_CONTACT_SQL;

    static {
        StringBuilder buffer = new StringBuilder();
        buffer.append(" create table if not exists ")
           .append(TABLE_NAME).append(" ( ")
           .append(COLUMN_ID).append(" integer primary key, ")
           .append(COLUMN_PHONE).append(" varchar, ")
           .append(COLUMN_NAME).append(" varchar, ")
           .append(COLUMN_ADDRESS).append(" varchar ")
           .append(" ) ");
        CREATE_TABLE_SQL = buffer.toString();

        buffer.delete(0, buffer.length());
        buffer.append(" insert into ")
           .append(TABLE_NAME)
           .append("(")
           .append(COLUMN_ID).append(",")
           .append(COLUMN_PHONE).append(",")
           .append(COLUMN_NAME).append(",")
           .append(COLUMN_ADDRESS)
           .append(")")
           .append(" values(?,?,?,?)");
        INSERT_CONTACT_SQL = buffer.toString();

    }

    private ContactDaoImpl(Context context) {

        mDBHelper = DBHelper.newInstance(context);
    }

    public static ContactDaoImpl newInstance(Context context) {
        return new ContactDaoImpl(context);
    }

    @Override
    public void insertObject(ContactEntity contactEntity) {

        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        try {
            db.beginTransaction();
            insert(db, contactEntity);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (db != null) {
                    db.endTransaction();
                    db.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        IOUtil.safelyCloseDb(db);
    }

    @Override
    public synchronized boolean insertObjects(List<ContactEntity> contactEntities) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        if (db == null || CollectionUtil.isCollectionEmpty(contactEntities)) {
            CLog.i("params is invalid when invoked insertObjects()");
            return false;
        }
        SQLiteStatement stat = null;
        try {
            // 预编译Sql语句避免重复解析Sql语句
            stat = db.compileStatement(INSERT_CONTACT_SQL);
            db.beginTransaction();
            for (ContactEntity entity : contactEntities) {
                stat.bindLong(1, entity.getId());
                stat.bindString(2, entity.getPhone());
                stat.bindString(3, entity.getName());
                stat.bindString(4, entity.getAddress());
                long result = stat.executeInsert();
                if (result < 0) {
                    CLog.i("executeInsert() failed when invoked insertObjects()");
                    return false;
                }
            }
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (stat != null) {
                    stat.close();
                }
                if (db == null) {
                    db.endTransaction();
                    db.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * 插入一条记录
     * @param db
     * @param entity
     */
    private synchronized void insert(SQLiteDatabase db, ContactEntity entity) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, entity.getId());
        values.put(COLUMN_NAME, entity.getName());
        values.put(COLUMN_PHONE, entity.getPhone());
        values.put(COLUMN_ADDRESS, entity.getAddress());
        // 生成的sql是 INSERT INTRO OR REPLACE INTO(如果存在就替换存在的字段值. 存在的判断标准是主键冲突, 这里的主键是c_id)
        db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public synchronized void deleteObjects(int[] ids) {
        mDBHelper.deleteObjects(COLUMN_ID, TABLE_NAME, ids);
    }

    @Override
    public synchronized void updateObject(ContactEntity contactEntity) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        update(contactEntity, db);
        IOUtil.safelyCloseDb(db);
    }

    /**
     * 更新记录
     * @param contactEntity
     * @param db
     */
    private int update(ContactEntity contactEntity, SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, contactEntity.getName());
        values.put(COLUMN_PHONE, contactEntity.getPhone());
        values.put(COLUMN_ADDRESS, contactEntity.getAddress());
        // 解决了执行纯SQL语句引入的SQL注入漏洞
        int affectedCount = db.updateWithOnConflict(TABLE_NAME, values, COLUMN_ID + "=" + contactEntity.getId(),
           null, SQLiteDatabase.CONFLICT_REPLACE);
        return affectedCount;
    }

    @Override
    public void updateObjects(List<ContactEntity> contactEntities) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        try {
            db.beginTransaction();
            for (ContactEntity entity : contactEntities) {
                update(entity, db);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (db != null) {
                    db.endTransaction();
                    db.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public ContactEntity getObjectById(int key) {

        SQLiteDatabase db = mDBHelper.getWritableDatabase();

        Cursor cursor = db.query(TABLE_NAME, null,
           COLUMN_ID + "=" + key, null, null, null, null);
        ContactEntity entity = null;
        if (cursor.moveToNext()) {
            entity = fillContactByCursor(cursor);
        }
        IOUtil.safelyCloseCursor(cursor);
        IOUtil.safelyCloseDb(db);
        return entity;
    }

    @Override
    public List<ContactEntity> getObjects(@OrderType String order, int offset, int limit) {

        SQLiteDatabase db = mDBHelper.getWritableDatabase();

        String orderBy = "";
        if (!TextUtils.isEmpty(order)) {
            orderBy += COLUMN_ID +  order + " LIMIT " + limit + " OFFSET " + offset;
        }
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, orderBy);
        List<ContactEntity> contactEntities = new ArrayList<ContactEntity>();
        if (cursor == null) {
            return Collections.emptyList();
        }
        while(cursor.moveToNext()) {
            contactEntities.add(fillContactByCursor(cursor));
        }
        IOUtil.safelyCloseCursor(cursor);
        IOUtil.safelyCloseDb(db);
        return contactEntities;
    }

    @Override
    public boolean isObjectExist(ContactEntity contactEntity) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();

        String sql = " select count(*) as count from " + TABLE_NAME + " where " + COLUMN_ID + " = ? ";
        String[] selectionArgs = new String[]{String.valueOf(contactEntity.getId())};
        int count;
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        count = 0;
        if (cursor != null && cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        IOUtil.safelyCloseCursor(cursor);
        IOUtil.safelyCloseDb(db);
        return count == 1;
    }

    private ContactEntity fillContactByCursor(Cursor cursor) {
        if (cursor == null) {
            return new ContactEntity();
        }
        ContactEntity entity = new ContactEntity();
        entity.setId(DBHelper.getIntValueInCursor(cursor, COLUMN_ID));
        entity.setName(DBHelper.getStringValueInCursor(cursor, COLUMN_NAME));
        entity.setPhone(DBHelper.getStringValueInCursor(cursor, COLUMN_PHONE));
        entity.setAddress(DBHelper.getStringValueInCursor(cursor, COLUMN_ADDRESS));
        return entity;
    }
}
