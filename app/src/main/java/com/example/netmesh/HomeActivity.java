package com.example.netmesh;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.netmesh.databinding.ActivityHomeBinding;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.home.sdk.bean.HomeBean;
import com.tuya.smart.home.sdk.builder.ActivatorBuilder;
import com.tuya.smart.home.sdk.callback.ITuyaHomeResultCallback;
import com.tuya.smart.sdk.api.ITuyaActivator;
import com.tuya.smart.sdk.api.ITuyaActivatorGetToken;
import com.tuya.smart.sdk.api.ITuyaSmartActivatorListener;
import com.tuya.smart.sdk.bean.DeviceBean;
import com.tuya.smart.sdk.enums.ActivatorEZStepCode;
import com.tuya.smart.sdk.enums.ActivatorModelEnum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;

    String homeName = "MyHome";
    String[] rooms = {"Kitchen", "Bedroom", "Study"};
    String currentRegistrationToken;

    ArrayList<String> roomList = new ArrayList<>();

    private String ssid = "Galaxy M510F8D";
    private String password = "acdf55188";

    private HomeBean currentHomeBean;
    private DeviceBean currentDeviceBean;

    ITuyaActivator tuyaActivator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        roomList.addAll(Arrays.asList(rooms));

        createHome(homeName, roomList);

        binding.btnScan.setClickable(false);
        binding.cardview1.setClickable(false);

        binding.btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentText = binding.btnScan.getText().toString();

                if (tuyaActivator == null) {
                    searchDevices(currentRegistrationToken);
                    Toast.makeText(HomeActivity.this, "Searching Devices", Toast.LENGTH_LONG).show();

                } else {
                    if (currentText.equalsIgnoreCase("search devices")) {
                        tuyaActivator.start();
                        Toast.makeText(HomeActivity.this, "Again Starting Activator", Toast.LENGTH_LONG).show();
                        binding.btnScan.setText("stop search");
                    } else {
                        binding.btnScan.setText("search devices");
                        Toast.makeText(HomeActivity.this, "Stoping Activator", Toast.LENGTH_LONG).show();
                        tuyaActivator.stop();
                    }
                }
            }
        });


       binding.cardview1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentDeviceBean != null) {
                    Intent intent = new Intent(HomeActivity.this, DeviceControlActivity.class);
                    intent.putExtra("DeviceId", currentDeviceBean.devId);
                    //   intent.putExtra("DeviceName", currentDeviceBean.name);
                    //   intent.putExtra("ProductId", currentDeviceBean.productId);

                    startActivity(intent);
                }
            }
        });

    }

    private void createHome(String homeName, List<String> roomList) {
        TuyaHomeSdk.getHomeManagerInstance().createHome(homeName,
                0, 0, "", roomList, new ITuyaHomeResultCallback() {
                    @Override
                    public void onSuccess(HomeBean bean) {
                        currentHomeBean = bean;
                        Toast.makeText(HomeActivity.this, "Home Creation Successful", Toast.LENGTH_LONG).show();
                        getRegistrationToken();


                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        Toast.makeText(HomeActivity.this, "Home Creation failed", Toast.LENGTH_LONG).show();

                    }
                });

    }


    private void getRegistrationToken() {

        long homeId = currentHomeBean.getHomeId();
        TuyaHomeSdk.getActivatorInstance().getActivatorToken(homeId, new ITuyaActivatorGetToken() {
            @Override
            public void onSuccess(String token) {
                currentRegistrationToken = token;
                binding.btnScan.setClickable(true);
                //searchDevices(token);
            }

            @Override
            public void onFailure(String errorCode, String errorMsg) {
                Toast.makeText(HomeActivity.this, "Failed to get Registration Token", Toast.LENGTH_LONG).show();

            }
        });
    }

    private void searchDevices(String token) {

        tuyaActivator = TuyaHomeSdk.getActivatorInstance().newMultiActivator(new ActivatorBuilder()
                .setSsid(ssid)
                .setPassword(password)
                .setContext(this)
                .setActivatorModel(ActivatorModelEnum.TY_EZ)
                .setTimeOut(1000)
                .setToken(token)
                .setListener(new ITuyaSmartActivatorListener() {
                    @Override
                    public void onError(String errorCode, String errorMsg) {

                        Toast.makeText(HomeActivity.this, "Device Detection Failed", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onActiveSuccess(DeviceBean devResp) {

                        Toast.makeText(HomeActivity.this, "Device Detection Successful", Toast.LENGTH_LONG).show();
                        currentDeviceBean = devResp;
                        binding.cardview1.setClickable(true);
                        binding.cardview1.setBackgroundColor(Color.WHITE);
                        binding.deviceId.setText("Device ID" + currentDeviceBean.devId);
                        binding.deviceName.setText("Device Name" + currentDeviceBean.name);
                        binding.productId.setText("Product ID" + currentDeviceBean.productId);
                        binding.btnScan.setText("Search Devices");
                        tuyaActivator.stop();
                    }

                    @Override
                    public void onStep(String step, Object data) {
                        switch (step) {
                            case ActivatorEZStepCode
                                    .DEVICE_BIND_SUCCESS:
                                Toast.makeText(HomeActivity.this, "Device Bind Successful", Toast.LENGTH_LONG).show();
                                break;
                            case ActivatorEZStepCode.DEVICE_FIND:
                                Toast.makeText(HomeActivity.this, "New Device found", Toast.LENGTH_LONG).show();
                                break;
                        }

                    }
                })

        );

    }


}