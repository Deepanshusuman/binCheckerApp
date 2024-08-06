package com.assets.binfinder.model.bin;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.assets.binfinder.BinOuterClass;

import java.util.List;

@Dao
public interface BinDao {





    @NonNull
    @Query("SELECT network FROM bins GROUP BY network ORDER BY network ASC")
    List<String> getAllNetworks();

    @NonNull
    @Query("SELECT product_name FROM bins GROUP BY product_name ORDER BY product_name ASC")
    List<String> getAllProducts();

    @NonNull
    @Query("SELECT type FROM bins GROUP BY type ORDER BY type ASC")
    List<String> getAllTypes();


    @NonNull
    @Query("SELECT  * FROM bins where start = :bin")
    Bin getBinDetails(Long bin);


    @RawQuery
    List<Bin> find_bins(SupportSQLiteQuery query);

    @RawQuery
    List<String> getFilteredList(SupportSQLiteQuery query);



}


