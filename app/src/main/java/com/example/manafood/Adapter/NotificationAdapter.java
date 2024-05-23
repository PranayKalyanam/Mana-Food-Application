package com.example.manafood.Adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manafood.databinding.NotificationItemBinding;

import java.util.List;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private final List<String> Notification;
    private final List<Integer> NotificationImage;

    public NotificationAdapter(List<String> items, List<Integer> image){
        this.Notification = items;
        this.NotificationImage = image;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        NotificationItemBinding binding = NotificationItemBinding.inflate(inflater, parent, false);
        return new NotificationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        String item = Notification.get(position);
//        String priceText = menuItemPrice.get(position);
        int imageResource = NotificationImage.get(position);

        holder.bind(item, imageResource);
    }



    @Override
    public int getItemCount() {
        return Notification.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder{


        private final NotificationItemBinding binding;


        public NotificationViewHolder(NotificationItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }


        public void bind(String item, int imageResource) {
            binding.notificationTextView.setText(item);
            ImageView imageView = binding.notificationImageView;
            imageView.setImageResource(imageResource);
        }
    }
}

