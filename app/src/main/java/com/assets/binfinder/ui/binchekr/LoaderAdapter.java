package com.assets.binfinder.ui.binchekr;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.LoadState;
import androidx.paging.LoadStateAdapter;
import androidx.recyclerview.widget.RecyclerView;


import com.assets.binfinder.R;
import com.assets.binfinder.databinding.LoaderItemBinding;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class LoaderAdapter extends LoadStateAdapter<LoaderAdapter.ViewHolder> {
    View.OnClickListener mRetryCallback;

    public LoaderAdapter(@NonNull View.OnClickListener retryCallback) {
        this.mRetryCallback = retryCallback;
    }

    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder, @NonNull LoadState loadState) {
        holder.bind(loadState);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, @NonNull LoadState loadState) {
        return new ViewHolder(parent, mRetryCallback);
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        LoaderItemBinding binding;

        public ViewHolder(@NonNull ViewGroup parent, @NonNull View.OnClickListener retryCallback) {
            super(LoaderItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false).getRoot());
            binding = LoaderItemBinding.bind(itemView);

            binding.btnRetry.setOnClickListener(retryCallback);
        }

        public void bind(LoadState loadState) {
            if (loadState instanceof LoadState.Error && Objects.equals(((LoadState.Error) loadState).getError().getLocalizedMessage(), "No more results")) {
                binding.errorMsg.setText(binding.getRoot().getContext().getString(R.string.no_more_results));
                binding.progressIndicator.setVisibility(View.GONE);
                binding.btnRetry.setVisibility(View.GONE);
                binding.errorMsg.setVisibility(View.VISIBLE);
            } else {
                binding.progressIndicator.setVisibility(loadState instanceof LoadState.Loading ? View.VISIBLE : View.GONE);
                binding.btnRetry.setVisibility(loadState instanceof LoadState.Error ? View.VISIBLE : View.GONE);
                binding.errorMsg.setVisibility(loadState instanceof LoadState.Error ? View.VISIBLE : View.GONE);
            }
        }
    }
}
