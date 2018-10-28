package com.json.dbdemo.database.utils;

import android.util.Log;

public class CLog {

   private static boolean IS_DEBUG = true;
   private static final String TAG = "CLog @@@: ";

   public static boolean isDebug() {
      return IS_DEBUG;
   }

   public static void setIsDebug(boolean isDebug) {
      IS_DEBUG = isDebug;
   }

   public static void e(String log) {
      if (IS_DEBUG) {
         Log.e(TAG, log);
      }
   }

   public static void i(String log) {
      if (IS_DEBUG) {
         Log.i(TAG, log);
      }
   }
}
