package com.example.planner.viewmodels;

import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.planner.app.StaticValues;
import com.example.planner.database.DBManager;
import com.example.planner.models.Plan;
import com.example.planner.models.comparators.ComparatorByDate;
import com.example.planner.models.comparators.ComparatorByPriority;

import java.io.Closeable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;


public class PlansRecyclerViewModel extends AndroidViewModel {
    private DBManager dbManager;
    private Map<LocalDate, List<Plan>> plansForDay;

    private MutableLiveData<Map<LocalDate, List<Plan>>> plansLiveData;

    public PlansRecyclerViewModel() {
        super();
        dbManager = new DBManager(getApplication());
        plansLiveData = new MutableLiveData<>();
        plansForDay = new HashMap<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            fetch();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void fetch(){
        plansForDay = dbManager.findPlansForUser();
        sort();
        updateLiveList();
    }

    private void sort(){
        for(LocalDate date:plansForDay.keySet()){
            Collections.sort(plansForDay.get(date));
        }
    }

    public void addPlanForDay(String dateStr, Plan plan){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDate date = LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toLocalDate();
            if(!plansForDay.containsKey(date)){
                plansForDay.put(date,new ArrayList<>());
            }
            plansForDay.get(date).add(plan);
            Collections.sort(plansForDay.get(date));
            updateLiveList();
        }
    }

    public void deletePlanForDay(String dateStr, Plan plan){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDate date = LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toLocalDate();
            if(plansForDay.containsKey(date)){
                plansForDay.get(date).remove(plan);
                updateLiveList();
            }
        }
    }

    public void editPlanForDay(String dateStr, Plan oldPlan,Plan plan){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDate date = LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toLocalDate();
            if(plansForDay.containsKey(date)){
                plansForDay.get(date).set(plansForDay.get(date).indexOf(oldPlan),plan);
                Collections.sort(plansForDay.get(date));
                updateLiveList();
            }
        }
    }

    public void filterPlans(String dateStr,String filter, boolean pastObl, String priority) {
        LocalDate date = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            date = LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toLocalDate();
            if(!plansForDay.containsKey(date))
                return;
        }
        List<Plan> filtered= plansForDay.get(date).stream().filter(plan -> {
                    if(pastObl)
                        return plan.getName().toLowerCase().contains(filter.toLowerCase());
                    return plan.getName().toLowerCase().contains(filter.toLowerCase()) && !plan.pastObligation();
                }

        ).collect(Collectors.toList());
        filtered.sort(new ComparatorByDate().thenComparing(new ComparatorByPriority(priority)));
        plansForDay.get(date).clear();
        plansForDay.get(date).addAll(filtered);
        updateLiveList();
    }
    public void filterCheck(String dateStr,boolean pastObl){
        LocalDate date = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            date = LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toLocalDate();
            if(!plansForDay.containsKey(date))
                return;
        }
        List<Plan> filtered= plansForDay.get(date).stream().filter(plan ->
            plan.pastObligation()==pastObl).collect(Collectors.toList());
        Collections.sort(filtered);
        plansForDay.get(date).clear();
        plansForDay.get(date).addAll(filtered);
        updateLiveList();
    }



    private void updateLiveList(){
        Map<LocalDate, List<Plan>> listToSubmit = new HashMap<>(plansForDay);
        plansLiveData.setValue(listToSubmit);
    }

    public Map<LocalDate, List<Plan>> getPlansForDay() {
        return plansForDay;
    }

    public void setPlansForDay(Map<LocalDate, List<Plan>> plansForDay) {
        this.plansForDay = plansForDay;
        updateLiveList();
    }

    public LiveData<Map<LocalDate, List<Plan>>> getPlansLiveData() {
        return plansLiveData;
    }




}
