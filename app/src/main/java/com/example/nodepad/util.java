package com.example.nodepad;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class util {
    private Bitmap readImageFromDb(Integer id, SQLiteDatabase db) {
        Bitmap img = null;
        byte[] bytes;
        String sql = "SELECT * FROM test WHERE id = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{id.toString()});
        if (cursor.moveToFirst()) {
            if ((bytes = cursor.getBlob(cursor.getColumnIndex("img"))) != null) {
                img = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            }
        }
        cursor.close();
        return img;
    }
}
