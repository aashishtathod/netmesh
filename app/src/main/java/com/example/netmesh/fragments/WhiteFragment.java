package com.example.netmesh.fragments;

import static com.example.netmesh.databinding.FragmentWhiteBinding.bind;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.netmesh.R;
import com.example.netmesh.databinding.FragmentWhiteBinding;
import com.tuya.smart.centralcontrol.TuyaLightDevice;
import com.tuya.smart.sdk.api.IResultCallback;
import com.tuya.smart.sdk.centralcontrol.api.ILightListener;
import com.tuya.smart.sdk.centralcontrol.api.ITuyaLightDevice;
import com.tuya.smart.sdk.centralcontrol.api.bean.LightDataPoint;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WhiteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WhiteFragment extends Fragment {
    private FragmentWhiteBinding binding;

    private static final String ARG_PARAM1 = "devId";
    private static final String ARG_PARAM2 = "devName";

    private String devId;
    private String devName;

    public WhiteFragment() {
    }


    public static WhiteFragment newInstance(String devId, String devName) {
        WhiteFragment fragment = new WhiteFragment();
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
        binding = bind(inflater.inflate(R.layout.fragment_white, container, false));

        if (devId != null) {
            binding.name.setText(devName);

            ITuyaLightDevice controlDevice = new TuyaLightDevice(devId);

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


            binding.switchCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    controlDevice.powerSwitch(isChecked, new IResultCallback() {
                        @Override
                        public void onError(String code, String error) {
                            Toast.makeText(getContext(), "Light Change Failed", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onSuccess() {
                        }
                    });
                }
            });


            binding.brightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    controlDevice.brightness(progress, new IResultCallback() {
                        @Override
                        public void onError(String code, String error) {
                            Toast.makeText(getContext(), "Light brightness Change Failed", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onSuccess() {
                        }
                    });
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });


            binding.cool.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    controlDevice.colorTemperature(progress, new IResultCallback() {
                        @Override
                        public void onError(String code, String error) {
                        }

                        @Override
                        public void onSuccess() {
                        }
                    });
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });





        } else {
            Toast.makeText(getContext(), "Device Id is empty", Toast.LENGTH_SHORT).show();
        }
        return binding.getRoot();
    }
}