package com.assets.binfinder.ui.seon;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.assets.binfinder.BinOuterClass;
import com.assets.binfinder.R;
import com.assets.binfinder.databinding.FragmentSeonBinding;

import org.json.JSONObject;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class Seon extends Fragment {
    FragmentSeonBinding binding;

    SeonViewModel seonViewModel;
    JSONObject object;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        seonViewModel = new ViewModelProvider(requireActivity()).get(SeonViewModel.class);
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSeonBinding.inflate(inflater, container, false);
        binding.findButton.setOnClickListener(v -> go());
        seonViewModel.liveData.observe(getViewLifecycleOwner(), jsonObjectEvent -> {
            if (!jsonObjectEvent.getHasBeenHandled()) {
                object = jsonObjectEvent.getContentIfNotHandled();
                setData(object);
            }
        });

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
        binding.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 8) {
                    go();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        binding.editText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                go();
                return true;
            }
            return false;
        });
        return binding.getRoot();
    }

    public void go() {
        binding.response.setText(null);
        String bin = Objects.requireNonNull(binding.editText.getText()).toString();
        if (bin.length() >= 6) {
            InputMethodManager inputMethodManager = (InputMethodManager) requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            if (inputMethodManager.isAcceptingText()) {
                inputMethodManager.hideSoftInputFromWindow(requireActivity().getCurrentFocus().getWindowToken(), 0);
            }

            seonViewModel.getData(Long.valueOf(bin));
            binding.editTextLayout.setError(null);
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.response.setText(null);
        } else {
            binding.editTextLayout.setError(getResources().getString(R.string.enter6digit));
        }

    }

    public void clear() {
        binding.issuer.setText(null);
        binding.type.setText(null);
        binding.network.setText(null);
        binding.product.setText(null);
        binding.country.setText(null);
        binding.bin.setText(null);
    }


    private void setData(JSONObject response) {
        binding.progressBar.setVisibility(View.GONE);
        var isSeonApi = PreferenceManager.getDefaultSharedPreferences(requireContext()).getBoolean("useSeonApiKey", false);
        if (isSeonApi) {
            if (response == null) {
                binding.linearLayout.setVisibility(View.GONE);
                binding.response.setText("No response from server or invalid API key");
                binding.response.setTextColor(getResources().getColor(R.color.red, null));
            } else {
                try {
                    if (response.getString("success").equals("true")) {
                        BinOuterClass.SearchRequest.Builder builder = BinOuterClass.SearchRequest.newBuilder();
                        var data = response.getJSONObject("data");
                        binding.issuer.setText(data.getString("bin_bank"));
                        binding.type.setText(data.getString("bin_type"));
                        binding.network.setText(data.getString("bin_card"));
                        binding.product.setText(data.getString("bin_level"));
                        binding.country.setText(data.getString("bin_country"));
                        binding.bin.setText(response.getString("bin"));
                        binding.linearLayout.setVisibility(View.VISIBLE);
                        binding.response.setText("Bin found");
                        binding.response.setTextColor(getResources().getColor(R.color.green, null));
                        builder.setIssuer(data.getString("bin_bank"));
                        builder.setType(data.getString("bin_type"));
                        builder.setNetwork(data.getString("bin_card"));
                        builder.setProductName(data.getString("bin_level"));
                        builder.setCountry(data.getString("bin_country"));
                        builder.setBin(Long.parseLong(response.getString("bin")));


                    } else {
                        var error = response.getJSONObject("error");
                        binding.linearLayout.setVisibility(View.GONE);
                        binding.response.setText(error.getString("message"));
                        binding.response.setTextColor(getResources().getColor(R.color.red, null));

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } else {
            binding.linearLayout.setVisibility(View.GONE);
            binding.response.setText("No API key");
            binding.response.setTextColor(getResources().getColor(R.color.red, null));
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        if (object != null) {
            setData(object);
        }
    }
}