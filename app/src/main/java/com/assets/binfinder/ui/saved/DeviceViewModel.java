package com.assets.binfinder.ui.saved;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.assets.binfinder.model.Database;
import com.assets.binfinder.model.device.Device;
import com.assets.binfinder.model.fav.Fav;
import com.assets.binfinder.util.Event;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class DeviceViewModel extends AndroidViewModel {

    MutableLiveData<Event<List<Device>>> favorites = new MutableLiveData<>();
    Database database;

    @Inject
    public DeviceViewModel(@NonNull Application application, @NonNull Database database) {
        super(application);
        this.database = database;
    }


    public void addBinToDevice(@NonNull Long bin) {
        new Thread(() -> {
            database.deviceDao().insert(new Device(bin));
        }).start();
    }

    public void removeBinFromDevice(@NonNull Long bin) {
        new Thread(() -> {
            database.deviceDao().delete(bin);
        }).start();
    }

    public void getDevice() {
        new Thread(() -> {
            favorites.postValue(new Event<>(database.deviceDao().getAll()));
        }).start();
    }
}
