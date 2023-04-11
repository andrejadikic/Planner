package com.example.planner.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.planner.models.User;

public class UserViewModel extends ViewModel {
    private final MutableLiveData<User> userInputLiveData = new MutableLiveData<>();

    public LiveData<User> getUserInput() {
        return userInputLiveData;
    }

    public void storeUserInput(User userInput) {
        userInputLiveData.setValue(userInput);
    }
}
