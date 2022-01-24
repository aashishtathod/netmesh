package com.example.netmesh.viewmodels;

import android.app.Activity;

import androidx.lifecycle.ViewModel;

import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.home.sdk.bean.HomeBean;
import com.tuya.smart.home.sdk.callback.ITuyaGetHomeListCallback;
import com.tuya.smart.home.sdk.callback.ITuyaHomeResultCallback;
import com.tuya.smart.sdk.bean.DeviceBean;

import java.util.ArrayList;
import java.util.List;

public class HomeActivityViewModel extends ViewModel {


    String homeName = "MyHome";
    String[] rooms = {"Kitchen", "Bedroom", "Study"};
    String currentRegistrationToken;

    ArrayList<String> roomList = new ArrayList<>();

    List<HomeBean> homeBeansList;
    List<DeviceBean> devicesBeansList;


    private HomeBean currentHomeBean;
    private DeviceBean currentDeviceBean;



    public void createHome() {
        TuyaHomeSdk.getHomeManagerInstance().createHome(homeName,
                0, 0, "", roomList, new ITuyaHomeResultCallback() {
                    @Override
                    public void onSuccess(HomeBean bean) {
                        currentHomeBean = bean;
                        // Toast.makeText(HomeActivity.this, "Home Creation Successful", Toast.LENGTH_LONG).show();
                        // getRegistrationToken();
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        //  Toast.makeText(HomeActivity.this, "Home Creation failed", Toast.LENGTH_LONG).show();
                    }
                });
    }


    public List<DeviceBean> getDeviceListFromCloud(Activity activity) {
        TuyaHomeSdk.getHomeManagerInstance().queryHomeList(new ITuyaGetHomeListCallback() {
            @Override
            public void onSuccess(List<HomeBean> homeBeans) {
                homeBeansList = homeBeans;
                devicesBeansList = homeBeansList.get(0).getDeviceList();

            }

            @Override
            public void onError(String errorCode, String error) {

            }
        });
        return devicesBeansList;
    }


}



/*

public void getRegistrationToken() {

        long homeId = currentHomeBean.getHomeId();
        TuyaHomeSdk.getActivatorInstance().getActivatorToken(homeId, new ITuyaActivatorGetToken() {
            @Override
            public void onSuccess(String token) {
                currentRegistrationToken = token;
                //  binding.btnScan.setClickable(true);
                //searchDevices(token);
            }

            @Override
            public void onFailure(String errorCode, String errorMsg) {
                // Toast.makeText(HomeActivity.this, "Failed to get Registration Token", Toast.LENGTH_LONG).show();

            }
        });
    }


 */