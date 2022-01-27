package com.example.netmesh.activities;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.netmesh.databinding.ActivitySwitchControlBinding;
import com.tuya.smart.centralcontrol.TuyaLightDevice;
import com.tuya.smart.sdk.api.IResultCallback;
import com.tuya.smart.sdk.centralcontrol.api.ILightListener;
import com.tuya.smart.sdk.centralcontrol.api.ITuyaLightDevice;
import com.tuya.smart.sdk.centralcontrol.api.bean.LightDataPoint;

public class SwitchControlActivity extends AppCompatActivity {
    ActivitySwitchControlBinding binding;
    private ITuyaLightDevice controlDevice;
    private String devId;
    private String devName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        devId = getIntent().getExtras().getString("DeviceId");
        devName = getIntent().getExtras().getString("DeviceName");

        binding.name.setText(devName);
        binding.switchCheck.setChecked(true);


        controlDevice = new TuyaLightDevice(devId);
        controlDevice.registerLightListener(new ILightListener() {
            @Override
            public void onDpUpdate(LightDataPoint lightDataPoint) {
            }

            @Override
            public void onRemoved() {
            }

            @Override
            public void onStatusChanged(boolean b) {
            }

            @Override
            public void onNetworkStatusChanged(boolean b) {
            }

            @Override
            public void onDevInfoUpdate() {
            }
        });


        binding.switchCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    controlDevice.publishDps("{\"1\": true}", new IResultCallback() {
                        @Override
                        public void onError(String code, String error) {
                            Toast.makeText(SwitchControlActivity.this, "Failed to switch on the light.", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onSuccess() {
                            Toast.makeText(SwitchControlActivity.this, "The light is switched on successfully.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    controlDevice.publishDps("{\"1\": false}", new IResultCallback() {
                        @Override
                        public void onError(String code, String error) {
                            Toast.makeText(SwitchControlActivity.this, "Failed to switch on the light.", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onSuccess() {
                            Toast.makeText(SwitchControlActivity.this, "The light is switched on successfully.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });



    }
}