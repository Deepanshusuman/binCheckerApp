package com.assets.binfinder;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.assets.binfinder.model.Database;
import com.assets.binfinder.model.history.History;
import com.assets.binfinder.util.Event;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MainViewModel extends AndroidViewModel {


    @NonNull
    public MutableLiveData<Event<List<History>>> historyList = new MutableLiveData<>();
    @NonNull
    public SharedPreferences sharedPreferences;

    Application application;
    Database database;
    Calendar calendar = Calendar.getInstance();



    @Inject
    public MainViewModel(@NonNull SharedPreferences sharedPreferences, @NonNull Application application, @NonNull Database database) {
        super(application);
        this.sharedPreferences = sharedPreferences;
        this.database = database;
        this.application = application;
        updateList();
    }


    @NonNull
    public String setGreeting() {
        int timeOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        if (timeOfDay < 12) {
            return "Good Morning";
        } else if (timeOfDay < 16) {
            return "Good Evening";
        } else {
            return "Good Evening";
        }
    }


    public void deleteAll() {
        new Thread(() -> {
            database.savedListDao().deleteAll();
            database.favDao().deleteAll();
        }).start();
    }


    public void clearHistory() {
        new Thread(() -> {
            database.historyDao().deleteAll();
            historyList.postValue(new Event<>(new ArrayList<>()));

        }).start();
    }

    public void getHistory() {
        new Thread(() -> {
            List<History> list = database.historyDao().getAll();
            Collections.reverse(list);
            historyList.postValue(new Event<>(list));
        }).start();
    }


    public void addHistory(@NonNull Long bin, @NonNull Long at) {
        new Thread(() -> {
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, yyyy", Locale.getDefault());
            String format = sdf.format(new Date(at));
            History history = database.historyDao().findByDate(format);
            if (history == null) {
                ArrayList<Long> arrayList = new ArrayList<>();
                arrayList.add(bin);
                database.historyDao().insert(new History(format, arrayList));
            } else {
                if (!history.bins.contains(bin)) {
                    history.bins.add(0, bin);
                    database.historyDao().update(history);
                }
            }
        }).start();
    }


    @NonNull
    public ArrayList<String> getNetwork() {
        try {
            JSONArray jsonArray = new JSONArray(sharedPreferences.getString("networks", "[]"));
            ArrayList<String> network = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                network.add(jsonArray.getString(i));
            }
            return network;
        } catch (Exception e) {
            return new ArrayList<>(Arrays.asList(getApplication().getResources().getStringArray(R.array.arrNetwork)));
        }
    }

    @NonNull
    public ArrayList<String> getTypes() {
        try {
            JSONArray jsonArray = new JSONArray(sharedPreferences.getString("types", "[]"));
            ArrayList<String> types = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                types.add(jsonArray.getString(i));
            }
            return types;
        } catch (Exception e) {
            return new ArrayList<>(Arrays.asList(getApplication().getResources().getStringArray(R.array.arrType)));
        }
    }


    @NonNull
    public ArrayList<String> getProduct() {
        try {
            JSONArray jsonArray = new JSONArray(sharedPreferences.getString("products", "[]"));
            ArrayList<String> product = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                product.add(jsonArray.getString(i));
            }
            return product;
        } catch (Exception e) {
            return new ArrayList<>(Arrays.asList(getApplication().getResources().getStringArray(R.array.arrProduct)));
        }
    }


    public void addNetwork(@NonNull List<String> network) {
        sharedPreferences.edit().remove("networks").apply();
        sharedPreferences.edit().putString("networks", new JSONArray(network).toString()).apply();
    }

    public void addProduct(@NonNull List<String> product) {
        sharedPreferences.edit().remove("products").apply();
        sharedPreferences.edit().putString("products", new JSONArray(product).toString()).apply();
    }

    public void addTypes(@NonNull List<String> types) {
        sharedPreferences.edit().remove("types").apply();
        sharedPreferences.edit().putString("types", new JSONArray(types).toString()).apply();
    }
    void  updateList(){
        new Thread(() -> {
            addNetwork(database.binDao().getAllNetworks());
            addProduct(database.binDao().getAllProducts());
            addTypes(database.binDao().getAllTypes());
        }).start();

    }
}