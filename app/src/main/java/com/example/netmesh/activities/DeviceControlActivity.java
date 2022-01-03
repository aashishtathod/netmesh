package com.example.netmesh.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;
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

    private String devId, devName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeviceControlBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        devId = getIntent().getExtras().getString("DeviceId");
        devName = getIntent().getExtras().getString("DeviceName");

        float[] hsv = new float[3];

        float[] hue = new float[1];
        float[] sat = new float[1];
        float[] val = new float[1];

        if (devId != null) {
            binding.name.setText(devName);
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


            binding.switchCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    controlDevice.powerSwitch(isChecked, new IResultCallback() {
                        @Override
                        public void onError(String code, String error) {
                            Toast.makeText(DeviceControlActivity.this, error, Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onSuccess() {
                        }
                    });
                }
            });


            binding.brightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    controlDevice.brightness(progress, new IResultCallback() {
                        @Override
                        public void onError(String code, String error) {
                            Toast.makeText(DeviceControlActivity.this, /*"Light brightness Change Failed"*/ error, Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onSuccess() {
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


            binding.cool.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    controlDevice.colorTemperature(progress, new IResultCallback() {
                        @Override
                        public void onError(String code, String error) {
                            Toast.makeText(DeviceControlActivity.this, /*"Light Cool Change Failed"*/ error, Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onSuccess() {
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

          binding.changeColor.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  controlDevice.colorHSV(0, 100, 100, new IResultCallback() {
                      @Override
                      public void onError(String code, String error) {
                          Toast.makeText(DeviceControlActivity.this, /*"Light Cool Change Failed"*/ error, Toast.LENGTH_LONG).show();
                      }
                      @Override
                      public void onSuccess() {
                      }
                  });
              }
          });


        } else {
            Toast.makeText(DeviceControlActivity.this, "Device Id is empty", Toast.LENGTH_SHORT).show();
        }




















/*
       // WhiteFragment fragment = WhiteFragment.newInstance(devId, devName);
      //  getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();

        binding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.white:
                        WhiteFragment fragment1 = WhiteFragment.newInstance(devId, devId);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment1).commit();
                        return true;
                    case R.id.color:
                        ColorFragment fragment2 = ColorFragment.newInstance(devId,devName);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment2).commit();
                        return true;
                }

                return false;
            }
        });

*/
    }
}
