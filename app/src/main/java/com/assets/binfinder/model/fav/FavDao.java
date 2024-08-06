package com.assets.binfinder.model.fav;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FavDao {
    @NonNull
    @Query("SELECT * FROM fav")
    List<Fav> getAll();

    @Insert(onConflict = 1)
    void insert(@NonNull Fav fav);


    @Query("DELETE FROM fav WHERE bin = :bin")
    void delete(long bin);


    @Query("DELETE FROM fav")
    void deleteAll();

    @Query("SELECT EXISTS(SELECT * FROM fav WHERE bin = :bin)")
    boolean hasFav(long bin);
}


