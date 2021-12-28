package com.example.netmesh;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.netmesh.databinding.ActivityDeviceControlBinding;
import com.tuya.smart.centralcontrol.TuyaLightDevice;
import com.tuya.smart.sdk.api.IResultCallback;
import com.tuya.smart.sdk.centralcontrol.api.ILightListener;
import com.tuya.smart.sdk.centralcontrol.api.ITuyaLightDevice;
import com.tuya.smart.sdk.centralcontrol.api.bean.LightDataPoint;

public class DeviceControlActivity extends AppCompatActivity {
    private ActivityDeviceControlBinding binding;

    String devId = "", devName, proId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeviceControlBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getIntent().hasExtra("DeviceId"))
            devId = getIntent().getExtras().getString("DeviceId");
        //  devName = getIntent().getExtras().getString("DeviceName");
        //  proId = getIntent().getExtras().getString("ProductId");
        // tvDeviceName.setText(devName);

        if (devId != null) {
            ITuyaLightDevice controlDevice = new TuyaLightDevice(devId);

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


            binding.switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    controlDevice.powerSwitch(isChecked, new IResultCallback() {
                        @Override
                        public void onError(String code, String error) {
                            Toast.makeText(DeviceControlActivity.this, "Light Change Failed", Toast.LENGTH_LONG).show();

                        }

                        @Override
                        public void onSuccess() {
                            Toast.makeText(DeviceControlActivity.this, "Light Change SUCCESSFUL", Toast.LENGTH_LONG).show();

                        }
                    });


                }
            });
        }else{
            Toast.makeText(DeviceControlActivity.this, "Device Id is empty", Toast.LENGTH_SHORT).show();
        }
    }
}