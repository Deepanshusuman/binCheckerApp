package com.assets.binfinder;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.assets.binfinder.databinding.FragmentMenuBinding;


public class MenuFragment extends Fragment {
    @NonNull
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        com.assets.binfinder.databinding.FragmentMenuBinding binding = FragmentMenuBinding.inflate(inflater, container, false);
        binding.listView.setOnItemClickListener((adapterView, view, i, l) -> {
            switch (i) {
                case 0 ->
                        Navigation.findNavController(requireView()).navigate(MenuFragmentDirections.actionMenuFragmentToSettingsFragment());
                case 1 ->
                        Navigation.findNavController(requireView()).navigate(MenuFragmentDirections.actionMenuFragmentToAboutFragment());

                case 2 -> {
                    Intent selectorIntent = new Intent(Intent.ACTION_SENDTO);
                    selectorIntent.setData(Uri.parse("mailto:"));
                    final Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"geeklord000@gmail.com"});
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "I am Erno Pap");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "Hi, I am having trouble with Bin Checker: ");
                    emailIntent.setSelector(selectorIntent);
                    startActivity(Intent.createChooser(emailIntent, "Send email..."));
                }
            }
        });
        return binding.getRoot();
    }


}