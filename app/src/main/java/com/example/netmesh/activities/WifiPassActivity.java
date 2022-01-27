package com.example.netmesh.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.netmesh.databinding.ActivityWifiPassBinding;

public class WifiPassActivity extends AppCompatActivity {
    private ActivityWifiPassBinding binding;
    String type;
    static final String TYPE ="type";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWifiPassBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        type = getIntent().getStringExtra(TYPE);


        binding.wifi.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT){
                    binding.password.requestFocus();
                    return true;
                }
                return  false;
            }

        });

        binding.password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT){
                   InputMethodManager manager = ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE));
                   View view = getCurrentFocus();
                   manager.hideSoftInputFromWindow(view.getWindowToken(),0);
                    return true;
                }
                return  false;
            }

        });


        binding.btnNxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String wifiText = binding.wifi.getText().toString().trim();
                String passText = binding.password.getText().toString().trim();

                if (!wifiText.isEmpty() && !passText.isEmpty()) {

                    Intent intent = new Intent(WifiPassActivity.this , DeviceSearchActivity.class);
                    intent.putExtra("wifi", wifiText);
                    intent.putExtra("type", type);
                    intent.putExtra("pass", passText);
                    startActivity(intent);
                    finish();
                }else{
                   // binding.wifi.setError("* required");
                   // binding.password.setError("* required");
                    Toast.makeText(WifiPassActivity.this, "WiFi or Password cant be empty.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}