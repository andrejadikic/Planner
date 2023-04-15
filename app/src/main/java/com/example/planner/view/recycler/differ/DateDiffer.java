package com.example.planner.view.recycler.differ;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.example.planner.models.Plan;

import java.time.LocalDate;
@SuppressLint("NewApi")
public class DateDiffer extends DiffUtil.ItemCallback<LocalDate> {
    @Override
    public boolean areItemsTheSame(@NonNull LocalDate oldItem, @NonNull LocalDate newItem) {
        return oldItem.isEqual(newItem);
    }

    @Override
    public boolean areContentsTheSame(@NonNull LocalDate oldItem, @NonNull LocalDate newItem) {
        return oldItem.isEqual(newItem);
    }
}