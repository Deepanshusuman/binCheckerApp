package com.assets.binfinder.ui.about;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;


import com.assets.binfinder.databinding.FragmentAboutBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AboutFragment extends Fragment {


    FragmentAboutBinding binding;


    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAboutBinding.inflate(inflater, container, false);
        binding.text2.setMovementMethod(LinkMovementMethod.getInstance());
        binding.text3.setMovementMethod(LinkMovementMethod.getInstance());
        binding.cardvalidator.setMovementMethod(LinkMovementMethod.getInstance());
        binding.toolbar.setNavigationOnClickListener(v -> Navigation.findNavController(v).navigateUp());
        return binding.getRoot();
    }


}