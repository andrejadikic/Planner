package com.example.planner.view.recycler.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planner.R;
import com.example.planner.models.Plan;
import com.example.planner.models.comparators.ComparatorByPriority;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@SuppressLint("NewApi")


public class CalendarViewAdapter extends RecyclerView.Adapter<CalendarViewAdapter.CalendarViewHolder> {
    private List<LocalDate> days;
    private Map<LocalDate, List<Plan>> planMap;
    private static final Integer[] colors = new Integer[]{0x878787, 0xAE636F, 0x984A54, 0x6B282E};

    public CalendarViewAdapter(Map<LocalDate, List<Plan>> planMap) {
        this.planMap = planMap;
        this.days = new ArrayList<>(planMap.keySet());
        Collections.sort(days);
    }

    public void setPlanMap(Map<LocalDate, List<Plan>> newPlanMap) {
        this.planMap = newPlanMap;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.calendar_item, parent, false);
        return new CalendarViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        LocalDate date = days.get(position);
        List<Plan> plansForDay = planMap.get(date);
        if(plansForDay.isEmpty()){
            holder.bind(0,date.getDayOfMonth());
            return;
        }
        plansForDay.sort(new ComparatorByPriority("low"));
        int priority = plansForDay.get(plansForDay.size() - 1).getPriorityNumber();
        holder.bind(priority,date.getDayOfMonth());
    }

    @Override
    public int getItemCount() {
        return planMap.keySet().size();
    }

    public LocalDate getItemDate(int position) {
        return days.get(position);
    }

    public static class CalendarViewHolder extends RecyclerView.ViewHolder {
        private TextView dateTextView;

        public CalendarViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.day);
        }

        public void bind(int priority, int day){
            dateTextView.setBackgroundColor(colors[priority]);
            dateTextView.setText(String.valueOf(day));
        }
    }
}
