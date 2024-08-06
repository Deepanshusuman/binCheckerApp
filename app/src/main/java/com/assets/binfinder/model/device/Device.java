package com.assets.binfinder.model.device;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "device")
public class Device {

    @NonNull
    @PrimaryKey
    public Long bin;

    public Device(@NonNull Long bin) {
        this.bin = bin;
    }
}

