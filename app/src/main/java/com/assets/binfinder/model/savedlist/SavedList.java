package com.assets.binfinder.model.savedlist;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(tableName = "savedList")
public class SavedList {

    @PrimaryKey
    @NonNull
    public UUID uuid;


    @ColumnInfo(name = "name")
    public String name;


    @ColumnInfo(name = "bins")
    @TypeConverters({SavedListConverters.class})
    public List<Long> bins;


    @NonNull
    public SavedList objectClone() {
        SavedList savedList = new SavedList();
        savedList.uuid = uuid;
        savedList.name = name;
        savedList.bins = new ArrayList<>(bins);
        return savedList;
    }
}
