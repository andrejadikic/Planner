package com.example.planner.view.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.planner.R;
import com.example.planner.app.StaticValues;
import com.example.planner.models.Plan;
import com.example.planner.viewmodels.PlansRecyclerViewModel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@SuppressLint("NewApi")
public class EditPlanFragment extends Fragment {
    private PlansRecyclerViewModel plansRecyclerViewModel;
    private Plan plan;
    private Context context;
    private TextView dateTxt;
    private TextView startTxt;
    private TextView endTxt;
    private EditText titleTxt;
    private EditText detailsTxt;
    private RadioGroup radioGroup;
    private int hourStart,minuteStart;
    private LocalDate date;
    private LocalDateTime start, end;
    private String priority;
    private Button editBtn;
    private Button cancelBtn;

    public EditPlanFragment(LocalDate date, Plan plan) {
        super(R.layout.fragment_add_plan);
        this.date = date;
        this.plan = plan;
        start = plan.getStartTime();
        end = plan.getEndTime();
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
        plansRecyclerViewModel = new ViewModelProvider(requireActivity(), new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication())).get(PlansRecyclerViewModel.class);
        init(view);
    }

    private void init(View view){
        initWidgets(view);
        updateUi();
        initActions(view);
        initListener(view);
    }

    private void initWidgets(View view) {
        dateTxt = view.findViewById(R.id.date);
        startTxt = view.findViewById(R.id.start);
        endTxt = view.findViewById(R.id.end);
        titleTxt = view.findViewById(R.id.title);
        detailsTxt = view.findViewById(R.id.details);
        radioGroup = view.findViewById(R.id.radioGroup);
        editBtn = view.findViewById(R.id.saveBtn);
        cancelBtn = view.findViewById(R.id.cancelBtn);

    }
    private void updateUi() {
        dateTxt.setText(start.format(DateTimeFormatter.ofPattern("dd.MMMM.yyyy")));
        startTxt.setText(start.format(DateTimeFormatter.ofPattern("hh:mm")));
        endTxt.setText(start.format(DateTimeFormatter.ofPattern("hh:mm")));
        titleTxt.setText(plan.getName());
        detailsTxt.setText(plan.getDetails());
        switch (plan.getPriorityNumber()) {
            case 1:
                radioGroup.check(R.id.low);
                radioGroup.setBackgroundResource(R.color.low_priority);
                break;
            case 2:
                radioGroup.check(R.id.mid);
                radioGroup.setBackgroundResource(R.color.mid_priority);
                break;
            case 3:
                radioGroup.check(R.id.high);
                radioGroup.setBackgroundResource(R.color.high_priority);
                break;
            default:
                radioGroup.setBackgroundResource(R.color.pink_100);
                break;
        }
        priority = plan.getPriority();
    }

    private void initActions(View view) {
        startTxt.setOnClickListener(v->{
            TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                    hourStart=hour;
                    minuteStart=minute;
                    start = LocalDateTime.of(date, LocalTime.of(hour,minute));
                    startTxt.setText(String.format(Locale.getDefault(),"%02d:%02d",hourStart,minuteStart));
                }
            };

            TimePickerDialog timePickerDialog = new TimePickerDialog(this.context,onTimeSetListener,hourStart,minuteStart,true);
            timePickerDialog.setTitle("Select start time");
            timePickerDialog.show();
        });
        endTxt.setOnClickListener(v->{
            TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                    hourStart=hour;
                    minuteStart=minute;
                    end = LocalDateTime.of(date, LocalTime.of(hour,minute));
                    startTxt.setText(String.format(Locale.getDefault(),"%02d:%02d",hourStart,minuteStart));
                }
            };

            TimePickerDialog timePickerDialog = new TimePickerDialog(this.context,onTimeSetListener,hourStart,minuteStart,true);
            timePickerDialog.setTitle("Select end time");
            timePickerDialog.show();
        });

        editBtn.setOnClickListener(v->{
            if(!plansRecyclerViewModel.editPlanForDay(date,plan,start,end,titleTxt.getText().toString(),detailsTxt.getText().toString(),priority)){
                Toast.makeText(context,"Something went wrong, try agaim",Toast.LENGTH_SHORT).show();
            }

        });

        cancelBtn.setOnClickListener(v->{
            getActivity().onBackPressed();
        });
    }

    private void initListener(View view) {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checked = (RadioButton)view.findViewById(checkedId);
                checked.setBackgroundResource(R.color.gray);
                priority = checked.getText().toString().toLowerCase();
            }
        });
    }
}
