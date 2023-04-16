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
import androidx.viewpager.widget.ViewPager;

import com.example.planner.R;
import com.example.planner.app.StaticValues;
import com.example.planner.database.DBManager;
import com.example.planner.view.viewpager.PagerAdapter;
import com.example.planner.viewmodels.PlansRecyclerViewModel;
import com.example.planner.viewmodels.UserViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;

public class MainFragment extends Fragment {
    private ViewPager viewPager;
    private Context context;

    public MainFragment() {
        super(R.layout.fragment_bottom_navigation);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewPager(view);
        initNavigation(view);
    }

    private void initViewPager(View view){
        viewPager = view.findViewById(R.id.viewPager);
        viewPager.setAdapter(new PagerAdapter(getChildFragmentManager()));
    }
    private void initNavigation(View view) {
        ((BottomNavigationView)view.findViewById(R.id.bottomNavigation)).setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                // setCurrentItem metoda viewPager samo obavesti koji je Item trenutno aktivan i onda metoda getItem u adapteru setuje odredjeni fragment za tu poziciju
                case R.id.navigation_1: viewPager.setCurrentItem(PagerAdapter.FRAGMENT_1, false); break;
                case R.id.navigation_2: viewPager.setCurrentItem(PagerAdapter.FRAGMENT_2, false); break;
                case R.id.navigation_3: viewPager.setCurrentItem(PagerAdapter.FRAGMENT_3, false); break;
            }
            return true;
        });
    }


    public void goToFragment(Fragment fragment, int position, LocalDate date) {
        PagerAdapter pagerAdapter = ((PagerAdapter)viewPager.getAdapter());
        pagerAdapter.setDate(date);
        //Toast.makeText(context,((DailyPlanFragment)((PagerAdapter) viewPager.getAdapter()).getItem(PagerAdapter.FRAGMENT_2)).getDate().toString(),Toast.LENGTH_SHORT).show();
        pagerAdapter.notifyDataSetChanged();

        viewPager.setCurrentItem(position, false);

    }
}
