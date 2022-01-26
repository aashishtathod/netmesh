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
import com.tuya.smart.home.sdk.callback.ITuyaGetHomeListCallback;
import com.tuya.smart.sdk.api.ITuyaActivator;
import com.tuya.smart.sdk.api.ITuyaActivatorGetToken;
import com.tuya.smart.sdk.api.ITuyaSmartActivatorListener;
import com.tuya.smart.sdk.bean.DeviceBean;
import com.tuya.smart.sdk.enums.ActivatorEZStepCode;
import com.tuya.smart.sdk.enums.ActivatorModelEnum;

import java.util.List;

public class DeviceSearchActivity extends AppCompatActivity {
    private ActivityDeviceSearchBinding binding;
    private DeviceSearchActivityViewModel viewModel;
    private String[] descriptionData = {"Scanning\nDevice", "Binding\nDevice", "Register on\nCloud"};

    private List<HomeBean> homeBeansList;
    private String currentRegistrationToken;

    private String ssid;       //Airtel_8308510460   //123456789
    private String password;   //air19457            //123456789
    private  String type;

    private DeviceBean currentDeviceBean;
    private ITuyaActivator tuyaActivator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeviceSearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(DeviceSearchActivityViewModel.class);

        ssid = getIntent().getStringExtra("wifi");
        password = getIntent().getStringExtra("pass");
        type = getIntent().getStringExtra("type");

      //  Toast.makeText(this, ssid +"/"+password +"/"+type, Toast.LENGTH_SHORT).show();


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
                    //  startActivity(intent);

                }
            }
        });

        binding.stateProgressBar.setStateDescriptionData(descriptionData);
        binding.progressBar.setVisibility(View.VISIBLE);



            queryHomeList();


      /*  createHome(homeName, roomList);


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
        });*/


        // viewModel.queryHomeList(this);
        //  viewModel.getRegistrationToken();
        // viewModel.startActivator();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    public void queryHomeList() {
        TuyaHomeSdk.getHomeManagerInstance().queryHomeList(new ITuyaGetHomeListCallback() {
            @Override
            public void onSuccess(List<HomeBean> homeBeans) {
                homeBeansList = homeBeans;
                Toast.makeText(DeviceSearchActivity.this, "HomeList Successful =" + homeBeansList.get(0).getHomeId(), Toast.LENGTH_SHORT).show();
                getRegistrationToken();
            }

            @Override
            public void onError(String errorCode, String error) {
                Toast.makeText(DeviceSearchActivity.this, error, Toast.LENGTH_SHORT).show();

            }
        });
    }


    public void getRegistrationToken() {
        long homeId = homeBeansList.get(0).getHomeId();
        // Toast.makeText(context.get(),String.valueOf(homeBeansList.get(0).getHomeId()), Toast.LENGTH_SHORT).show();

        TuyaHomeSdk.getActivatorInstance().getActivatorToken(homeId, new ITuyaActivatorGetToken() {
            @Override
            public void onSuccess(String token) {
                //   Toast.makeText(DeviceSearchActivity.this, "Token Successful " + token, Toast.LENGTH_SHORT).show();
                currentRegistrationToken = token;
                searchDevices(currentRegistrationToken);
            }

            @Override
            public void onFailure(String errorCode, String errorMsg) {
                Toast.makeText(DeviceSearchActivity.this, errorMsg, Toast.LENGTH_LONG).show();
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
                        /*
                        ArrayList<DeviceBean> deviceBeanArrayList = new ArrayList<>();
                        deviceBeanArrayList.add(currentDeviceBean);
                        currentHomeBean.setDeviceList(deviceBeanArrayList);
                        */
                        tuyaActivator.stop();

                        if(type == "light") {
                            Intent intent = new Intent(DeviceSearchActivity.this, DeviceControlActivity.class);
                            intent.putExtra("DeviceId", currentDeviceBean.devId);
                            intent.putExtra("DeviceName", currentDeviceBean.name);
                            startActivity(intent);
                            finish();
                        }else  if(type == "switch"){
                            Intent intent = new Intent(DeviceSearchActivity.this, SwitchControlActivity.class);
                            intent.putExtra("DeviceId", currentDeviceBean.devId);
                            intent.putExtra("DeviceName", currentDeviceBean.name);
                            startActivity(intent);
                            finish();
                        }else{
                            Intent intent = new Intent(DeviceSearchActivity.this, DimmerControlActivity.class);
                            intent.putExtra("DeviceId", currentDeviceBean.devId);
                            intent.putExtra("DeviceName", currentDeviceBean.name);
                            startActivity(intent);
                            finish();
                        }

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