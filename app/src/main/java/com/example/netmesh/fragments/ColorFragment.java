package com.example.netmesh.fragments;

import static com.example.netmesh.databinding.FragmentColorBinding.bind;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.netmesh.R;
import com.example.netmesh.databinding.FragmentColorBinding;
import com.example.netmesh.viewmodels.DeviceControlViewModel;
import com.flask.colorpicker.OnColorChangedListener;
import com.tuya.smart.sdk.centralcontrol.api.constants.LightMode;

public class ColorFragment extends Fragment {
    private FragmentColorBinding binding;
    private DeviceControlViewModel viewModel;

    boolean isWorkModeColor = false;

    private float[] hsv = new float[3];
    private int hue, sat, val;


    public ColorFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = bind(inflater.inflate(R.layout.fragment_color, container, false));

        binding.colorPickerView.addOnColorChangedListener(new OnColorChangedListener() {
            @Override
            public void onColorChanged(int selectedColor) {
                Color.colorToHSV(selectedColor, hsv);
                hue = ((int) hsv[0]);
                sat = ((int) (hsv[1] * 100));
                val = ((int) (hsv[2] * 100));

                if (sat > 100) {
                    sat = 100;
                }
                if (val > 100) {
                    val = 100;
                }
                if (!isWorkModeColor) {
                    viewModel.switchMode(LightMode.MODE_COLOUR);
                }
                if (isWorkModeColor) {
                    viewModel.switchColor(hue, sat, val);
                }
            }
        });

        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(DeviceControlViewModel.class);

        if (!isWorkModeColor) {
            viewModel.switchMode(LightMode.MODE_COLOUR);
        }

        viewModel.isWorkModeColor.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                isWorkModeColor = aBoolean;
            }
        });

    }
}


/*
 private static final String ARG_PARAM1 = "devId";
    private static final String ARG_PARAM2 = "devName";

    private String devId;
    private String devName;

 public static ColorFragment newInstance(String devId, String devName) {
        ColorFragment fragment = new ColorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, devId);
        args.putString(ARG_PARAM2, devName);
        fragment.setArguments(args);
        return fragment;
    }

  if (getArguments() != null) {
            devId = getArguments().getString(ARG_PARAM1);
            devName = getArguments().getString(ARG_PARAM2);
        }

 */