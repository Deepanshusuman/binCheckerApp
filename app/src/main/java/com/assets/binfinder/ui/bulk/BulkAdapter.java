package com.assets.binfinder.ui.bulk;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.assets.binfinder.BinOuterClass;
import com.assets.binfinder.RecyclerViewInterface;
import com.assets.binfinder.databinding.BinchekrMultipleItemBinding;
import com.assets.binfinder.model.bin.Bin;
import com.assets.binfinder.util.EmojiUtil;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class BulkAdapter extends RecyclerView.Adapter<BulkAdapter.ViewHolder> {

    ArrayList<Bin> bins = new ArrayList<>();
    RecyclerViewInterface recyclerViewInterface;

    public BulkAdapter(@NonNull RecyclerViewInterface recyclerViewInterface) {
        this.recyclerViewInterface = recyclerViewInterface;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BulkAdapter.ViewHolder(BinchekrMultipleItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), recyclerViewInterface);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(bins.get(position));
    }

    @Override
    public int getItemCount() {
        return bins.size();
    }


    public void addBin(@NonNull Bin bin) {
        bins.add(bin);
        notifyItemInserted(bins.size() - 1);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        BinchekrMultipleItemBinding binding;

        public ViewHolder(@NonNull BinchekrMultipleItemBinding binding, @NonNull RecyclerViewInterface recyclerViewInterface) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(view -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    recyclerViewInterface.onItemClicked(position);
                }
            });
        }

        public void bind(@NonNull Bin bin) {
            binding.country.setText(EmojiUtil.fromCountryCode(bin.country));
            binding.issuer.setText(Objects.equals(bin.issuer, "") ? "-" : bin.issuer);
            binding.type.setText(Objects.equals(bin.type, "") ? "-" : bin.type);
            binding.start.setText(String.format(Locale.getDefault(), "%d - %d", bin.start, bin.end));
            //  binding.cardView.setOnClickListener(view -> Navigation.findNavController(binding.getRoot()).navigate(com.assets.binfinder.ui.binchekr.MultipleDirections.actionMultipleToNavigationSingle().setBin(bin.getStart())));
        }
    }
}

