package com.dpridoy.bleexample;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter_BTLE_Devices extends ArrayAdapter<BTLE_Device> {

    Activity activity;
    int layoutResource;
    ArrayList<BTLE_Device> devices;

    public ListAdapter_BTLE_Devices(Activity activity, int layoutResource, ArrayList<BTLE_Device> devices) {
        super(activity.getApplicationContext(), layoutResource, devices);
        this.activity = activity;
        this.layoutResource = layoutResource;
        this.devices = devices;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null){
            LayoutInflater inflater= (LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layoutResource,parent,false);
        }

        BTLE_Device device=devices.get(position);
        String name=device.getName();
        String address=device.getAddress();
        int rssi=device.getRssi();

        TextView tv_name=convertView.findViewById(R.id.tv_name);
        if (name!=null && name.length()>0){
            tv_name.setText(device.getName());
        }else {
            tv_name.setText("No name");
        }
        TextView tv_rssi=convertView.findViewById(R.id.tv_rssi);
        tv_rssi.setText("RSSI : "+Integer.toString(rssi));
        TextView tv_mac=convertView.findViewById(R.id.tv_macaddr);
        if (address!=null && address.length()>0){
            tv_mac.setText(address);
        }else {
            tv_mac.setText("No Mac");
        }

        return convertView;
    }
}
