package com.assets.binfinder.ui.byIssuer;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.assets.binfinder.BinOuterClass;
import com.assets.binfinder.databinding.FragmentIssuerBinding;
import com.assets.binfinder.ui.binchekr.BinchekrViewModel;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class Issuer extends Fragment {
    BinchekrViewModel binchekrViewModel;
    FragmentIssuerBinding binding;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binchekrViewModel = new ViewModelProvider(requireActivity()).get(BinchekrViewModel.class);
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentIssuerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.editBank.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    binchekrViewModel.findIssuer(BinOuterClass.SearchRequest.newBuilder().setIssuer(s.toString()));
                    binding.findButton.setEnabled(true);
                } else {
                    binding.findButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binchekrViewModel.issuers.observe(getViewLifecycleOwner(), listEvent -> {
            if (!listEvent.getHasBeenHandled()) {
                binding.editBank.setAdapter(new ArrayAdapter<>(requireContext(), com.google.android.material.R.layout.support_simple_spinner_dropdown_item, listEvent.getContentIfNotHandled()));
            }
        });


        //onSelected Remaining
        binding.editBank.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                go();
                return true;
            }
            return false;
        });
        binding.findButton.setOnClickListener(view1 -> {
            go();
        });
    }

    public void go() {
        com.assets.binfinder.ui.lookup.LookupFragmentDirections.ActionLookupToMultipleFragment directions = com.assets.binfinder.ui.lookup.LookupFragmentDirections.actionLookupToMultipleFragment();
        if (!Objects.equals(binding.editBank.getText().toString(), "")) {
            directions.setIssuer(binding.editBank.getText().toString());
        }
        Navigation.findNavController(binding.getRoot()).navigate(directions);
    }
}