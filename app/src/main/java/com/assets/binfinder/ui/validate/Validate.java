package com.assets.binfinder.ui.validate;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;

import com.assets.binfinder.R;
import com.assets.binfinder.databinding.FragmentValidateBinding;
import com.assets.binfinder.util.Card;
import com.assets.binfinder.util.Validator;

import java.util.Arrays;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class Validate extends Fragment {
    FragmentValidateBinding binding;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentValidateBinding.inflate(inflater, container, false);
        binding.validateTextInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Card card = Validator.findType(s.toString());
                boolean aBoolean = Validator.luhnCheck(s.toString());
                binding.validateTextInputLayout.setStartIconTintList(null);
                binding.validateTextInputLayout.setStartIconDrawable(card.drawable);
                boolean containsLength = false;
                for (int length : card.lengths) {
                    if (length == s.length()) {
                        containsLength = true;
                        break;
                    }
                }

                binding.linearLayout.setVisibility(aBoolean && containsLength ? LinearLayoutCompat.VISIBLE : LinearLayoutCompat.GONE);


                binding.linearCvv.setVisibility(aBoolean && card.codeSize.length != 0 ? LinearLayoutCompat.VISIBLE : LinearLayoutCompat.GONE);
                binding.cvvCode.setText(Arrays.toString(card.codeSize));
                binding.cvvName.setText(card.codeName);
                binding.isValid.setTextColor(requireContext().getColor(aBoolean ? R.color.green : R.color.red));
                binding.isValid.setText(aBoolean ? R.string.text_true : R.string.text_false);
                binding.cardType.setText(card.network);
                binding.length.setText(Arrays.toString(card.lengths));
                binding.cardNumber.setText(card.bin);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return binding.getRoot();
    }


}