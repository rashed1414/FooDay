package com.example.fooday.db.query;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.fooday.db.DBHelper;

public class FoodType extends DBHelper {
    public FoodType(Context context) {
        super(context);
    }

    SQLiteDatabase db = getReadableDatabase();
}
