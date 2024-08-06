package com.assets.binfinder.ui.randomuser;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.assets.binfinder.util.Event;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class RandomUserViewModel extends AndroidViewModel {
    private final RequestQueue requestQueue;
    SharedPreferences sharedPreferences;
    MutableLiveData<Event<String>> data = new MutableLiveData<>();

    @Inject
    public RandomUserViewModel(@NonNull Application application, @NonNull RequestQueue requestQueue, @NonNull SharedPreferences sharedPreferences) {
        super(application);
        this.sharedPreferences = sharedPreferences;
        this.requestQueue = requestQueue;
    }

    public void get(@NonNull String seed) {
        requestQueue.add(new StringRequest("https://randomuser.me/api/" + seed, response -> data.postValue(new Event<>(response)), error -> data.postValue(new Event<>(null))));
    }
}
