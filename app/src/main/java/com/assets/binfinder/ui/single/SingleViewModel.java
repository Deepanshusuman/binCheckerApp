package com.assets.binfinder.ui.single;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.preference.PreferenceManager;

import com.assets.binfinder.model.Database;
import com.assets.binfinder.model.bin.Bin;
import com.assets.binfinder.model.fav.Fav;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SingleViewModel extends ViewModel {
    @NonNull
    public MutableLiveData<Bin> singleBin = new MutableLiveData<>();
    Application application;
    Database database;
    SharedPreferences preferenceManager;

    @Inject
    public SingleViewModel(@NonNull Application application, @NonNull Database database) {

        this.database = database;
        this.application = application;
        this.preferenceManager = PreferenceManager.getDefaultSharedPreferences(application.getApplicationContext());

    }


    public void addBinToFav(@NonNull Long bin) {
        new Thread(() -> {
            database.favDao().insert(new Fav(bin));
        }).start();
    }

    public void removeBinFromFav(@NonNull Long bin) {
        new Thread(() -> {
            database.favDao().delete(bin);
        }).start();
    }

    public void getBinDetails(@NonNull Long bin) {
        new Thread(() -> singleBin.postValue(database.binDao().getBinDetails(bin))).start();
    }


    public boolean isFavorite(long bin) {
        return database.favDao().hasFav(bin);
    }
}