package com.example.planner.view.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.planner.R;
import com.example.planner.models.Plan;
import com.example.planner.view.viewpager.ShowPagerAdapter;
import com.example.planner.viewmodels.PlansRecyclerViewModel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@SuppressLint("NewApi")
public class ShowPlanFragment extends Fragment {
    private PlansRecyclerViewModel planViewModel;
    private TextView month;
    private TextView start;
    private TextView end;
    private TextView title;
    private TextView description;
    private Button editBtn;
    private Button deleteBtn;
    private final LocalDate date;
    private Plan plan;


    public ShowPlanFragment(LocalDate date, Plan plan) {
        super(R.layout.fragment_show_plan);
        this.date = date;
        this.plan = plan;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toast.makeText(requireContext(),"neee",Toast.LENGTH_SHORT).show();
        planViewModel = new ViewModelProvider(requireActivity(), new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication())).get(PlansRecyclerViewModel.class);
        initView(view);
        updateUi();
        initObservers();
        initListeners(view);

    }

    private void initView(View view) {
        start = view.findViewById(R.id.start);
        month = view.findViewById(R.id.date);
        end = view.findViewById(R.id.end);
        title = view.findViewById(R.id.title);
        description = view.findViewById(R.id.details);
        editBtn = view.findViewById(R.id.editBtn);
        deleteBtn = view.findViewById(R.id.deleteBtn);
    }

    private void updateUi(){
        start.setText(plan.getStart());
        end.setText(plan.getEnd());
        title.setText(plan.getName());
        description.setText(plan.getDetails());
        month.setText(date.format(DateTimeFormatter.ofPattern("dd.MMMM")));
    }

    private void initObservers() {
        planViewModel.getPlansLiveData().observe(getViewLifecycleOwner(), new Observer<Map<LocalDate, List<Plan>>>() {
            @Override
            public void onChanged(Map<LocalDate, List<Plan>> newPlanMap) {
                if(newPlanMap.get(date).contains(plan)){
                    plan = newPlanMap.get(date).get((newPlanMap.get(date).indexOf(plan)));
                }
            }
        });
    }

    private void initListeners(View view) {
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //planViewModel.editPlanForDay(date,plan,newPlan);
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                planViewModel.deletePlanForDay(date,plan);
                //getActivity().onBackPressed();
            }
        });
    }

    public LocalDate getDate() {
        return date;
    }
}