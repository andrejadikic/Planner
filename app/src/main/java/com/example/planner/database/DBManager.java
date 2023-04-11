package com.example.planner.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.planner.models.Plan;
import com.example.planner.models.Plan.*;
import com.example.planner.models.User;
import com.example.planner.models.User.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DBManager {
    private PlannerDBHelper plannerDBHelper;
    private UserDBHelper userDBHelper;
    private SQLiteDatabase db;
    private Context context;

    public DBManager(Context context) {
        this.context = context;
    }

    public DBManager open(){
        userDBHelper = new UserDBHelper(context);
        plannerDBHelper = new PlannerDBHelper(context);
        db = userDBHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        userDBHelper.close();
        plannerDBHelper.close();
    }


    public User registerUser(String email, String username, String password){
        ContentValues contentValues = new ContentValues();
        contentValues.put(UserEntry.COLUMN_EMAIL, email);
        contentValues.put(UserEntry.COLUMN_USERNAME, username);
        contentValues.put(UserEntry.COLUMN_PASSWORD, password);
        long id = db.insertOrThrow(UserEntry.TABLE_NAME, null, contentValues);
        return new User(id,email,username,password);
    }

    public User loginUser(String username, String password) {
        Cursor resultSet = db.rawQuery("Select * from " + UserEntry.TABLE_NAME + " where " + UserEntry.COLUMN_USERNAME + " = '" + username + "'",null);
        if(resultSet.moveToFirst() && password.equals(resultSet.getString(3))){
            return new User(resultSet.getLong(0),resultSet.getString(1),username,password);
        }
        return null;
    }

    public int changePassword(String username, String password) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(UserEntry.COLUMN_PASSWORD, password);
        int i = db.update(UserEntry.TABLE_NAME, contentValues, UserEntry.COLUMN_USERNAME + " = ? "  , new String[]{username});
        return i;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Plan createPlan(Long userId, String name, LocalDateTime startTime, LocalDateTime endTime, String details){
        ContentValues contentValues = new ContentValues();
        contentValues.put(PlanEntry.COLUMN_USER, userId);
        contentValues.put(PlanEntry.COLUMN_NAME, name);
        contentValues.put(PlanEntry.COLUMN_STARTDATE, startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        contentValues.put(PlanEntry.COLUMN_ENDDATE, endTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        contentValues.put(PlanEntry.COLUMN_DETAILS, details);
        long id = db.insertOrThrow(UserEntry.TABLE_NAME, null, contentValues);
        return new Plan(id,userId,name,startTime,endTime,details);
    }



}
