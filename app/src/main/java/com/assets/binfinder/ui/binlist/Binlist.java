package com.assets.binfinder.ui.binlist;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.assets.binfinder.R;
import com.assets.binfinder.databinding.FragmentBinlistBinding;
import com.assets.binfinder.ui.lookup.LookupFragmentDirections;

import java.util.Objects;


import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class Binlist extends Fragment {
    FragmentBinlistBinding binding;




    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBinlistBinding.inflate(inflater, container, false);
        binding.findButton.setOnClickListener(v -> {
            String bin = Objects.requireNonNull(binding.editText.getText()).toString();
            if (bin.length() < 6) {
                binding.editTextLayout.setError(getResources().getString(R.string.enter6digit));
            } else {
                binding.editTextLayout.setError(null);
                Navigation.findNavController(requireView()).navigate(LookupFragmentDirections.actionLookupFragmentToBinlistResponseDialog(Long.parseLong(bin)));
            }
        });
        binding.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.findButton.setEnabled(charSequence.length() >= 6);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.editText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String bin = Objects.requireNonNull(binding.editText.getText()).toString();
                if (bin.length() < 6) {
                    binding.editTextLayout.setError(getResources().getString(R.string.enter6digit));
                } else {
                    binding.editTextLayout.setError(null);
                    Navigation.findNavController(requireView()).navigate(LookupFragmentDirections.actionLookupFragmentToBinlistResponseDialog(Long.parseLong(bin)));
                }
                return true;
            }
            return false;
        });
        binding.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 8) {
                    String bin = Objects.requireNonNull(binding.editText.getText()).toString();
                    binding.editTextLayout.setError(null);
                    Navigation.findNavController(requireView()).navigate(LookupFragmentDirections.actionLookupFragmentToBinlistResponseDialog(Long.parseLong(bin)));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return binding.getRoot();
    }


}