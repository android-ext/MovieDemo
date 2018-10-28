package com.json.dbdemo.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.json.dbdemo.database.utils.CLog;

/**
 * Created by json on 2016/1/17.
 * 如果是跨进程需要使用ContentProvider
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "student.db";
    public static final String STUDENT_TABLE_NAME = "tb_student";
    public static final String NEWS_TABLE_NAME = "tb_news";

    private DBHelper(Context context) {

        super(context, DB_NAME, null, 1);
    }

    private static DBHelper instance = null;


    public static DBHelper newInstance(Context context) {

        if (instance == null) {
            synchronized (DBHelper.class) {
                if (instance == null) {
                    instance = new DBHelper(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String studentSql = " create table if not exists " + STUDENT_TABLE_NAME + " ( id integer primary key," +
                " name varchar, address varchar ) ";

        db.execSQL(studentSql);
        db.execSQL(NewsDaoImpl.NEWS_CREATE_TABLE);
        db.execSQL(ContactDaoImpl.CREATE_TABLE_SQL);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // 如果要保留数据，先将数据读取出来 删表 建表 插入数据
        if (newVersion > oldVersion) {
            db.execSQL("drop table if exists " + STUDENT_TABLE_NAME);
            db.execSQL("drop table if exists " + NewsDaoImpl.TABLE_NAME);
            db.execSQL("drop table if exists " + ContactDaoImpl.TABLE_NAME);
        }
        onCreate(db);
    }

    public void deleteObjects(@NonNull String primaryKey, @NonNull String tableName, int[] ids) {
        if (TextUtils.isEmpty(primaryKey) || TextUtils.isEmpty(tableName)) {
            return;
        }
        StringBuilder builder = null;
        SQLiteDatabase db = getWritableDatabase();
        if (ids != null && ids.length > 0) {
            if (ids.length == 1) {
                builder.append(primaryKey + " = " + ids[0]);
            } else {
                for (int id : ids) {
                    if (builder.length() == 0) {
                        builder.append(primaryKey + " IN (" + id);
                    } else {
                        builder.append(", " + id);
                    }
                }
                builder.append(")");
            }
        }
        String where = TextUtils.isEmpty(builder.toString()) ? null : builder.toString();
        try {
            db.beginTransaction();
            int affectedRows = db.delete(tableName, where, null);
            if (affectedRows > 0) {
                CLog.i("deleteObjects totals " + affectedRows);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }

    public static int getIntValueInCursor(Cursor cursor, String key) {
        if (cursor == null || TextUtils.isEmpty(key)) {
            return -1;
        }
        int index = cursor.getColumnIndex(key);
        if (index == -1) {
            return -1;
        }
        return cursor.getInt(index);
    }


    public static String getStringValueInCursor(Cursor cursor, String key) {
        if (cursor == null || TextUtils.isEmpty(key)) {
            return "";
        }
        int index = cursor.getColumnIndex(key);
        if (index == -1) {
            return "";
        }
        return cursor.getString(index);
    }
}
