package com.assets.binfinder.ui.saved;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.assets.binfinder.MobileNavigationDirections;
import com.assets.binfinder.R;
import com.assets.binfinder.databinding.FragmentSavedDeviceBinding;
import com.assets.binfinder.model.device.Device;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SavedDevice extends Fragment {
    List<Long> list;
    FragmentSavedDeviceBinding binding;
    DeviceViewModel deviceViewModel;
    SavedItemsRecyclerView adapter;
    ItemTouchHelper itemTouchHelper;
    Long device;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        deviceViewModel = new ViewModelProvider(requireActivity()).get(DeviceViewModel.class);
        adapter = new SavedItemsRecyclerView(position -> Navigation.findNavController(requireView()).navigate(MobileNavigationDirections.actionGlobalSingleFragment().setBin(list.get(position))));
        itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int position = viewHolder.getBindingAdapterPosition();
                deviceViewModel.removeBinFromDevice(list.get(position));
                device = list.get(position);
                list.remove(position);

                if (list.size() == 0) {
                    binding.emptyView.setVisibility(View.VISIBLE);
                    binding.recyclerView.setVisibility(View.GONE);
                }
                adapter.notifyItemRemoved(position);

                Snackbar.make(binding.getRoot(), "Item removed", Snackbar.LENGTH_LONG).setAction("Undo", v -> {
                    deviceViewModel.addBinToDevice(device);
                    list.add(position, device);
                    adapter.notifyItemInserted(position);
                    binding.emptyView.setVisibility(View.GONE);
                    binding.recyclerView.setVisibility(View.VISIBLE);
                }).show();
            }

        });

    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSavedDeviceBinding.inflate(inflater, container, false);
        binding.toolbar.setNavigationOnClickListener(v -> Navigation.findNavController(v).navigateUp());
        binding.recyclerView.setAdapter(adapter);
        itemTouchHelper.attachToRecyclerView(binding.recyclerView);
        deviceViewModel.favorites.observe(getViewLifecycleOwner(), event -> {
            if (!event.getHasBeenHandled()) {
                List<Device> binList = event.getContentIfNotHandled();
                if (binList == null || binList.isEmpty()) {
                    binding.emptyView.setVisibility(View.VISIBLE);
                    binding.recyclerView.setVisibility(View.GONE);
                } else {
                    list = new ArrayList<>();
                    for (Device fav1 : binList) {
                        list.add(fav1.bin);
                    }
                    adapter.addItems(list);
                    binding.emptyView.setVisibility(View.GONE);
                    binding.recyclerView.setVisibility(View.VISIBLE);
                }
            }
        });


        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        deviceViewModel.getDevice();
    }

    @Override
    public void onResume() {
        super.onResume();
        deviceViewModel.getDevice();
    }
}
