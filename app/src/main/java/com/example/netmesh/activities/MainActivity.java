package com.example.netmesh.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.netmesh.databinding.ActivityMainBinding;
import com.tuya.smart.home.sdk.TuyaHomeSdk;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (TuyaHomeSdk.getUserInstance().getUser()!= null) {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            intent.putExtra("type",1);
            startActivity(intent);
            finish();
        }

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });

        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));

            }
        });
    }
}