package com.example.fooday.db.query;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.fooday.db.DBHelper;

public class User extends DBHelper {

    public User(Context context) {
        super(context);

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
}

