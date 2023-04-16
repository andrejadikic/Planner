package com.example.planner.viewmodels;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.SharedPreferences;
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

@SuppressLint("NewApi")
public class PlansRecyclerViewModel extends AndroidViewModel {
    private DBManager dbManager;
    private Map<LocalDate, List<Plan>> plansForDay;
    private MutableLiveData<Map<LocalDate, List<Plan>>> plansLiveData;

    @SuppressLint("NewApi")
    public PlansRecyclerViewModel(@NonNull Application application) {
        super(application);
        dbManager = new DBManager(getApplication());
        dbManager.open();
        plansLiveData = new MutableLiveData<>();
        plansForDay = new HashMap<>();
        fetch();
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void fetch(){
        plansForDay=dbManager.findPlansForUser();
        for(int i=-20;i<=300;i++){
            plansForDay.putIfAbsent(LocalDate.now().plusDays(i), new ArrayList<>());
        }
        sort();
        updateLiveList();
    }

    private void sort(){
        for(LocalDate date:plansForDay.keySet()){
            plansForDay.get(date).sort(new ComparatorByPriority("low").thenComparing(new ComparatorByDate()));
        }
    }

    public boolean addPlanForDay(LocalDate date, LocalDateTime start, LocalDateTime end, String title, String details, String priority){
        SharedPreferences sp = getApplication().getSharedPreferences(StaticValues.USER_SHARED_PREF,MODE_PRIVATE);
        long userId = sp.getLong(StaticValues.ID, 0);
        if(!plansForDay.containsKey(date)){
            plansForDay.put(date,new ArrayList<>());
        }
        if(!checkTime(date,start,end))
            return false;
        Plan plan = dbManager.createPlan(userId,title,priority.toLowerCase(),start,end,details);
        plansForDay.get(date).add(plan);
        plansForDay.get(date).sort(new ComparatorByPriority("low").thenComparing(new ComparatorByDate()));
        updateLiveList();
        return true;
    }

    private boolean checkTime(LocalDate date, LocalDateTime start, LocalDateTime end) {
        List<Plan> plans = plansForDay.get(date);
        for(Plan plan:plans){
            if(start.isBefore(plan.getEndTime()) && plan.getStartTime().isBefore(end))
                return false;
        }
        return true;
    }
    private boolean checkTime(LocalDate date, LocalDateTime start, LocalDateTime end, Plan oldPlan) {
        List<Plan> plans = plansForDay.get(date);
        for(Plan plan:plans){
            if(!oldPlan.equals(plan) && start.isBefore(plan.getEndTime()) && plan.getStartTime().isBefore(end))
                return false;
        }
        return true;
    }

    public void deletePlanForDay(LocalDate date, Plan plan){
        if(plansForDay.containsKey(date)){
            dbManager.deletePlan(plan.getId());
            plansForDay.get(date).remove(plan);
            updateLiveList();
        }
    }

    public void editPlanForDay(LocalDate date, Plan oldPlan,Plan plan){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            if(plansForDay.containsKey(date)){
                plansForDay.get(date).set(plansForDay.get(date).indexOf(oldPlan),plan);
                plansForDay.get(date).sort(new ComparatorByPriority("low").thenComparing(new ComparatorByDate()));
                updateLiveList();
            }
        }
    }

    public boolean editPlanForDay(LocalDate date, Plan oldPlan, LocalDateTime start, LocalDateTime end, String title, String details, String priority){
        if(!plansForDay.containsKey(date) || !checkTime(date,start,end,oldPlan)){
            return false;
        }
        SharedPreferences sp = getApplication().getSharedPreferences(StaticValues.USER_SHARED_PREF,MODE_PRIVATE);
        long userId = sp.getLong(StaticValues.ID, 0);
        Plan plan = dbManager.editPlan(oldPlan.getId(),userId,title,priority.toLowerCase(),start,end,details);
        plansForDay.get(date).set(plansForDay.get(date).indexOf(oldPlan),plan);
        plansForDay.get(date).sort(new ComparatorByPriority("low").thenComparing(new ComparatorByDate()));
        updateLiveList();
        return true;
    }

    public void filterPlans(LocalDate date, String filter, boolean pastObl, String priority) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            if(!plansForDay.containsKey(date))
                return;
        }
        List<Plan> filtered = plansForDay.get(date).stream().filter(plan -> {
                    if(pastObl)
                        return plan.getName().toLowerCase().startsWith(filter.toLowerCase());
                    return plan.getName().toLowerCase().startsWith(filter.toLowerCase()) && !plan.pastObligation();
                }

        ).sorted(new ComparatorByPriority(priority).thenComparing(new ComparatorByDate())).collect(Collectors.toList());
        filtered.sort(new ComparatorByDate().thenComparing(new ComparatorByPriority(priority)));
        Map<LocalDate, List<Plan>> filteredMap = new HashMap<>();
        for (Map.Entry<LocalDate, List<Plan>> entry : plansForDay.entrySet()) {
            LocalDate key = entry.getKey();
            List<Plan> value = new ArrayList<>(entry.getValue());
            filteredMap.put(key, value);
        }
        filteredMap.get(date).clear();
        filteredMap.get(date).addAll(new ArrayList<>(filtered));
        filteredMap.get(date).sort(new ComparatorByDate().thenComparing(new ComparatorByPriority(priority)));
        plansLiveData.setValue(filteredMap);
    }

    public void filterCheck(LocalDate date, boolean pastObl){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            if(!plansForDay.containsKey(date))
                return;
        }
        List<Plan> filtered= plansForDay.get(date).stream().filter(plan ->
            plan.pastObligation()==pastObl).collect(Collectors.toList());
        filtered.sort(new ComparatorByPriority("low").thenComparing(new ComparatorByDate()));
        plansForDay.get(date).clear();
        plansForDay.get(date).addAll(filtered);
        updateLiveList();
    }

    public int colorForDate(LocalDate date){
        if(!plansForDay.containsKey(date))
            return 0;
        plansForDay.get(date).sort(new ComparatorByPriority("low"));
        return plansForDay.get(date).get(plansForDay.get(date).size()-1).getPriorityNumber();
    }



    private void updateLiveList(){
        Map<LocalDate, List<Plan>> deepCopy = new HashMap<>();
        for (Map.Entry<LocalDate, List<Plan>> entry : plansForDay.entrySet()) {
            LocalDate key = entry.getKey();
            List<Plan> value = new ArrayList<>(entry.getValue());
            deepCopy.put(key, value);
        }
        plansLiveData.setValue(deepCopy);
    }

    public Map<LocalDate, List<Plan>> getPlansForDay() {
        return plansForDay;
    }

    public void setPlansForDay(Map<LocalDate, List<Plan>> plansForDay) {
        this.plansForDay = plansForDay;
        updateLiveList();
    }



    public LiveData<Map<LocalDate, List<Plan>>> getPlansLiveData() {
        if (plansLiveData == null) {
            plansLiveData = new MutableLiveData<>();
            fetch();
        }
        return plansLiveData;
    }
}
