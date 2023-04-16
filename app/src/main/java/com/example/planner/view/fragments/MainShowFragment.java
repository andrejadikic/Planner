package com.example.planner.view.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.planner.R;
import com.example.planner.models.Plan;
import com.example.planner.view.recycler.adapter.DetailsPlanAdapter;
import com.example.planner.view.recycler.adapter.PlanAdapter;
import com.example.planner.view.recycler.differ.PlanDiffer;
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
    //    private ShowPagerAdapter showPagerAdapter;
    private DetailsPlanAdapter showPagerAdapter;
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
        initViewPager(view);
        initObservers();
    }



    private void initViewPager(View view){
        ViewPager2 viewPager = view.findViewById(R.id.viewPager);

        //showPagerAdapter = new ShowPagerAdapter(getChildFragmentManager(),date,planViewModel.getPlansForDay().get(date));
        showPagerAdapter = new DetailsPlanAdapter(planViewModel.getPlansLiveData().getValue(),date,new PlanDiffer(), plan -> {
            FragmentTransaction transaction = createTransactionWithAnimation();
            transaction.replace(R.id.mainFragment, new MainShowFragment(date,plan));
            transaction.commit();
        }, plan -> {
            planViewModel.deletePlanForDay(date,plan);
        }, plan -> {
            FragmentTransaction transaction = createTransactionWithAnimation();
            transaction.replace(R.id.mainFragment, new EditPlanFragment(plan.getStartTime().toLocalDate(), plan));
            transaction.commit();
        });
        viewPager.setAdapter(showPagerAdapter);
        viewPager.postDelayed(new Runnable() {
            @Override
            public void run() {
                viewPager.setCurrentItem(showPagerAdapter.getPosition(plan),false);
            }
        },100);


        Toast.makeText(requireContext(),plan.getName(),Toast.LENGTH_SHORT).show();
//        setNavigation(plan);
//        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                if (positionOffset > 0.5) {
//                    // korisnik skroluje u desno
//                    viewPager.setCurrentItem(position + 1);
//                } else if (positionOffset < 0.5) {
//                    // korisnik skroluje u levo
//                    viewPager.setCurrentItem(position - 1);
//                }
//            }
//        });
//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                if (positionOffset > 0.5) {
//                    // korisnik skroluje u desno
//                    viewPager.setCurrentItem(position + 1);
//                } else if (positionOffset < 0.5) {
//                    // korisnik skroluje u levo
//                    viewPager.setCurrentItem(position - 1);
//                }
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                // ova metoda se poziva kada se promeni trenutna pozicija, mozete ovde dodati kod za azuriranje UI-a
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//                // ova metoda se poziva kada se promeni stanje ViewPager-a (pocetak skrolovanja, prestanak skrolovanja, itd.)
//            }
//        });
    }

    private void initObservers() {
        planViewModel.getPlansLiveData().observe(getViewLifecycleOwner(), new Observer<Map<LocalDate, List<Plan>>>() {
            @Override
            public void onChanged(Map<LocalDate, List<Plan>> newPlanMap) {
               // showPagerAdapter.setPlanList(newPlanMap.get(date)); // Postavite novu mapu u adapter kada se ona promeni
                showPagerAdapter.setPlanMap(newPlanMap);
            }
        });
    }

    public void setNavigation(Plan plan) {
        //viewPager.setCurrentItem(showPagerAdapter.getPosition(plan), false);
    }
    private FragmentTransaction createTransactionWithAnimation() {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        return transaction;
    }


}
