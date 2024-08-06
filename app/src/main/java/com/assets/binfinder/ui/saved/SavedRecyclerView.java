package com.assets.binfinder.ui.saved;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.assets.binfinder.RecyclerViewInterface;
import com.assets.binfinder.databinding.SavedItemListBinding;
import com.assets.binfinder.model.savedlist.SavedList;

import java.util.List;

public class SavedRecyclerView extends RecyclerView.Adapter<SavedRecyclerView.ViewHolder> {
    RecyclerViewInterface recyclerViewInterface;
    List<SavedList> oldList;

    public SavedRecyclerView(RecyclerViewInterface recyclerViewInterface) {
        this.recyclerViewInterface = recyclerViewInterface;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SavedRecyclerView.ViewHolder(SavedItemListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedRecyclerView.ViewHolder holder, int position) {
        holder.binding.textView.setText(oldList.get(position).name);
    }


    @Override
    public int getItemCount() {
        return oldList.size();
    }


    public void setData(List<SavedList> savedLists) {
        this.oldList = savedLists;
    }


    public void setNewData(List<SavedList> newList) {
        SavedDiffUtil util = new SavedDiffUtil(oldList, newList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(util);
        this.oldList.clear();
        this.oldList.addAll(newList);
        diffResult.dispatchUpdatesTo(this);
    }


    static class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        final SavedItemListBinding binding;

        public ViewHolder(@NonNull SavedItemListBinding binding, RecyclerViewInterface recyclerViewInterface) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.getRoot().setOnClickListener(view -> {
                if (recyclerViewInterface != null) {
                    int position = getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        recyclerViewInterface.onItemClicked(position);
                    }
                }
            });
        }
    }
}
