package com.example.manafood.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.manafood.LoginActivity;
import com.example.manafood.databinding.FragmentProfileBinding;
import com.example.manafood.model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProfileFragment extends Fragment {

    FragmentProfileBinding binding;

    private FirebaseAuth auth;
    DatabaseReference database;

    private boolean isEditable = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        binding.userName.setEnabled(isEditable);
        binding.email.setEnabled(isEditable);
        binding.adrress.setEnabled(isEditable);
        binding.phone.setEnabled(isEditable);
        binding.saveInformationButton.setVisibility(View.INVISIBLE);
        binding.logOutButton.setVisibility(View.VISIBLE);

        setUserData();

        binding.editButton.setOnClickListener(v -> toggleEditMode());

        binding.saveInformationButton.setOnClickListener(v -> {
            String name = binding.userName.getText().toString();
            String email = binding.email.getText().toString();
            String address = binding.adrress.getText().toString();
            String phone = binding.phone.getText().toString();

            updateUserData(name, email, address, phone);
            toggleEditMode(); // disable edit mode after saving
        });

        binding.logOutButton.setOnClickListener(v -> logOutUser());

        return binding.getRoot();
    }

    private void toggleEditMode() {
        isEditable = !isEditable;
        binding.userName.setEnabled(isEditable);
        binding.email.setEnabled(isEditable);
        binding.adrress.setEnabled(isEditable);
        binding.phone.setEnabled(isEditable);
        binding.saveInformationButton.setVisibility(isEditable ? View.VISIBLE : View.GONE);
        binding.logOutButton.setVisibility(isEditable ? View.INVISIBLE : View.VISIBLE);
    }

    private void logOutUser() {
        auth.signOut();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        requireActivity().finish();
    }

    private void updateUserData(String name, String email, String address, String phone) {
        String userId = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        DatabaseReference userRef = database.child("user").child(userId);

        Map<String, String> userData = new HashMap<>();
        userData.put("username", name);
        userData.put("email", email);
        userData.put("address", address);
        userData.put("phone", phone);

        userRef.setValue(userData).addOnSuccessListener(task -> Toast.makeText(requireContext(), "Profile Updated Successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(task -> Toast.makeText(requireContext(), "Profile Not Updated", Toast.LENGTH_SHORT).show());
    }

    private void setUserData() {
        FirebaseUser user = auth.getCurrentUser();
        String userId = Objects.requireNonNull(user).getUid();
        DatabaseReference userRef = database.child("user").child(userId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    UserModel userModel = snapshot.getValue(UserModel.class);

                    if (userModel != null) {
                        binding.userName.setText(userModel.getUsername());
                        binding.email.setText(userModel.getEmail());
                        binding.adrress.setText(userModel.getAddress());
                        binding.phone.setText(userModel.getPhone());
                    } else {
                        Toast.makeText(requireContext(), "Profile does not exist", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "Failed to load profile", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
