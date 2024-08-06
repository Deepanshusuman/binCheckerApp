package com.assets.binfinder.ui.binlist;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.assets.binfinder.util.Event;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class BinlistViewModel extends ViewModel {


    @NonNull
    public MutableLiveData<Event<String>> liveData = new MutableLiveData<>();
    RequestQueue requestQueue;


    @Inject
    public BinlistViewModel(@NonNull RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    public void getData(@NonNull Long bin) {
        String url = "https://lookup.binlist.net/" + bin;
        requestQueue.add(new StringRequest(Request.Method.GET, url, response -> {
            liveData.postValue(new Event<>(response));

        }, error -> liveData.postValue(new Event<>(null))) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Accept-Version", "3");
                return params;
            }
        });
    }
}