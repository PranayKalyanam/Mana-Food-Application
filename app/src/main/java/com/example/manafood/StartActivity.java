package com.example.manafood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.manafood.databinding.ActivityStartBinding;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStartBinding binding;
        binding = ActivityStartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.nextbutton.setOnClickListener(v->{
            Intent intent = new Intent(StartActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}