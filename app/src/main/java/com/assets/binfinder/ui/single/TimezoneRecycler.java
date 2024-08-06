package com.assets.binfinder.ui.single;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.assets.binfinder.databinding.TimezoneItemBinding;
import com.assets.binfinder.util.DateTimeUtil;

public class TimezoneRecycler extends RecyclerView.Adapter<TimezoneRecycler.ViewHolder> {
    String[] timezone;

    public TimezoneRecycler(String[] timezone) {
        this.timezone = timezone;
    }


    @NonNull
    @Override
    public TimezoneRecycler.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TimezoneRecycler.ViewHolder(TimezoneItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull TimezoneRecycler.ViewHolder holder, int position) {
        holder.bind(timezone[position]);
    }


    @Override
    public int getItemCount() {
        return timezone.length;
    }

    static class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        TimezoneItemBinding binding;

        public ViewHolder(@NonNull TimezoneItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(String timezone) {
            binding.key.setText(timezone);
            binding.val.setText(DateTimeUtil.getCurrentDateTime(timezone));
        }

    }
}
