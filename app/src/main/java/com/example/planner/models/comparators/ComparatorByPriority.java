package com.example.planner.models.comparators;

import com.example.planner.models.Plan;

import java.util.Comparator;

public class ComparatorByPriority implements Comparator<Plan> {

    private String priority;

    public ComparatorByPriority(String priority) {
        this.priority = priority.toLowerCase();
    }

    @Override
    public int compare(Plan plan1, Plan plan2) {
        if(priority.equals("high"))
            return plan2.getPriorityNumber()-plan1.getPriorityNumber();
        if(priority.equals("low"))
            return plan1.getPriorityNumber()-plan2.getPriorityNumber();
        if(plan1.getPriority().equals(priority))
            return -1;
        else if (plan2.getPriority().equals(priority))
            return 1;
        if(plan1.getPriorityNumber()<plan2.getPriorityNumber())
            return -1;
        return 1;
    }
}
