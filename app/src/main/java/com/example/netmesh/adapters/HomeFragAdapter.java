package com.example.netmesh.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.netmesh.R;
import com.example.netmesh.activities.DeviceControlActivity;
import com.tuya.smart.sdk.bean.DeviceBean;

import java.util.List;

public class HomeFragAdapter extends RecyclerView.Adapter<HomeFragAdapter.HomeFragViewHolder> {

    Context context;
    List<DeviceBean> deviceBeans;

    public HomeFragAdapter(Context context, List<DeviceBean> deviceBeans) {
        this.context = context;
        this.deviceBeans = deviceBeans;
    }

    @NonNull
    @Override
    public HomeFragAdapter.HomeFragViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_frag_rv_item, parent, false);
        return new HomeFragViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeFragAdapter.HomeFragViewHolder holder, int position) {
        int pos = holder.getAdapterPosition();
        holder.textView.setText(deviceBeans.get(position).name);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DeviceControlActivity.class);
                intent.putExtra("DeviceId", deviceBeans.get(pos).devId);
                intent.putExtra("DeviceName", deviceBeans.get(pos).name);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (deviceBeans != null) {
            deviceBeans.size();
        }
        return 0;
    }

    public class HomeFragViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private TextView textView;

        public HomeFragViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardview);
            textView = itemView.findViewById(R.id.deviceName);
        }
    }
}
