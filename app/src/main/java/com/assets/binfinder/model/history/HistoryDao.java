package com.assets.binfinder.model.history;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface HistoryDao {


    @Query("SELECT * FROM history")
    List<History> getAll();


    @Insert(onConflict = 1)
    void insert(History items);

    @Query("DELETE FROM History")
    void deleteAll();


    @Query("SELECT * FROM history WHERE date = :date")
    History findByDate(String date);

    @Update
    void update(History history);
}
