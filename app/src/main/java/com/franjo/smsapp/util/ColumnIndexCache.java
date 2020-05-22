package com.franjo.smsapp.util;

import android.database.Cursor;

import java.util.HashMap;
import java.util.Map;

public class ColumnIndexCache {

    private static Map<String, Integer> map = new HashMap<>();

    public static Integer getColumnIndexOrThrow(Cursor cursor, String columnName) {
        Integer value = cursor.getColumnIndex(columnName);
        if (!map.containsKey(columnName)) {
            map.put(columnName, value);
        }
        return map.get(columnName);
    }

    public static void clear() {
        map.clear();
    }
}
