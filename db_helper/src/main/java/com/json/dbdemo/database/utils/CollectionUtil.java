package com.json.dbdemo.database.utils;

import java.util.List;

public class CollectionUtil {

   public static boolean isCollectionEmpty(List list) {
      if (list == null || list.isEmpty()) {
         return true;
      }
      return false;
   }
}
