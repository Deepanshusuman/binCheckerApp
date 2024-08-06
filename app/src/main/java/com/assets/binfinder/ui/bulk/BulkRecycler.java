package com.assets.binfinder.ui.bulk;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceManager;

import com.assets.binfinder.MobileNavigationDirections;
import com.assets.binfinder.databinding.FragmentBulkRecyclerBinding;
import com.assets.binfinder.model.Database;
import com.assets.binfinder.model.bin.Bin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.grpc.stub.StreamObserver;

@AndroidEntryPoint
public class BulkRecycler extends Fragment {
    FragmentBulkRecyclerBinding binding;
    BulkAdapter adapter;
    @Inject
    Database db;
    List<Bin> bins;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bins = new ArrayList<>();
        adapter = new BulkAdapter(position -> Navigation.findNavController(binding.getRoot()).navigate(MobileNavigationDirections.actionGlobalSingleFragment().setBin(bins.get(position).start)));
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBulkRecyclerBinding.inflate(inflater, container, false);
        binding.topAppBar.setNavigationOnClickListener(v -> Navigation.findNavController(binding.getRoot()).navigateUp());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.recyclerView.setAdapter(adapter);
    }


    @Override
    public void onStart() {
        super.onStart();

        if (bins.isEmpty()) {
            BulkRecyclerArgs args = BulkRecyclerArgs.fromBundle(requireArguments());
            List<Long> list = Arrays.stream(args.getBins()).boxed().collect(Collectors.toList());
            SharedPreferences preferenceManager = PreferenceManager.getDefaultSharedPreferences(requireContext());
            new Thread(() -> {
                for (long l : list) {
                    Log.d("TAG", "onStart: " + l);
                    var d = db.binDao().getBinDetails(l);


                    Log.d("TAG", "onStart: " + d);
                    if (d != null) {
                        bins.add(d);
                        requireActivity().runOnUiThread(() -> adapter.addBin(d));
                    }

                }
            }).start();
//            login.getStub().bulkLookup(BinOuterClass.BulkRequest.newBuilder().addAllBin(list)
//                    .setIncognito(preferenceManager.getBoolean("incognitoMode", false)
//            ).build(), new StreamObserver<>() {
//                @Override
//                public void onNext(BinOuterClass.Bin value) {
//                    bins.add(value);
//                    requireActivity().runOnUiThread(() -> adapter.addBin(value));
//                }
//
//                @Override
//                public void onError(Throwable t) {
//                    t.printStackTrace();
//                }
//
//                @Override
//                public void onCompleted() {
//
//                }
//            });
        }

    }


}
