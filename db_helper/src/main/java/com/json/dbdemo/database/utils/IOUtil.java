package com.json.dbdemo.database.utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class IOUtil {

   public static void safelyCloseDb(SQLiteDatabase db) {
      try {
         if (db != null) {
            db.close();
         }
      } catch (Exception e){
         e.printStackTrace();
      }
   }

   public static void safelyCloseCursor(Cursor cursor) {
      try {
         if (cursor != null) {
            cursor.close();
         }
      } catch (Exception e){
         e.printStackTrace();
      }
   }
}
