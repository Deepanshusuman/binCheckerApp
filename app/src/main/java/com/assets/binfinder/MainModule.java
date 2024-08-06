package com.assets.binfinder;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.room.Room;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.assets.binfinder.model.Database;
import com.assets.binfinder.model.bin.BinDao;
import com.assets.binfinder.model.history.HistoryDao;
import com.assets.binfinder.model.savedlist.SavedlistDao;
import com.assets.binfinder.util.SaveState;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;


@InstallIn(SingletonComponent.class)
@Module
public class MainModule {
    @NonNull
    @Provides
    @Singleton
    @Inject
    public RequestQueue getRequestQueue(@ApplicationContext @NonNull Context context) {
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024);
        Network network = new BasicNetwork(new HurlStack());
        RequestQueue requestQueue = new RequestQueue(cache, network);
        requestQueue.start();
        return requestQueue;
    }


    @NonNull
    @Provides
    @Singleton
    @Inject
    public SharedPreferences getSharedPreferences(@ApplicationContext @NonNull Context context) {
        return context.getSharedPreferences(context.getString(R.string.preference_file_key), 0);

    }


    @NonNull
    @Provides
    @Singleton
    @Inject
    public Database getDatabase(@ApplicationContext @NonNull Context context) {
        return Room.databaseBuilder(context, Database.class, "defaultDb").createFromAsset("bins.db").build();
    }


    @NonNull
    @Provides
    @Singleton
    @Inject
    public SavedlistDao getSavedListDao(@NonNull Database database) {
        return database.savedListDao();
    }


    @NonNull
    @Provides
    @Singleton
    @Inject
    public BinDao getBinDao(@NonNull Database database) {
        return database.binDao();
    }

    @NonNull
    @Provides
    @Singleton
    @Inject
    public HistoryDao getHistoryDao(@NonNull Database database) {
        return database.historyDao();
    }


    @NonNull
    @Provides
    @Singleton
    @Inject
    public SaveState getSaveState() {
        return new SaveState();
    }



    @NonNull
    @Provides
    @Singleton
    @Inject
    public ExecutorService getExecutors() {
        return Executors.newFixedThreadPool(100);
    }
}
