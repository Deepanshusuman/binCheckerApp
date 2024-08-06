package com.assets.binfinder.util;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

public class Card {
    public final String network;
    public final int[] gaps;
    public final int[] lengths;
    public final String codeName;
    public final int[] codeSize;
    public final String bin;
    public boolean valid;
    @DrawableRes
    public int drawable;


    public Card(@NonNull String bin, @NonNull String network, @NonNull int[] gaps, @NonNull int[] lengths, @NonNull String codeName, @NonNull int[] codeSize, int drawable) {
        this.network = network;
        this.gaps = gaps;
        this.valid = Validator.luhnCheck(bin);
        this.lengths = lengths;
        this.codeName = codeName;
        this.codeSize = codeSize;
        this.bin = Validator.format(bin);
        this.drawable = drawable;
    }
}
