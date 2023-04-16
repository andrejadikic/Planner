package com.example.planner.view.viewpager;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.planner.models.Plan;
import com.example.planner.view.fragments.CalendarFragment;
import com.example.planner.view.fragments.DailyPlanFragment;
import com.example.planner.view.fragments.ProfileFragment;
import com.example.planner.view.fragments.ShowPlanFragment;

import java.time.LocalDate;
import java.util.List;

@SuppressLint("NewApi")
public class ShowPagerAdapter extends FragmentPagerAdapter {
    private List<Plan> planList;
    private LocalDate date;
    private boolean changed;

    public ShowPagerAdapter(@NonNull FragmentManager fm, LocalDate date,List<Plan> plans) {
        super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        planList = plans;
        this.date = date;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        return new ShowPlanFragment(date,planList.get(position));
    }

    @Override
    public int getCount() {
        return planList.size();
    }



    public void setDate(LocalDate date) {
        this.date = date;
        changed=true;
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        if (changed){
            changed=false;
            return POSITION_NONE;
        }
        return POSITION_UNCHANGED;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setPlanList(List<Plan> planList) {
        this.planList = planList;
        changed = true;
        notifyDataSetChanged();
    }
}
