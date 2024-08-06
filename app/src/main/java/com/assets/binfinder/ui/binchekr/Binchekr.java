package com.assets.binfinder.ui.binchekr;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.assets.binfinder.R;
import com.assets.binfinder.databinding.FragmentBinchekrBinding;
import com.assets.binfinder.ui.lookup.LookupFragmentDirections;
import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class Binchekr extends Fragment {
    FragmentBinchekrBinding binding;
    BinchekrViewModel binchekrViewModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binchekrViewModel = new ViewModelProvider(this).get(BinchekrViewModel.class);

    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBinchekrBinding.inflate(inflater, container, false);
        int country_pos = Arrays.asList(getResources().getStringArray(R.array.arrCode)).indexOf(binchekrViewModel.state.getSearchRequest().getCountry());
        if (country_pos != -1) {
            binding.editCountry.setText(getResources().getStringArray(R.array.arrCountry)[country_pos]);
        }
        binding.editNetwork.setText(binchekrViewModel.state.getSearchRequest().getNetwork());
        binding.editTypes.setText(binchekrViewModel.state.getSearchRequest().getType());
        binding.editProduct.setText(binchekrViewModel.state.getSearchRequest().getProductName());
        binding.editIssuer.setText(binchekrViewModel.state.getSearchRequest().getIssuer());


        binding.editCountry.setOnItemClickListener((adapterView, view, position, l) -> {
            enableClearButton();
            hideKeyboard();
            String country = getResources().getStringArray(R.array.arrCode)[Arrays.asList(getResources().getStringArray(R.array.arrCountry)).indexOf((String) adapterView.getItemAtPosition(position))];
            binchekrViewModel.state.getSearchRequest().setCountry(country);
            binchekrViewModel.updateFilter(binchekrViewModel.state.getSearchRequest());
        });


        binding.editIssuer.setOnItemClickListener((adapterView, view, i, l) -> {
            enableClearButton();
            hideKeyboard();
            binchekrViewModel.state.getSearchRequest().setIssuer((String) adapterView.getItemAtPosition(i));
            binchekrViewModel.updateFilter(binchekrViewModel.state.getSearchRequest());
        });

        binding.editIssuer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    binding.btnApply.setEnabled(true);
                    binchekrViewModel.findIssuer(binchekrViewModel.state.getSearchRequest().setIssuer(s.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.editBin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    binding.btnApply.setEnabled(true);
                    binding.btnClear.setEnabled(true);
                    try {
                        binchekrViewModel.updateFilter(binchekrViewModel.state.getSearchRequest().setBin(Long.parseLong(s.toString())));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        binding.editNetwork.setOnItemClickListener((adapterView, view, i, l) -> {
            enableClearButton();
            hideKeyboard();
            binchekrViewModel.state.getSearchRequest().setNetwork((String) adapterView.getItemAtPosition(i));
            binchekrViewModel.updateFilter(binchekrViewModel.state.getSearchRequest());
        });


        binding.editTypes.setOnItemClickListener((adapterView, view, i, l) -> {
            enableClearButton();
            hideKeyboard();
            binchekrViewModel.state.getSearchRequest().setType((String) adapterView.getItemAtPosition(i));
            binchekrViewModel.updateFilter(binchekrViewModel.state.getSearchRequest());
        });

        binding.editProduct.setOnItemClickListener((adapterView, view, i, l) -> {
            enableClearButton();
            hideKeyboard();
            binchekrViewModel.state.getSearchRequest().setProductName((String) adapterView.getItemAtPosition(i));
            binchekrViewModel.updateFilter(binchekrViewModel.state.getSearchRequest());
        });

        binchekrViewModel.issuers.observe(getViewLifecycleOwner(), listEvent -> {
            if (!listEvent.getHasBeenHandled()) {
                binding.editIssuer.setAdapter(new ArrayAdapter<>(requireContext(), com.google.android.material.R.layout.support_simple_spinner_dropdown_item, listEvent.getContentIfNotHandled()));
            }
        });
        binding.btnClear.setOnClickListener(v -> {
            disableClearButton();
            binding.editBin.setText(null);
            binding.editCountry.setText(null);
            binding.editNetwork.setText(null);
            binding.editTypes.setText(null);
            binding.editProduct.setText(null);
            binding.editIssuer.setText(null);
            binding.editCountry.setAdapter(null);
            binding.editNetwork.setAdapter(null);
            binding.editTypes.setAdapter(null);
            binding.editProduct.setAdapter(null);
            binchekrViewModel.state.getSearchRequest().clear();
            binchekrViewModel.updateFilter(binchekrViewModel.state.getSearchRequest());
            Snackbar.make(requireView(), "Cleared", Snackbar.LENGTH_SHORT).setAnchorView(requireActivity().findViewById(R.id.bottom_nav)).show();
        });


        binchekrViewModel.countriesData.observe(getViewLifecycleOwner(), event -> {
            if (!event.getHasBeenHandled()) {
                var list = event.getContentIfNotHandled();
                if (list != null) {
                    var countryListByName = list.stream().map(country -> {
                        int pos = Arrays.asList(getResources().getStringArray(R.array.arrCode)).indexOf(country);
                        if (pos != -1) {
                            return getResources().getStringArray(R.array.arrCountry)[pos];
                        }
                        return null;
                    }).filter(Objects::nonNull).sorted().collect(Collectors.toList());
                    binding.editCountry.setAdapter(new ArrayAdapter<>(requireContext(), com.google.android.material.R.layout.support_simple_spinner_dropdown_item, countryListByName));

                }
            }
        });
        binchekrViewModel.typesData.observe(getViewLifecycleOwner(), event -> {
            if (!event.getHasBeenHandled()) {
                var list = event.getContentIfNotHandled();
                if (list != null) {
                    binding.editTypes.setAdapter(new ArrayAdapter<>(requireContext(), com.google.android.material.R.layout.support_simple_spinner_dropdown_item, list));

                }
            }
        });
        binchekrViewModel.networksData.observe(getViewLifecycleOwner(), event -> {
            if (!event.getHasBeenHandled()) {
                var list = event.getContentIfNotHandled();
                if (list != null) {
                    binding.editNetwork.setAdapter(new ArrayAdapter<>(requireContext(), com.google.android.material.R.layout.support_simple_spinner_dropdown_item, list));

                }
            }
        });
        binchekrViewModel.productsData.observe(getViewLifecycleOwner(), event -> {
            if (!event.getHasBeenHandled()) {
                var list = event.getContentIfNotHandled();
                if (list != null) {
                    binding.editProduct.setAdapter(new ArrayAdapter<>(requireContext(), com.google.android.material.R.layout.support_simple_spinner_dropdown_item, list));

                }
            }
        });


        binding.editBin.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                go();
                return true;
            }
            return false;
        });
        binding.btnApply.setOnClickListener(view -> go());

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        binchekrViewModel.updateFilter(binchekrViewModel.state.getSearchRequest());
    }

    public void go() {
        hideKeyboard();
        binchekrViewModel.state.getSearchRequest().setType(binding.editTypes.getText().toString());
        binchekrViewModel.state.getSearchRequest().setNetwork(binding.editNetwork.getText().toString());
        binchekrViewModel.state.getSearchRequest().setProductName(binding.editProduct.getText().toString());
        binchekrViewModel.state.getSearchRequest().setIssuer(binding.editIssuer.getText().toString());
        LookupFragmentDirections.ActionLookupToMultipleFragment directions = LookupFragmentDirections.actionLookupToMultipleFragment();

        if (Objects.requireNonNull(binding.editBin.getText()).toString().isEmpty()) {
            directions.setBin(0L);
        } else {
            directions.setBin(Long.parseLong(binding.editBin.getText().toString()));
        }


        if (binchekrViewModel.state.getSearchRequest().getCountry() != null) {
            directions.setCountry(binchekrViewModel.state.getSearchRequest().getCountry());
        }

        if (binchekrViewModel.state.getSearchRequest().getType() != null) {
            directions.setType(binchekrViewModel.state.getSearchRequest().getType());
        }
        if (binchekrViewModel.state.getSearchRequest().getIssuer() != null) {
            directions.setIssuer(binchekrViewModel.state.getSearchRequest().getIssuer());
        }

        if (binchekrViewModel.state.getSearchRequest().getNetwork() != null) {
            directions.setNetwork(binchekrViewModel.state.getSearchRequest().getNetwork());
        }

        if (binchekrViewModel.state.getSearchRequest().getProductName() != null) {
            directions.setProductName(binchekrViewModel.state.getSearchRequest().getProductName());
        }

        Navigation.findNavController(binding.getRoot()).navigate(directions);
    }

    public void enableClearButton() {
        binding.btnClear.setEnabled(true);
    }

    public void disableClearButton() {
        binding.btnClear.setEnabled(false);
    }

    public void hideKeyboard() {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            if (inputMethodManager.isAcceptingText()) {
                inputMethodManager.hideSoftInputFromWindow(requireActivity().getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}