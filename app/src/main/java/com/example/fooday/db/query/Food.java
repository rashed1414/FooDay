package com.example.fooday.db.query;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.fooday.db.DBHelper;

import java.util.ArrayList;
import java.util.List;

public class Food extends DBHelper {
    public Food(Context context) {
        super(context);
    }

    DBHelper dbHelper;


    public List<String> GetFoodByType(String type){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select FoodName from food where foodtypeid in (select foodtypeid from foodtype where foodtypename = ?)",new String[]{type});
        List<String> food = new ArrayList<>();
        if(cursor.getCount() > 0){
            while(cursor.moveToNext()){
                food.add(cursor.getString(0));
            }
        }
        return food;
    }

    public boolean insertFood(String foodName, String foodType,String day){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("FoodName", foodName);
        contentValues.put("FoodTypeID", dbHelper.GetFoodTypeID(foodType));
        long result = MyDB.insert("Food", null, contentValues);

        if(result == -1){
            return false;
        }else{
            insertFoodDay(foodName, dbHelper.GetDayID(day));
            return true;
        }
    }



}
