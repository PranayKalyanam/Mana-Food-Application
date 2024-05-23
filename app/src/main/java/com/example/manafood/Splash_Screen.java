package com.example.manafood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class Splash_Screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Check if the user is new or returning
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        boolean isNewUser = sharedPreferences.getBoolean("isNewUser", true);

        new Handler().postDelayed(()->{
            Intent intent;
            if(isNewUser){
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isNewUser", false);
                editor.apply();
            intent = new Intent(Splash_Screen.this, StartActivity.class);
            } else {
            intent = new Intent(Splash_Screen.this, LoginActivity.class);
            }
            startActivity(intent);
            finish();
        }, 3000);
    }
}