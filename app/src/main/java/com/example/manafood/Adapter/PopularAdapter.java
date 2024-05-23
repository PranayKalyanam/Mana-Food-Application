package com.example.manafood.Adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
//import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manafood.FoodDetailsActivity;
import com.example.manafood.databinding.PopularItemBinding;

import java.util.List;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.PopularViewHolder> {

        private final List<String> items;
        private final List<String> price;
        private final List<Integer> image;

        private OnClickListener itemClickListener;
        final Context requiredContext;

        public PopularAdapter(List<String> items, List<String> price, List<Integer> image, Context requiredContext){
                this.items = items;
                this.price = price;
                this.image = image;
                this.requiredContext = requiredContext;
        }

        @NonNull
        @Override
        public PopularViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
               LayoutInflater inflater = LayoutInflater.from(parent.getContext());
               PopularItemBinding binding = PopularItemBinding.inflate(inflater, parent, false);
               return new PopularViewHolder(binding, requiredContext, items, image, itemClickListener);
        }

        @Override
        public void onBindViewHolder(@NonNull PopularViewHolder holder, int position) {
                String item = items.get(position);
                String priceText = price.get(position);
                int imageResource = image.get(position);

                holder.bind(item, priceText, imageResource);
        }



        @Override
        public int getItemCount() {
                return items.size();
        }

        public void setOnItemClickListener(OnClickListener itemClickListener){
                this.itemClickListener = itemClickListener;
        }
        public interface OnClickListener{
                void onItemClick(int position);
        }

        public static class PopularViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


                private final PopularItemBinding binding;

                private final Context context;
                private final List<String> popularItemNames;
                private final List<Integer> popularImage;
                private final OnClickListener itemClickListener;


                public PopularViewHolder(PopularItemBinding binding, Context context, List<String> popularItemNames, List<Integer> popularImage, OnClickListener itemClickListener) {
                        super(binding.getRoot());
                        this.binding = binding;
                        this.context = context;
                        this.popularItemNames = popularItemNames;
                        this.popularImage = popularImage;
                        this.itemClickListener = itemClickListener;
                        binding.getRoot().setOnClickListener(this);

                }


                public void bind(String item, String price, int imageResource) {
                        binding.foodNamePopular.setText(item);
                        binding.pricePopular.setText(price);
                        ImageView imageView = binding.imageView6;
                        imageView.setImageResource(imageResource);
                }

                public void onClick(View v) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION && itemClickListener != null) {
                                itemClickListener.onItemClick(position);
                        }
                        Intent intent = new Intent(context, FoodDetailsActivity.class);
                        intent.putExtra("MenuItemName", popularItemNames.get(position));
                        intent.putExtra("MenuItemImage", popularImage.get(position));
                        context.startActivity(intent);
                }
        }
}
