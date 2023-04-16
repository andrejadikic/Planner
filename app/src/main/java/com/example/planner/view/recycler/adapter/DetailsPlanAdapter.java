package com.example.planner.view.recycler.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planner.R;
import com.example.planner.models.Plan;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@SuppressLint("NewApi")
public class DetailsPlanAdapter extends RecyclerView.Adapter<DetailsPlanAdapter.PlanViewHolder> {
    private final Consumer<Plan> onPlanClicked;
    private final Consumer<Plan> onDeleteClicked;
    private final Consumer<Plan> onEditClicked;
    private final LocalDate date;
    private Map<LocalDate, List<Plan>> planMap;

    public DetailsPlanAdapter(Map<LocalDate, List<Plan>> planMap, LocalDate date, @NonNull DiffUtil.ItemCallback<Plan> diffCallback, Consumer<Plan> onPlanClicked, Consumer<Plan> onDeleteClicked, Consumer<Plan> onEditClicked) {
        this.planMap = planMap;
        this.date = date;
        this.onPlanClicked = onPlanClicked;
        this.onDeleteClicked = onDeleteClicked;
        this.onEditClicked = onEditClicked;
    }

    public void setPlanMap(Map<LocalDate, List<Plan>> newPlanMap) {
        this.planMap = newPlanMap;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_show_plan, parent, false);
        return new PlanViewHolder(itemView, position -> {
            Plan plan = getItem(position);
            onPlanClicked.accept(plan);
        },position -> {
            Plan plan = getItem(position);
            onEditClicked.accept(plan);
        },position -> {
            Plan plan = getItem(position);
            onDeleteClicked.accept(plan);
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
    public int getPosition(Plan plan){ return planMap.get(date).indexOf(plan);}




    public static class PlanViewHolder extends RecyclerView.ViewHolder {
        public PlanViewHolder(@NonNull View itemView, Consumer<Integer> onItemClicked,Consumer<Integer> onEditClicked,Consumer<Integer> onDeleteClicked) {
            super(itemView);
            itemView.setOnClickListener(v -> {
                if (getBindingAdapterPosition() != RecyclerView.NO_POSITION) {
                    onItemClicked.accept(getBindingAdapterPosition());
                }
            });
            itemView.findViewById(R.id.editBtn).setOnClickListener(view -> {
                if (getBindingAdapterPosition() != RecyclerView.NO_POSITION) {
                    onEditClicked.accept(getBindingAdapterPosition());
                }
            });
            itemView.findViewById(R.id.deleteBtn).setOnClickListener(view -> {
                if (getBindingAdapterPosition() != RecyclerView.NO_POSITION) {
                    onDeleteClicked.accept(getBindingAdapterPosition());
                }
            });
        }

        public void bind(Plan plan) {
            switch (plan.getPriorityNumber()) {
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
            if(plan.pastObligation())
                itemView.setBackgroundResource(R.color.gray);
            //itemView.findViewById(R.id.icon).setBackgroundColor(StaticValues.colors[plan.getPriorityNumber()]);
            ((TextView) itemView.findViewById(R.id.start)).setText(plan.getStart());
            ((TextView) itemView.findViewById(R.id.end)).setText(plan.getEnd());
            ((TextView) itemView.findViewById(R.id.title)).setText(plan.getName());
            ((TextView) itemView.findViewById(R.id.details)).setText(plan.getDetails());

//            itemView.findViewById(R.id.edit).setOnClickListener(view -> {
//                FragmentTransaction transaction = ((AppCompatActivity) view.getContext()).getSupportFragmentManager().beginTransaction();
//                // Dodajemo transakciju na backstack kako bi se pritisokm na back transakcija rollback-ovala
//                transaction.addToBackStack(null);
//                transaction.replace(R.id.mainFragment, new EditPlanFragment(plan.getStartTime().toLocalDate(), plan));
//                transaction.commit();
//            });


            //itemView.findViewById(R.id.plan_list).setBackgroundColor(StaticValues.colors[plan.getPriorityNumber()]);
        }
    }
}
