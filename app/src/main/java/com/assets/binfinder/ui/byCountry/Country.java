package com.assets.binfinder.ui.byCountry;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.assets.binfinder.MainViewModel;
import com.assets.binfinder.R;
import com.assets.binfinder.ui.lookup.LookupFragmentDirections;
import com.assets.binfinder.databinding.FragmentCountryBinding;

import java.util.Arrays;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class Country extends Fragment {


    MainViewModel mainViewModel;
    FragmentCountryBinding binding;
    String country = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCountryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.spinnerCountry.setOnItemClickListener((adapterView, view1, position, l) -> {
            country = getResources().getStringArray(R.array.arrCode)[Arrays.asList(getResources().getStringArray(R.array.arrCountry)).indexOf((String) adapterView.getItemAtPosition(position))];
            if (country.equals("Country")) {
                country = "";
            }
        });

        binding.spinnerCountry.setOnEditorActionListener((v, actionId, event) -> {
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
        LookupFragmentDirections.ActionLookupToMultipleFragment directions = LookupFragmentDirections.actionLookupToMultipleFragment();
        if (!Objects.equals(country, "")) {
            directions.setCountry(country);
        }
        Navigation.findNavController(binding.getRoot()).navigate(directions);
    }
}