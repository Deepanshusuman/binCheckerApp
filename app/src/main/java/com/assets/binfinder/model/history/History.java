package com.assets.binfinder.model.history;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.List;

@Entity(tableName = "history")
public class History {

    @PrimaryKey
    @NonNull
    public String date;
    @NonNull
    @TypeConverters({HistoryConverters.class})
    public List<Long> bins;


    public History(@NonNull String date, @NonNull List<Long> bins) {
        this.date = date;
        this.bins = bins;
    }
}
