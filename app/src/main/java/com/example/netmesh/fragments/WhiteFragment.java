package com.example.netmesh.fragments;

import static com.example.netmesh.databinding.FragmentWhiteBinding.bind;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.netmesh.R;
import com.example.netmesh.databinding.FragmentWhiteBinding;
import com.example.netmesh.viewmodels.DeviceControlViewModel;
import com.tuya.smart.sdk.centralcontrol.api.constants.LightMode;


public class WhiteFragment extends Fragment {
    private FragmentWhiteBinding binding;
    private DeviceControlViewModel viewModel;

    private boolean isWorkModeWhite = false;

    public WhiteFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = bind(inflater.inflate(R.layout.fragment_white, container, false));

        binding.switchCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                viewModel.switchBulb(isChecked);
            }
        });


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

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(DeviceControlViewModel.class);

        viewModel.isWorkModeWhite.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                isWorkModeWhite = aBoolean;
            }
        });

        if (!isWorkModeWhite) {
            viewModel.switchMode(LightMode.MODE_WHITE);
        }

       binding.name.setText(viewModel.deviceName);
    }
}









/*
    private static final String ARG_PARAM1 = "devId";
    private static final String ARG_PARAM2 = "devName";

      private String devId;
    private String devName;

     public static WhiteFragment newInstance(String devId, String devName) {
        WhiteFragment fragment = new WhiteFragment();
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