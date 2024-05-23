package com.example.manafood.Adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.manafood.RecentOrderItems;
import com.example.manafood.databinding.RecentBuyItemBinding;

import java.util.List;

public class RecentBuyAdapter extends RecyclerView.Adapter<RecentBuyAdapter.RecentViewHolder> {
    Context context;
    List<String> foodNameList;
    List<String> foodImageList;
    List<String> foodPriceList;
    List<Integer> foodQuantityList;

    public RecentBuyAdapter(RecentOrderItems context, List<String> allFoodNames, List<String> allFoodImages, List<String> allFoodPrices, List<Integer> allFoodQuantities) {
        this.foodNameList = allFoodNames;
        this.foodPriceList = allFoodPrices;
        this.foodImageList = allFoodImages;
        this.foodQuantityList = allFoodQuantities;
        this.context = context;
        Log.d("food quality", foodQuantityList.toString());
    }

    @NonNull
    @Override
    public RecentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecentBuyItemBinding binding = RecentBuyItemBinding.inflate(inflater, parent, false);
        return new RecentViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentBuyAdapter.RecentViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return foodNameList.size();
    }

    public class RecentViewHolder extends RecyclerView.ViewHolder {
        RecentBuyItemBinding binding;

        public RecentViewHolder(@NonNull RecentBuyItemBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }

        public void bind(int position) {
            binding.recentBuyFoodName.setText(foodNameList.get(position));
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("$").append(foodPriceList.get(position));
            binding.recentBuyFoodPrice.setText(stringBuilder);
            Uri uri = Uri.parse(foodImageList.get(position));
            Glide.with(context).load(uri).into(binding.recentBuyFoodImage);
            binding.textView23.setText(String.valueOf(foodQuantityList.get(position)));

            itemView.setOnClickListener(v->{
                int value = foodQuantityList.get(position);
                Toast.makeText(context, String.valueOf(value), Toast.LENGTH_SHORT).show();
            });
        }
    }
}
