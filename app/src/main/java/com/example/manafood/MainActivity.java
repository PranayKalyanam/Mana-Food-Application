package com.example.manafood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.example.manafood.Fragment.NotificationBottomSheetFragment;
import com.example.manafood.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        NavController navController = Navigation.findNavController(this, R.id.fragmentContainerView);
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        NavigationUI.setupWithNavController(bottomNav, navController);

        binding.notificationButton.setOnClickListener(v->{
            NotificationBottomSheetFragment bottomSheetDialog = new NotificationBottomSheetFragment();
            bottomSheetDialog.show(getSupportFragmentManager(), "test");
        });
    }
}