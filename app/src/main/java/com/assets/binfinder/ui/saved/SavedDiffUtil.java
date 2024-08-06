package com.assets.binfinder.ui.saved;

import androidx.recyclerview.widget.DiffUtil;

import com.assets.binfinder.model.savedlist.SavedList;

import java.util.List;

public class SavedDiffUtil extends DiffUtil.Callback {


    List<SavedList> oldList;
    List<SavedList> newList;

    SavedDiffUtil(List<SavedList> oldList, List<SavedList> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).uuid == newList.get(newItemPosition).uuid;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).uuid == newList.get(newItemPosition).uuid;
    }
}
