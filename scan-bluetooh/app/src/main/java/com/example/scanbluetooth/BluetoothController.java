package com.example.scanbluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class BluetoothController {
    private BluetoothAdapter mBluetoothAdapter;

    private List<BluetoothDevice> mFoundDevices;

    public BluetoothController() {
        initializeBluetoothAdapter();
    }

    public boolean isBluetoothEnabled() {
        return mBluetoothAdapter != null && mBluetoothAdapter.isEnabled();
    }

    private void initializeBluetoothAdapter() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public void scanDevices(Context context, OnScanDeviceListener listener) {
        BroadcastReceiver receiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                    mFoundDevices = new ArrayList<>();
                    listener.onStart();

                } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    mFoundDevices.add(intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE));

                } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                    context.unregisterReceiver(this);
                    listener.onCompleted(mFoundDevices);
                }
            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        context.registerReceiver(receiver, filter);

        mBluetoothAdapter.startDiscovery();
    }

}
