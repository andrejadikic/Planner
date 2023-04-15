package com.example.planner.view.recycler.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planner.R;
import com.example.planner.models.Plan;
import com.example.planner.models.comparators.ComparatorByPriority;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@SuppressLint("NewApi")


public class CalendarViewAdapter extends RecyclerView.Adapter<CalendarViewAdapter.CalendarViewHolder> {
    private List<LocalDate> days;

    private Map<LocalDate, List<Plan>> planMap;
    private Consumer<LocalDate> onDayClicked;

    public CalendarViewAdapter(Map<LocalDate, List<Plan>> planMap, @NonNull DiffUtil.ItemCallback<LocalDate> diffCallback, Consumer<LocalDate> onDayClicked) {
        this.planMap = planMap;
        this.days = new ArrayList<>(planMap.keySet());
        Collections.sort(days);
        this.onDayClicked = onDayClicked;
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
        return new CalendarViewHolder(itemView,position->{
            LocalDate date = getItemDate(position);
            onDayClicked.accept(date);
        });
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

        public CalendarViewHolder(@NonNull View itemView, Consumer<Integer> onItemClicked) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.day);
            itemView.setOnClickListener(v -> {
                if (getBindingAdapterPosition() != RecyclerView.NO_POSITION) {
                    onItemClicked.accept(getBindingAdapterPosition());
                }
            });
        }

        public void bind(int priority, int day){
            switch (priority) {
                case 1:
                    itemView.setBackgroundResource(R.color.low_priority);
                    break;
                case 2:
                    itemView.setBackgroundResource(R.color.mid_priority);
                    break;
                case 3:
                    itemView.setBackgroundResource(R.color.high_priority);
                    break;
                default:
                    itemView.setBackgroundResource(R.color.pink_100);
                    break;
            }
            //itemView.setBackgroundResource(R.color.low_priority);
            dateTextView.setText(String.valueOf(day));
        }
    }
}
