package com.example.manafood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.manafood.databinding.ActivityFoodDetailsBinding;
import com.example.manafood.model.CartItemModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FoodDetailsActivity extends AppCompatActivity {

    String foodName;
    String foodPrice;
    String foodDescription;
    String foodIngredient;
    String foodImage;

    //firebase
    FirebaseAuth auth;
    DatabaseReference database;
    ActivityFoodDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFoodDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Initializing firebase auth
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        foodName = intent.getStringExtra("MenuItemName");
        foodPrice = intent.getStringExtra("MenuItemPrice");
        foodDescription = intent.getStringExtra("MenuItemDescription");
        foodIngredient = intent.getStringExtra("MenuItemIngredient");
        foodImage = intent.getStringExtra("MenuItemImage");

        binding.foodDetailTextView.setText(foodName);
        binding.foodDetailDescTextView.setText(foodDescription);
        binding.foodDetailIngredientsTextView.setText(foodIngredient);
        Glide.with(this).load(Uri.parse(foodImage)).into(binding.foodDetailImageView);


        binding.foodDetailBackButton.setOnClickListener(v -> finish());
        binding.foodDetailAddToCartButton.setOnClickListener(v-> addItemToCart());
    }

    private void addItemToCart() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            //create a cartItems object
            CartItemModel cartItem = new CartItemModel(foodName, foodPrice, foodImage, foodDescription, 1, foodIngredient);

            //save data to cart item node in firebase
            database.child("user").child(userId).child("cartItems").push().setValue(cartItem).addOnSuccessListener(t->{
                Toast.makeText(this, "Item added to cart Successfully ", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(t->{
                Toast.makeText(this, "Item is not added to cart", Toast.LENGTH_SHORT).show();
            });

        }
    }
}