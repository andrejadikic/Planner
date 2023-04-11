package com.example.planner.app;

import android.app.Application;

import timber.log.Timber;

public class PlannerApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
    }
}
