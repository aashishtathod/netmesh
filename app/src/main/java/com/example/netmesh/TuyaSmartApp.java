package com.example.netmesh;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.sdk.api.INeedLoginListener;

public class TuyaSmartApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        TuyaHomeSdk.setDebugMode(true);

        TuyaHomeSdk.init(this, "sxm7kvushpstqxnxwxqf", "f5nhfytqwtxr3sqqrjxq3tvuhfvyhww7");
        TuyaHomeSdk.setOnNeedLoginListener(new INeedLoginListener() {
            @Override
            public void onNeedLogin(Context context) {
                startActivity(new Intent(TuyaSmartApp.this, MainActivity.class));
            }
        });
    }
}
