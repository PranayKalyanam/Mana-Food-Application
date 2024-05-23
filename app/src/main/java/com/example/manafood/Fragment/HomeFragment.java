package com.example.manafood.Fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.manafood.Adapter.MenuAdapter;
import com.example.manafood.R;
import com.example.manafood.databinding.FragmentHomeBinding;
import com.example.manafood.model.MenuItemModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class HomeFragment extends Fragment {


    FragmentHomeBinding binding;
     FirebaseDatabase database;
    private final List<MenuItemModel> menuItems = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        binding.viewMenu.setOnClickListener(v -> {
            MenuBottomSheetFragment bottomSheetDialog = new MenuBottomSheetFragment();
            bottomSheetDialog.show(getParentFragmentManager(), "Test");
        });

        //get data from database
        retrievePopularItems();

        return binding.getRoot();
    }

    private void retrievePopularItems() {
        // Clear existing data before populating
        menuItems.clear();
        //get reference to the databases
        database = FirebaseDatabase.getInstance();
        DatabaseReference foodRef = database.getReference().child("menu");

        // retrieve menu items from the database
        foodRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //loop for through each food item
                for (DataSnapshot foodSnapshot : snapshot.getChildren()) {
                    MenuItemModel menuItem = foodSnapshot.getValue(MenuItemModel.class);
                    if (menuItem != null) {
                        menuItems.add(menuItem);
                    }
                }
                //display a random popular items
                randomPopularItems();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void randomPopularItems() {
        //shuffle list indices of menu items
        List<Integer> index = new ArrayList<>();
        for (int i=0; i< menuItems.size(); i++){
            index.add(i);
        }
        Collections.shuffle(index);

        //store the first 6 menu items using random indices
        int numOfItemsToSHow = 6;
        List<MenuItemModel> subsetMenuItems =new ArrayList<>();
        for(int i=0; i< index.size()&&i<numOfItemsToSHow; i++){
            subsetMenuItems.add((menuItems.get(index.get(i))));
        }

        setPopularItemsAdapter(subsetMenuItems);
    }

    private void setPopularItemsAdapter(List<MenuItemModel> subsetMenuItems) {
        MenuAdapter adapter = new MenuAdapter(subsetMenuItems, requireContext());
        binding.popularRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.popularRecyclerView.setAdapter(adapter);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentHomeBinding.bind(view);
        List<SlideModel> imageList = new ArrayList<>();
        imageList.add(new SlideModel(R.drawable.banner1, ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.banner2, ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.banner3, ScaleTypes.FIT));

        binding.imageSlider.setImageList(imageList);

        binding.imageSlider.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemSelected(int position) {
                SlideModel itemPosition = imageList.get(position);
                String itemMessage = "Selected image" + position;
                Toast.makeText(requireContext(), itemMessage, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void doubleClick(int i) {

            }
        });

    }
}