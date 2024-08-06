package com.assets.binfinder.ui.seon;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.preference.PreferenceManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.assets.binfinder.util.Event;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SeonViewModel extends AndroidViewModel {
    MutableLiveData<Event<JSONObject>> liveData = new MutableLiveData<>();

    RequestQueue requestQueue;

    Application application;
    SharedPreferences sharedPreferences;

    @Inject
    public SeonViewModel(RequestQueue requestQueue, Application application) {
        super(application);
        this.application = application;
        this.requestQueue = requestQueue;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application);
    }


    public void getData(@NonNull Long bin) {

        // if seon_api_key :EditTextPreference is not empty then do request
        var usekey = sharedPreferences.getBoolean("useSeonApiKey", false);
        var key = sharedPreferences.getString("seon_api_key", null);
        var incognito = sharedPreferences.getBoolean("incognitoMode", false);
        if (usekey) {
            String url = "https://api.seon.io/SeonRestService/bin-api/v1.0/" + bin;
            HashMap<String, String> headers = new HashMap<>();
            headers.put("X-API-KEY", key);
            headers.put("Cache-Control", "no-cache");
            headers.put("Content-Type", "application/json");
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
                try {
                    liveData.setValue(new Event<>(response.put("bin", bin)));
                } catch (JSONException e) {
                    e.printStackTrace();
                    liveData.setValue(new Event<>(response));
                }
            }, error -> {
                liveData.setValue(new Event<>(null));
            }) {
                @Override
                public HashMap<String, String> getHeaders() {
                    return headers;
                }
            };
            requestQueue.add(jsonObjectRequest);
        } else {
            liveData.postValue(new Event<>(null));

        }
    }


}