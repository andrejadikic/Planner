package com.example.planner.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.planner.models.Plan.PlanEntry;
import com.example.planner.models.User;

public class PlannerDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "planner.db";
    public static final int DATABASE_VERSION = 1;

    public PlannerDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_USERS_TABLE = "CREATE TABLE " +
                PlanEntry.TABLE_NAME + " (" +
                PlanEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PlanEntry.COLUMN_USER + " INTEGER NOT NULL, " +
                PlanEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                PlanEntry.COLUMN_PRIORITY + " TEXT NOT NULL, " +
                PlanEntry.COLUMN_STARTDATE + " TEXT NOT NULL, " +
                PlanEntry.COLUMN_ENDDATE + " TEXT NOT NULL," +
                PlanEntry.COLUMN_DETAILS + " TEXT NOT NULL, " +
                PlanEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY("+ PlanEntry.COLUMN_USER+") REFERENCES "+ User.UserEntry.TABLE_NAME+"("+ User.UserEntry._ID+")"+
                ");";
        db.execSQL(SQL_CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + PlanEntry.TABLE_NAME);
        onCreate(db);
    }
}
