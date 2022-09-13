package com.example.fooday.db.query;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.fooday.db.DBHelper;

import java.util.ArrayList;
import java.util.List;

public class Days extends DBHelper {

    public Days(Context context) {
        super(context);
    }


    public List<String> GetDays(){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select DayName from days", null);
        List<String> days = new ArrayList<>();
        if(cursor.getCount() > 0){
            while(cursor.moveToNext()){
                days.add(cursor.getString(0));
            }
        }
        return days;
    }
}
