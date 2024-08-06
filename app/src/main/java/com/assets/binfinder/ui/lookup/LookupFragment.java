package com.assets.binfinder.ui.lookup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.assets.binfinder.MainViewModel;
import com.assets.binfinder.R;
import com.assets.binfinder.databinding.FragmentLookupBinding;
import com.assets.binfinder.ui.Generate.Generate;
import com.assets.binfinder.ui.Ifsc.Ifsc;
import com.assets.binfinder.ui.binchekr.Binchekr;
import com.assets.binfinder.ui.binlist.Binlist;
import com.assets.binfinder.ui.bulk.Bulk;
import com.assets.binfinder.ui.byCountry.Country;
import com.assets.binfinder.ui.byIssuer.Issuer;
import com.assets.binfinder.ui.byNetwork.Network;
import com.assets.binfinder.ui.byProductName.Product;
import com.assets.binfinder.ui.byTypes.Type;
import com.assets.binfinder.ui.ip.Ip;
import com.assets.binfinder.ui.randomuser.RandomUser;
import com.assets.binfinder.ui.seon.Seon;
import com.assets.binfinder.ui.validate.Validate;
import com.google.android.material.chip.Chip;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.Objects;


import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class LookupFragment extends Fragment {
    FragmentManager fragmentManager;
    LookupViewModel lookupViewModel;

    MainViewModel mainViewModel;
    private FragmentLookupBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lookupViewModel = new ViewModelProvider(requireActivity()).get(LookupViewModel.class);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        fragmentManager = getChildFragmentManager();
    }

    @NonNull
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLookupBinding.inflate(inflater, container, false);
        binding.toolbar.setTitle(mainViewModel.setGreeting());
        binding.toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.historyFragment) {
                Navigation.findNavController(requireView()).navigate(LookupFragmentDirections.actionLookupFragmentToHistoryFragment());
                return true;
            } else if (item.getItemId() == R.id.menuFragment) {
                Navigation.findNavController(requireView()).navigate(LookupFragmentDirections.actionLookupFragmentToMenuFragment());
                return true;
            }
            return false;
        });
        binding.basic.setOnClickListener(view -> {
            loadFragment(new Binchekr(), "Binchekr");
            binding.basic.setChecked(true);
        });


        String selectedTool = lookupViewModel.getSelectedTool();
        ArrayList<String> tools = lookupViewModel.getToolsSelected();
        if (tools.isEmpty()) {
            loadFragment(new Binchekr(), "Binchekr");
            binding.basic.setChecked(true);
        } else {
            for (String chips : tools) {

                addChip(chips, selectedTool);
            }
        }


        binding.add.setOnClickListener(view -> {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext());
            builder.setTitle("Select a Tool");
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(requireContext(), com.google.android.material.R.layout.select_dialog_singlechoice_material);
            arrayAdapter.addAll(lookupViewModel.getTools());
            builder.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss());

            builder.setAdapter(arrayAdapter, (dialogInterface, i) -> {
                lookupViewModel.addChip(arrayAdapter.getItem(i));
                addChip(arrayAdapter.getItem(i), arrayAdapter.getItem(i));
            });

            builder.show();
        });
        return binding.getRoot();
    }

    void addChip(String s, String selectedTool) {
        Chip chip = new Chip(requireContext());
        chip.setCheckable(true);
        chip.setText(s);
        chip.setId(ViewCompat.generateViewId());
        chip.setCloseIconVisible(true);
        int c = binding.chipGroup.getChildCount();
        binding.chipGroup.addView(chip, c - 1);
        if (Objects.equals(s, selectedTool)) {
            load(s);
            chip.setChecked(true);
        } else {
            loadFragment(new Binchekr(), "Binchekr");
            binding.basic.setChecked(true);
        }
        if (c == 13) {
            binding.add.setVisibility(View.GONE);
        }
        chip.setOnClickListener(v -> {
            load(s);
            chip.setChecked(true);
        });
        chip.setOnCloseIconClickListener(v -> {
            binding.chipGroup.removeView(chip);
            lookupViewModel.removeChip(s);
            binding.add.setVisibility(View.VISIBLE);
            int a = binding.chipGroup.getChildCount();
            if (a > 1) {
                binding.chipGroup.getChildAt(a - 2).performClick();
            } else {
                binding.basic.performClick();
            }

        });
    }

    public void load(@NonNull String s) {
        switch (s) {
            case "By Type":
                loadFragment(new Type(), "By Type");
                break;
            case "Binlist.net":
                loadFragment(new Binlist(), "Binlist.net");
                break;
            case "Seon.io":
                loadFragment(new Seon(), "Seon.io");
                break;

            case "Ip Details":
                loadFragment(new Ip(), "Ip Details");
                break;
            case "Validate":
                loadFragment(new Validate(), "Validate");
                break;
            case "By Country":
                loadFragment(new Country(), "By Country");
                break;
            case "By Issuer":
                loadFragment(new Issuer(), "By Issuer");
                break;
            case "By Product":
                loadFragment(new Product(), "By Product");
                break;
            case "By Network":
                loadFragment(new Network(), "By Network");
                break;
            case "IFSC":
                loadFragment(new Ifsc(), "IFSC");
                break;
            case "Generate":
                loadFragment(new Generate(), "Generate");
                break;
            case "Random User":
                loadFragment(new RandomUser(), "Random User");
                break;
            case "Bulk":
                loadFragment(new Bulk(), "Bulk");
                break;

        }
    }

    private void loadFragment(Fragment fragment, String name) {
        lookupViewModel.setSelectedTool(name);
        FragmentManager fragmentManager = getChildFragmentManager();
        if (fragmentManager.findFragmentByTag(name) != null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragment_container, Objects.requireNonNull(fragmentManager.findFragmentByTag(name)), name).commit();
        } else {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragment_container, fragment, name).commit();
        }
    }

}