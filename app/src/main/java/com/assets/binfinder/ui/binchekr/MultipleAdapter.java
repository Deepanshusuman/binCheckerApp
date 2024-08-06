package com.assets.binfinder.ui.binchekr;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;

import com.assets.binfinder.MobileNavigationDirections;
import com.assets.binfinder.databinding.BinchekrMultipleItemBinding;
import com.assets.binfinder.model.bin.Bin;
import com.assets.binfinder.util.EmojiUtil;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Objects;

public class MultipleAdapter extends PagingDataAdapter<Bin, MultipleAdapter.ViewHolder> {

    MultipleAdapter(@NotNull DiffUtil.ItemCallback<Bin> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MultipleAdapter.ViewHolder(BinchekrMultipleItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MultipleAdapter.ViewHolder holder, int position) {
        holder.bind(Objects.requireNonNull(getItem(position)));
    }


    static class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        final BinchekrMultipleItemBinding binding;

        public ViewHolder(@NonNull BinchekrMultipleItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Bin bin) {
            binding.country.setText(EmojiUtil.fromCountryCode(bin.country));
            binding.product.setText(Objects.equals(bin.productName, "") ? "-" : bin.productName);
            binding.issuer.setText(Objects.equals(bin.issuer, "") ? "-" : bin.issuer);
            binding.type.setText(Objects.equals(bin.type, "") ? "-" : bin.type);
            binding.start.setText(String.format(Locale.getDefault(), "%d - %d", bin.start, bin.end));
            binding.cardView.setOnClickListener(view -> Navigation.findNavController(binding.getRoot()).navigate(MobileNavigationDirections.actionGlobalSingleFragment().setBin(bin.start)));
        }
    }

}
