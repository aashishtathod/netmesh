package com.example.netmesh;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.netmesh.databinding.ActivityVerificationCodeBinding;
import com.tuya.smart.android.user.api.IRegisterCallback;
import com.tuya.smart.android.user.bean.User;
import com.tuya.smart.home.sdk.TuyaHomeSdk;

public class VerificationCodeActivity extends AppCompatActivity {
    private ActivityVerificationCodeBinding binding;

    String countryCode, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerificationCodeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        email = getIntent().getStringExtra("email");
        countryCode = getIntent().getStringExtra("countryCode");
        password = getIntent().getStringExtra("password");

        Log.d("email", email);
        Log.d("pass", email);
        Log.d("country", email);

        binding.btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String verifyCode = binding.verificationCode.getText().toString().trim();
                TuyaHomeSdk.getUserInstance().registerAccountWithEmail(countryCode, email, password, verifyCode, new IRegisterCallback() {
                    @Override
                    public void onSuccess(User user) {
                        startActivity(new Intent(VerificationCodeActivity.this, HomeActivity.class));
                        finish();
                    }

                    @Override
                    public void onError(String code, String error) {
                        Toast.makeText(VerificationCodeActivity.this, "Login Failed due to " + error + verifyCode, Toast.LENGTH_LONG).show();
                        Toast.makeText(VerificationCodeActivity.this, verifyCode, Toast.LENGTH_LONG).show();

                    }
                });
            }
        });

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VerificationCodeActivity.super.onBackPressed();
                finish();
            }
        });


    }


}