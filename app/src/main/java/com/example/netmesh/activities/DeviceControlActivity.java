package com.example.netmesh.activities;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.netmesh.databinding.ActivityDeviceControlBinding;
import com.example.netmesh.viewmodels.DeviceControlViewModel;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.tuya.smart.sdk.centralcontrol.api.constants.LightMode;

public class DeviceControlActivity extends AppCompatActivity {
    private ActivityDeviceControlBinding binding;

    private String devId, devName;
    boolean isWorkModeColor = false, isColorSelected = false;

    private float[] hsv = new float[3];
    private float[] hue = new float[1];

    private DeviceControlViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeviceControlBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        devId = getIntent().getExtras().getString("DeviceId");
        devName = getIntent().getExtras().getString("DeviceName");
        viewModel = new ViewModelProvider(this).get(DeviceControlViewModel.class);

        viewModel.isWorkModeColor.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                isWorkModeColor = aBoolean;
            }
        });

        viewModel.isColorSelected.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                // isColorSelected = aBoolean;
                if (aBoolean && isWorkModeColor) {
                    viewModel.switchColor(((int) hue[0]));
                }
            }
        });


        binding.name.setText(devName);

        viewModel.createDevice(devId, this);

        changeOnOff();
        changeBrightness();
        changeCool();


        binding.changeColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isWorkModeColor) {
                    viewModel.switchModeToColor(LightMode.MODE_COLOUR);
                }
                if (isWorkModeColor) {
                    changeColor();
                }

            }
        });


    }


    private void changeOnOff() {
        binding.switchCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                viewModel.switchBulb(isChecked);
            }
        });
    }


    private void changeBrightness() {
        binding.brightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                viewModel.switchBrightness(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void changeCool() {
        binding.cool.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                viewModel.switchCool(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void changeColor() {
        ColorPickerDialogBuilder
                .with(DeviceControlActivity.this)
                .setTitle("Choose Color")
                .initialColor(Color.BLUE)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .noSliders()
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
                    }
                })
                .setPositiveButton("Ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        Color.colorToHSV(selectedColor, hsv);
                        hue[0] = hsv[0];
                        viewModel.isColorSelected.setValue(true);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build()
                .show();
    }


}
