package com.example.netmesh.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.netmesh.databinding.ActivityWifiPassBinding;

public class WifiPassActivity extends AppCompatActivity {
    private ActivityWifiPassBinding binding;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWifiPassBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        type = getIntent().getStringExtra("type");


        binding.btnNxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String wifiText = binding.wifi.getText().toString().trim();
                String passText = binding.password.getText().toString().trim();

                if (!wifiText.isEmpty() && !passText.isEmpty()) {

                    Intent intent = new Intent(WifiPassActivity.this , DeviceSearchActivity.class);
                    intent.putExtra("wifi", wifiText);
                    intent.putExtra("type", type);
                    intent.putExtra("pass", passText);
                    startActivity(intent);
                    finish();
                }else{
                   // binding.wifi.setError("* required");
                   // binding.password.setError("* required");
                    Toast.makeText(WifiPassActivity.this, "WiFi or Password cant be empty.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}