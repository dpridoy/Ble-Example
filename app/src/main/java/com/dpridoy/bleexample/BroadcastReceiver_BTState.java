package com.dpridoy.bleexample;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BroadcastReceiver_BTState extends BroadcastReceiver {

    Context context;

    public BroadcastReceiver_BTState(Context context) {
        this.context = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();

        if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)){
            final int state=intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,BluetoothAdapter.ERROR);
            switch (state){
                case BluetoothAdapter.STATE_OFF:
                    String s="Bluetooth is off";
                    Utils.toast(context,s);
                    break;
                case BluetoothAdapter.STATE_TURNING_OFF:
                    String s2="Bluetooth is Turning off";
                    Utils.toast(context,s2);
                    break;
                case BluetoothAdapter.STATE_ON:
                    String s3="Bluetooth is on";
                    Utils.toast(context,s3);
                    break;
                case BluetoothAdapter.STATE_TURNING_ON:
                    String s4="Bluetooth is Turning on";
                    Utils.toast(context,s4);
                    break;
            }
        }

    }
}
