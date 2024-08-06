package com.assets.binfinder.model;

import androidx.annotation.NonNull;
import androidx.room.RoomDatabase;

import com.assets.binfinder.model.bin.Bin;
import com.assets.binfinder.model.bin.BinDao;
import com.assets.binfinder.model.device.Device;
import com.assets.binfinder.model.device.DeviceDao;
import com.assets.binfinder.model.fav.Fav;
import com.assets.binfinder.model.fav.FavDao;
import com.assets.binfinder.model.feedback.Feedback;
import com.assets.binfinder.model.feedback.FeedbackDao;
import com.assets.binfinder.model.globalstat.GlobalStat;
import com.assets.binfinder.model.globalstat.StatDao;
import com.assets.binfinder.model.history.History;
import com.assets.binfinder.model.history.HistoryDao;
import com.assets.binfinder.model.savedlist.SavedList;
import com.assets.binfinder.model.savedlist.SavedlistDao;

@androidx.room.Database(entities = {GlobalStat.class, SavedList.class, History.class, Fav.class, Bin.class, Device.class, Feedback.class}, version = 3, exportSchema = false)

public abstract class Database extends RoomDatabase {
    @NonNull
    public abstract SavedlistDao savedListDao();

    @NonNull
    public abstract HistoryDao historyDao();


    @NonNull
    public abstract DeviceDao deviceDao();

    @NonNull
    public abstract BinDao binDao();

    @NonNull
    public abstract StatDao statDao();

    @NonNull
    public abstract FavDao favDao();

    @NonNull
    public abstract FeedbackDao feedbackDao();

}
