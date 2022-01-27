package com.example.netmesh.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.netmesh.R;
import com.example.netmesh.databinding.ActivityDeviceControlBinding;
import com.example.netmesh.fragments.ColorFragment;
import com.example.netmesh.fragments.WhiteFragment;
import com.example.netmesh.viewmodels.DeviceControlViewModel;
import com.google.android.material.navigation.NavigationBarView;

public class DeviceControlActivity extends AppCompatActivity {
    private ActivityDeviceControlBinding binding;
    private String devId, devName;



    private DeviceControlViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeviceControlBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        devId = getIntent().getExtras().getString("DeviceId");
        devName = getIntent().getExtras().getString("DeviceName");
        viewModel = new ViewModelProvider(this).get(DeviceControlViewModel.class);

        viewModel.deviceName = devName;

        if (viewModel.createDevice(devId, this)) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_device_control, new WhiteFragment()).commit();
        }

        binding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.white:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_device_control, new WhiteFragment()).commit();
                        return true;
                    case R.id.color:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_device_control, new ColorFragment()).commit();
                        return true;
                }

                return false;
            }
        });


    }

}
