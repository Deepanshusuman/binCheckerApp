package com.assets.binfinder.model.globalstat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface StatDao {


    @Query("SELECT * FROM stat")
    List<GlobalStat> getAll();


    @Insert(onConflict = 1)
    void insert( GlobalStat stat);

    @Query("DELETE FROM stat")
    void deleteAll();

}
