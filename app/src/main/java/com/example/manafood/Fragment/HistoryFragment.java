package com.example.manafood.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.manafood.Adapter.BuyAgainAdapter;
import com.example.manafood.RecentOrderItems;
import com.example.manafood.databinding.FragmentHistoryBinding;
import com.example.manafood.model.OrderDetailsModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class HistoryFragment extends Fragment {


    FragmentHistoryBinding binding;
    BuyAgainAdapter adapter;
    List<String> buyAgainFoodName = new ArrayList<>();
    List<String> buyAgainFoodPrice = new ArrayList<>();
    List<String> buyAgainFoodImage = new ArrayList<>();


    DatabaseReference database;
    FirebaseAuth auth;
    String userId;
    List<OrderDetailsModel> listOfOrderItem = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHistoryBinding.inflate(getLayoutInflater(), container, false);
        auth = FirebaseAuth.getInstance();
        database =FirebaseDatabase.getInstance().getReference();
        retrieveBuyHistory();
        binding.recentBuyItem.setOnClickListener(v-> seeItemsRecentBuy());
        return binding.getRoot();
    }

    private void seeItemsRecentBuy() {
        if(!listOfOrderItem.isEmpty()){
            Intent intent = new Intent(requireContext(), RecentOrderItems.class);
            intent.putExtra("RecentBuyOrderFoodNames",new ArrayList<>(listOfOrderItem.get(0).getFoodNames()));
            intent.putExtra("RecentBuyOrderFoodImages",new ArrayList<>(listOfOrderItem.get(0).getFoodImages()));
            intent.putExtra("RecentBuyOrderFoodPrices",new ArrayList<>(listOfOrderItem.get(0).getFoodPrice()));
            intent.putExtra("RecentBuyOrderFoodQuantities",new ArrayList<>(listOfOrderItem.get(0).getFoodQuantities()));
            Log.d("quantity", listOfOrderItem.get(0).getFoodQuantities().toString());
            startActivity(intent);
        }
    }

    private void retrieveBuyHistory() {
        binding.recentlyBuyImage.setVisibility(View.INVISIBLE);
        userId = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        DatabaseReference buyItemReference = database.child("user").child(userId).child("BuyHistory");
        Query shortingQuery = buyItemReference.orderByChild("currentTime");

        shortingQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot buySnapShot : snapshot.getChildren()) {
                    OrderDetailsModel buyHistoryItem = buySnapShot.getValue(OrderDetailsModel.class);
                    if (buyHistoryItem != null) {
                        listOfOrderItem.add(buyHistoryItem);
                    }
                }
                Collections.reverse(listOfOrderItem);
                if(!listOfOrderItem.isEmpty()){
                   binding.recentlyBuyImage.setVisibility(View.VISIBLE);
                   OrderDetailsModel recentOrderItem = listOfOrderItem.get(0);
                   if(recentOrderItem != null){
                       binding.menuFoodName.setText(recentOrderItem.getFoodNames().get(0));
                       StringBuilder stringBuilder = new StringBuilder();
                       stringBuilder.append("$").append(recentOrderItem.getFoodPrice().get(0));
                       binding.menuFoodPrice.setText(stringBuilder);
                       String image = recentOrderItem.getFoodImages().get(0);
                       Uri uri = Uri.parse(image);
                       Glide.with(requireContext()).load(uri).into(binding.recentlyBuyImage);

                       setPreviousBuyItemsRecyclerView();
                   }
                }
            }

            private void setPreviousBuyItemsRecyclerView() {
                for(int i=0; i<listOfOrderItem.size();i++){
                    buyAgainFoodName.add(listOfOrderItem.get(i).getFoodNames().get(0));
                    buyAgainFoodPrice.add(listOfOrderItem.get(i).getFoodPrice().get(0));
                    buyAgainFoodImage.add(listOfOrderItem.get(i).getFoodImages().get(0));
                }
                binding.buyAgainRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                adapter = new BuyAgainAdapter(requireContext(), buyAgainFoodName, buyAgainFoodPrice, buyAgainFoodImage);
                binding.buyAgainRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



}