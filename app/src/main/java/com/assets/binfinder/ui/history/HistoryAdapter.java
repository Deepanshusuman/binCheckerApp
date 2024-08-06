package com.assets.binfinder.ui.history;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.assets.binfinder.R;
import com.assets.binfinder.RecyclerViewInterfaceMap;
import com.assets.binfinder.model.history.History;
import com.assets.binfinder.databinding.HistoryItemRowBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    static final RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    List<History> historyList;
    RecyclerViewInterfaceMap recyclerViewInterface;

    public HistoryAdapter(@NonNull List<History> historyList, @NonNull RecyclerViewInterfaceMap recyclerViewInterface) {
        this.historyList = historyList;
        this.recyclerViewInterface = recyclerViewInterface;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(HistoryItemRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(historyList.get(position));
    }


    @Override
    public int getItemCount() {
        return historyList.size();
    }

    static class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        HistoryItemRowBinding binding;
        RecyclerViewInterfaceMap recyclerViewInterface;

        public ViewHolder(@NonNull HistoryItemRowBinding binding, RecyclerViewInterfaceMap recyclerViewInterface) {
            super(binding.getRoot());
            this.binding = binding;
            this.recyclerViewInterface = recyclerViewInterface;
        }


        public void bind(History history) {
            int position = getBindingAdapterPosition();
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, yyyy", Locale.getDefault());
            if (sdf.format(new Date(System.currentTimeMillis())).equals(history.date)) {
                binding.date.setText(R.string.today);
            } else if (sdf.format(new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000)).equals(history.date)) {
                binding.date.setText(R.string.yesterday);
            } else {
                binding.date.setText(history.date);
            }
            ChildAdapter childRecycler = new ChildAdapter(history.bins, i -> {
                if (recyclerViewInterface != null) {
                    if (position != RecyclerView.NO_POSITION) {
                        recyclerViewInterface.onItemClicked(position, i);
                    }
                }
            });
            binding.recyclerView.setAdapter(childRecycler);
            binding.recyclerView.setRecycledViewPool(viewPool);
        }

    }
}
