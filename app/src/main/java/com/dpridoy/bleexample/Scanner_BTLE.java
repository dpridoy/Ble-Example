package com.dpridoy.bleexample;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Handler;


public class Scanner_BTLE {

    private MainActivity mA;

    private BluetoothAdapter adapter;
    private boolean mScanning;
    private Handler mHandler;

    private long scanPeriod;
    private int signalStrength;

    public Scanner_BTLE(MainActivity mainActivity, long scanPeriod, int signalStrength){
        mA=mainActivity;
        this.scanPeriod=scanPeriod;
        this.signalStrength=signalStrength;

        mHandler= new Handler();

        final BluetoothManager bluetoothManager= (BluetoothManager) mA.getSystemService(Context.BLUETOOTH_SERVICE);
        adapter=bluetoothManager.getAdapter();

    }

    public boolean isScanning(){
        return mScanning;
    }

    public void start(){
        if (!Utils.checkBluetooth(adapter)){
            Utils.requestUserBluetooth(mA);
            mA.stopScan();
        }else {
            scanLeDevice(true);
        }
    }

    public void stop(){
        scanLeDevice(false);
    }

    private void scanLeDevice(final boolean b) {
        if (b && !mScanning){
            Utils.toast(mA.getApplication(),"Starting BLE Scan..");
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Utils.toast(mA.getApplicationContext(),"Stoping BLE Scan");
                    mScanning=false;
                    adapter.startLeScan(mLeScanCallback);
                    mA.stopScan();
                }
            },scanPeriod);

            mScanning=true;
            adapter.startLeScan(mLeScanCallback);
        }
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback=new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            final int new_rssi=rssi;
            if (rssi>signalStrength){
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mA.addDevice(device,new_rssi);
                    }
                });
            }
        }
    };
}
