package com.example.fooday.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.fooday.ui.register.SignUpFragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "foodayDB";

    public DBHelper( Context context) {
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
    }




    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("create Table Roles(RoleID INTEGER PRIMARY KEY AUTOINCREMENT, RoleName TEXT)");
        MyDB.execSQL("INSERT INTO Roles (RoleName) VALUES ('User'), ('Admin')");

        MyDB.execSQL("create Table Users( UserName TEXT Primary Key, Password TEXT, Email TEXT, RoleID INTEGER, FOREIGN KEY (RoleID) REFERENCES Roles(RoleID))");

        MyDB.execSQL("create Table FoodType(FoodTypeID INTEGER PRIMARY KEY AUTOINCREMENT, FoodTypeName TEXT)");
        MyDB.execSQL("INSERT INTO FoodType (FoodTypeName) VALUES ('Breakfast'), ('Lunch'), ('Dinner')");

        MyDB.execSQL("create Table Food(FoodID INTEGER PRIMARY KEY AUTOINCREMENT, FoodName TEXT, FoodTypeID INTEGER, FOREIGN KEY (FoodTypeID) REFERENCES FoodType(FoodTypeID))");

        MyDB.execSQL("create Table Orders(OrderID INTEGER PRIMARY KEY AUTOINCREMENT,  UserName TEXT, FoodID INTEGER,FoodTypeID INTEGER,DayID INTEGER,NumberOfOrders ITEGER, FOREIGN KEY (UserName) REFERENCES Users(UserName)," +
                "FOREIGN KEY (FoodID) REFERENCES Food(FoodID),FOREIGN KEY (FoodTypeID) REFERENCES FoodType(FoodTypeID),FOREIGN KEY (DayID) REFERENCES Days(DayID))");



        MyDB.execSQL("create Table Days(DayID INTEGER PRIMARY KEY AUTOINCREMENT, DayName TEXT)");
        MyDB.execSQL("Insert into Days (DayName) VALUES ('MONDAY'), ('TUESDAY'), ('WEDNESDAY'), ('THURSDAY'), ('FRIDAY'), ('SATURDAY'), ('SUNDAY')");
        MyDB.execSQL("create Table DayFood(DayFoodId INTEGER PRIMARY KEY AUTOINCREMENT,DayID INTEGER, FoodID INTEGER, FOREIGN KEY (DayID) REFERENCES Days(DayID), FOREIGN KEY (FoodID) REFERENCES Food(FoodID))");

    }



    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {
        MyDB.execSQL("drop table if exists users");
        MyDB.execSQL("drop table if exists roles");
        MyDB.execSQL("drop table if exists food");
        MyDB.execSQL("drop table if exists foodtype");
        MyDB.execSQL("drop table if exists days");
        MyDB.execSQL("drop table if exists dayfood");
        MyDB.execSQL("drop table if exists orders");
    }

    public boolean insertRegisterData(String name, String password, String email, String role){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("UserName", name);
        contentValues.put("Password", password);
        contentValues.put("Email", email);
        contentValues.put("RoleID", role);
        long result = MyDB.insert("Users", null, contentValues);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    public boolean checkUsernameEmail(String username, String email){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where username = ? or email = ?", new String[]{username, email});
        if(cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    public boolean checkUsernamePassword(String username, String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where username = ? and password = ?"
                , new String[]{username, password});
        if(cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    public boolean IsAdmin(String username){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select RoleName from roles where roleid in (select roleid from users where username = ?)", new String[]{username});
        if(cursor.getCount() > 0){
            while(cursor.moveToNext()){
                if(cursor.getString(0).equals("Admin"))
                    return true;
            }
        }
        return false;
    }

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

    public List<String> GetAllFoodTypes(){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select FoodTypeName from foodtype", null);
        List<String> foodTypes = new ArrayList<>();
        if(cursor.getCount() > 0){
            while(cursor.moveToNext()){
                foodTypes.add(cursor.getString(0));
            }
        }
        return foodTypes;
    }

    public int GetFoodTypeID(String foodType){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select FoodTypeID from foodtype where foodtypename = ?", new String[]{foodType});
        if(cursor.getCount() > 0){
            while(cursor.moveToNext()){
                return cursor.getInt(0);
            }
        }
        return -1;
    }

    public boolean insertFood(String foodName, String foodType,String day){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("FoodName", foodName);
        contentValues.put("FoodTypeID", GetFoodTypeID(foodType));
        long result = MyDB.insert("Food", null, contentValues);

        if(result == -1){
            return false;
        }else{
            insertFoodDay(foodName, GetDayID(day));
            return true;
        }
    }

    public void insertFoodDay(String foodName, int day){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("DayID", day);
        contentValues.put("FoodID", GetFoodID(foodName));
        long result = MyDB.insert("DayFood", null, contentValues);
    }


    public int GetFoodID(String foodName) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select FoodID from food where foodname = ? ", new String[]{foodName});
        if(cursor.getCount() > 0){
            while(cursor.moveToNext()){
                return cursor.getInt(0);
            }
        }
        return -1;
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

        if(DateExisits(day) && FoodExisits(foodName)){
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

    public boolean FoodExisits(String foodName) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select FoodName from food where foodname = ?", new String[]{foodName});
        if(cursor.getCount() > 0){
            return true;
        }
        return false;
    }

    public int GetDayID(String day) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select DayID from days where dayname = ?", new String[]{day});
        if(cursor.getCount() > 0){
            while(cursor.moveToNext()){
                return cursor.getInt(0);
            }
        }
        return -1;
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





    public boolean DateExisits(String day) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select DayID from days where dayname = ?", new String[]{day});
        if(cursor.getCount() > 0){
            while(cursor.moveToNext()){
                return true;
            }
        }
        return false;
    }
}
