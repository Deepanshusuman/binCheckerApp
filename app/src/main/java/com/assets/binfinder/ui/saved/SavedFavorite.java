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
import com.assets.binfinder.databinding.FragmentSavedFavoriteBinding;
import com.assets.binfinder.model.fav.Fav;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SavedFavorite extends Fragment {

    List<Long> list;
    FragmentSavedFavoriteBinding binding;
    FavoriteViewModel favoriteViewModel;
    SavedItemsRecyclerView adapter;
    ItemTouchHelper itemTouchHelper;
    Long fav;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        favoriteViewModel = new ViewModelProvider(requireActivity()).get(FavoriteViewModel.class);
        adapter = new SavedItemsRecyclerView(position -> Navigation.findNavController(requireView()).navigate(MobileNavigationDirections.actionGlobalSingleFragment().setBin(list.get(position))));
        itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int position = viewHolder.getBindingAdapterPosition();
                favoriteViewModel.removeBinFromFav(list.get(position));
                fav = list.get(position);
                list.remove(position);

                if (list.isEmpty()) {
                    binding.emptyView.setVisibility(View.VISIBLE);
                    binding.recyclerView.setVisibility(View.GONE);
                }
                adapter.notifyItemRemoved(position);

                Snackbar.make(binding.getRoot(), "Item removed", Snackbar.LENGTH_LONG).setAction("Undo", v -> {
                    favoriteViewModel.addBinToFav(fav);
                    list.add(position, fav);
                    adapter.notifyItemInserted(position);
                    binding.emptyView.setVisibility(View.GONE);
                    binding.recyclerView.setVisibility(View.VISIBLE);
                }).setAnchorView(requireActivity().findViewById(R.id.bottom_nav)).show();
            }

        });
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSavedFavoriteBinding.inflate(inflater, container, false);
        binding.toolbar.setNavigationOnClickListener(v -> Navigation.findNavController(v).navigateUp());
        binding.recyclerView.setAdapter(adapter);
        itemTouchHelper.attachToRecyclerView(binding.recyclerView);
        favoriteViewModel.favorites.observe(getViewLifecycleOwner(), event -> {
            if (!event.getHasBeenHandled()) {
                List<Fav> binList = event.getContentIfNotHandled();
                if (binList == null || binList.size() == 0) {
                    binding.emptyView.setVisibility(View.VISIBLE);
                    binding.recyclerView.setVisibility(View.GONE);
                } else {
                    list = new ArrayList<>();
                    for (Fav fav1 : binList) {
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
        favoriteViewModel.getFav();
    }

    @Override
    public void onResume() {
        super.onResume();
        favoriteViewModel.getFav();
    }
}