package com.example.netmesh.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONObject;
import com.example.netmesh.databinding.ActivityDimmerControlBinding;
import com.tuya.smart.centralcontrol.TuyaLightDevice;
import com.tuya.smart.sdk.api.IResultCallback;
import com.tuya.smart.sdk.centralcontrol.api.ILightListener;
import com.tuya.smart.sdk.centralcontrol.api.ITuyaLightDevice;
import com.tuya.smart.sdk.centralcontrol.api.bean.LightDataPoint;

import java.util.HashMap;

public class DimmerControlActivity extends AppCompatActivity {
    private ActivityDimmerControlBinding binding;
    private ITuyaLightDevice controlDevice;
    private String devId;
    private String devName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDimmerControlBinding.inflate(getLayoutInflater());
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
                    controlDevice.publishDps("{\"101\": true}", new IResultCallback() {
                        @Override
                        public void onError(String code, String error) {
                            Toast.makeText(DimmerControlActivity.this, "Failed to switch on the light.", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onSuccess() {
                            Toast.makeText(DimmerControlActivity.this, "The light is switched on successfully.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    controlDevice.publishDps("{\"101\": false}", new IResultCallback() {
                        @Override
                        public void onError(String code, String error) {
                            Toast.makeText(DimmerControlActivity.this, "Failed to switch on the light.", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onSuccess() {
                            Toast.makeText(DimmerControlActivity.this, "The light is switched on successfully.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


        binding.dimmerSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                HashMap map = new HashMap();
                //map.put("3",25);
                //map.put("102",25);
                map.put("102", progress + 25);

                controlDevice.publishDps(JSONObject.toJSONString(map), new IResultCallback() {
                    @Override
                    public void onError(String code, String error) {
                        Toast.makeText(DimmerControlActivity.this, "Failed to dimming the light.", Toast.LENGTH_SHORT).show();
                        Log.d("Tuya", "code: " + code + "error : " + error);
                    }

                    @Override
                    public void onSuccess() {
                        Toast.makeText(DimmerControlActivity.this, "The light is dimming successfully.", Toast.LENGTH_SHORT).show();

                    }
                });
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
}