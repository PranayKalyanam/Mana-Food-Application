package com.example.manafood.Fragment;

import android.os.Bundle;

//import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//import com.example.manafood.R;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.manafood.Adapter.MenuAdapter;
import com.example.manafood.databinding.FragmentMenuBottomSheetBinding;
import com.example.manafood.model.MenuItemModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class MenuBottomSheetFragment extends BottomSheetDialogFragment {

    FragmentMenuBottomSheetBinding binding;
    private final List<MenuItemModel> menuItemModelList = new ArrayList<>();
     FirebaseDatabase database;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMenuBottomSheetBinding.inflate(inflater, container,false);
        binding.backButton.setOnClickListener(v-> dismiss() );

        //get data from database
        retrieveItems();


        return binding.getRoot();


    }

    private void retrieveItems() {
        database = FirebaseDatabase.getInstance();
        DatabaseReference foodRef = database.getReference().child("menu");
        foodRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Clear existing data before populating
                menuItemModelList.clear();

                //loop for through each food item
                for (DataSnapshot foodSnapshot : snapshot.getChildren()) {
                    MenuItemModel menuItem = foodSnapshot.getValue(MenuItemModel.class);
                    if (menuItem != null) {
                        menuItemModelList.add(menuItem);
                    }
                }
                setAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void setAdapter() {
        MenuAdapter adapter = new MenuAdapter(menuItemModelList, requireContext());
        binding.menuRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.menuRecyclerView.setAdapter(adapter);

    }

}