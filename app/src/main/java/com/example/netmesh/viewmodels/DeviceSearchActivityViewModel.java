package com.example.netmesh.viewmodels;

import android.app.Activity;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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

import java.lang.ref.WeakReference;
import java.util.List;

public class DeviceSearchActivityViewModel extends ViewModel {

    List<HomeBean> homeBeansList;
    ITuyaActivator tuyaActivator;
    private String ssid = "Airtel_8308510460";
    private String password = "air19457";
    String currentRegistrationToken;
    public DeviceBean currentDeviceBean;
    private WeakReference<Activity> context;

    public MutableLiveData<Boolean> devFound = new MutableLiveData<>();
    public MutableLiveData<Boolean> devBind = new MutableLiveData<>();
    public MutableLiveData<Boolean> devActivate = new MutableLiveData<>();


    // private HomeBean currentHomeBean;

    public void queryHomeList(Activity activity) {
        this.context = new WeakReference<>(activity);
        this.devActivate.setValue(false);
        this.devFound.setValue(false);
        this.devBind.setValue(false);


        TuyaHomeSdk.getHomeManagerInstance().queryHomeList(new ITuyaGetHomeListCallback() {
            @Override
            public void onSuccess(List<HomeBean> homeBeans) {
                homeBeansList = homeBeans;
                Toast.makeText(context.get(), "HomeList Successful", Toast.LENGTH_SHORT).show();
                getRegistrationToken();
            }

            @Override
            public void onError(String errorCode, String error) {
                Toast.makeText(context.get(), error, Toast.LENGTH_SHORT).show();

            }
        });
    }





    public void getRegistrationToken() {
         long homeId = homeBeansList.get(0).getHomeId();
        // Toast.makeText(context.get(),String.valueOf(homeBeansList.get(0).getHomeId()), Toast.LENGTH_SHORT).show();

        TuyaHomeSdk.getActivatorInstance().getActivatorToken(homeId, new ITuyaActivatorGetToken() {
            @Override
            public void onSuccess(String token) {
                Toast.makeText(context.get(), "Token Successful", Toast.LENGTH_SHORT).show();
                currentRegistrationToken = token;
                startActivator();
            }

            @Override
            public void onFailure(String errorCode, String errorMsg) {
                Toast.makeText(context.get(), errorMsg, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void startActivator() {
        Toast.makeText(context.get(), "Activator Started", Toast.LENGTH_SHORT).show();

        tuyaActivator = TuyaHomeSdk.getActivatorInstance().newMultiActivator(new ActivatorBuilder()
                .setSsid(ssid)
                .setPassword(password)
                .setContext(context.get())
                .setActivatorModel(ActivatorModelEnum.TY_EZ)
                .setTimeOut(1000)
                .setToken(currentRegistrationToken)
                .setListener(new ITuyaSmartActivatorListener() {
                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        Toast.makeText(context.get(), "Device Detection Failed", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onActiveSuccess(DeviceBean devResp) {
                       Toast.makeText(context.get(), "Device Activation Successful", Toast.LENGTH_LONG).show();
                        devActivate.setValue(true);
                        currentDeviceBean = devResp;
                        tuyaActivator.stop();



                    }

                    @Override
                    public void onStep(String step, Object data) {
                        switch (step) {
                            case ActivatorEZStepCode.DEVICE_BIND_SUCCESS:
                                devBind.setValue(true);
                               Toast.makeText(context.get(), "Device Bind Successful", Toast.LENGTH_LONG).show();
                                break;
                            case ActivatorEZStepCode.DEVICE_FIND:
                                Toast.makeText(context.get(), "New Device found", Toast.LENGTH_LONG).show();
                                devFound.setValue(true);
                                break;
                        }

                    }
                })

        );
    }
}
