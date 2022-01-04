package com.example.netmesh.viewmodels;

import android.app.Activity;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tuya.smart.centralcontrol.TuyaLightDevice;
import com.tuya.smart.sdk.api.IResultCallback;
import com.tuya.smart.sdk.centralcontrol.api.ILightListener;
import com.tuya.smart.sdk.centralcontrol.api.ITuyaLightDevice;
import com.tuya.smart.sdk.centralcontrol.api.bean.LightDataPoint;
import com.tuya.smart.sdk.centralcontrol.api.constants.LightMode;

import java.lang.ref.WeakReference;

public class DeviceControlViewModel extends ViewModel {
    public MutableLiveData<Boolean> isWorkModeColor = new MutableLiveData<>();
    public MutableLiveData<Boolean> isColorSelected = new MutableLiveData<>();
    private ITuyaLightDevice controlDevice;
    private WeakReference<Activity> context;



    public void createDevice(String devId , Activity activity) {

        isWorkModeColor.setValue(false);
        isColorSelected.setValue(false);
        this.context = new WeakReference<>(activity);

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
    }

    public void switchBulb(boolean isChecked) {
        controlDevice.powerSwitch(isChecked, new IResultCallback() {
            @Override
            public void onError(String code, String error) {
                 Toast.makeText(context.get(), error, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess() {
                Toast.makeText(context.get(), "Successfully ", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void switchBrightness(int progress) {
        controlDevice.brightness(progress, new IResultCallback() {
            @Override
            public void onError(String code, String error) {
                Toast.makeText(context.get(), /*"Light brightness Change Failed"*/ error, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess() {
            }
        });
    }

    public void switchCool(int progress) {
        controlDevice.colorTemperature(progress, new IResultCallback() {
            @Override
            public void onError(String code, String error) {
                 Toast.makeText(context.get(), /*"Light Cool Change Failed"*/ error, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess() {
            }
        });
    }

    public void switchModeToColor(LightMode lightMode) {
        controlDevice.workMode(lightMode, new IResultCallback() {
            @Override
            public void onError(String code, String error) {
                 Toast.makeText(context.get(), /*"Light Cool Change Failed"*/ error, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess() {
                isWorkModeColor.setValue(true);
            }
        });
    }

    public void switchColor(int hue){
        controlDevice.colorHSV(hue, 100, 100, new IResultCallback() {
            @Override
            public void onError(String code, String error) {
                Toast.makeText(context.get(), error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess() {
            }
        });
    }

}
