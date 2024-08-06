package com.assets.binfinder.ui.binchekr;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.assets.binfinder.BinOuterClass;
import com.assets.binfinder.model.bin.Bin;

public class BinComparator extends DiffUtil.ItemCallback<Bin> {


    @Override
    public boolean areItemsTheSame(@NonNull Bin oldItem, @NonNull Bin newItem) {
        return oldItem.start.equals(newItem.start);
    }

    @Override
    public boolean areContentsTheSame(@NonNull Bin oldItem, @NonNull Bin newItem) {
        return oldItem.start.equals(newItem.start);
    }
}
