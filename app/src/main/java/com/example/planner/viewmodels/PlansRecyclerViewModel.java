package com.example.planner.viewmodels;

import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.planner.app.StaticValues;
import com.example.planner.database.DBManager;
import com.example.planner.models.Plan;

import java.io.Closeable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlansRecyclerViewModel extends ViewModel {
    private Map<LocalDate, List<Plan>> plansForDay;

    private MutableLiveData<Map<LocalDate, List<Plan>>> plansLiveData;

    public PlansRecyclerViewModel() {
        plansLiveData = new MutableLiveData<>();
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void fetch(DBManager dbManager){
        plansForDay = dbManager.findPlansForUser();
        Map<LocalDate, List<Plan>> listToSubmit = new HashMap<LocalDate, List<Plan>>(plansForDay);
        plansLiveData.setValue(listToSubmit);

    }
}
