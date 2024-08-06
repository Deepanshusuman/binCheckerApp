package com.assets.binfinder.ui.saved;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.assets.binfinder.model.Database;
import com.assets.binfinder.model.fav.Fav;
import com.assets.binfinder.util.Event;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class FavoriteViewModel extends AndroidViewModel {
    MutableLiveData<Event<List<Fav>>> favorites = new MutableLiveData<>();
    Database database;
    Application application;

    @Inject
    public FavoriteViewModel(@NonNull Application application, @NonNull Database database) {
        super(application);
        this.database = database;
        this.application = application;
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

    public void getFav() {
        new Thread(() -> {
            favorites.postValue(new Event<>(database.favDao().getAll()));
        }).start();
    }
}

