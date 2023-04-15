package com.example.planner.view.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.planner.R;
import com.example.planner.app.StaticValues;
import com.example.planner.database.DBManager;
import com.example.planner.models.User;

import java.util.Locale;

public class ProfileFragment extends Fragment {
    private Context context;
    private SharedPreferences sp;
    private DBManager dbManager;

    private TextView usernameTxt;
    private TextView emailTxt;
    private Button changePassBtn;
    private Button logoutBtn;
    private String newPassword;
    private String newPasswordConf;
    private String password;

    public ProfileFragment() {
        super(R.layout.fragment_profile);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @SuppressLint("NewApi")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbManager = new DBManager(getContext());
        dbManager.open();
        sp = context.getSharedPreferences(StaticValues.USER_SHARED_PREF, MODE_PRIVATE);
        long id = sp.getLong(StaticValues.ID, -1);
        password = sp.getString(StaticValues.PASSWORD,"");
        Toast.makeText(context, "Id" + id, Toast.LENGTH_SHORT).show();
        init(view);
    }

    private void init(View view) {
        initWidgets(view);
        loadData();
        initActions(view);
    }

    private void initWidgets(View view) {
        usernameTxt = view.findViewById(R.id.username);
        emailTxt = view.findViewById(R.id.email);
        changePassBtn = view.findViewById(R.id.changePass);
        logoutBtn = view.findViewById(R.id.logout);
    }

    private void loadData() {
        usernameTxt.setText(sp.getString(StaticValues.USERNAME,"username"));
        emailTxt.setText(sp.getString(StaticValues.EMAIL,"email"));
    }

    @SuppressLint("NewApi")
    private void initActions(View view) {
        changePassBtn.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle(R.string.change_password);


// Set up the input
            final EditText input = new EditText(context);
            final EditText inputConf = new EditText(context);
            final LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            inputConf.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            layout.addView(input);
            layout.addView(inputConf);
            builder.setView(layout);

// Set up the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    newPassword = input.getText().toString();
                    newPasswordConf = inputConf.getText().toString();
                    if(newPasswordConf.equals(newPassword) && User.validatePassword(newPassword) && !newPassword.equals(password)){
                        dbManager.changePassword(sp.getString(StaticValues.USERNAME,""), newPassword);
                        sp.edit().putString(StaticValues.PASSWORD, newPassword).apply();
                        Toast.makeText(context,"Password changed", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context,"Password is not changed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
        });

        logoutBtn.setOnClickListener(v -> {
            sp.edit().remove(StaticValues.ID).apply();
            sp.edit().remove(StaticValues.EMAIL).apply();
            sp.edit().remove(StaticValues.USERNAME).apply();
            sp.edit().remove(StaticValues.PASSWORD).apply();
            sp.edit().putBoolean(StaticValues.LOGGED,false).apply();
            FragmentTransaction transaction = createTransactionWithAnimation();
            transaction.replace(R.id.mainFragment, new LoginFragment());
            transaction.commit();
        });
    }


    public FragmentTransaction createTransactionWithAnimation() {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        return transaction;
    }
}
