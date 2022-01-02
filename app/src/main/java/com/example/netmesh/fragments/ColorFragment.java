package com.example.netmesh.fragments;

import static com.example.netmesh.databinding.FragmentColorBinding.bind;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.netmesh.R;
import com.example.netmesh.databinding.FragmentColorBinding;
import com.flask.colorpicker.OnColorChangedListener;
import com.tuya.smart.centralcontrol.TuyaLightDevice;
import com.tuya.smart.sdk.api.IResultCallback;
import com.tuya.smart.sdk.centralcontrol.api.ILightListener;
import com.tuya.smart.sdk.centralcontrol.api.ITuyaLightDevice;
import com.tuya.smart.sdk.centralcontrol.api.bean.LightDataPoint;

public class ColorFragment extends Fragment {
    private FragmentColorBinding binding;

    private static final String ARG_PARAM1 = "devId";
    private static final String ARG_PARAM2 = "devName";

    private String devId;
    private String devName;

    public ColorFragment() {

    }

    public static ColorFragment newInstance(String devId, String devName) {
        ColorFragment fragment = new ColorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, devId);
        args.putString(ARG_PARAM2, devName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            devId = getArguments().getString(ARG_PARAM1);
            devName = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = bind(inflater.inflate(R.layout.fragment_color, container, false));

        ITuyaLightDevice controlDevice;

        float[] hsv = new float[3];

         float[] hue = new float[1];
         float[] sat = new float[1];
         float[] val = new float[1];

        binding.colorPickerView.addOnColorChangedListener(new OnColorChangedListener() {
            @Override
            public void onColorChanged(int selectedColor) {

                Color.colorToHSV(selectedColor, hsv);
                hue[0] = hsv[0];
                sat[0] = hsv[1];
                val[0] = hsv[2];


                Log.d("aaaaaaaaaaaaaaaaaaa'", "onColorChanged:                               hue" + hue[0]);
                Log.d("aaaaaaaaaaaaaaaaaaa'", "onColorChanged:                               sat" + sat[0]);
                Log.d("aaaaaaaaaaaaaaaaaaa'", "onColorChanged:                               val" + val[0]);

            }
        });


        if (devId != null) {
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


            controlDevice.colorHSV(((int) hue[0]), ((int) sat[0]), ((int) val[0]), new IResultCallback() {
                @Override
                public void onError(String code, String error) {
                    Toast.makeText(getContext(), "Failed to change Color", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess() {
                }
            });



        } else {
            Toast.makeText(getContext(), "Device Id is empty", Toast.LENGTH_SHORT).show();
        }




        return binding.getRoot();
    }
}