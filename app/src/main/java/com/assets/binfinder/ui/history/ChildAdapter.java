package com.assets.binfinder.ui.history;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.assets.binfinder.RecyclerViewInterface;
import com.assets.binfinder.databinding.HistoryChildRowBinding;

import java.util.List;

public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ViewHolder> {

    List<Long> list;
    RecyclerViewInterface recyclerViewInterface;

    public ChildAdapter(@NonNull List<Long> list, @NonNull RecyclerViewInterface recyclerViewInterface) {
        this.list = list;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(HistoryChildRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(String.valueOf(list.get(position)));
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        HistoryChildRowBinding binding;

        public ViewHolder(@NonNull HistoryChildRowBinding rowBinding, @NonNull RecyclerViewInterface recyclerViewInterface) {
            super(rowBinding.getRoot());
            this.binding = rowBinding;
            binding.getRoot().setOnClickListener(view -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    recyclerViewInterface.onItemClicked(position);
                }
            });

        }

        public void bind(@NonNull String bin) {
            binding.bin.setText(bin);
        }
    }
}
