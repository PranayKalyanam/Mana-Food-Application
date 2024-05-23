package com.example.manafood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.manafood.Adapter.RecentBuyAdapter;
import com.example.manafood.databinding.ActivityRecentOrderItemsBinding;

import java.util.ArrayList;
import java.util.List;

public class RecentOrderItems extends AppCompatActivity {

    ActivityRecentOrderItemsBinding binding;
    List<String> allFoodNames = new ArrayList<>();
    List<String> allFoodImages = new ArrayList<>();
    List<String> allFoodPrices = new ArrayList<>();
    List<Integer> allFoodQuantities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecentOrderItemsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.recentOrderBackButton.setOnClickListener(v->{
            finish();
        });

        Intent intent = getIntent();
         allFoodNames = intent.getStringArrayListExtra("RecentBuyOrderFoodNames");
         allFoodImages = intent.getStringArrayListExtra("RecentBuyOrderFoodImages");
         allFoodPrices = intent.getStringArrayListExtra("RecentBuyOrderFoodPrices");
         allFoodQuantities = intent.getIntegerArrayListExtra("RecentBuyOrderFoodQuantities");

        // Check if any of the lists are null or empty before proceeding
        if (allFoodNames != null && !allFoodNames.isEmpty() &&
                allFoodImages != null && !allFoodImages.isEmpty() &&
                allFoodPrices != null && !allFoodPrices.isEmpty() &&
                allFoodQuantities != null && !allFoodQuantities.isEmpty()) {
            // Set up RecyclerView adapter
            setRecyclerViewAdapter();
        } else {
            // Handle the case where one or more lists are empty or null
            Toast.makeText(this, "List is empty", Toast.LENGTH_SHORT).show();
        }
    }

    private void setRecyclerViewAdapter() {
        if (!allFoodNames.isEmpty() && !allFoodImages.isEmpty() && !allFoodPrices.isEmpty() && !allFoodQuantities.isEmpty()) {
            binding.recentOrderRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            RecentBuyAdapter adapter = new RecentBuyAdapter(this, allFoodNames, allFoodImages, allFoodPrices, allFoodQuantities);
            binding.recentOrderRecyclerView.setAdapter(adapter);
        } else {
            Toast.makeText(this, "list is empty", Toast.LENGTH_SHORT).show();
        }
    }
}