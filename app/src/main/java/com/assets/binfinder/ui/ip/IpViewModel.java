package com.assets.binfinder.ui.ip;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.assets.binfinder.util.Event;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;


@HiltViewModel
public class IpViewModel extends ViewModel {

    public MutableLiveData<Event<String>> ip_info = new MutableLiveData<>();
    public MutableLiveData<Event<String>> geo_info = new MutableLiveData<>();
    public MutableLiveData<Event<String>> asn_info = new MutableLiveData<>();
    public MutableLiveData<Event<String>> vulnerability_info = new MutableLiveData<>();
    public MutableLiveData<Event<String>> spam_info = new MutableLiveData<>();
    public MutableLiveData<Event<String>> datacenter_info = new MutableLiveData<>();
    RequestQueue requestQueue;

    @Inject
    public IpViewModel(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }


    public void getIpData(String ip) {
        requestQueue.add(new StringRequest(Request.Method.GET,
                "http://ip-api.com/json/" + ip,
                response -> ip_info.postValue(new Event<>(response)),
                error -> ip_info.postValue(new Event<>(null))));
    }

    public void getSpamInfo(String ip) {
        requestQueue.add(new StringRequest(Request.Method.GET,
                "https://api.stopforumspam.org/api?json&ip=" + ip,
                response -> spam_info.postValue(new Event<>(response)),
                error -> spam_info.postValue(new Event<>(null))));
    }


    public void getDatacenterInfo(String ip) {
        requestQueue.add(new StringRequest(Request.Method.GET,
                "https://api.incolumitas.com/datacenter?ip=" + ip,
                response -> datacenter_info.postValue(new Event<>(response)),
                error -> datacenter_info.postValue(new Event<>(null))));
    }

    public void getVulInfo(String ip) {
        requestQueue.add(new StringRequest(Request.Method.GET,
                "https://internetdb.shodan.io/" + ip,
                response -> vulnerability_info.postValue(new Event<>(response)),
                error -> vulnerability_info.postValue(new Event<>(null))));
    }

}