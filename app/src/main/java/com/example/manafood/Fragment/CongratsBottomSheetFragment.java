package com.example.manafood.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.manafood.MainActivity;
import com.example.manafood.databinding.FragmentCongratsBottomSheetBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class CongratsBottomSheetFragment extends BottomSheetDialogFragment {

    FragmentCongratsBottomSheetBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCongratsBottomSheetBinding.inflate(inflater, container, false);

        binding.homeButton.setOnClickListener(v->{
            Intent intent = new Intent(requireContext(), MainActivity.class);
            startActivity(intent);
        });
        return binding.getRoot();
    }
}