package com.example.netmesh.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.netmesh.R;
import com.example.netmesh.databinding.ActivityDeviceControlBinding;
import com.example.netmesh.fragments.ColorFragment;
import com.example.netmesh.fragments.WhiteFragment;
import com.google.android.material.navigation.NavigationBarView;

public class DeviceControlActivity extends AppCompatActivity {
    private ActivityDeviceControlBinding binding;

    private String devId, devName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeviceControlBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getIntent().getExtras() != null) {

            devId = getIntent().getStringExtra("DeviceId");
            devName = getIntent().getStringExtra("DeviceName");
        }

        WhiteFragment fragment = WhiteFragment.newInstance(devId, devName);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment.getClass(), null, null).commit();



        binding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.white:
                        WhiteFragment fragment1 = WhiteFragment.newInstance("1", "1");
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment1.getClass(), null, null).commit();
                        return true;
                    case R.id.color:
                        ColorFragment fragment2 = ColorFragment.newInstance(devId , devName);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment2.getClass(), null, null).commit();
                        return true;
                }

                return false;
            }
        });


    }
}
