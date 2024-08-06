package com.assets.binfinder.ui.Ifsc;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.assets.binfinder.util.Event;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class IfscViewModel extends AndroidViewModel {
    MutableLiveData<Event<String>> data = new MutableLiveData<>();
    RequestQueue requestQueue;
    @Inject
    public IfscViewModel(@NonNull Application application, @NonNull RequestQueue requestQueue) {
        super(application);
        this.requestQueue = requestQueue;
    }

    public void getIfsc(@NonNull String ifsc) {
        requestQueue.add(new StringRequest("https://ifsc.razorpay.com/" + ifsc, response -> data.postValue(new Event<>(response)), error -> data.postValue(new Event<>(null))));
    }


}
