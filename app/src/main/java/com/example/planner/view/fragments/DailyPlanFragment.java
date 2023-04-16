package com.example.planner.view.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planner.R;
import com.example.planner.app.MainActivity;
import com.example.planner.models.Plan;
import com.example.planner.view.recycler.adapter.CalendarViewAdapter;
import com.example.planner.view.recycler.adapter.PlanAdapter;
import com.example.planner.view.recycler.differ.PlanDiffer;
import com.example.planner.view.viewpager.PagerAdapter;
import com.example.planner.viewmodels.PlansRecyclerViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
    private CheckBox pastObl;
    private RadioGroup radioGroup;
    private EditText search;
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
        initListeners(view);

    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.listRv);
        month = view.findViewById(R.id.date);
        search = view.findViewById(R.id.search_bar);
        pastObl = view.findViewById(R.id.pastObligations);
        radioGroup = view.findViewById(R.id.radioGroup);
    }

    private void initObservers() {
        planViewModel.getPlansLiveData().observe(getViewLifecycleOwner(), new Observer<Map<LocalDate, List<Plan>>>() {
            @Override
            public void onChanged(Map<LocalDate, List<Plan>> newPlanMap) {
                planAdapter.setPlanMap(newPlanMap); // Postavite novu mapu u adapter kada se ona promeni
            }
        });
    }

    private void initRecycler() {
        planAdapter = new PlanAdapter(planViewModel.getPlansLiveData().getValue(),date,new PlanDiffer(),plan -> {
            FragmentTransaction transaction = createTransactionWithAnimation();
            transaction.replace(R.id.mainFragment, new MainShowFragment(date,plan));
            transaction.commit();
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        recyclerView.setAdapter(planAdapter);
    }

    private void initListeners(View view) {
        month.setText(date.format(DateTimeFormatter.ofPattern("dd.MMMM")));

        view.findViewById(R.id.floatingAdd).setOnClickListener(view1 -> {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            // Dodajemo transakciju na backstack kako bi se pritisokm na back transakcija rollback-ovala
            transaction.addToBackStack(null);
            transaction.replace(R.id.mainFragment, new AddPlanFragment(date));
            transaction.commit();
        });


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Toast.makeText(requireContext(),"checked"+((RadioButton)(view.findViewById(checkedId))).getText().toString(), Toast.LENGTH_SHORT).show();
                planViewModel.filterPlans(date,search.getText().toString(),pastObl.isChecked(),((RadioButton)(view.findViewById(checkedId))).getText().toString());
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                planViewModel.filterPlans(date,editable.toString(),pastObl.isChecked(),((RadioButton)view.findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString());
            }
        });
        pastObl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //Toast.makeText(requireContext(),"checked"+b, Toast.LENGTH_SHORT).show();
                planViewModel.filterPlans(date,search.getText().toString(),b,((RadioButton)view.findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString());
            }
        });


    }

    public LocalDate getDate() {
        return date;
    }

    private FragmentTransaction createTransactionWithAnimation() {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        // Dodajemo transakciju na backstack kako bi se pritisokm na back transakcija rollback-ovala
        transaction.addToBackStack(null);
        return transaction;
    }
}