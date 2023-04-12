package com.example.planner.database;

import static android.content.Context.MODE_PRIVATE;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.planner.app.StaticValues;
import com.example.planner.models.Plan;
import com.example.planner.models.Plan.*;
import com.example.planner.models.User;
import com.example.planner.models.User.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DBManager {
    private PlannerDBHelper plannerDBHelper;
    private UserDBHelper userDBHelper;
    private SQLiteDatabase db;
    private final Context context;

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
        Cursor resultSet = db.rawQuery("Select * from " + UserEntry.TABLE_NAME,null);
        resultSet.moveToFirst();
        if (resultSet.getCount() > 0 && password.equals(resultSet.getString(3))) {
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
    public Plan createPlan(Long userId, String name, String priority, LocalDateTime startTime, LocalDateTime endTime, String details){
        ContentValues contentValues = new ContentValues();
        contentValues.put(PlanEntry.COLUMN_USER, userId);
        contentValues.put(PlanEntry.COLUMN_NAME, name);
        contentValues.put(PlanEntry.COLUMN_PRIORITY, name);
        contentValues.put(PlanEntry.COLUMN_STARTDATE, startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        contentValues.put(PlanEntry.COLUMN_ENDDATE, endTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        contentValues.put(PlanEntry.COLUMN_DETAILS, details);
        long id = db.insertOrThrow(PlanEntry.TABLE_NAME, null, contentValues);
        return new Plan(id,userId,name,priority, startTime,endTime,details);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Map<LocalDate,List<Plan>> findPlansForUser(){
        Map<LocalDate,List<Plan>> planForDay = new HashMap<>();

        List<Plan> plans = new ArrayList<>();
        SharedPreferences sp = context.getSharedPreferences(StaticValues.USER_SHARED_PREF,MODE_PRIVATE);
        long id = sp.getLong(StaticValues.ID,-1);
        if(id!=-1){
            Cursor result=db.rawQuery("select * from "+PlanEntry.TABLE_NAME+" where "+ PlanEntry.COLUMN_USER+
                    " = " + id,null);
            result.moveToFirst();
            while (result.moveToNext()) {
                LocalDate date = LocalDateTime.parse(result.getString(3),DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toLocalDate();
                Plan plan = new Plan(
                        Long.parseLong(result.getString(0)),
                        Long.parseLong(result.getString(1)),
                        result.getString(2),
                        result.getString(3),
                        LocalDateTime.parse(result.getString(4),DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                        LocalDateTime.parse(result.getString(5),DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                        result.getString(6));
                if(!planForDay.containsKey(date)){
                    planForDay.put(date,new ArrayList<>());
                }
                planForDay.get(date).add(plan);
                plans.add(plan);
            }
        }

        for(LocalDate localDate:planForDay.keySet()){
            Collections.sort(Objects.requireNonNull(planForDay.get(localDate)));
        }
        return planForDay;
    }







}
