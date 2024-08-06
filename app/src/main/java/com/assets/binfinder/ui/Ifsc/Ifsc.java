package com.assets.binfinder.ui.Ifsc;

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
import androidx.lifecycle.ViewModelProvider;


import com.assets.binfinder.R;
import com.assets.binfinder.databinding.FragmentIfscBinding;

import org.json.JSONObject;

import java.util.Objects;


import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class Ifsc extends Fragment {
    FragmentIfscBinding binding;
    IfscViewModel ifscViewModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ifscViewModel = new ViewModelProvider(requireActivity()).get(IfscViewModel.class);
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentIfscBinding.inflate(inflater, container, false);

        binding.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 11) {
                    clear();
                    binding.editTextLayout.setError(null);
                    binding.progressBar.setVisibility(View.VISIBLE);
                    ifscViewModel.getIfsc(Objects.requireNonNull(binding.editText.getText()).toString());
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        binding.editText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (Objects.requireNonNull(binding.editText.getText()).toString().isEmpty()) {
                    binding.editTextLayout.setError("Please enter a valid ifsc");

                } else {
                    clear();
                    binding.editTextLayout.setError(null);
                    binding.progressBar.setVisibility(View.VISIBLE);
                    ifscViewModel.getIfsc(binding.editText.getText().toString());
                }
                return true;
            }
            return false;
        });

        ifscViewModel.data.observe(getViewLifecycleOwner(), event -> {
            if (!event.getHasBeenHandled()) {
                String data = event.getContentIfNotHandled();
                if (data != null) {
                    binding.response.setTextColor(getResources().getColor(R.color.green, null));
                    binding.progressBar.setVisibility(View.GONE);
                    binding.nestedScrollView.setVisibility(View.VISIBLE);
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        binding.ifsc.setText(jsonObject.getString("IFSC"));
                        binding.bank.setText(jsonObject.getString("BANK"));
                        binding.branch.setText(jsonObject.getString("BRANCH"));
                        binding.address.setText(jsonObject.getString("ADDRESS"));
                        binding.contact.setText(jsonObject.getString("CONTACT"));
                        binding.city.setText(jsonObject.getString("CITY"));
                        binding.district.setText(jsonObject.getString("DISTRICT"));
                        binding.state.setText(jsonObject.getString("STATE"));
                        binding.iso3166.setText(jsonObject.getString("ISO3166"));
                        binding.centre.setText(jsonObject.getString("CENTRE"));
                        binding.swift.setText(jsonObject.getString("SWIFT"));
                        binding.bankcode.setText(jsonObject.getString("BANKCODE"));
                        binding.micr.setText(jsonObject.getString("MICR"));
                        binding.upiImage.setImageResource(jsonObject.getBoolean("UPI") ? R.drawable.ic_right : R.drawable.ic_wrong);
                        binding.rtgsImage.setImageResource(jsonObject.getBoolean("RTGS") ? R.drawable.ic_right : R.drawable.ic_wrong);
                        binding.neftImage.setImageResource(jsonObject.getBoolean("NEFT") ? R.drawable.ic_right : R.drawable.ic_wrong);
                        binding.impsImage.setImageResource(jsonObject.getBoolean("IMPS") ? R.drawable.ic_right : R.drawable.ic_wrong);


                    } catch (Exception e) {
                        binding.progressBar.setVisibility(View.GONE);
                        binding.response.setText("Something Went Wrong");
                        binding.response.setTextColor(getResources().getColor(R.color.red, null));
                    }
                } else {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.response.setText("No data found");
                    binding.response.setTextColor(getResources().getColor(R.color.red, null));
                }
            }

        });
        return binding.getRoot();
    }


    void clear() {
        binding.nestedScrollView.setVisibility(View.GONE);
        binding.ifsc.setText(null);
        binding.bank.setText(null);
        binding.branch.setText(null);
        binding.address.setText(null);
        binding.contact.setText(null);
        binding.city.setText(null);
        binding.district.setText(null);
        binding.state.setText(null);
        binding.iso3166.setText(null);
        binding.centre.setText(null);
        binding.swift.setText(null);
        binding.bankcode.setText(null);
        binding.micr.setText(null);
        binding.upiImage.setImageResource(R.drawable.ic_error);
        binding.rtgsImage.setImageResource(R.drawable.ic_error);
        binding.neftImage.setImageResource(R.drawable.ic_error);
        binding.impsImage.setImageResource(R.drawable.ic_error);
    }

}