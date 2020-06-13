package com.dpridoy.bleexample;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = MainActivity.class.getSimpleName();

    public static final int REQUEST_ENABLE_BT = 1;

    private HashMap<String,BTLE_Device> mBTDeviceHashMap;
    private ArrayList<BTLE_Device> mBTDevicesArrayList;
    private ListAdapter_BTLE_Devices adapter;

    private Button btn_Scan;

    private BroadcastReceiver_BTState mBTStateUpdateReceiver;
    private Scanner_BTLE mScannerBtle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Use this check to determine whether BLE is supported on the device. Then
        // you can selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Utils.toast(getApplicationContext(),"BLE not supported");
            finish();
        }

        mBTStateUpdateReceiver = new BroadcastReceiver_BTState(getApplicationContext());
        mScannerBtle=new Scanner_BTLE(this,7500,-75);

        mBTDeviceHashMap = new HashMap<>();
        mBTDevicesArrayList = new ArrayList<>();

        adapter = new ListAdapter_BTLE_Devices(this,R.layout.btle_device_list_item,mBTDevicesArrayList);

        ListView listView=new ListView(this);
        listView.setAdapter(adapter);
        ((ScrollView)findViewById(R.id.scrollView)).addView(listView);
        btn_Scan=findViewById(R.id.btn_scan);
        btn_Scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.toast(getApplicationContext(),"Scan button pressed");
                if (!mScannerBtle.isScanning()){
                    startScan();
                }else {
                    stopScan();
                }
            }
        });

    }



    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(mBTStateUpdateReceiver,new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mBTStateUpdateReceiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {

            } else if (resultCode == RESULT_CANCELED) {
                Utils.toast(getApplicationContext(), "Please turn on Bluetooth");
            }
        }
    }

    public void addDevice(BluetoothDevice device, int new_rssi) {
        String address=device.getAddress();
        if (!mBTDeviceHashMap.containsKey(address)){
            BTLE_Device btle_device=new BTLE_Device(device);
            btle_device.setRssi(new_rssi);
            mBTDeviceHashMap.put(address,btle_device);
            mBTDevicesArrayList.add(btle_device);
        }else {
            mBTDeviceHashMap.get(address).setRssi(new_rssi);
        }
        adapter.notifyDataSetChanged();
        mScannerBtle.start();
    }

    private void startScan() {
        btn_Scan.setText("Scanning..");
        mBTDevicesArrayList.clear();
        mBTDeviceHashMap.clear();
        adapter.notifyDataSetChanged();
        mScannerBtle.start();
    }

    public void stopScan() {
        btn_Scan.setText("Scan Again");
        mScannerBtle.stop();
    }
}
