package com.example.planner.app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.planner.R;
import com.example.planner.database.DBManager;
import com.example.planner.models.User;
import com.example.planner.view.fragments.CalendarFragment;
import com.example.planner.view.fragments.LoginFragment;
import com.example.planner.view.fragments.MainFragment;
import com.example.planner.view.fragments.SecondFragment;
import com.example.planner.viewmodels.SplashViewModel;

public class MainActivity extends AppCompatActivity {
    public static final  String MainFragmentTag = "firstFragment";
    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbManager = new DBManager(this);
        dbManager.open();

        SplashViewModel splashViewModel = new ViewModelProvider(this).get(SplashViewModel.class);
        // Handle the splash screen transition.
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        splashScreen.setKeepOnScreenCondition(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return false;
        });
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        initFragment();
    }


    private void initFragment() {
        FragmentTransaction transaction = createTransactionWithAnimation();
        SharedPreferences sp = getSharedPreferences(StaticValues.USER_SHARED_PREF,MODE_PRIVATE);
        if(sp.getBoolean(StaticValues.LOGGED,false)){
            transaction.add(R.id.mainFragment, new MainFragment(), MainFragmentTag);
        }else{
            transaction.add(R.id.mainFragment, new LoginFragment());
        }
        transaction.commit();
    }

    private FragmentTransaction createTransactionWithAnimation() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // Dodajemo animaciju kada se fragment doda
        //transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
        // Dodajemo transakciju na backstack kako bi se pritisokm na back transakcija rollback-ovala
        transaction.addToBackStack(null);
        return transaction;
    }


}