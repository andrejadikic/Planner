package com.example.planner.view.recycler.differ;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.example.planner.models.Plan;

public class PlanDiffer extends DiffUtil.ItemCallback<Plan> {
    @Override
    public boolean areItemsTheSame(@NonNull Plan oldItem, @NonNull Plan newItem) {
        return oldItem.getId() == newItem.getId();
    }

    @Override
    public boolean areContentsTheSame(@NonNull Plan oldItem, @NonNull Plan newItem) {
        return oldItem.equalContext(newItem);
    }
}