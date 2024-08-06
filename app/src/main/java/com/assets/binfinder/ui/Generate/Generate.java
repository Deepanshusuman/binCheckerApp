package com.assets.binfinder.ui.Generate;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.assets.binfinder.R;
import com.assets.binfinder.databinding.FragmentGenerateBinding;
import com.assets.binfinder.util.Validator;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Objects;


import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class Generate extends Fragment {
    String cvv = "Random", month = "Random", year = "Random";
    int bin, length, total;

    FragmentGenerateBinding binding;
    private StringBuilder s;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentGenerateBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    //enable copy button when text is entered
    public void enableCopyButton() {
        binding.layoutResult.setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) binding.layoutResult.findViewById(com.google.android.material.R.id.text_input_end_icon).getLayoutParams();
        params.gravity = Gravity.TOP;
        binding.layoutResult.findViewById(com.google.android.material.R.id.text_input_end_icon).setLayoutParams(params);
        binding.layoutResult.setEndIconOnClickListener(v -> copy());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnGenerate.setOnClickListener(this::onClick);
        binding.btnClear.setOnClickListener(v -> clear());
        binding.editFormat.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                onClick(v);
                return true;
            }

            return false;
        });

        binding.editBin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.btnGenerate.setEnabled(s.length() >= 1);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void clearErrors() {
        binding.layoutBin.setError(null);
        binding.layoutTotal.setError(null);
        binding.layoutLength.setError(null);

    }


    private void clear() {
        binding.layoutResult.setEndIconMode(TextInputLayout.END_ICON_NONE);
        binding.result.setText(null);
        binding.editBin.setText(R.string._4);
        binding.editLength.setText(R.string._16);
        binding.editTotal.setText(R.string._100);
        binding.editCvv.setText(null);
        binding.editMonth.setText(null);
        binding.editYear.setText(null);
        binding.editFormat.setText(R.string._pipe);
        clearErrors();
    }

    private void copy() {
        ClipboardManager clipboard = (ClipboardManager) requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Data", s);
        Objects.requireNonNull(clipboard).setPrimaryClip(clip);
        Snackbar.make(requireView(), getResources().getString(R.string.copied), Snackbar.LENGTH_SHORT).setAnchorView(requireActivity().findViewById(R.id.bottom_nav)).show();

    }

    private void onClick(View v) {
        binding.btnClear.setEnabled(true);
        clearErrors();
        if (Objects.requireNonNull(binding.editLength.getText()).toString().isEmpty()) {
            binding.editLength.requestFocus();
            binding.layoutLength.setError(getResources().getString(R.string.empty));
        } else if (Objects.requireNonNull(binding.editBin.getText()).toString().isEmpty()) {
            binding.editBin.requestFocus();
            binding.layoutBin.setError(getResources().getString(R.string.empty));
        } else if (Objects.requireNonNull(binding.editTotal.getText()).toString().isEmpty()) {
            binding.editTotal.requestFocus();
            binding.layoutTotal.setError(getResources().getString(R.string.empty));
        } else if (Integer.parseInt(binding.editTotal.getText().toString()) < 1) {
            binding.editTotal.requestFocus();
            binding.layoutTotal.setError(getResources().getString(R.string.lessthan1));
        } else if (Integer.parseInt(binding.editLength.getText().toString()) < 6) {
            binding.editLength.requestFocus();
            binding.layoutLength.setError(getResources().getString(R.string.lessthan6));
        } else {

            enableCopyButton();
            try {
                InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(Objects.requireNonNull(requireActivity().getCurrentFocus()).getWindowToken(), 0);
            } catch (Exception ignored) {
            }

            try {
                bin = Integer.parseInt(Objects.requireNonNull(binding.editBin.getText()).toString());
            } catch (Exception e) {
                bin = 4;
            }
            try {
                length = Integer.parseInt(Objects.requireNonNull(binding.editLength.getText()).toString());
            } catch (Exception e) {
                length = 16;
            }

            try {
                total = Integer.parseInt(Objects.requireNonNull(binding.editTotal.getText()).toString());
            } catch (Exception e) {
                total = 1;
            }

            var cvvStr = binding.editCvv.getText().toString();
            var monthStr = binding.editMonth.getText().toString();
            var yearStr = binding.editYear.getText().toString();
            var format = binding.editFormat.getText().toString();
            if (cvvStr.isEmpty()) cvv = "Random";
            else if (cvvStr.equals("Random") || cvvStr.equals("No Cvv")) {
                cvv = cvvStr;
            } else {
                try {
                    cvv = String.valueOf(Integer.parseInt(cvvStr));
                } catch (Exception e) {
                    cvv = "Random";
                }
            }

            if (monthStr.isEmpty()) month = "Random";
            else if (monthStr.equals("Random") || monthStr.equals("No Month")) {
                month = monthStr;
            } else {
                try {
                    month = String.valueOf(Integer.parseInt(monthStr));
                } catch (Exception e) {
                    month = "Random";
                }
            }


            if (yearStr.isEmpty()) year = "Random";
            else if (yearStr.equals("Random") || yearStr.equals("No Year")) {
                year = yearStr;
            } else {
                try {
                    year = String.valueOf(Integer.parseInt(yearStr));
                } catch (Exception e) {
                    year = "Random";
                }
            }


            ArrayList<String> json = Validator.createCards(String.valueOf(bin), length, total, cvv, month, year, format);
            s = new StringBuilder();

            for (String s : json) {
                this.s.append(s).append("\n");
            }

            binding.result.setText(s.toString().trim());
            binding.btnGenerate.setEnabled(true);
        }
    }
}