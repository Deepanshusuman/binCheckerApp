package com.assets.binfinder.ui.saved;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;


import com.assets.binfinder.R;
import com.assets.binfinder.databinding.FragmentEditBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;
import java.util.UUID;


public class Edit extends BottomSheetDialogFragment {
    FragmentEditBinding binding;
    SavedViewModel savedViewModel;
    EditArgs args;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        savedViewModel = new ViewModelProvider(requireActivity()).get(SavedViewModel.class);
        binding = FragmentEditBinding.inflate(inflater, container, false);
        args = EditArgs.fromBundle(requireArguments());
        savedViewModel.getSavedList(UUID.fromString(args.getUuid()));
        savedViewModel.savedListInfo.observe(getViewLifecycleOwner(), savedListInfo -> {
            if (!savedListInfo.getHasBeenHandled()) {
                binding.editEdit.setText(Objects.requireNonNull(savedListInfo.getContentIfNotHandled()).name);
            }
        });

        binding.btnSave.setOnClickListener(v -> check());

        binding.editEdit.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                check();
            }
            return false;
        });

        return binding.getRoot();
    }


    private void check() {

        if (Objects.requireNonNull(binding.editEdit.getText()).toString().isEmpty()) {
            binding.txtNameLayout.setError("Name cannot be empty");
            binding.txtNameLayout.requestFocus();
        } else {
            binding.txtNameLayout.setError(null);
            savedViewModel.editNameToSavedList(UUID.fromString(args.getUuid()), binding.editEdit.getText().toString());
            Snackbar.make(requireView(), "Edited", Snackbar.LENGTH_SHORT).show();
            dismiss();
        }
    }
}