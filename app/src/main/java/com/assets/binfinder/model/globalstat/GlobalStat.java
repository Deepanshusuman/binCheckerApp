package com.assets.binfinder.model.globalstat;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.assets.binfinder.model.savedlist.SavedListConverters;

import java.util.List;


@Entity(tableName = "stat")
public class GlobalStat {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "name")
    public String name;


    @NonNull
    @ColumnInfo(name = "bins")
    @TypeConverters({SavedListConverters.class})
    public List<Long> bins;


    public GlobalStat(@NonNull String name, @NonNull List<Long> bins) {
        this.name = name;
        this.bins = bins;
    }
}
