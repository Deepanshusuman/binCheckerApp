package com.assets.binfinder.ui.history;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.assets.binfinder.MainViewModel;
import com.assets.binfinder.MobileNavigationDirections;
import com.assets.binfinder.R;
import com.assets.binfinder.databinding.FragmentHistoryBinding;
import com.assets.binfinder.model.history.History;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HistoryFragment extends Fragment {
    MainViewModel mainViewModel;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentHistoryBinding binding = FragmentHistoryBinding.inflate(inflater, container, false);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        binding.toolbar.setNavigationOnClickListener(v -> Navigation.findNavController(v).navigateUp());
        binding.toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.clear_history) {
                new AlertDialog.Builder(requireContext()).setTitle("Clear History").setMessage("After 30 days from the date of the search, the history will be removed from our systems. Would you really like to remove your history?").setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {
                    mainViewModel.clearHistory();
                    Snackbar.make(binding.getRoot(), "History cleared", Snackbar.LENGTH_SHORT).setAnchorView(requireActivity().findViewById(R.id.bottom_nav)).show();
                }).setNegativeButton(android.R.string.no, null).show();

                return true;
            }
            return false;
        });
        mainViewModel.historyList.observe(getViewLifecycleOwner(), event -> {
            if (!event.getHasBeenHandled()) {
                List<History> histories = event.getContentIfNotHandled();
                if (histories != null) {
                    binding.toolbar.getMenu().findItem(R.id.clear_history).setEnabled(!histories.isEmpty());
                    binding.history.setAdapter(new HistoryAdapter(histories, (k, v) -> Navigation.findNavController(requireView()).navigate(MobileNavigationDirections.actionGlobalSingleFragment().setBin(histories.get(k).bins.get(v)))));
                }

            }
        });
        return binding.getRoot();
    }


    @Override
    public void onResume() {
        super.onResume();
        mainViewModel.getHistory();
    }

    @Override
    public void onStart() {
        super.onStart();
        mainViewModel.getHistory();
    }



}