package com.example.planner.models.comparators;

import android.annotation.SuppressLint;

import com.example.planner.models.Plan;

import java.util.Comparator;

public class ComparatorByDate implements Comparator<Plan> {

    @SuppressLint("NewApi")
    @Override
    public int compare(Plan plan, Plan plan1) {
        return plan.getStartTime().toLocalTime().compareTo(plan1.getStartTime().toLocalTime());
    }
}
