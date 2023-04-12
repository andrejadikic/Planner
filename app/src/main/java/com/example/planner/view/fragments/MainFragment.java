package com.example.planner.view.fragments;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.planner.R;
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





    public MainFragment() {
        super(R.layout.fragment_add_plan);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbManager = new DBManager(getContext());
        dbManager.open();
        plansRecyclerViewModel = new ViewModelProvider(this).get(PlansRecyclerViewModel.class);
        init(view);
    }

    private void init(View view){
        initWidgets(view);
        initActions(view);
    }

    private void initWidgets(View view) {
        startTxt = view.findViewById(R.id.start);
        endTxt = view.findViewById(R.id.end);
    }

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


    }
}
