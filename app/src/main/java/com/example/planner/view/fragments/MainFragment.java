package com.example.planner.view.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.planner.R;
import com.example.planner.app.StaticValues;
import com.example.planner.database.DBManager;
import com.example.planner.viewmodels.PlansRecyclerViewModel;
import com.example.planner.viewmodels.UserViewModel;

import java.time.LocalDateTime;
import java.util.Locale;

public class MainFragment extends Fragment {
    private PlansRecyclerViewModel plansRecyclerViewModel;
    private Context context;
    private DBManager dbManager;

    private TextView startTxt;
    private TextView endTxt;
    private int hourStart,minuteStart;

    private Button createBtn;
    private Button cancelBtn;

    public MainFragment() {
        super(R.layout.fragment_add_plan);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @SuppressLint("NewApi")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbManager = new DBManager(getContext());
        dbManager.open();
        plansRecyclerViewModel = new ViewModelProvider(requireActivity(), new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication())).get(PlansRecyclerViewModel.class);
        SharedPreferences sp = context.getSharedPreferences(StaticValues.USER_SHARED_PREF,MODE_PRIVATE);
        long id = sp.getLong(StaticValues.ID,0);
        Toast.makeText(context,"Id"+id,Toast.LENGTH_SHORT).show();
        init(view);
    }

    private void init(View view){
        initWidgets(view);
        initActions(view);
    }

    private void initWidgets(View view) {
        startTxt = view.findViewById(R.id.start);
        endTxt = view.findViewById(R.id.end);
        createBtn = view.findViewById(R.id.createBtn);
        cancelBtn = view.findViewById(R.id.cancelBtn);
    }

    @SuppressLint("NewApi")
    private void initActions(View view) {
        startTxt.setOnClickListener(v->{
            TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                    hourStart=hour;
                    minuteStart=minute;
                    startTxt.setText(String.format(Locale.getDefault(),"%02d:%02d",hourStart,minuteStart));
                }
            };

            TimePickerDialog timePickerDialog = new TimePickerDialog(this.context,onTimeSetListener,hourStart,minuteStart,true);
            timePickerDialog.setTitle("Select start time");
            timePickerDialog.show();
        });

        createBtn.setOnClickListener(v->{

        });


    }
}
