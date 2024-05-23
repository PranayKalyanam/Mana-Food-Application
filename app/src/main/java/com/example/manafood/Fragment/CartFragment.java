package com.example.manafood.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.manafood.Adapter.CartAdapter;
import com.example.manafood.PayOutActivity;
import com.example.manafood.databinding.FragmentCartBinding;
import com.example.manafood.model.CartItemModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class CartFragment extends Fragment {

    FragmentCartBinding binding;

    //firebase
    private FirebaseAuth auth;
    FirebaseDatabase database;
    String userId;
    private final List<String> foodNames = new ArrayList<>();
    private final List<String> foodPrice = new ArrayList<>();
    private final List<String> foodImage = new ArrayList<>();
    private final List<String> foodDescription = new ArrayList<>();
    private final List<String> foodIngredients = new ArrayList<>();
    private final List<Integer> quantity = new ArrayList<>();
    CartAdapter cartAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater, container, false);

        return binding.getRoot();
        //return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentCartBinding.bind(view);

        //Initializing firebase
        auth = FirebaseAuth.getInstance();
        retrieveCartItems();

        binding.proceedButton.setOnClickListener(v -> getOrderItemsDetails());


    }

    private void getOrderItemsDetails() {
        if (foodNames.isEmpty()) {
            Toast.makeText(getContext(), "Your cart is empty.", Toast.LENGTH_SHORT).show();
            return;
        }
        DatabaseReference orderIdReference = database.getReference().child("user").child(userId).child("cartItems");
        List<String> foodPrices = new ArrayList<>();
        List<Integer> foodQuantities = cartAdapter.getUpdatedItemsQuantities();


        orderIdReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot foodSnapShot : snapshot.getChildren()) {
                    //get the cartItems to respective list
                    CartItemModel orderItems = foodSnapShot.getValue(CartItemModel.class);
                    //add items details into list
                    foodPrices.add(orderItems != null ? orderItems.getFoodPrice() : null);
                }
                orderNow(foodNames, foodPrices, foodImage, foodDescription, foodIngredients, foodQuantities);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error retrieving data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void orderNow(List<String> foodNames,
                          List<String> foodPrices,
                          List<String> foodImage,
                          List<String> foodDescription,
                          List<String> foodIngredients,
                          List<Integer> foodQuantities) {
        if (isAdded() && getContext() != null) {
            Intent intent = new Intent(requireContext(), PayOutActivity.class);
            intent.putExtra("foodName", new ArrayList<>(foodNames));
            intent.putExtra("foodPrice", new ArrayList<>(foodPrices));
            intent.putExtra("foodImage", new ArrayList<>(foodImage));
            intent.putExtra("foodDescription", new ArrayList<>(foodDescription));
            intent.putExtra("foodIngredients", new ArrayList<>(foodIngredients));
            intent.putExtra("foodQuantities", new ArrayList<>(foodQuantities));
            startActivity(intent);
        }
    }

    private void retrieveCartItems() {
        //database reference to the firebase
        database = FirebaseDatabase.getInstance();
        userId = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        DatabaseReference foodRef = database.getReference().child("user").child(userId).child("cartItems");


        //fetch data from the database
        foodRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //loop to get all items
                for (DataSnapshot foodSnapShot : snapshot.getChildren()) {
                    //get the cartItems object from the child node
                    CartItemModel cartItem = foodSnapShot.getValue(CartItemModel.class);

                    //add cart items details to the list
                    foodNames.add(Objects.requireNonNull(cartItem).getFoodName());
                    foodPrice.add(Objects.requireNonNull(cartItem).getFoodPrice());
                    foodImage.add(Objects.requireNonNull(cartItem).getFoodImage());
                    foodDescription.add(Objects.requireNonNull(cartItem).getFoodDescription());
                    quantity.add(Objects.requireNonNull(cartItem).getFoodQuantity());
                    foodIngredients.add(Objects.requireNonNull(cartItem).getFoodIngredients());
                }
                setAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "data not fetch", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setAdapter() {
        cartAdapter = new CartAdapter(foodNames, foodPrice, foodImage, foodDescription, quantity, foodIngredients, requireContext());
        binding.cartRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        binding.cartRecyclerView.setAdapter(cartAdapter);

    }
}