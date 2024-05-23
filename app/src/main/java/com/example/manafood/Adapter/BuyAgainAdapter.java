package com.example.manafood.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.manafood.databinding.BuyAgainItemBinding;

import java.util.List;




public class BuyAgainAdapter extends RecyclerView.Adapter<BuyAgainAdapter.BuyAgainViewHolder> {

    private final List<String> buyAgainFoodName;
    private final List<String> buyAgainFoodPrice;
    private final List<String> buyAgainFoodImage;
    private final Context context;

    public BuyAgainAdapter(Context context, List<String> items, List<String> price, List<String> image){
        this.context = context;
        this.buyAgainFoodName = items;
        this.buyAgainFoodPrice = price;
        this.buyAgainFoodImage = image;
    }

    @NonNull
    @Override
    public BuyAgainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        BuyAgainItemBinding binding = BuyAgainItemBinding.inflate(inflater, parent, false);
        return new BuyAgainViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BuyAgainViewHolder holder, int position) {
        String item = buyAgainFoodName.get(position);
        String priceText = buyAgainFoodPrice.get(position);

        String imageResource = buyAgainFoodImage.get(position);
        Uri uri = Uri.parse(imageResource);

        holder.bind(context, item, priceText, uri);
    }



    @Override
    public int getItemCount() {
        return buyAgainFoodName.size();
    }

    public static class BuyAgainViewHolder extends RecyclerView.ViewHolder{


        private final BuyAgainItemBinding binding;


        public BuyAgainViewHolder(BuyAgainItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }


        public void bind(Context context, String item, String price, Uri imageResource) {
            binding.buyAgainFoodName.setText(item);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("$").append(price);
            binding.buyAgainPrice.setText(stringBuilder);
            Glide.with(context).load(imageResource).into(binding.buyAgainImage);
        }
    }
}

