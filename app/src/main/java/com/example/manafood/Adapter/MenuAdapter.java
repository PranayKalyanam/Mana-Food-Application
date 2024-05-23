package com.example.manafood.Adapter;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.manafood.FoodDetailsActivity;
import com.example.manafood.databinding.MenuItemBinding;
import com.example.manafood.model.MenuItemModel;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {



    private final List<MenuItemModel> menuItems;

    private final Context context;

    public MenuAdapter(List<MenuItemModel> menuItems, Context requiredContext) {
        this.menuItems = menuItems;
        this.context = requiredContext;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        MenuItemBinding binding = MenuItemBinding.inflate(inflater, parent, false);
        return new MenuViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        holder.bind(position);
    }


    @Override
    public int getItemCount() {
        return menuItems.size();
    }



    public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final MenuItemBinding binding;

        public MenuViewHolder(MenuItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(this);
        }

        //set data into recyclerview
        public void bind(int position) {
            MenuItemModel menuItem = menuItems.get(position);
            binding.menuFoodName.setText(menuItem.getFoodName());
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("$").append(menuItem.getFoodPrice());
            binding.menuPrice.setText(stringBuilder);
            Uri uri = Uri.parse(menuItem.getFoodImage());
            // Glide convert the link to image and send it to us
            Glide.with(context).load(uri).into(binding.menuImage);
        }

        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                openDetailsActivity(position);
            }

        }

        private void openDetailsActivity(int position) {
            MenuItemModel menuItem = menuItems.get(position);
            //a intent to open details activity and pass data
            Intent intent = new Intent(context, FoodDetailsActivity.class);
            intent.putExtra("MenuItemName", menuItem.getFoodName());
            intent.putExtra("MenuItemPrice", menuItem.getFoodPrice());
            intent.putExtra("MenuItemImage", menuItem.getFoodImage());
            intent.putExtra("MenuItemDescription", menuItem.getFoodDescription());
            intent.putExtra("MenuItemIngredient", menuItem.getFoodIngredient());
            context.startActivity(intent);
        }
    }

}
