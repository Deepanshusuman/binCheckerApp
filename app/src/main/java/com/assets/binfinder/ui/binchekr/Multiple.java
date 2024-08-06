package com.assets.binfinder.ui.binchekr;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.paging.LoadState;
import androidx.paging.PagingDataAdapter;

import com.assets.binfinder.BinOuterClass;
import com.assets.binfinder.R;
import com.assets.binfinder.databinding.FragmentMultipleBinding;

import java.util.Objects;


import dagger.hilt.android.AndroidEntryPoint;
import kotlin.Unit;

@AndroidEntryPoint
public class Multiple extends Fragment {
    FragmentMultipleBinding binding;
    BinchekrViewModel binchekrViewModel;
    MultipleAdapter multipleAdapter;
    BinOuterClass.SearchRequest.Builder searchRequest;


    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binchekrViewModel = new ViewModelProvider(this).get(BinchekrViewModel.class);
        binding = FragmentMultipleBinding.inflate(inflater, container, false);

        searchRequest = BinOuterClass.SearchRequest.newBuilder();
        MultipleArgs multipleArgs = MultipleArgs.fromBundle(requireArguments());


        if (!Objects.equals(multipleArgs.getNetwork(), "null")) {
            binding.topAppBar.setTitle(multipleArgs.getNetwork());
            searchRequest.setNetwork(multipleArgs.getNetwork());
        } else {
            searchRequest.setNetwork("");
        }

        if (!Objects.equals(multipleArgs.getCountry(), "null")) {
            binding.topAppBar.setTitle(multipleArgs.getCountry());
            searchRequest.setCountry(multipleArgs.getCountry());
        } else {
            searchRequest.setCountry("");
        }


        if (!Objects.equals(multipleArgs.getType(), "null")) {
            binding.topAppBar.setTitle(multipleArgs.getType());
            searchRequest.setType(multipleArgs.getType());
        } else {
            searchRequest.setType("");
        }


        if (!Objects.equals(multipleArgs.getProductName(), "null")) {
            binding.topAppBar.setTitle(multipleArgs.getProductName());
            searchRequest.setProductName(multipleArgs.getProductName());
        } else {
            searchRequest.setProductName("");
        }


        if (!Objects.equals(multipleArgs.getIssuer(), "null")) {
            binding.topAppBar.setTitle(multipleArgs.getIssuer());
            searchRequest.setIssuer(multipleArgs.getIssuer());
        } else {
            searchRequest.setIssuer("");
        }


        if (multipleArgs.getBin() != 1) {
            binding.topAppBar.setTitle(String.valueOf(multipleArgs.getBin()));
            searchRequest.setBin(multipleArgs.getBin());
        }


        binding.topAppBar.setNavigationOnClickListener(v -> Navigation.findNavController(v).navigateUp());
        if (multipleAdapter == null) {
            multipleAdapter = new MultipleAdapter(new BinComparator());
            multipleAdapter.setStateRestorationPolicy(PagingDataAdapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY);

            multipleAdapter.addLoadStateListener(loadStates -> {
                if (loadStates.getRefresh() instanceof LoadState.Error) {
                    LoadState.Error loadStateError = (LoadState.Error) loadStates.getRefresh();
                    binding.errorMsg.setText(loadStateError.getError().getMessage());
                }


                if (loadStates.getRefresh() instanceof LoadState.Error && Objects.equals(((LoadState.Error) loadStates.getRefresh()).getError().getLocalizedMessage(), "No more results")) {
                    binding.recyclerView.setVisibility(View.GONE);
                    binding.progressIndicator.setVisibility(View.GONE);
                    binding.btnRetry.setVisibility(View.GONE);
                    binding.errorMsg.setVisibility(View.VISIBLE);
                    binding.errorMsg.setText(binding.getRoot().getContext().getString(R.string.no_more_results));
                } else {
                    binding.recyclerView.setVisibility(loadStates.getRefresh() instanceof LoadState.NotLoading ? View.VISIBLE : View.GONE);
                    binding.progressIndicator.setVisibility(loadStates.getRefresh() instanceof LoadState.Loading ? View.VISIBLE : View.GONE);
                    binding.btnRetry.setVisibility(loadStates.getRefresh() instanceof LoadState.Error ? View.VISIBLE : View.GONE);
                    binding.errorMsg.setVisibility(loadStates.getSource().getRefresh() instanceof LoadState.Error ? View.VISIBLE : View.GONE);
                    binding.errorMsg.setText(binding.getRoot().getContext().getString(R.string.error_occurred));
                }
                return Unit.INSTANCE;
            });
        }

        binding.recyclerView.setAdapter(multipleAdapter.withLoadStateHeaderAndFooter(new LoaderAdapter(v -> multipleAdapter.retry()), new LoaderAdapter(v -> multipleAdapter.retry())));
        binding.btnRetry.setOnClickListener(view -> multipleAdapter.retry());

        binchekrViewModel.find_bin(searchRequest);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binchekrViewModel.binList.observe(getViewLifecycleOwner(), pagingData -> multipleAdapter.submitData(getLifecycle(), pagingData));
    }
}


