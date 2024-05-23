package com.example.manafood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.manafood.Fragment.CongratsBottomSheetFragment;
import com.example.manafood.databinding.ActivityPayOutBinding;
import com.example.manafood.model.OrderDetailsModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Objects;


public class PayOutActivity extends AppCompatActivity {
    ActivityPayOutBinding binding;
    FirebaseAuth auth;
    DatabaseReference databaseReference;
    String userId;

    String name;
    String address;
    String phone;
    String totalAmount;
     List<String> foodItemName;
     List<String> foodItemPrice;
     List<String> foodItemImage;
     List<String> foodItemDescription;
     List<String> foodItemIngredient;
     List<Integer> foodItemQuantities;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPayOutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.payOutBackButton.setOnClickListener(v->finish());

        //Initialize firebase
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        //set user data
        setUserData();

        //get user details from intent
        Intent intent = getIntent();
        foodItemName = intent.getStringArrayListExtra("foodName");
        foodItemPrice = intent.getStringArrayListExtra("foodPrice");
        foodItemImage = intent.getStringArrayListExtra("foodImage");
        foodItemDescription = intent.getStringArrayListExtra("foodDescription");
        foodItemIngredient = intent.getStringArrayListExtra("foodIngredients");
        foodItemQuantities = intent.getIntegerArrayListExtra("foodQuantities") ;

        totalAmount = calculateTotalAmount() + "$";
        binding.totalAmount.setEnabled(false);
        binding.totalAmount.setText(totalAmount);


        binding.placeMyOrderButton.setOnClickListener(v -> {
            name = binding.userName.getText().toString().trim();
            address = binding.address.getText().toString().trim();
            phone = binding.phone.getText().toString().trim();
            if(name.isEmpty() && address.isEmpty() && phone.isEmpty()){
                Toast.makeText(this, "Please enter all details 🙂", Toast.LENGTH_SHORT).show();
            } else{
                placeOrder();
            }

        });
    }

    private void placeOrder() {
        userId = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        long time = System.currentTimeMillis();
         String itemPushKey = databaseReference.child("OrderDetails").push().getKey();
         OrderDetailsModel orderDetails = new OrderDetailsModel(userId, name, address, phone, foodItemName, foodItemImage, foodItemPrice, foodItemQuantities, totalAmount,  time, itemPushKey, false, false);
        assert itemPushKey != null;
        DatabaseReference orderReference = databaseReference.child("OrderDetails").child(itemPushKey);
        orderReference.setValue(orderDetails).addOnSuccessListener(v->{
            CongratsBottomSheetFragment bottomSheetFragment = new CongratsBottomSheetFragment();
            bottomSheetFragment.show(getSupportFragmentManager(), "test");
            addOrderToHistory(orderDetails);
            removeItemFromCart();

        }).addOnFailureListener(v->{
            Toast.makeText(this, "order failed to order 😒", Toast.LENGTH_SHORT).show();
        });
    }

    private void addOrderToHistory(OrderDetailsModel orderDetails) {
        databaseReference.child("user").child(userId).child("BuyHistory").child(orderDetails.getItemPushKey()).setValue(orderDetails).addOnSuccessListener(v->{

        });
    }

    private void removeItemFromCart() {
        DatabaseReference cartItemReference = databaseReference.child("user").child(userId).child("cartItems");
        cartItemReference.removeValue();
    }

    private String calculateTotalAmount() {
        int totalAmount = 0;
        if (foodItemPrice !=null  && foodItemQuantities != null) {
            for (int i = 0; i < foodItemPrice.size(); i++) {
                String price = foodItemPrice.get(i);
                char lastChar = price.charAt(price.length() - 1);

                int priceInValue;
                if (lastChar == '$') {
                    priceInValue = Integer.parseInt(price.substring(0, price.length() - 1));
                } else {
                    priceInValue = Integer.parseInt(price);
                }

                int quantity = foodItemQuantities.get(i);
                int itemTotal = priceInValue * quantity;
                totalAmount += itemTotal;
                Log.d("Item Total", "Item " + (i+1) + " total: " + itemTotal);
            }
        } else{
            Log.e("Calculate Total", "foodItemPrice or foodItemQuantities is null");
        }
        Log.d("Total Amount", "Total Amount: " + totalAmount);
        return String.valueOf(totalAmount);
    }

    private void setUserData() {
        FirebaseUser user = auth.getCurrentUser();
        String userId =  Objects.requireNonNull(user).getUid();
        DatabaseReference userRef = databaseReference.child("user").child(userId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                     name = snapshot.child("username").getValue(String.class);
                     address = snapshot.child("address").getValue(String.class);
                     phone = snapshot.child("phone").getValue(String.class);

                    binding.userName.setText(name);
                    binding.address.setText(address);
                    binding.phone.setText(phone);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}