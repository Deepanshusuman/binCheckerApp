package com.assets.binfinder.ui.randomuser;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import com.assets.binfinder.R;
import com.assets.binfinder.databinding.FragmentRandomUserBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;


import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RandomUser extends Fragment {
    RandomUserViewModel randomUserViewModel;
    FragmentRandomUserBinding binding;

    String gender, nat;
    String image;
    String json;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        randomUserViewModel = new ViewModelProvider(requireActivity()).get(RandomUserViewModel.class);
        gender = randomUserViewModel.sharedPreferences.getString("gender", "male");
        nat = randomUserViewModel.sharedPreferences.getString("nat", "IN");
        randomUserViewModel.get("?gender=" + gender + "&nat=" + nat);
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRandomUserBinding.inflate(inflater, container, false);
        binding.shuffleButton.setOnClickListener(view -> randomUserViewModel.get("?gender=" + gender + "&nat=" + nat));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.downloadImage.setOnClickListener(v -> Glide.with(requireContext()).asBitmap().load(image).into(new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                saveImage(resource);
                Snackbar.make(binding.getRoot(), "Image saved", Snackbar.LENGTH_SHORT).setAnchorView(requireActivity().findViewById(R.id.bottom_nav)).show();
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {
            }
        }));

        binding.editGender.setText(gender);
        binding.editCountry.setText(nat);
        binding.editGender.setOnItemClickListener((adapterView, view1, i, l) -> {
            gender = adapterView.getItemAtPosition(i).toString();
            randomUserViewModel.sharedPreferences.edit().putString("gender", gender).apply();
        });
        binding.editCountry.setOnItemClickListener((adapterView, view1, i, l) -> {
            nat = adapterView.getItemAtPosition(i).toString();
            randomUserViewModel.sharedPreferences.edit().putString("nat", nat).apply();
        });


        randomUserViewModel.data.observe(getViewLifecycleOwner(), event -> {
            if (!event.getHasBeenHandled()) {
                json = event.getContentIfNotHandled();
                setData(json);

            }
        });
    }

    void setData(String json) {

        if (json != null) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                JSONObject results = jsonObject.getJSONArray("results").getJSONObject(0);
                JSONObject name = results.getJSONObject("name");
                binding.txtName.setText(name.getString("first") + " " + name.getString("last"));
                binding.txtEmail.setText(results.getString("email"));
                binding.txtPhone.setText(results.getString("phone"));
                binding.txtAddress.setText(results.getJSONObject("location").getJSONObject("street").getString("number") + " " + results.getJSONObject("location").getJSONObject("street").getString("name"));
                binding.txtCell.setText(results.getString("cell"));
                binding.txtCity.setText(results.getJSONObject("location").getString("city"));
                binding.txtState.setText(results.getJSONObject("location").getString("state"));
                binding.txtCountry.setText(results.getJSONObject("location").getString("country"));
                binding.txtPostcode.setText(results.getJSONObject("location").getString("postcode"));
                binding.txtDOB.setText(results.getJSONObject("dob").getString("date").split("T")[0]);
                binding.txtUsername.setText(results.getJSONObject("login").getString("username"));
                binding.txtPassword.setText(results.getJSONObject("login").getString("password"));
                image = results.getJSONObject("picture").getString("large");

                binding.downloadImage.setEnabled(true);
                Glide.with(requireContext()).load(image).into(binding.accountImage);
                binding.txtAge.setText(results.getJSONObject("dob").getString("age"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Snackbar.make(binding.getRoot(), "Error", Snackbar.LENGTH_SHORT).setAnchorView(requireActivity().findViewById(R.id.bottom_nav)).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (json == null) {
            randomUserViewModel.get("?gender=" + gender + "&nat=" + nat);
        } else {
            setData(json);
        }
    }

    void saveImage(Bitmap bitmap) {
        if (android.os.Build.VERSION.SDK_INT >= 29) {
            ContentValues values = contentValues();
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/" + getString(R.string.app_name));
            values.put(MediaStore.Images.Media.IS_PENDING, true);

            Uri uri = requireActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            if (uri != null) {
                try {
                    saveImageToStream(bitmap, requireActivity().getContentResolver().openOutputStream(uri));
                    values.put(MediaStore.Images.Media.IS_PENDING, false);
                    requireActivity().getContentResolver().update(uri, values, null, null);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        } else {
            File directory = new File(Environment.getExternalStorageDirectory().toString() + '/' + getString(R.string.app_name));

            if (!directory.exists()) {
                directory.mkdirs();
            }
            String fileName = System.currentTimeMillis() + ".png";
            File file = new File(directory, fileName);
            try {
                saveImageToStream(bitmap, new FileOutputStream(file));
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
                requireActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    private ContentValues contentValues() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        }
        return values;
    }

    private void saveImageToStream(Bitmap bitmap, OutputStream outputStream) {
        if (outputStream != null) {
            try {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}