package com.example.netmesh.fragments;

import static com.example.netmesh.databinding.FragmentHomeBinding.bind;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.netmesh.R;
import com.example.netmesh.adapters.HomeFragAdapter;
import com.example.netmesh.databinding.FragmentHomeBinding;
import com.example.netmesh.viewmodels.HomeActivityViewModel;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.home.sdk.bean.HomeBean;
import com.tuya.smart.home.sdk.callback.ITuyaGetHomeListCallback;
import com.tuya.smart.sdk.bean.DeviceBean;

import java.util.List;


public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private HomeActivityViewModel viewModel;
    private List<DeviceBean> deviceBeanList;
    HomeFragAdapter adapter ;
    List<HomeBean> homeBeansList;
    List<DeviceBean> devicesBeansList;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = bind(inflater.inflate(R.layout.fragment_home, container, false));

        TuyaHomeSdk.getHomeManagerInstance().queryHomeList(new ITuyaGetHomeListCallback() {
            @Override
            public void onSuccess(List<HomeBean> homeBeans) {
                homeBeansList = homeBeans;
                devicesBeansList = homeBeansList.get(0).getDeviceList();

                long homeid = homeBeans.get(0).getHomeId();
                Toast.makeText(getContext(), String.valueOf(homeid) , Toast.LENGTH_SHORT).show();

                if(!devicesBeansList.isEmpty()) {
                    Toast.makeText(getContext(), String.valueOf(devicesBeansList.get(0).getDevId()), Toast.LENGTH_SHORT).show();
                }else{
                   Toast.makeText(getContext(), "Empty", Toast.LENGTH_SHORT).show();
               }
            }

            @Override
            public void onError(String errorCode, String error) {

            }
        });


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(HomeActivityViewModel.class);

        deviceBeanList = viewModel.getDeviceListFromCloud(requireActivity());
        adapter = new HomeFragAdapter(requireContext(), deviceBeanList);
        binding.fragHomeRv.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.fragHomeRv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
   Log.d("***********************","              **********************************************                    "+deviceBeanList);
    }
}