package com.assets.binfinder.ui.saved;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.assets.binfinder.R;
import com.assets.binfinder.databinding.FragmentEditBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class Add extends BottomSheetDialogFragment {
    FragmentEditBinding binding;
    SavedViewModel savedViewModel;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        savedViewModel = new ViewModelProvider(requireActivity()).get(SavedViewModel.class);
        binding = FragmentEditBinding.inflate(inflater, container, false);
        binding.btnSave.setOnClickListener(v -> check());
        binding.editEdit.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                check();
            }
            return false;
        });
        return binding.getRoot();
    }


    public void check() {
        if (Objects.requireNonNull(binding.editEdit.getText()).toString().isEmpty()) {
            binding.txtNameLayout.setError("Enter a name");
            binding.txtNameLayout.requestFocus();
        } else {
            binding.txtNameLayout.setError(null);
            savedViewModel.addNameToSavedList(binding.editEdit.getText().toString());
            Snackbar.make(requireView(), "Saved", Snackbar.LENGTH_SHORT).show();
            dismiss();
        }
    }
}