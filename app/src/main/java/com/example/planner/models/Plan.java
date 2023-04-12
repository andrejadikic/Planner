package com.example.planner.models;

import android.os.Build;
import android.provider.BaseColumns;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class Plan implements Comparable<Plan>{
    private long id;
    private long userId;
    private String name;
    private String priority;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String details;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int compareTo(Plan plan) {
        return startTime.toLocalTime().compareTo(plan.startTime.toLocalTime());
    }

    public static final class PlanEntry implements BaseColumns {
        public static final String TABLE_NAME = "plans";
        public static final String COLUMN_USER = "user_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_PRIORITY = "priority";
        public static final String COLUMN_DETAILS = "details";
        public static final String COLUMN_STARTDATE = "startDate";
        public static final String COLUMN_ENDDATE = "endDate";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }


}
