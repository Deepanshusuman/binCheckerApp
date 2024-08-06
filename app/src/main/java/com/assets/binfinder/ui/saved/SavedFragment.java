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

import com.assets.binfinder.R;
import com.assets.binfinder.databinding.FragmentSavedBinding;
import com.assets.binfinder.model.savedlist.SavedList;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SavedFragment extends Fragment {
    SavedViewModel savedViewModel;
    List<SavedList> savedLists = new ArrayList<>();
    SavedRecyclerView adapter;
    ItemTouchHelper itemTouchHelper;

    FragmentSavedBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        savedViewModel = new ViewModelProvider(requireActivity()).get(SavedViewModel.class);
        adapter = new SavedRecyclerView(position -> Navigation.findNavController(requireView()).navigate(SavedFragmentDirections.actionSavedListFragmentToSavedListDetailsFragment(savedLists.get(position).uuid.toString())));
        adapter.setData(savedLists);
        itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int position = viewHolder.getBindingAdapterPosition();
                savedViewModel.deleteNameFromSavedList(savedLists.get(position).uuid);
                SavedList deletedItem = savedLists.get(position);
                savedLists.remove(position);
                adapter.setNewData(savedLists);
                if (savedLists.isEmpty()) {
                    binding.recyclerView.setVisibility(View.GONE);
                }
                Snackbar.make(binding.getRoot(), "Deleted", Snackbar.LENGTH_LONG).setAction("Undo", v -> {
                    savedViewModel.undoDelete(deletedItem);
                    savedLists.add(position, deletedItem);
                    adapter.setNewData(savedLists);
                    if (!savedLists.isEmpty()) {
                        binding.recyclerView.setVisibility(View.VISIBLE);
                    }
                }).setAnchorView(requireActivity().findViewById(R.id.bottom_nav)).show();

            }
        });


    }

    @NonNull
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSavedBinding.inflate(inflater, container, false);

        binding.deviceCard.setOnClickListener(v -> {
            Navigation.findNavController(requireView()).navigate(SavedFragmentDirections.actionSavedListFragmentToSavedDevice());
        });

        binding.favoriteCard.setOnClickListener(v -> {
            Navigation.findNavController(requireView()).navigate(SavedFragmentDirections.actionSavedListFragmentToSavedFavorite());


        });
        itemTouchHelper.attachToRecyclerView(binding.recyclerView);
        binding.toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.add) {
//                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
//                    Navigation.findNavController(requireView()).navigate(SavedFragmentDirections.actionSavedListFragmentToAddDialog());
//                } else {
//                    Snackbar.make(binding.getRoot(), "You need to sign in to create a new list", Snackbar.LENGTH_SHORT).setAnchorView(requireActivity().findViewById(R.id.adView)).show();
//                }
                Navigation.findNavController(requireView()).navigate(SavedFragmentDirections.actionSavedListFragmentToAddDialog());

                return true;
            }
            return false;
        });

        binding.recyclerView.setAdapter(adapter);
        savedViewModel.savedLists.observe(getViewLifecycleOwner(), listEvent -> {
            if (!listEvent.getHasBeenHandled()) {
                savedLists = listEvent.getContentIfNotHandled();
                if (savedLists == null || savedLists.isEmpty()) {
                    binding.recyclerView.setVisibility(View.GONE);
                } else {
                    binding.recyclerView.setVisibility(View.VISIBLE);
                    binding.recyclerView.setAdapter(adapter);
                    adapter.setNewData(savedLists);
                }
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        savedViewModel.getSavedList();
    }

    @Override
    public void onResume() {
        super.onResume();
        savedViewModel.getSavedList();
    }
}