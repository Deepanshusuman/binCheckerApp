package com.assets.binfinder.model.device;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DeviceDao {


    @NonNull
    @Query("SELECT * FROM device")
    List<Device> getAll();

    @Insert(onConflict = 1)
    void insert(@NonNull Device device);


    @Query("DELETE FROM device WHERE bin = :bin")
    void delete(long bin);


    @Query("DELETE FROM device")
    void deleteAll();

    @Query("SELECT EXISTS(SELECT * FROM device WHERE bin = :bin)")
    boolean hasFav(long bin);
}


