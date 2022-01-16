package com.example.netmesh.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.netmesh.databinding.ActivityDeviceSearchBinding;
import com.example.netmesh.viewmodels.DeviceSearchActivityViewModel;
import com.kofigyan.stateprogressbar.StateProgressBar;
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
import java.util.List;

public class DeviceSearchActivity extends AppCompatActivity {
    private ActivityDeviceSearchBinding binding;
    private DeviceSearchActivityViewModel viewModel;
    String[] descriptionData = {"Scanning\nDevice", "Binding\nDevice", "Register on\nCloud"};


    String homeName = "MyHome";
    String[] rooms = {"Kitchen", "Bedroom", "Study"};
    String currentRegistrationToken;

    ArrayList<String> roomList = new ArrayList<>();

    private String ssid = "123456789";
    private String password = "123456789";

    private HomeBean currentHomeBean;
    private DeviceBean currentDeviceBean;

    ITuyaActivator tuyaActivator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeviceSearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(DeviceSearchActivityViewModel.class);

        viewModel.devFound.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    binding.stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
                }
            }
        });
        viewModel.devBind.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    binding.stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
                }
            }
        });
        viewModel.devActivate.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    binding.stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
                    Intent intent = new Intent(DeviceSearchActivity.this, DeviceControlActivity.class);
                    intent.putExtra("DeviceId", viewModel.currentDeviceBean.devId);
                    intent.putExtra("DeviceName", viewModel.currentDeviceBean.name);
                    startActivity(intent);

                }
            }
        });

        binding.stateProgressBar.setStateDescriptionData(descriptionData);
        binding.progressBar.setVisibility(View.VISIBLE);


        createHome(homeName, roomList);

        binding.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tuyaActivator == null) {
                    searchDevices(currentRegistrationToken);
                } else {
                    tuyaActivator.stop();
                    tuyaActivator.start();

                }
            }
        });


        // viewModel.queryHomeList(this);
        //  viewModel.getRegistrationToken();
        // viewModel.startActivator();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    private void createHome(String homeName, List<String> roomList) {
        TuyaHomeSdk.getHomeManagerInstance().createHome(homeName,
                0, 0, "home", roomList, new ITuyaHomeResultCallback() {
                    @Override
                    public void onSuccess(HomeBean bean) {
                        currentHomeBean = bean;
                        Toast.makeText(DeviceSearchActivity.this, "Home Creation Successful", Toast.LENGTH_LONG).show();
                        getRegistrationToken();


                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        Toast.makeText(DeviceSearchActivity.this, "Home Creation failed", Toast.LENGTH_LONG).show();

                    }
                });

    }


    private void getRegistrationToken() {

        long homeId = currentHomeBean.getHomeId();
        TuyaHomeSdk.getActivatorInstance().getActivatorToken(homeId, new ITuyaActivatorGetToken() {
            @Override
            public void onSuccess(String token) {
                currentRegistrationToken = token;
                Toast.makeText(DeviceSearchActivity.this, "Token = " + token, Toast.LENGTH_LONG).show();
                searchDevices(currentRegistrationToken);
            }

            @Override
            public void onFailure(String errorCode, String errorMsg) {
                Toast.makeText(DeviceSearchActivity.this, "Failed to get Registration Token", Toast.LENGTH_LONG).show();

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

                        Toast.makeText(DeviceSearchActivity.this, "Device Detection Failed", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onActiveSuccess(DeviceBean devResp) {

                        Toast.makeText(DeviceSearchActivity.this, "Device Detection Successful", Toast.LENGTH_LONG).show();
                        currentDeviceBean = devResp;
                        binding.stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
                        tuyaActivator.stop();

                        Intent intent = new Intent(DeviceSearchActivity.this, DeviceControlActivity.class);
                        intent.putExtra("DeviceId", currentDeviceBean.devId);
                        intent.putExtra("DeviceName", currentDeviceBean.name);
                        startActivity(intent);

                    }

                    @Override
                    public void onStep(String step, Object data) {
                        switch (step) {
                            case ActivatorEZStepCode
                                    .DEVICE_BIND_SUCCESS:
                                binding.stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
                                Toast.makeText(DeviceSearchActivity.this, "Device Bind Successful", Toast.LENGTH_LONG).show();
                                break;
                            case ActivatorEZStepCode.DEVICE_FIND:
                                binding.stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);

                                Toast.makeText(DeviceSearchActivity.this, "New Device found", Toast.LENGTH_LONG).show();
                                break;
                        }

                    }
                })

        );

        tuyaActivator.start();

    }

}