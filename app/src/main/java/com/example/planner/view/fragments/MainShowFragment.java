package com.example.planner.view.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.example.planner.R;
import com.example.planner.models.Plan;
import com.example.planner.view.viewpager.PagerAdapter;
import com.example.planner.view.viewpager.ShowPagerAdapter;
import com.example.planner.viewmodels.PlansRecyclerViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@SuppressLint("NewApi")
public class MainShowFragment extends Fragment {

    private PlansRecyclerViewModel planViewModel;
    private ViewPager viewPager;
    private ShowPagerAdapter showPagerAdapter;
    private Context context;

    private LocalDate date;
    private Plan plan;

    public MainShowFragment(LocalDate date, Plan plan) {
        super(R.layout.fragment_swipe);
        this.date=date;
        this.plan=plan;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        planViewModel = new ViewModelProvider(requireActivity(), new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication())).get(PlansRecyclerViewModel.class);
        planViewModel.fetch();
        Toast.makeText(requireContext(),plan.getName(),Toast.LENGTH_SHORT);
        initViewPager(view);
        initObservers();
    }



    private void initViewPager(View view){
        viewPager = view.findViewById(R.id.viewPager);
        showPagerAdapter = new ShowPagerAdapter(getChildFragmentManager(),date,planViewModel.getPlansForDay().get(date));
        viewPager.setAdapter(showPagerAdapter);
        Toast.makeText(requireContext(),plan.getName(),Toast.LENGTH_SHORT);
        setNavigation(plan);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (positionOffset > 0.5) {
                    // korisnik skroluje u desno
                    viewPager.setCurrentItem(position + 1);
                } else if (positionOffset < 0.5) {
                    // korisnik skroluje u levo
                    viewPager.setCurrentItem(position - 1);
                }
            }

            @Override
            public void onPageSelected(int position) {
                // ova metoda se poziva kada se promeni trenutna pozicija, mozete ovde dodati kod za azuriranje UI-a
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // ova metoda se poziva kada se promeni stanje ViewPager-a (pocetak skrolovanja, prestanak skrolovanja, itd.)
            }
        });
    }

    private void initObservers() {
        planViewModel.getPlansLiveData().observe(getViewLifecycleOwner(), new Observer<Map<LocalDate, List<Plan>>>() {
            @Override
            public void onChanged(Map<LocalDate, List<Plan>> newPlanMap) {
                showPagerAdapter.setPlanList(newPlanMap.get(date)); // Postavite novu mapu u adapter kada se ona promeni
            }
        });
    }

    public void setNavigation(Plan plan) {
        viewPager.setCurrentItem(showPagerAdapter.getPosition(plan), false);
    }


}
