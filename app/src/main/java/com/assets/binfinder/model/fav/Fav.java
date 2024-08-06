package com.assets.binfinder.model.fav;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "fav")
public class Fav {
    @NonNull
    @PrimaryKey
    public Long bin;

    public Fav(@NonNull Long bin) {
        this.bin = bin;
    }
}

