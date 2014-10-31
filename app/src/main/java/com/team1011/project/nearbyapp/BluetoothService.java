package com.team1011.project.nearbyapp;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.IBinder;

public class BluetoothService extends Service {

    private BluetoothAdapter mBtAdapter;

    public BluetoothService() {

        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        new Thread(new Runnable() {
            public void run() {
                for(;;) {
                    synchronized (mBtAdapter) {
                        // If we're already discovering, stop it
                        if (mBtAdapter.isDiscovering()) {
                            mBtAdapter.cancelDiscovery();
                        }
                        // Request discover from BluetoothAdapter
                        mBtAdapter.startDiscovery();
                        try {
                            mBtAdapter.wait(30000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    public void onDestroy() {
        super.onDestroy();

        // Make sure we're not doing discovery anymore
        if (mBtAdapter != null) {
            mBtAdapter.cancelDiscovery();
        }
    }

}
