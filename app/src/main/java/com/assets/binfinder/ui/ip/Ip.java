package com.assets.binfinder.ui.ip;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.Navigation;


import com.assets.binfinder.ui.lookup.LookupFragmentDirections;
import com.assets.binfinder.databinding.FragmentIpBinding;

import java.net.InetAddress;
import java.util.Objects;


import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class Ip extends Fragment {
    FragmentIpBinding binding;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentIpBinding.inflate(inflater, container, false);
        MutableLiveData<String> ip = new MutableLiveData<>();
        binding.findButton.setOnClickListener(v -> {
            String IP = Objects.requireNonNull(binding.editText.getText()).toString();
            new Thread(() -> {
                try {
                    ip.postValue(InetAddress.getByName(IP).getHostAddress());
                } catch (Exception e) {
                    ip.postValue(null);
                }

            }).start();
        });
        binding.editText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String IP = Objects.requireNonNull(binding.editText.getText()).toString();
                new Thread(() -> {
                    try {
                        ip.postValue(InetAddress.getByName(IP).getHostAddress());
                    } catch (Exception e) {
                        ip.postValue(null);
                    }

                }).start();

                return true;
            }
            return false;
        });
        ip.observe(getViewLifecycleOwner(), s -> {
            if (s == null) {
                binding.editTextLayout.setError("Invalid IP");
            } else {
                binding.editTextLayout.setError(null);
                binding.editTextLayout.setHelperText(s);
                Navigation.findNavController(binding.getRoot()).navigate(LookupFragmentDirections.actionLookupFragmentToIpResponseDialog(s));
            }
        });

        return binding.getRoot();
    }


}