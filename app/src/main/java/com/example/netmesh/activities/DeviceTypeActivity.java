package com.example.netmesh.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.netmesh.databinding.ActivityDeviceTypeBinding;

public class DeviceTypeActivity extends AppCompatActivity {
    private ActivityDeviceTypeBinding binding;

    static final String LIGHT ="light";
    static final String SWITCH ="switch";
    static final String DIMMER ="dimmer";
    static final String TYPE ="type";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeviceTypeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        binding.smartlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeviceTypeActivity.this , WifiPassActivity.class);
                intent.putExtra(TYPE, LIGHT);
                startActivity(intent);
                finish();
            }
        });

        binding.smartSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeviceTypeActivity.this , WifiPassActivity.class);
                intent.putExtra(TYPE, SWITCH);
                startActivity(intent);
                finish();
            }
        });

        binding.dimmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeviceTypeActivity.this , WifiPassActivity.class);
                intent.putExtra(TYPE, DIMMER);
                startActivity(intent);
                finish();
            }
        });

    }
}