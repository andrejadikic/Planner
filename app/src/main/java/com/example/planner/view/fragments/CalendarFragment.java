package com.example.planner.view.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.transition.Transition;
import com.example.planner.R;
import com.example.planner.models.Plan;
import com.example.planner.view.recycler.adapter.CalendarViewAdapter;
import com.example.planner.viewmodels.PlansRecyclerViewModel;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;
@SuppressLint("NewApi")
public class CalendarFragment extends Fragment {
    private PlansRecyclerViewModel planViewModel;
    private CalendarViewAdapter calendarAdapter;
    private RecyclerView recyclerView;
    private TextView month;

    public CalendarFragment() {
        super(R.layout.fragment_calendar);
    }

    @SuppressLint("NewApi")
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
        recyclerView = view.findViewById(R.id.recyclerview);
        month = view.findViewById(R.id.date);
    }
    private void initObservers() {
        planViewModel.getPlansLiveData().observe(getViewLifecycleOwner(),new Observer<Map<LocalDate, List<Plan>>>() {
            @Override
            public void onChanged(Map<LocalDate, List<Plan>> newPlanMap) {
                calendarAdapter.setPlanMap(newPlanMap); // Postavite novu mapu u adapter kada se ona promeni
            }
        });
    }
    private void initRecycler() {
        calendarAdapter = new CalendarViewAdapter(planViewModel.getPlansLiveData().getValue());
        recyclerView.setLayoutManager(new GridLayoutManager(requireActivity(),7));
        recyclerView.setAdapter(calendarAdapter);
    }

    private void initListeners() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstVisiblePosition = ((GridLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                LocalDate firstVisibleDate = calendarAdapter.getItemDate(firstVisiblePosition);
                String monthYearString = firstVisibleDate.format(DateTimeFormatter.ofPattern("MMMM yyyy"));
                month.setText(monthYearString);
            }
        });
    }






}
