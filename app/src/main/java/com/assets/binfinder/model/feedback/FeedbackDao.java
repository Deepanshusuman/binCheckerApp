package com.assets.binfinder.model.feedback;

import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface FeedbackDao {

    @Insert(onConflict = 1)
    void insert(Feedback feedback);


    @Query("DELETE FROM feedback WHERE bin = :bin")
    void delete(long bin);


    @Query("DELETE FROM feedback")
    void deleteAll();

    @Query("SELECT EXISTS(SELECT * FROM feedback WHERE bin = :bin)")
    boolean hasFeedback(long bin);

}
