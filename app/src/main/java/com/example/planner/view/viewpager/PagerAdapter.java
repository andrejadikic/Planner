package com.example.planner.view.viewpager;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.planner.view.fragments.CalendarFragment;
import com.example.planner.view.fragments.DailyPlanFragment;
import com.example.planner.view.fragments.ProfileFragment;

import java.time.LocalDate;

@SuppressLint("NewApi")
public class PagerAdapter extends FragmentStatePagerAdapter {

    private final int ITEM_COUNT = 3;
    public static final int FRAGMENT_1 = 0;
    public static final int FRAGMENT_2 = 1;
    public static final int FRAGMENT_3 = 2;
    private LocalDate date;
    private Boolean changed;


    public PagerAdapter(@NonNull FragmentManager fm) {
        super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        date = LocalDate.now();
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position) {
            case FRAGMENT_1: fragment = new CalendarFragment(); break;
            case FRAGMENT_2: fragment = new DailyPlanFragment(date); break;
            default: fragment = new ProfileFragment(); break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return ITEM_COUNT;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch(position){
            case FRAGMENT_1: return "1";
            case FRAGMENT_2: return "2";
            default: return "3";
        }
    }

    public void setDate(LocalDate date) {
        this.date = date;
        changed=true;
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        if (object instanceof DailyPlanFragment && changed){
            changed=false;
            return POSITION_NONE;
        }
        return POSITION_UNCHANGED;
    }

    public LocalDate getDate() {
        return date;
    }
}
