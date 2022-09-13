package com.example.fooday.db.query;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.fooday.db.DBHelper;

public class Order extends DBHelper {

    public Order(Context context) {
        super(context);
    }

    public boolean InsertOrder(String foodName, int day, String foodType, String username){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        if (!OrderExists(username,day)){
            contentValues.put("FoodID", GetFoodID(foodName));
            contentValues.put("DayID", day);
            contentValues.put("FoodTypeID", GetFoodTypeID(foodType));
            contentValues.put("Username", username);
            long result = MyDB.insert("Orders", null, contentValues);
            if(result == -1){
                return false;
            }else{
                return true;
            }
        }else{
            contentValues.put("numberoforders", GetNumberOfOrders(username, day) + 1);
            long result = MyDB.update("Orders", contentValues, "Username = ? and DayID = ?",
                    new String[]{username, String.valueOf(day)});
            if(result == -1){
                return false;
            }else{
                return true;
            }
        }


    }

    private int GetNumberOfOrders(String username, int day) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select NumberOfOrders from orders where username = ? and dayid = ?", new String[]{username, String.valueOf(day)});
        if(cursor.getCount() > 0){
            while(cursor.moveToNext()){
                return cursor.getInt(0);
            }
        }
        return -1;
    }

    public boolean OrderExists(String username, int day){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select OrderID from orders where username = ? and dayid = ?",
                new String[]{username, String.valueOf(day)});
        if(cursor.getCount() > 0){
            while(cursor.moveToNext()){
                return true;
            }
        }
        return false;
    }

}
