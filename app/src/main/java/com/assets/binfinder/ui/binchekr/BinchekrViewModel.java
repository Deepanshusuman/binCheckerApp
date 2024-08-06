package com.assets.binfinder.ui.binchekr;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelKt;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.PagingLiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.assets.binfinder.BinOuterClass;
import com.assets.binfinder.model.Database;
import com.assets.binfinder.model.bin.Bin;
import com.assets.binfinder.util.Event;
import com.assets.binfinder.util.SaveState;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class BinchekrViewModel extends AndroidViewModel {
    @NonNull
    public MutableLiveData<Event<BinOuterClass.TypeResponse>> filtNerData = new MutableLiveData<>();
    @NonNull
    public MutableLiveData<Event<List<String>>> issuers = new MutableLiveData<>();
    public MutableLiveData<Event<List<String>>> typesData = new MutableLiveData<>();
    public MutableLiveData<Event<List<String>>> networksData = new MutableLiveData<>();
    public MutableLiveData<Event<List<String>>> productsData = new MutableLiveData<>();
    public MutableLiveData<Event<List<String>>> countriesData = new MutableLiveData<>();

    LiveData<PagingData<Bin>> binList = new MutableLiveData<>();
    PagingConfig config;
    SaveState state;


    Database database;

    @Inject
    BinchekrViewModel(@NonNull Application application, @NonNull SaveState saveState, @NonNull Database database) {
        super(application);
        this.database = database;
        this.state = saveState;
        config = new PagingConfig(50, 100, true, 100);
    }

    public void findIssuer(@NonNull BinOuterClass.SearchRequest.Builder searchRequest) {
        String query = "Select IFNULL(issuer,'NULL') from bins where 1=1";
        if (searchRequest.getNetwork() != null && !searchRequest.getNetwork().isEmpty()) {
            if (searchRequest.getNetwork().equals("NULL")) {
                query += " AND network IS NULL";
            } else {
                query += " AND network = '" + searchRequest.getNetwork() + "'";
            }

        }
        if (searchRequest.getProductName() != null && !searchRequest.getProductName().isEmpty()) {
            if (searchRequest.getProductName().equals("NULL")) {
                query += " AND product_name IS NULL";
            } else {
                query += " AND product_name = '" + searchRequest.getProductName() + "'";
            }
        }
        if (searchRequest.getType() != null && !searchRequest.getType().isEmpty()) {

            if (searchRequest.getType().equals("NULL")) {
                query += " AND type IS NULL";
            } else {
                query += " AND type = '" + searchRequest.getType() + "'";
            }
        }
        if (searchRequest.getCountry() != null && !searchRequest.getCountry().isEmpty()) {
            if (searchRequest.getCountry().equals("NULL")) {
                query += " AND country IS NULL";
            } else {
                query += " AND country = '" + searchRequest.getCountry() + "'";
            }
        }


        if (searchRequest.getBin() != 0) {
            query += " AND start LIKE '" + searchRequest.getBin() + "%'";
        }


        if (searchRequest.getIssuer() != null && !searchRequest.getIssuer().isEmpty()) {
            if (searchRequest.getIssuer().equals("NULL")) {
                query += " AND issuer IS NULL";
            } else {
                query += " AND issuer LIKE '" + searchRequest.getIssuer() + "%' group by issuer";
            }
        }
        Log.d("TAG", "findIssuer: " + query);
        String finalQuery = query;
        new Thread(() -> {
            SimpleSQLiteQuery qr = new SimpleSQLiteQuery(finalQuery);
            var res = database.binDao().getFilteredList(qr);
            Log.d("TAG", "findIssuer: " + res.size());
            Log.d("TAG", "findIssuer: " + res);
            issuers.postValue(new Event<>(res));
        }).start();
    }

    public void find_bin(@NonNull BinOuterClass.SearchRequest.Builder searchRequest) {
        String query = "Select * from bins where 1=1";
        Log.d("TAG", "find_bin: "+searchRequest.getNetwork());
        if (searchRequest.getNetwork() != null && !searchRequest.getNetwork().isEmpty()) {
            if (searchRequest.getNetwork().equals("NULL")) {
                query += " AND network IS NULL";
            } else {
                query += " AND network = '" + searchRequest.getNetwork() + "'";
            }

        }
        if (searchRequest.getProductName() != null && !searchRequest.getProductName().isEmpty()) {
            if (searchRequest.getProductName().equals("NULL")) {
                query += " AND product_name IS NULL";
            } else {
                query += " AND product_name = '" + searchRequest.getProductName() + "'";
            }
        }
        if (searchRequest.getType() != null && !searchRequest.getType().isEmpty()) {

            if (searchRequest.getType().equals("NULL")) {
                query += " AND type IS NULL";
            } else {
                query += " AND type = '" + searchRequest.getType() + "'";
            }
        }
        if (searchRequest.getCountry() != null && !searchRequest.getCountry().isEmpty()) {
            if (searchRequest.getCountry().equals("NULL")) {
                query += " AND country IS NULL";
            } else {
                query += " AND country = '" + searchRequest.getCountry() + "'";
            }
        }

        if (searchRequest.getIssuer() != null && !searchRequest.getIssuer().isEmpty()) {
            if (searchRequest.getIssuer().equals("NULL")) {
                query += " AND issuer IS NULL";
            } else {
                query += " AND issuer LIKE '" + searchRequest.getIssuer() + "%'";
            }
        }

        if (searchRequest.getBin() != 0) {
            query += " AND start LIKE '" + searchRequest.getBin() + "%'";
        }

        String finalQuery = query;
        Log.d("TAG", "find_bin: ");
        binList = PagingLiveData.cachedIn(PagingLiveData.getLiveData(new Pager<>(config, () -> new BinchekrPagingSource(database.binDao(), finalQuery))), ViewModelKt.getViewModelScope(this));
    }

    public void updateFilter(@NonNull BinOuterClass.SearchRequest.Builder searchRequest) {
        String typeQuery = "Select IFNULL(type,'NULL') from bins where 1=1";
        String productQuery = "Select IFNULL(product_name,'NULL') from bins where 1=1";
        String networkQuery = "Select IFNULL(network,'NULL') from bins where 1=1";
        String countryQuery = "Select IFNULL(country,'NULL') from bins where 1=1";
        if (searchRequest.getNetwork() != null && !searchRequest.getNetwork().isEmpty()) {
            if (searchRequest.getNetwork().equals("NULL")) {
                typeQuery += " AND network IS NULL";
                productQuery += " AND network IS NULL";
                networkQuery += " AND network IS NULL";
                countryQuery += " AND network IS NULL";
            } else {
                typeQuery += " AND network = '" + searchRequest.getNetwork() + "'";
                productQuery += " AND network = '" + searchRequest.getNetwork() + "'";
                networkQuery += " AND network = '" + searchRequest.getNetwork() + "'";
                countryQuery += " AND network = '" + searchRequest.getNetwork() + "'";
            }

        }
        if (searchRequest.getProductName() != null && !searchRequest.getProductName().isEmpty()) {
            if (searchRequest.getProductName().equals("NULL")) {
                typeQuery += " AND product_name IS NULL";
                productQuery += " AND product_name IS NULL";
                networkQuery += " AND product_name IS NULL";
                countryQuery += " AND product_name IS NULL";
            } else {
                typeQuery += " AND product_name = '" + searchRequest.getProductName() + "'";
                productQuery += " AND product_name = '" + searchRequest.getProductName() + "'";
                networkQuery += " AND product_name = '" + searchRequest.getProductName() + "'";
                countryQuery += " AND product_name = '" + searchRequest.getProductName() + "'";
            }
        }
        if (searchRequest.getType() != null && !searchRequest.getType().isEmpty()) {

            if (searchRequest.getType().equals("NULL")) {
                typeQuery += " AND type IS NULL";
                productQuery += " AND type IS NULL";
                networkQuery += " AND type IS NULL";
                countryQuery += " AND type IS NULL";
            } else {
                typeQuery += " AND type = '" + searchRequest.getType() + "'";
                productQuery += " AND type = '" + searchRequest.getType() + "'";
                networkQuery += " AND type = '" + searchRequest.getType() + "'";
                countryQuery += " AND type = '" + searchRequest.getType() + "'";
            }
        }
        if (searchRequest.getCountry() != null && !searchRequest.getCountry().isEmpty()) {
            if (searchRequest.getCountry().equals("NULL")) {
                typeQuery += " AND country IS NULL";
                productQuery += " AND country IS NULL";
                networkQuery += " AND country IS NULL";
                countryQuery += " AND country IS NULL";
            } else {
                typeQuery += " AND country = '" + searchRequest.getCountry() + "'";
                productQuery += " AND country = '" + searchRequest.getCountry() + "'";
                networkQuery += " AND country = '" + searchRequest.getCountry() + "'";
                countryQuery += " AND country = '" + searchRequest.getCountry() + "'";
            }
        }


        if (searchRequest.getBin() != 0) {
            typeQuery += " AND start LIKE '" + searchRequest.getBin() + "%'";
            productQuery += " AND start LIKE '" + searchRequest.getBin() + "%'";
            networkQuery += " AND start LIKE '" + searchRequest.getBin() + "%'";
            countryQuery += " AND start LIKE '" + searchRequest.getBin() + "%'";
        }


        if (searchRequest.getIssuer() != null && !searchRequest.getIssuer().isEmpty()) {
            if (searchRequest.getIssuer().equals("NULL")) {
                typeQuery += " AND issuer IS NULL";
                productQuery += " AND issuer IS NULL";
                networkQuery += " AND issuer IS NULL";
                countryQuery += " AND issuer IS NULL";
            } else {
                typeQuery += " AND issuer LIKE '" + searchRequest.getIssuer() + "%'";
                productQuery += " AND issuer LIKE '" + searchRequest.getIssuer() + "%'";
                networkQuery += " AND issuer LIKE '" + searchRequest.getIssuer() + "%'";
                countryQuery += " AND issuer LIKE '" + searchRequest.getIssuer() + "%'";
            }
        }
        typeQuery += " group by type";
        productQuery += " group by product_name";
        networkQuery += " group by network";
        countryQuery += " group by country";
        Log.d("TAG", "updateFilter: " + typeQuery);
        Log.d("TAG", "updateFilter: " + productQuery);
        Log.d("TAG", "updateFilter: " + networkQuery);
        Log.d("TAG", "updateFilter: " + countryQuery);
        String finalTypeQuery = typeQuery;
        String finalProductQuery = productQuery;
        String finalNetworkQuery = networkQuery;
        String finalCountryQuery = countryQuery;
        new Thread(() -> {
            SimpleSQLiteQuery typeQr = new SimpleSQLiteQuery(finalTypeQuery);
            SimpleSQLiteQuery productQr = new SimpleSQLiteQuery(finalProductQuery);
            SimpleSQLiteQuery networkQr = new SimpleSQLiteQuery(finalNetworkQuery);
            SimpleSQLiteQuery countryQr = new SimpleSQLiteQuery(finalCountryQuery);
            var countryResponse = database.binDao().getFilteredList(countryQr);
            var typeResponse = database.binDao().getFilteredList(typeQr);
            var productResponse = database.binDao().getFilteredList(productQr);
            var networkResponse = database.binDao().getFilteredList(networkQr);
            Log.d("TAG", "findIssuer: " + countryResponse.size());
            Log.d("TAG", "findIssuer: " + countryResponse);
            Log.d("TAG", "findIssuer: " + typeResponse.size());
            Log.d("TAG", "findIssuer: " + typeResponse);
            Log.d("TAG", "findIssuer: " + productResponse.size());
            Log.d("TAG", "findIssuer: " + productResponse);
            Log.d("TAG", "findIssuer: " + networkResponse.size());
            Log.d("TAG", "findIssuer: " + networkResponse);
            typesData.postValue(new Event<>(typeResponse));
            networksData.postValue(new Event<>(networkResponse));
            productsData.postValue(new Event<>(productResponse));
            countriesData.postValue(new Event<>(countryResponse));
        }).start();

    }
}




