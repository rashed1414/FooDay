package com.example.fooday.db.query;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.fooday.db.DBHelper;

public class Role extends DBHelper {

    public Role(Context context) {
        super(context);
    }

    SQLiteDatabase db = getWritableDatabase();
}

