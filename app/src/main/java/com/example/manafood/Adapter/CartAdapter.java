package com.example.manafood.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.manafood.databinding.CartItemBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private final List<String> CartItems;
    private final List<String> CartItemPrice;
    private final List<String> CartImage;
    private final List<String> CartDescription;
    private final List<String> CartIngredient;
    private final List<Integer> CartQuantity;
    //    private final List<String> foodPrices;
    Context context;


    private final int[] itemQuantities;

    //firebase
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference cartItemReference;

    public CartAdapter(@NonNull List<String> items, List<String> price, List<String> image, List<String> cartDescription, List<Integer> cartQuantity, List<String> cartIngredient, Context context) {
        this.CartItems = items;
        this.CartItemPrice = price;
        this.CartImage = image;
        this.CartDescription = cartDescription;
        this.CartQuantity = cartQuantity;
        this.CartIngredient = cartIngredient;
//        this.foodPrices = price;
        this.context = context;

        this.itemQuantities = new int[items.size()];
        for (int i = 0; i < items.size(); i++) {
            this.itemQuantities[i] = 1;
        }

        //Initializing firebase auth and database
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            cartItemReference = database.getReference().child("user").child(userId).child("cartItems");
        }


    }


    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        CartItemBinding binding = CartItemBinding.inflate(inflater, parent, false);
        return new CartViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.CartViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return CartItems.size();
    }

    //get updated quantity
    public List<Integer> getUpdatedItemsQuantities() {
        return CartQuantity;
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        private final CartItemBinding binding;

        public CartViewHolder(@NonNull CartItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }


        public void bind(int position) {
            int quantity = CartQuantity.get(position);
            binding.cartFoodName.setText(CartItems.get(position));
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("$").append(CartItemPrice.get(position));
            binding.cartItemPrice.setText(stringBuilder);
            Glide.with(context).load(Uri.parse(CartImage.get(position))).into(binding.cartImage);

            binding.cartQuantity.setText(String.valueOf(quantity));

            binding.minusButton.setOnClickListener(v -> decreaseQuantity(position));

            binding.plusButton.setOnClickListener(v -> increaseQuantity(position));

            binding.trashButton.setOnClickListener(v -> {
                int itemPosition = getAdapterPosition();
                if (itemPosition != RecyclerView.NO_POSITION) {
                    deleteItem(itemPosition);
                }

            });
        }

        private void decreaseQuantity(int position) {
            if (itemQuantities[position] > 1) {
                itemQuantities[position]--;
                CartQuantity.set(position, itemQuantities[position]);
                binding.cartQuantity.setText(String.valueOf(itemQuantities[position]));

            }
        }

        private void increaseQuantity(int position) {
            if (itemQuantities[position] < 10) {
                itemQuantities[position]++;
                CartQuantity.set(position, itemQuantities[position]);
                binding.cartQuantity.setText(String.valueOf(itemQuantities[position]));

            }
        }



        private void deleteItem(int position) {
            getUniqueKeyAtPosition(position, uniqueKey -> removeItem(position, uniqueKey));
        }


        private void getUniqueKeyAtPosition(int position, OnUniqueKeyListener onUniqueKeyListener) {
            cartItemReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String uniqueKey = null;
                    int index = 0;
                    //loop for snapshot children
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (index == position) {
                            uniqueKey = dataSnapshot.getKey();
                            break;
                        }
                        index++;
                    }
                    onUniqueKeyListener.onUniqueKeyReceived(uniqueKey);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


        private void removeItem(int position, String uniqueKey) {
            if (uniqueKey != null) {
                cartItemReference.child(uniqueKey).removeValue().addOnSuccessListener(it -> {
                    CartItems.remove(position);
                    CartItemPrice.remove(position);
                    CartImage.remove(position);
                    CartDescription.remove(position);
                    CartQuantity.remove(position);
                    CartIngredient.remove(position);
//                    foodPrices.remove(position);
                    Toast.makeText(context, "Item Deleted", Toast.LENGTH_SHORT).show();

                    //update itemQuantity
                    itemQuantities[position] = 0;
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, CartItems.size());

                }).addOnFailureListener(it -> Toast.makeText(context, "Failed to Delete", Toast.LENGTH_SHORT).show());
            }

        }

    }

    // Define an interface for the unique key callback
    interface OnUniqueKeyListener {
        void onUniqueKeyReceived(String uniqueKey);
    }
}
