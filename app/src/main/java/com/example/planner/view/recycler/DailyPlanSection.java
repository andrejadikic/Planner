package com.example.planner.view.recycler;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planner.R;
import com.example.planner.app.StaticValues;
import com.example.planner.models.Plan;

import java.time.LocalDate;
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;

public class DailyPlanSection extends Section {
    private final LocalDate localDate;
    private final List<Plan> list;

    private final ClickListener clickListener;

    public DailyPlanSection(LocalDate localDate, List<Plan> list, ClickListener clickListener) {
        super(SectionParameters.builder()
                .itemResourceId(R.layout.plan_list_item)
                .headerResourceId(R.layout.header_date)
                .build());
        this.localDate = localDate;
        this.list = list;
        this.clickListener = clickListener;
    }

    @Override
    public int getContentItemsTotal() {
        return list.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new ViewHolder(view,view.getContext());
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ViewHolder itemHolder = (ViewHolder) holder;
        final Plan plan = list.get(position);
        itemHolder.bind(plan);

        itemHolder.rootView.setOnClickListener(v ->
                clickListener.onItemRootViewClicked(this, itemHolder.getAdapterPosition()));

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final Context context;
        final View rootView;

        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            this.context = context;
            rootView=itemView;
        }

        public void bind(Plan plan) {
            ((TextView) itemView.findViewById(R.id.start)).setText(plan.getStart());
            ((TextView) itemView.findViewById(R.id.end)).setText(plan.getEnd());
            ((TextView) itemView.findViewById(R.id.title)).setText(plan.getName());
            itemView.findViewById(R.id.plan_list).setBackgroundColor(StaticValues.colors[plan.getPriorityNumber()]);
        }

    }


    interface ClickListener {

        void onItemRootViewClicked(@NonNull final DailyPlanSection section, final int itemAdapterPosition);

        void onEditButtonClicked(@NonNull final DailyPlanSection section);

        void onDeleteButtonClicked(@NonNull final DailyPlanSection section);
    }
}
