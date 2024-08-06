package com.assets.binfinder.ui.byProductName;

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
import com.assets.binfinder.databinding.FragmentProductBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class Product extends Fragment {
    MainViewModel mainViewModel;
    FragmentProductBinding binding;
    List<String> products;
    String product = "";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        products = mainViewModel.getProduct();

    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProductBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), com.google.android.material.R.layout.support_simple_spinner_dropdown_item, products);
        binding.spinnerProduct.setAdapter(adapter);
        binding.spinnerProduct.setOnItemClickListener((adapterView, view1, i, l) -> product = products.get(products.indexOf((String) adapterView.getItemAtPosition(i))));
        binding.spinnerProduct.setOnEditorActionListener((v, actionId, event) -> {
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
        if (!Objects.equals(product, "")) {
            directions.setProductName(product);
        }
        Navigation.findNavController(binding.getRoot()).navigate(directions);
    }
}