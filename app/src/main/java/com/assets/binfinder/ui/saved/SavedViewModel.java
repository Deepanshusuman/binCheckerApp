package com.assets.binfinder.ui.saved;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.assets.binfinder.model.Database;
import com.assets.binfinder.model.fav.FavDao;
import com.assets.binfinder.model.savedlist.SavedList;
import com.assets.binfinder.model.savedlist.SavedlistDao;
import com.assets.binfinder.util.Event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;


@HiltViewModel
public class SavedViewModel extends ViewModel {
    @NonNull
    public MutableLiveData<Event<List<SavedList>>> savedLists = new MutableLiveData<>();
    public MutableLiveData<List<SavedList>> singleSavedLists = new MutableLiveData<>();
    @NonNull
    public MutableLiveData<Event<SavedList>> savedListInfo = new MutableLiveData<>();
    SavedlistDao savedlistDao;
    FavDao favDao;

    Application application;

    @Inject
    public SavedViewModel(@NonNull Application application, @NonNull Database database) {
        this.application = application;
        this.savedlistDao = database.savedListDao();
        this.favDao = database.favDao();
    }


    public void undoDelete(@NonNull SavedList savedList) {
        new Thread(() -> {
            savedlistDao.insert(savedList);
            savedLists.postValue(new Event<>(savedlistDao.getAll()));
        }).start();
    }

    public void undoItemDeleted(@NonNull SavedList savedList) {
        new Thread(() -> {
            savedlistDao.update(savedList);
            savedListInfo.postValue(new Event<>(savedlistDao.findByUuid(savedList.uuid)));
        }).start();
    }

    public void addNameToSavedList(@NonNull String name) {
        new Thread(() -> {
            SavedList savedList = new SavedList();
            savedList.name = name;
            savedList.uuid = UUID.randomUUID();
            savedList.bins = new ArrayList<>();
            savedlistDao.insert(savedList);


            List<SavedList> list = savedlistDao.getAll();
            Collections.reverse(list);
            savedLists.postValue(new Event<>((list)));
        }).start();

    }


    public void addBinToSavedList(@NonNull UUID uuid, @NonNull Long bin) {
        new Thread(() -> {
            SavedList savedList = savedlistDao.findByUuid(uuid);
            if (!(savedList.bins.contains(bin))) {
                savedList.bins.add(bin);
                savedlistDao.update(savedList);

            }


        }).start();
    }


    public void deleteFromSavedList(@NonNull UUID uuid, @NonNull Long bin) {
        new Thread(() -> {
            SavedList savedList = savedlistDao.findByUuid(uuid);
            savedList.bins.remove(bin);
            savedlistDao.update(savedList);

        }).start();
    }

    public void deleteNameFromSavedList(@NonNull UUID uuid) {
        new Thread(() -> {
            SavedList savedList = new SavedList();
            savedList.uuid = uuid;
            savedlistDao.delete(savedList);

        }).start();
    }

    public void getSavedList() {
        new Thread(() -> savedLists.postValue(new Event<>(savedlistDao.getAll()))).start();
    }

    public void getSingleSavedList() {
        new Thread(() -> singleSavedLists.postValue(savedlistDao.getAll())).start();
    }


    public void getSavedList(@NonNull UUID uuid) {
        new Thread(() -> savedListInfo.postValue(new Event<>(savedlistDao.findByUuid(uuid)))).start();
    }


    public void editNameToSavedList(@NonNull UUID uuid, @NonNull String name) {
        new Thread(() -> {
            SavedList savedList = savedlistDao.findByUuid(uuid);
            savedList.name = name;
            savedlistDao.update(savedList);

            savedListInfo.postValue(new Event<>(savedList));
        }).start();
    }
}