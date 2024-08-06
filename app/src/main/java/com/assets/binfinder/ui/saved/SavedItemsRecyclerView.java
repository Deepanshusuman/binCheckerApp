package com.assets.binfinder.ui.saved;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.assets.binfinder.RecyclerViewInterface;
import com.assets.binfinder.databinding.SavedItemListBinding;

import java.util.ArrayList;
import java.util.List;

public class SavedItemsRecyclerView extends RecyclerView.Adapter<SavedItemsRecyclerView.ViewHolder> {
    List<Long> items;
    RecyclerViewInterface recyclerViewInterface;

    public SavedItemsRecyclerView(@NonNull RecyclerViewInterface recyclerViewInterface) {
        this.items = new ArrayList<>();
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public SavedItemsRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SavedItemsRecyclerView.ViewHolder(SavedItemListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), recyclerViewInterface);

    }

    @Override
    public void onBindViewHolder(@NonNull SavedItemsRecyclerView.ViewHolder holder, int position) {
        holder.binding.textView.setText(String.valueOf(items.get(position)));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItems(@NonNull List<Long> bins) {
        this.items = bins;
        notifyDataSetChanged();
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
