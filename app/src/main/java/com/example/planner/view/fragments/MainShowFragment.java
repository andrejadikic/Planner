package com.example.planner.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.planner.R;
import com.example.planner.view.viewpager.PagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalDate;

public class MainShowFragment extends Fragment {
    private ViewPager viewPager;
    private Context context;

    public MainShowFragment() {
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
        viewPager.setAdapter(new PagerAdapter(requireActivity().getSupportFragmentManager()));
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
