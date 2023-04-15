package com.example.planner.view.recycler.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planner.R;
import com.example.planner.app.StaticValues;
import com.example.planner.models.Plan;
import com.example.planner.models.comparators.ComparatorByPriority;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@SuppressLint("NewApi")


public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.PlanViewHolder> {

    private final Consumer<Plan> onPlanClicked;
    private LocalDate date;

    private Map<LocalDate, List<Plan>> planMap;

    public PlanAdapter(Map<LocalDate, List<Plan>> planMap, LocalDate date, @NonNull DiffUtil.ItemCallback<Plan> diffCallback, Consumer<Plan> onPlanClicked) {
        this.planMap = planMap;
        this.date = date;
        this.onPlanClicked = onPlanClicked;
    }

    public void setPlanMap(Map<LocalDate, List<Plan>> newPlanMap) {
        this.planMap = newPlanMap;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.plan_list_item, parent, false);
        return new PlanViewHolder(itemView, parent.getContext(), position -> {
            Plan car = getItem(position);
            onPlanClicked.accept(car);
        });
    }

    @Override
    public void onBindViewHolder(@NonNull PlanViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    @Override
    public int getItemCount() {
        return planMap.get(date).size();
    }

    public Plan getItem(int position) {
        return planMap.get(date).get(position);
    }




    public static class PlanViewHolder extends RecyclerView.ViewHolder {
        private final Context context;

        public PlanViewHolder(@NonNull View itemView, Context context, Consumer<Integer> onItemClicked) {
            super(itemView);
            this.context = context;
            itemView.setOnClickListener(v -> {
                if (getBindingAdapterPosition() != RecyclerView.NO_POSITION) {
                    onItemClicked.accept(getBindingAdapterPosition());
                }
            });
        }

        public void bind(Plan plan) {
            ((TextView) itemView.findViewById(R.id.start)).setText(plan.getStart());
            ((TextView) itemView.findViewById(R.id.end)).setText(plan.getEnd());
            ((TextView) itemView.findViewById(R.id.title)).setText(plan.getName());
            itemView.findViewById(R.id.plan_list).setBackgroundColor(StaticValues.colors[plan.getPriorityNumber()]);
        }

    }


}
