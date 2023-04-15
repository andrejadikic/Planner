package com.example.planner.database;

import static com.example.planner.models.User.UserEntry.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.planner.models.User.UserEntry;

public class UserDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "planner.db";
    public static final int DATABASE_VERSION = 2;


    public UserDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_USERS_TABLE = "CREATE TABLE " +
                UserEntry.TABLE_NAME + " (" +
                UserEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EMAIL + " TEXT NOT NULL, " +
                UserEntry.COLUMN_USERNAME + " TEXT NOT NULL, " +
                UserEntry.COLUMN_PASSWORD + " TEXT NOT NULL, " +
                UserEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" + ");";
        db.execSQL(SQL_CREATE_USERS_TABLE);

        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, "admin@gmail.com");
        values.put(COLUMN_USERNAME, "admin");
        values.put(COLUMN_PASSWORD, "Admin123");
        long id = db.insert(TABLE_NAME, null, values);
//        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME);
        onCreate(db);
    }
}
