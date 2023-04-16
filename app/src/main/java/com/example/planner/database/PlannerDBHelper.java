package com.example.planner.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.planner.models.Plan.PlanEntry;
import com.example.planner.models.User;
import static com.example.planner.models.Plan.PlanEntry.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalUnit;
import java.util.Random;

public class PlannerDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "planner.db";
    public static final int DATABASE_VERSION = 4;

    public PlannerDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_USERS_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER + " INTEGER NOT NULL, " +
                COLUMN_NAME + " TEXT NOT NULL, " +
                COLUMN_PRIORITY + " TEXT NOT NULL, " +
                COLUMN_STARTDATE + " TEXT NOT NULL, " +
                COLUMN_ENDDATE + " TEXT NOT NULL," +
                COLUMN_DETAILS + " TEXT NOT NULL, " +
                COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY("+ COLUMN_USER+") REFERENCES "+ User.UserEntry.TABLE_NAME+"("+ User.UserEntry._ID+")"+
                ");";
        db.execSQL(SQL_CREATE_USERS_TABLE);
        String[] priority = new String[]{"low","mid","high"};
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Random random = new Random();
        for(int i=0;i<50;i++){
            ContentValues values = new ContentValues();
            LocalDateTime start = LocalDateTime.now().plusMonths(random.nextInt(3)-1).plusDays(random.nextInt(20)-1).plusHours(random.nextInt(20)-3).plusMinutes(random.nextInt(20)-2);
            for(int j=1;j<=10;j++){
                start = start.plusMinutes(random.nextInt(200)-100);
                LocalDateTime end = start.plusMinutes(random.nextInt(100));
                values.put(COLUMN_USER, 1);
                values.put(COLUMN_NAME, "Title "+i+j);
                values.put(COLUMN_PRIORITY, priority[random.nextInt(3)]);
                values.put(COLUMN_STARTDATE, start.format(formatter));
                values.put(COLUMN_ENDDATE, end.format(formatter));
                values.put(COLUMN_DETAILS, "Details for plan " + i+j);
                db.insert(TABLE_NAME, null, values);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + PlanEntry.TABLE_NAME);
        onCreate(db);
    }
}
