package com.assets.binfinder.model.feedback;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "feedback")
public class Feedback {
    @NonNull
    @PrimaryKey
    public Long bin;

    public Feedback(@NonNull Long bin) {
        this.bin = bin;
    }
}


