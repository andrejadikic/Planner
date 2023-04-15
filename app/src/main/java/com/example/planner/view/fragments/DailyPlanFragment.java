package com.example.planner.view.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planner.R;
import com.example.planner.models.Plan;
import com.example.planner.view.recycler.adapter.CalendarViewAdapter;
import com.example.planner.view.recycler.adapter.PlanAdapter;
import com.example.planner.view.recycler.differ.PlanDiffer;
import com.example.planner.view.viewpager.PagerAdapter;
import com.example.planner.viewmodels.PlansRecyclerViewModel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@SuppressLint("NewApi")
public class DailyPlanFragment extends Fragment {
    private PlansRecyclerViewModel planViewModel;
    private CalendarViewAdapter calendarAdapter;
    private PlanAdapter planAdapter;
    private RecyclerView recyclerView;
    private TextView month;
    private LocalDate date;

    public DailyPlanFragment(LocalDate date) {
        super(R.layout.fragment_list_plans);
        this.date=date;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        planViewModel = new ViewModelProvider(requireActivity(), new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication())).get(PlansRecyclerViewModel.class);
        planViewModel.fetch();
        initRecycler();
        initObservers();
        initListeners();

    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.listRv);
        month = view.findViewById(R.id.date);
    }

    private void initObservers() {
        Toast.makeText(requireContext(),date.toString(),Toast.LENGTH_SHORT).show();
        planViewModel.getPlansLiveData().observe(getViewLifecycleOwner(), new Observer<Map<LocalDate, List<Plan>>>() {
            @Override
            public void onChanged(Map<LocalDate, List<Plan>> newPlanMap) {
                planAdapter.setPlanMap(newPlanMap); // Postavite novu mapu u adapter kada se ona promeni
            }
        });
    }

    private void initRecycler() {
        planAdapter = new PlanAdapter(planViewModel.getPlansLiveData().getValue(),date,new PlanDiffer(),plan -> {
            Toast.makeText(requireContext(), plan.getId() + "", Toast.LENGTH_SHORT).show();
            //viewPager.setCurrentItem(PagerAdapter.FRAGMENT_2, false);
        });
        //calendarAdapter = new CalendarViewAdapter(planViewModel.getPlansLiveData().getValue());
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        recyclerView.setAdapter(planAdapter);
    }

    private void initListeners() {
        month.setText(date.format(DateTimeFormatter.ofPattern("dd.MMMM")));
    }

    public LocalDate getDate() {
        return date;
    }
}