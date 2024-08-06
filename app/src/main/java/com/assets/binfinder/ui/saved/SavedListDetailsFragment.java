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
import com.assets.binfinder.databinding.FragmentSavedListDetailsBinding;
import com.assets.binfinder.model.savedlist.SavedList;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;
import java.util.UUID;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SavedListDetailsFragment extends Fragment {
    FragmentSavedListDetailsBinding binding;
    SavedViewModel savedViewModel;
    SavedItemsRecyclerView adapter;
    SavedList savedList = new SavedList();
    String uuid;
    ItemTouchHelper itemTouchHelper;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        savedViewModel = new ViewModelProvider(requireActivity()).get(SavedViewModel.class);
        uuid = SavedListDetailsFragmentArgs.fromBundle(requireArguments()).getUuid();
        adapter = new SavedItemsRecyclerView(position -> Navigation.findNavController(requireView()).navigate(MobileNavigationDirections.actionGlobalSingleFragment().setBin(savedList.bins.get(position))));
        itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int position = viewHolder.getBindingAdapterPosition();
                SavedList deletedList = savedList.objectClone();
                savedViewModel.deleteFromSavedList(savedList.uuid, savedList.bins.get(position));
                savedList.bins.remove(position);
                if (savedList.bins.isEmpty()) {
                    binding.emptyView.setVisibility(View.VISIBLE);
                    binding.recyclerView.setVisibility(View.GONE);
                }
                adapter.notifyItemRemoved(position);

                Snackbar.make(binding.getRoot(), "Item removed", Snackbar.LENGTH_LONG).setAction("Undo", v -> {
                    savedViewModel.undoItemDeleted(deletedList);
                    savedList.bins.add(position, deletedList.bins.get(position));
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
        binding = FragmentSavedListDetailsBinding.inflate(inflater, container, false);
        binding.toolbar.setNavigationOnClickListener(v -> Navigation.findNavController(v).navigateUp());
        binding.recyclerView.setAdapter(adapter);
        itemTouchHelper.attachToRecyclerView(binding.recyclerView);
        savedViewModel.savedListInfo.observe(getViewLifecycleOwner(), listEvent -> {
            if (!listEvent.getHasBeenHandled()) {
                savedList = listEvent.getContentIfNotHandled();
                binding.toolbar.setTitle(Objects.requireNonNull(savedList).name);
                adapter.addItems(savedList.bins);
                if (savedList.bins == null || savedList.bins.isEmpty()) {
                    binding.emptyView.setVisibility(View.VISIBLE);
                    binding.recyclerView.setVisibility(View.GONE);
                } else {
                    binding.emptyView.setVisibility(View.GONE);
                    binding.recyclerView.setVisibility(View.VISIBLE);
                }
            }
        });
        binding.toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.edit) {
                Navigation.findNavController(requireView()).navigate(SavedListDetailsFragmentDirections.actionSavedListDetailsFragmentToEditDialog(uuid));
            }
            return true;
        });

        return binding.getRoot();

    }


    @Override
    public void onStart() {
        super.onStart();
        savedViewModel.getSavedList(UUID.fromString(uuid));
    }

    @Override
    public void onResume() {
        super.onResume();
        savedViewModel.getSavedList(UUID.fromString(uuid));
    }
}