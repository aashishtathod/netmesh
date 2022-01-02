package com.example.netmesh.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.netmesh.databinding.ActivityRegisterBinding;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.sdk.api.IResultCallback;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String countryCode = binding.countryCode.getText().toString().trim();
                String email = binding.email.getText().toString().trim();
                String password = binding.password.getText().toString().trim();
                if (!countryCode.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Creating Account ", Toast.LENGTH_SHORT).show();
                    TuyaHomeSdk.getUserInstance().sendVerifyCodeWithUserName(email, "", countryCode, 1, new IResultCallback() {
                        @Override
                        public void onError(String code, String error) {
                            Toast.makeText(RegisterActivity.this, error, Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onSuccess() {
                            Toast.makeText(RegisterActivity.this, "success", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(RegisterActivity.this, VerificationCodeActivity.class);
                            intent.putExtra("email", email);
                            intent.putExtra("password", password);
                            intent.putExtra("countryCode", countryCode);
                            startActivity(intent);
                        }
                    });
                }else{
                    Toast.makeText(RegisterActivity.this, "Please enter data in every field", Toast.LENGTH_LONG).show();
                }


            }
        });


        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                RegisterActivity.super.onBackPressed();
                finish();
            }
        });


    }
}