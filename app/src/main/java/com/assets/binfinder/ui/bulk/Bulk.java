package com.assets.binfinder.ui.bulk;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.assets.binfinder.databinding.FragmentBulkBinding;
import com.assets.binfinder.model.Database;
import com.assets.binfinder.ui.lookup.LookupFragmentDirections;
import com.assets.binfinder.util.Util;

import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class Bulk extends Fragment {
    FragmentBulkBinding binding;
    @Inject
    Database database;
    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBulkBinding.inflate(inflater, container, false);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) binding.textInputLayout.findViewById(com.google.android.material.R.id.text_input_end_icon).getLayoutParams();
        params.gravity = Gravity.TOP;
        binding.textInputLayout.findViewById(com.google.android.material.R.id.text_input_end_icon).setLayoutParams(params);
        binding.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.findButton.setEnabled(s.length() >= 6);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.findButton.setOnClickListener(v -> {
            String s = Objects.requireNonNull(binding.editText.getText()).toString();
            long[] longs = Util.extractBins(s);
            Navigation.findNavController(requireView()).navigate(LookupFragmentDirections.actionLookupFragmentToBulkRecyclerFragment(longs));
        });
    }
}