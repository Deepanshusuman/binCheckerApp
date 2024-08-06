package com.assets.binfinder.ui.single;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceManager;

import com.assets.binfinder.MainViewModel;
import com.assets.binfinder.R;
import com.assets.binfinder.databinding.FragmentSingleBinding;
import com.assets.binfinder.model.bin.Bin;
import com.assets.binfinder.model.savedlist.SavedList;
import com.assets.binfinder.ui.saved.DeviceViewModel;
import com.assets.binfinder.ui.saved.SavedViewModel;
import com.assets.binfinder.util.Card;
import com.assets.binfinder.util.CountryDetails;
import com.assets.binfinder.util.Validator;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SingleFragment extends Fragment {

    public Long start = 0L;

    @NonNull
    public List<SavedList> savedLists = new ArrayList<>();
    SharedPreferences pManager;
    FragmentSingleBinding binding;
    SingleViewModel singleViewModel;
    SavedViewModel savedViewModel;
    MainViewModel mainViewModel;
    DeviceViewModel deviceViewModel;


    @Inject
    SharedPreferences sharedPreferences;
    boolean isFavorite = false;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        singleViewModel = new ViewModelProvider(requireActivity()).get(SingleViewModel.class);
        savedViewModel = new ViewModelProvider(requireActivity()).get(SavedViewModel.class);
        deviceViewModel = new ViewModelProvider(requireActivity()).get(DeviceViewModel.class);

        pManager = PreferenceManager.getDefaultSharedPreferences(requireContext());
    }

    @NonNull
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSingleBinding.inflate(inflater, container, false);

        SingleFragmentArgs args = SingleFragmentArgs.fromBundle(requireArguments());
        start = args.getBin();
        new Thread(() -> {
            isFavorite = singleViewModel.database.favDao().hasFav(args.getBin());
            requireActivity().runOnUiThread(() -> {
                binding.toolbar.getMenu().findItem(R.id.favorite).setIcon(isFavorite ? R.drawable.ic_round_favorite_24 : R.drawable.ic_round_favorite_border_24);
            });
        }).start();
        binding.toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.save) {
                if (start != 0) {
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext());
                    builder.setTitle("Select a list");
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(requireContext(), com.google.android.material.R.layout.select_dialog_singlechoice_material);
                    arrayAdapter.add("Device");
                    builder.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss());
                    for (SavedList list : savedLists) {
                        arrayAdapter.add(list.name);
                    }

                    builder.setAdapter(arrayAdapter, (dialogInterface, i) -> {
                        if (i == 0) {
                            deviceViewModel.addBinToDevice(start);
                            Snackbar.make(requireView(), "Added to Device", Snackbar.LENGTH_SHORT).show();
                        } else {
                            savedViewModel.addBinToSavedList(savedLists.get(i - 1).uuid, start);
                            Snackbar.make(requireView(), "Saved", Snackbar.LENGTH_SHORT).show();
                        }
                    });
                    builder.show();


                } else {
                    Snackbar.make(requireView(), "No data to save", Snackbar.LENGTH_SHORT).show();
                }
                return true;
            } else if (item.getItemId() == R.id.favorite) {
                if (start != 0) {
                    if (isFavorite) {
                        singleViewModel.removeBinFromFav(start);
                        item.setIcon(R.drawable.ic_round_favorite_border_24);
                    } else {
                        singleViewModel.addBinToFav(start);
                        item.setIcon(R.drawable.ic_round_favorite_24);
                    }
                    isFavorite = !isFavorite;
                } else {
                    Snackbar.make(requireView(), "No data to add", Snackbar.LENGTH_SHORT).show();
                }

                return true;
            }
            return false;
        });
        singleViewModel.getBinDetails(args.getBin());
        savedViewModel.getSingleSavedList();
        if (!pManager.getBoolean("incognitoMode", false)) {
            mainViewModel.addHistory(args.getBin(), System.currentTimeMillis());
        }


        var image = sharedPreferences.getString("image", null);
        if (image != null) {
            byte[] imageAsBytes = Base64.decode(image.getBytes(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
            Drawable d = new BitmapDrawable(getResources(), decodedByte);
            binding.front.setBackground(d);
        }
        savedViewModel.singleSavedLists.observe(getViewLifecycleOwner(), savedLists1 -> savedLists = savedLists1);
        singleViewModel.singleBin.observe(getViewLifecycleOwner(), bin -> {

            updateCardDetails(bin);
            updateAdditionalDetails(bin);

        });

        binding.toolbar.setNavigationOnClickListener(v -> Navigation.findNavController(v).navigateUp());
        return binding.getRoot();
    }

    private void updateCardDetails(Bin bin) {
        Card c = Validator.findType(String.valueOf(bin.start));
        binding.card.setText(c.bin);
        binding.cardIssuer.setText(bin != null ? bin.issuer : "");
        binding.issuerName.setText(bin != null ? bin.issuer : "");
        binding.range.setText(bin.start + " - " + bin.end);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
        binding.lastUpdated.setText("Last Updated: " + sdf.format(new Date(bin.updatedAt)));
        if (bin.info != null) {
            binding.linearMore.setVisibility(View.VISIBLE);
            binding.moreInfo.setText(bin.info);
        }
        binding.length.setText(Arrays.toString(c.lengths));
        binding.cvvcodename.setText(c.codeName);
        binding.cvvcodenumber.setText(Arrays.toString(c.codeSize));
        binding.cardType.setText(bin.type);
        binding.cardProduct.setText(bin.productName);
        binding.cardNetwork.setText(bin.network);
        binding.productName.setText(bin.productName);
        binding.network.setText(bin.network);
        binding.type.setText(bin.type);
    }

    private void updateAdditionalDetails(Bin bin) {
        if (bin.country != null) {
            CountryDetails.Country country = CountryDetails.getData(bin.country);
            binding.countryName.setText(String.format(Locale.getDefault(), "%s %s (%s)", country.getEmoji(), country.getName(), country.getAlpha2()));
            binding.capital.setText(country.getCapital());
            binding.region.setText(country.getRegion());
            binding.currencyInfo.setText(String.format(Locale.getDefault(), "%s (%s)", country.getCurrencyName(), country.getCurrencyCode()));
            binding.callingCode.setText("+ ".concat(String.valueOf(country.getCallingCode())));
            binding.language.setText(country.getLanguage());
            binding.startOfWeek.setText(country.getStartofWeek());
            binding.postalCodeFormat.setText(country.getPostalCodeFormat());
            if (PreferenceManager.getDefaultSharedPreferences(requireContext()).getBoolean("showTimezone", false)) {
                binding.showTimezone.setVisibility(View.VISIBLE);
                binding.timezones.setAdapter(new TimezoneRecycler(country.getTimezones()));
            }
        }
    }
}