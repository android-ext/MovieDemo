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
import com.json.dbdemo.entity.NewsEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Created by json on 2016/1/16.
 */
public class NewsDaoImpl implements IBaseDao<NewsEntity>{

    private DBHelper mDBHelper;
    public static final String TABLE_NAME = "tb_news";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_PUBLISH_TIME = "publish_time";
    public static final String NEWS_CREATE_TABLE;
    private static final String INSERT_NEWS_SQL;

    static {
        StringBuilder buffer = new StringBuilder();
        buffer.append(" create table if not exists ")
           .append(TABLE_NAME).append(" ( ")
           .append(COLUMN_ID).append(" integer primary key, ")
           .append(COLUMN_TITLE).append(" varchar, ")
           .append(COLUMN_PUBLISH_TIME).append(" varchar)");
        NEWS_CREATE_TABLE = buffer.toString();

        buffer.delete(0, buffer.length());
        buffer.append(" insert into ")
           .append(TABLE_NAME)
           .append("(")
           .append(COLUMN_ID).append(",")
           .append(COLUMN_TITLE).append(",")
           .append(COLUMN_PUBLISH_TIME)
           .append(")")
           .append(" values(?,?,?)");
        INSERT_NEWS_SQL = buffer.toString();
    }

    private NewsDaoImpl(Context context) {
        mDBHelper = DBHelper.newInstance(context);
    }
    
    public static NewsDaoImpl newInstance(Context context) {
        return new NewsDaoImpl(context);
    }

    @Override
    public synchronized void insertObject(NewsEntity newsEntity) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        try {
            db.beginTransaction();
            insert(db, newsEntity);
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

    /**
     * 插入一条记录
     * @param db
     * @param entity
     */
    private synchronized void insert(SQLiteDatabase db, NewsEntity entity) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, entity.getId());
        values.put(COLUMN_TITLE, entity.getTitle());
        values.put(COLUMN_PUBLISH_TIME, entity.getPublishTime());
        // 生成的sql是 INSERT INTRO OR REPLACE INTO(如果存在就替换存在的字段值. 存在的判断标准是主键冲突, 这里的主键是c_id)
        db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }
    @Override
    public synchronized boolean insertObjects(List<NewsEntity> newsEntities) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        if (db == null || CollectionUtil.isCollectionEmpty(newsEntities)) {
            CLog.i("params is invalid when invoked insertObjects()");
            return false;
        }
        SQLiteStatement stat = null;
        try {
            // 预编译Sql语句避免重复解析Sql语句
            stat = db.compileStatement(INSERT_NEWS_SQL);
            db.beginTransaction();
            for (NewsEntity entity : newsEntities) {
                stat.bindLong(1, entity.getId());
                stat.bindString(2, entity.getTitle());
                stat.bindString(3, entity.getPublishTime());
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

    @Override
    public void deleteObjects(int[] ids) {
        mDBHelper.deleteObjects(COLUMN_ID, TABLE_NAME, ids);
    }

    @Override
    public void updateObject(NewsEntity newsEntity) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        update(newsEntity, db);
        IOUtil.safelyCloseDb(db);
    }

    /**
     * 更新记录
     * @param newsEntity
     * @param db
     */
    private int update(NewsEntity newsEntity, SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, newsEntity.getTitle());
        values.put(COLUMN_PUBLISH_TIME, newsEntity.getPublishTime());
        // 解决了执行纯SQL语句引入的SQL注入漏洞
        int affectedCount = db.updateWithOnConflict(TABLE_NAME, values, COLUMN_ID + "=" + newsEntity.getId(),
           null, SQLiteDatabase.CONFLICT_REPLACE);
        return affectedCount;
    }

    @Override
    public void updateObjects(List<NewsEntity> newsEntities) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        try {
            db.beginTransaction();
            for (NewsEntity entity : newsEntities) {
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
    public NewsEntity getObjectById(int key) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();

        Cursor cursor = db.query(TABLE_NAME, null,
           COLUMN_ID + "=" + key, null, null, null, null);
        NewsEntity entity = null;
        if (cursor.moveToNext()) {
            entity = fillContactByCursor(cursor);
        }
        IOUtil.safelyCloseCursor(cursor);
        IOUtil.safelyCloseDb(db);
        return entity;
    }

    @Override
    public List<NewsEntity> getObjects(String order, int offset, int limit) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();

        String orderBy = "";
        if (!TextUtils.isEmpty(order)) {
            orderBy += COLUMN_ID +  order + " LIMIT " + limit + " OFFSET " + offset;
        }
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, orderBy);
        List<NewsEntity> newsEntities = new ArrayList<NewsEntity>();
        if (cursor == null) {
            return Collections.emptyList();
        }
        while(cursor.moveToNext()) {
            newsEntities.add(fillContactByCursor(cursor));
        }
        IOUtil.safelyCloseCursor(cursor);
        IOUtil.safelyCloseDb(db);
        return newsEntities;
    }

    @Override
    public boolean isObjectExist(NewsEntity newsEntity) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();

        String sql = " select count(*) as count from " + TABLE_NAME + " where " + COLUMN_ID + " = ? ";
        String[] selectionArgs = new String[]{String.valueOf(newsEntity.getId())};
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

    private NewsEntity fillContactByCursor(Cursor cursor) {
        if (cursor == null) {
            return new NewsEntity();
        }
        NewsEntity entity = new NewsEntity();
        entity.setId(DBHelper.getIntValueInCursor(cursor, COLUMN_ID));
        entity.setTitle(DBHelper.getStringValueInCursor(cursor, COLUMN_TITLE));
        entity.setPublishTime(DBHelper.getStringValueInCursor(cursor, COLUMN_PUBLISH_TIME));
        return entity;
    }
}
