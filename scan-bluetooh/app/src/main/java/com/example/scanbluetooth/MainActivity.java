package com.example.scanbluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    BluetoothController mBluetoothController;

    private ProgressBar mLoading;
    private RecyclerView mListDevices;

    private DeviceItemAdpater mDeviceItemAdpater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoading = findViewById(R.id.progressBar);
        mListDevices = findViewById(R.id.list_devices);
        initializeListDevices();

        findViewById(R.id.btn_scan_devices).setOnClickListener((view) -> {
            mBluetoothController.scanDevices(this, new OnScanDeviceListener() {
                @Override
                public void onStart() {
                    showLoading();
                }

                @Override
                public void onCompleted(List<BluetoothDevice> devices) {
                    hideLoading();


//                    showDevices(devices);
                }
            });
        });

        mBluetoothController = new BluetoothController();
        if (!mBluetoothController.isBluetoothEnabled()) {
            requestBluetooth();
        }
    }

    private void initializeListDevices() {
        mListDevices.setLayoutManager(new LinearLayoutManager(this));
        mDeviceItemAdpater = new DeviceItemAdpater();
        mListDevices.setAdapter(mDeviceItemAdpater);
    }

    private void showDevices(List<BluetoothDevice> devices) {
        mDeviceItemAdpater.replaceData(devices);
    }

    private void hideLoading() {
        mLoading.setVisibility(View.GONE);
    }

    private void showLoading() {
        mLoading.setVisibility(View.VISIBLE);
    }

    private void showWarningNoBluetooth() {
        Toast.makeText(
                this,
                "O bluetooth deve está ligado!",
                Toast.LENGTH_LONG
        ).show();
    }

    private void requestBluetooth() {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        this.startActivityForResult(intent, 1000);
    }

    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1000) {
            if (!mBluetoothController.isBluetoothEnabled()) showWarningNoBluetooth();
        }
    }






}
