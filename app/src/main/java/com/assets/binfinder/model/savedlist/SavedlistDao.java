package com.assets.binfinder.model.savedlist;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
import java.util.UUID;

@Dao
public interface SavedlistDao {
    @NonNull
    @Query("SELECT * FROM savedlist")
    List<SavedList> getAll();

    @NonNull
    @Query("SELECT * FROM savedlist WHERE uuid =  :uuid")
    SavedList findByUuid(@NonNull UUID uuid);


    @Insert(onConflict = 1)
    void insert(@NonNull SavedList list);


    @Delete
    void delete(@NonNull SavedList list);


    @Query("DELETE FROM savedlist")
    void deleteAll();

    @Update
    void update(@NonNull SavedList savedList);


}
