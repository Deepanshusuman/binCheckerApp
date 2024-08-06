package com.assets.binfinder;

import android.app.Application;

import androidx.annotation.NonNull;

import com.google.android.material.color.DynamicColors;

import javax.inject.Inject;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class MainApplication extends Application {



    @Override
    public void onCreate() {
        super.onCreate();
        DynamicColors.applyToActivitiesIfAvailable(this);

    }


}
