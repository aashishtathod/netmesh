package com.example.netmesh.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.netmesh.R;
import com.example.netmesh.databinding.ActivityHomeBinding;
import com.example.netmesh.fragments.HomeFragment;
import com.example.netmesh.fragments.ProfileFragment;
import com.example.netmesh.viewmodels.HomeActivityViewModel;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
    private HomeActivityViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(HomeActivityViewModel.class);

        int type = 0;

        if (getIntent().hasExtra("type")) {
            type = getIntent().getIntExtra("type", 0);
        }

        if (type == 1) {
            viewModel.createHome();
        }



        binding.addDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,DeviceSearchActivity.class));
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_home_activity, new HomeFragment()).commit();


        binding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_home_activity, new HomeFragment()).commit();
                        return true;
                    case R.id.person:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_home_activity, new ProfileFragment()).commit();
                        return true;
                }

                return false;
            }
        });

    }
}


        /*


         String homeName = "MyHome";
    String[] rooms = {"Kitchen", "Bedroom", "Study"};
    String currentRegistrationToken;

    ArrayList<String> roomList = new ArrayList<>();

    private String ssid = "Airtel_8308510460";
    private String password = "air19457";

    private HomeBean currentHomeBean;
    private DeviceBean currentDeviceBean;

    ITuyaActivator tuyaActivator;


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
                    intent.putExtra("DeviceName",currentDeviceBean.name);
                    //    intent.putExtra("ProductId", currentDeviceBean.productId);
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


}*/