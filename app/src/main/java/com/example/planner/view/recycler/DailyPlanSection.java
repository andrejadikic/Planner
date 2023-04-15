package com.example.planner.view.recycler;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planner.R;
import com.example.planner.app.StaticValues;
import com.example.planner.models.Plan;
import com.example.planner.view.recycler.adapter.CalendarViewAdapter;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;

import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;

public class DailyPlanSection extends Section {
    LocalDate localDate;
    List<Plan> list;


    public DailyPlanSection(LocalDate localDate, List<Plan> list) {
        super(SectionParameters.builder()
                .itemResourceId(R.layout.plan_list_item)
                .headerResourceId(R.layout.header_date)
                .build());
        this.localDate = localDate;
        this.list = list;
    }

    @Override
    public int getContentItemsTotal() {
        return list.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return null;
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final Context context;

        public ViewHolder(@NonNull View itemView, Context context, Consumer<Integer> onItemClicked) {
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
