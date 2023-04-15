package com.example.planner.viewmodels;

import android.annotation.SuppressLint;
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
        for(int i=-20;i<=300;i++){
            plansForDay.putIfAbsent(LocalDate.now().plusDays(i), new ArrayList<>());
        }
        Map<LocalDate, List<Plan>> plans=dbManager.findPlansForUser();
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

    public int colorForDate(LocalDate date){
        if(!plansForDay.containsKey(date))
            return 0;
        plansForDay.get(date).sort(new ComparatorByPriority("low"));
        return plansForDay.get(date).get(plansForDay.get(date).size()-1).getPriorityNumber();
    }



    private void updateLiveList(){
        //Map<LocalDate, List<Plan>> listToSubmit = new HashMap<>(plansForDay);
        plansLiveData.setValue(plansForDay);
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
