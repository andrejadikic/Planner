package com.example.planner.models;

import android.annotation.SuppressLint;
import android.os.Build;
import android.provider.BaseColumns;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@SuppressLint("NewApi")
@Getter
@Setter
@AllArgsConstructor
public class Plan {
    private long id;
    private long userId;
    private String name;
    private String priority;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String details;

    public int getPriorityNumber(){
        switch (priority){
            case "low": return 1;
            case "mid": return 2;
            case "high": return 3;
        }
        return -1;
    }

    public boolean pastObligation(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return endTime.isBefore(LocalDateTime.now());
        }
        return false;
    }


    public String getStart(){
        return startTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    public String getDay(){
        return startTime.format(DateTimeFormatter.ofPattern("d"));
    }

    public String getEnd(){
        return endTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

//    @Override
//    public int compareTo(Plan plan) {
//        if(startTime.toLocalTime().equals(plan.startTime.toLocalTime())){
//            if(getPriorityNumber() <= plan.getPriorityNumber())
//                return 1;
//            else
//                return -1;
//        }
//        return startTime.toLocalTime().compareTo(plan.startTime.toLocalTime());
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Plan plan = (Plan) o;
        return id == plan.id;
    }

    public boolean equalContext(Plan plan){
        return plan.getName().equals(name) && plan.getStart().equals(getStart()) && plan.getEnd().equals(getEnd()) && plan.getPriorityNumber()==getPriorityNumber();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
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


