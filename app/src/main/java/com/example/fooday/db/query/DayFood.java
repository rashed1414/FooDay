package com.example.fooday.db.query;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.fooday.db.DBHelper;

public class DayFood extends DBHelper {
    public DayFood(Context context) {
        super(context);
    }

    DBHelper dbHelper;

    public void insertFoodDay(String foodName, int day){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("DayID", day);
        contentValues.put("FoodID", dbHelper.GetFoodID(foodName));
        long result = MyDB.insert("DayFood", null, contentValues);
    }

    public String getBreakFastByDay(int day){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select FoodName from food where foodtypeid=1 and foodid in " +
                "(select foodid from dayfood where dayid =?)", new String[]{String.valueOf(day)});
        if(cursor.getCount() > 0){
            while (cursor.moveToNext()){
                return cursor.getString(0);
            }
        }
        return null;
    }

    public String getLunchByDay(int day){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select FoodName from food where foodtypeid=2 and foodid in " +
                "(select foodid from dayfood where dayid=?)", new String[]{String.valueOf(day)});
        if(cursor.getCount() > 0){
            while (cursor.moveToNext()){
                return cursor.getString(0);
            }
        }

        return null;
    }

    public String getDinnerByDay(int day){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select FoodName from food where foodtypeid=3 and foodid in " +
                "(select foodid from dayfood where dayid =?)", new String[]{String.valueOf(day)});
        if(cursor.getCount() > 0){
            while (cursor.moveToNext()){
                return cursor.getString(0);
            }

        }
        return null;
    }

    public boolean UpdateDayFoodById(int foodayId, String foodName){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("FoodID", GetFoodID(foodName));
        long result = MyDB.update("DayFood", contentValues, "DayFoodID = ?",
                new String[]{String.valueOf(foodayId)});
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    public int GetFoodDayID(String foodName, int day){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select DayFoodID from dayfood where foodid in" +
                        " (select foodid from food where foodname = ?) and dayid = ?",
                new String[]{foodName, String.valueOf(day)});
        if(cursor.getCount() > 0){
            while(cursor.moveToNext()){
                return cursor.getInt(0);
            }
        }
        return -1;
    }

    public boolean UpdateDayFood(String foodName, String day, String foodType){

        if(dbHelper.DateExisits(day) && dbHelper.FoodExisits(foodName)){
            SQLiteDatabase MyDB = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            contentValues.put("FoodID", GetFoodID(foodName));
            long result = MyDB.update("DayFood", contentValues, "DayID = ? and FoodID in " +
                            "(select foodid from food where foodtypeid = ?)"
                    , new String[]{String.valueOf(GetDayID(day)), String.valueOf(GetFoodTypeID(foodType))});
            System.out.println(result);
            if(result == -1){
                return false;
            }else{
                return true;
            }
        }else{
            insertFood(foodName, foodType, day);
        }
        return true;
    }

}
