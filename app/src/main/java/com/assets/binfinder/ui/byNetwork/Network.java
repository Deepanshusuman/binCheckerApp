package com.assets.binfinder.ui.byNetwork;

import android.os.Bundle;
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

import com.assets.binfinder.MainViewModel;
import com.assets.binfinder.ui.lookup.LookupFragmentDirections;
import com.assets.binfinder.databinding.FragmentNetworkBinding;

import java.util.ArrayList;
import java.util.Objects;


import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class Network extends Fragment {

    MainViewModel mainViewModel;
    FragmentNetworkBinding binding;
    ArrayList<String> networks;
    String network = "";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        networks = mainViewModel.getNetwork();

    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentNetworkBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), com.google.android.material.R.layout.support_simple_spinner_dropdown_item, networks);
        binding.spinnerNetwork.setAdapter(adapter);
        binding.spinnerNetwork.setOnItemClickListener((adapterView, view1, i, l) -> network = networks.get(networks.indexOf((String) adapterView.getItemAtPosition(i))));
        binding.spinnerNetwork.setOnEditorActionListener((v, actionId, event) -> {
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
        if (!Objects.equals(network, "")) {
            directions.setNetwork(network);
        }
        Navigation.findNavController(binding.getRoot()).navigate(directions);
    }
}