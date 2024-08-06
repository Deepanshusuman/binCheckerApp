package com.assets.binfinder.model.bin;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "bins")
public class Bin {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "start")
    public Long start;

    @ColumnInfo(name = "end")
    public Long end;

    @ColumnInfo(name = "flag")
    public String flag;

    @ColumnInfo(name = "network")
    public String network;

    @ColumnInfo(name = "type")
    public String type;

    @ColumnInfo(name = "product_name")
    public String productName;

    @ColumnInfo(name = "issuer")
    public String issuer;

    @ColumnInfo(name = "country")
    public String country;

    @ColumnInfo(name = "info")
    public String info;

    @ColumnInfo(name = "updated_at")
    public Long updatedAt;

    public Bin(@NonNull Long start, Long end, String flag, String network, String type, String productName,
               String issuer, String country, String info, Long updatedAt) {
        this.start = start;
        this.end = end;
        this.flag = flag;
        this.network = network;
        this.type = type;
        this.productName = productName;
        this.issuer = issuer;
        this.country = country;
        this.info = info;
        this.updatedAt = updatedAt;
    }
}


