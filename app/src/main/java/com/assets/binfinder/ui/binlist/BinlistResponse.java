package com.assets.binfinder.ui.binlist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.assets.binfinder.R;
import com.assets.binfinder.databinding.FragmentBinlistResponseBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class BinlistResponse extends BottomSheetDialogFragment {
    JSONObject bankdata, numberdata;
    BinlistViewModel binlistViewModel;
    FragmentBinlistResponseBinding binding;


    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBinlistResponseBinding.inflate(inflater, container, false);
        binlistViewModel = new ViewModelProvider(requireActivity()).get(BinlistViewModel.class);
        binlistViewModel.getData(BinlistResponseArgs.fromBundle(requireArguments()).getBin());
        binlistViewModel.liveData.observe(getViewLifecycleOwner(), event -> {
            if (!event.getHasBeenHandled()) {
                binding.progressIndicator.hide();
                String s = event.getContentIfNotHandled();
                if (s != null) {
                    loadBin(s);
                } else {
                    Snackbar.make(requireView(), "Bin Not Found", Snackbar.LENGTH_SHORT).setAnchorView(requireActivity().findViewById(R.id.bottom_nav)).show();
                    dismiss();
                }
            }
        });
        return binding.getRoot();
    }


    private void loadBin(String json) {
        try {
            binding.binObject.setText(String.valueOf(BinlistResponseArgs.fromBundle(requireArguments()).getBin()));
            JSONObject jsonResponse = new JSONObject(json);
            try {
                numberdata = jsonResponse.getJSONObject("number");
            } catch (Exception ignored) {

            }
            JSONObject countrydata = null;
            try {
                countrydata = jsonResponse.getJSONObject("country");
            } catch (Exception ignored) {
            }

            try {
                bankdata = jsonResponse.getJSONObject("bank");
            } catch (Exception ignored) {

            }

            try {
                binding.length.setText(numberdata.getString("length"));
            } catch (Exception e) {
                binding.length.setText(getResources().getString(R.string.dash));
            }

            try {
                binding.brand.setText(jsonResponse.getString("scheme"));
            } catch (Exception e) {
                binding.brand.setText(getResources().getString(R.string.dash));
            }
            try {
                binding.type.setText(jsonResponse.getString("type"));
            } catch (Exception e) {
                binding.type.setText(getResources().getString(R.string.dash));
            }
            try {
                binding.level.setText(jsonResponse.getString("brand"));
            } catch (Exception e) {
                binding.brand.setText(getResources().getString(R.string.dash));
            }

            try {
                if (jsonResponse.getString("prepaid").equals("true")) {
                    binding.imgPrepaid.setImageResource(R.drawable.ic_right);
                } else {
                    binding.imgPrepaid.setImageResource(R.drawable.ic_wrong);
                }
                binding.prepaid.setText(jsonResponse.getString("prepaid"));

            } catch (Exception e) {
                binding.imgPrepaid.setImageResource(R.drawable.ic_error);
                binding.prepaid.setText(getResources().getString(R.string.dash));
            }
            try {
                binding.cAlpha2.setText(Objects.requireNonNull(countrydata).getString("alpha2"));
            } catch (Exception e) {
                binding.cAlpha2.setText(getResources().getString(R.string.dash));
            }
            try {
                binding.cName.setText(String.format("%s%s%s", countrydata.getString("name"), " ", countrydata.getString("emoji")));
            } catch (Exception e) {
                binding.cName.setText(getResources().getString(R.string.dash));
            }
            try {
                binding.currency.setText(countrydata.getString("currency"));
            } catch (Exception e) {
                binding.currency.setText(getResources().getString(R.string.dash));
            }
            try {
                binding.cLatitude.setText(countrydata.getString("latitude"));
            } catch (Exception e) {
                binding.cLatitude.setText(getResources().getString(R.string.dash));
            }
            try {
                binding.cLongitude.setText(countrydata.getString("longitude"));
            } catch (Exception e) {
                binding.cLongitude.setText(getResources().getString(R.string.dash));
            }

            try {
                binding.bName.setText(bankdata.getString("name"));
            } catch (Exception e) {
                binding.bName.setText(getResources().getString(R.string.dash));
            }
            try {
                binding.bUrl.setText(bankdata.getString("url"));
            } catch (Exception e) {
                binding.bUrl.setText(getResources().getString(R.string.dash));
            }
            try {
                binding.bPhone.setText(bankdata.getString("phone"));
            } catch (Exception e) {
                binding.bPhone.setText(getResources().getString(R.string.dash));
            }
        } catch (Exception ignored) {
            Snackbar.make(requireView(), "Bin Not Found", Snackbar.LENGTH_SHORT).setAnchorView(requireActivity().findViewById(R.id.bottom_nav)).show();
            dismiss();
        }
    }
}