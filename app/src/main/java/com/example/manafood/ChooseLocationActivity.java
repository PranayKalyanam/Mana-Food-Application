package com.example.manafood;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.example.manafood.databinding.ActivityChooseLocationBinding;

public class ChooseLocationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityChooseLocationBinding binding;
        binding = ActivityChooseLocationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String[] locationList = {"New Delhi", "Hyderabad"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.select_dialog_item,locationList);
        binding.listOfLocation.setAdapter(adapter);
    }
}