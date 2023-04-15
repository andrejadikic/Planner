package com.example.planner.view.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.planner.R;
import com.example.planner.app.MainActivity;
import com.example.planner.app.StaticValues;
import com.example.planner.database.DBManager;
import com.example.planner.models.User;
import com.example.planner.viewmodels.UserViewModel;

import java.util.Objects;

public class LoginFragment extends Fragment {
    private UserViewModel userViewModel;
    private Context context;
    private DBManager dbManager;
    private Button loginBtn;
    private EditText emailTxt;
    private EditText usernameTxt;
    private EditText passwordTxt;

    public LoginFragment() {
        super(R.layout.fragment_login);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        dbManager = new DBManager(getContext());
        dbManager.open();
        init(view);
    }

    private void init(View view){
        initWidgets(view);
        initActions(view);
    }

    private void initWidgets(View view) {
        loginBtn = view.findViewById(R.id.loginBtn);
        emailTxt = view.findViewById(R.id.emailTxt);
        usernameTxt = view.findViewById(R.id.usernameTxt);
        passwordTxt = view.findViewById(R.id.passwordTxt);
    }

    private void initActions(View view) {
        loginBtn.setOnClickListener(v->{
            String email = emailTxt.getText().toString();
            String username = usernameTxt.getText().toString();
            String password = passwordTxt.getText().toString();

            SharedPreferences sp = context.getSharedPreferences(StaticValues.USER_SHARED_PREF,MODE_PRIVATE);
            if(!User.validatePassword(password) || !Patterns.EMAIL_ADDRESS.matcher(email).matches() || username.isEmpty()){
                Toast.makeText(context, "Wrong input", Toast.LENGTH_LONG).show();
                return;
            }
            // registruj se na bazu
            User user = dbManager.loginUser(username,password);
            if(user==null){
                Toast.makeText(context, "Wrong password or username", Toast.LENGTH_LONG).show();
                return;
            }
            sp.edit().putString(StaticValues.EMAIL,user.getEmail()).apply();
            sp.edit().putBoolean(StaticValues.LOGGED,true).apply();
            sp.edit().putString(StaticValues.USERNAME,username).apply();
            sp.edit().putString(StaticValues.PASSWORD,password).apply();
            sp.edit().putLong(StaticValues.ID,user.getId()).apply();
            userViewModel.storeUserInput(user);

            FragmentTransaction transaction = createTransactionWithAnimation();
            transaction.replace(R.id.mainFragment, new MainFragment(), MainActivity.MainFragmentTag);
            transaction.commit();


        });
    }

    private void addPlan(){
        SharedPreferences sp = context.getSharedPreferences(StaticValues.USER_SHARED_PREF,MODE_PRIVATE);
        long userId = sp.getLong(StaticValues.ID, 0);
    }



    private FragmentTransaction createTransactionWithAnimation() {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        // Dodajemo transakciju na backstack kako bi se pritisokm na back transakcija rollback-ovala
        transaction.addToBackStack(null);
        return transaction;
    }


}
