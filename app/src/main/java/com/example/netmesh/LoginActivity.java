package com.example.netmesh;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.netmesh.databinding.ActivityLoginBinding;
import com.tuya.smart.android.user.api.ILoginCallback;
import com.tuya.smart.android.user.bean.User;
import com.tuya.smart.home.sdk.TuyaHomeSdk;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String countryCode = binding.countryCode.getText().toString().trim();
                String email = binding.email.getText().toString().trim();
                String password = binding.password.getText().toString().trim();

                if (!countryCode.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Signing In", Toast.LENGTH_SHORT).show();
                    TuyaHomeSdk.getUserInstance().loginWithEmail(countryCode, email, password, new ILoginCallback() {
                        @Override
                        public void onSuccess(User user) {
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        }

                        @Override
                        public void onError(String code, String error) {
                            Toast.makeText(LoginActivity.this, "Login Failed due to " + error, Toast.LENGTH_LONG).show();

                        }
                    });
                } else {
                    Toast.makeText(LoginActivity.this, "Please enter data in every field", Toast.LENGTH_LONG).show();

                }
            }
        });

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  startActivity(new Intent(LoginActivity.this, MainActivity.class));
                LoginActivity.super.onBackPressed();
                finish();
            }
        });
    }

}