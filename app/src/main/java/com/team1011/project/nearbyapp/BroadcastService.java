package com.team1011.project.nearbyapp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.DnsSdServiceResponseListener;
import android.net.wifi.p2p.WifiP2pManager.DnsSdTxtRecordListener;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Broadcast your GCM registration ID over WiFi Direct to be discovered by
 * other users of this app.
 * Once found, send JSON object
 * Created by Filip on 2014-11-03.
 */
public class BroadcastService extends Service implements
        WifiP2pManager.ConnectionInfoListener{

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;

    public static final String TAG = "wifidirectdemo";

    // TXT RECORD properties
    public static final String SERVICE_INSTANCE = "_nearbyapp";
    public static final String SERVICE_REG_TYPE = "_presence._tcp";

    private WifiP2pManager manager;


    private final IntentFilter intentFilter = new IntentFilter();
    private Channel channel;
    private WifiP2pDnsSdServiceRequest serviceRequest;
    private String rID;
    private HashMap<String, String> records = new HashMap<String, String>();
    private JSONObject data = new JSONObject();






    public static boolean isRunning = false;



    @Override
    public void onCreate() {
        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND);

        thread.setDaemon(true);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Broadcasting started", Toast.LENGTH_SHORT).show();
        isRunning = true;

        Bundle bundle;

        if (intent != null) {

            bundle = intent.getExtras();
            rID = bundle.getString("REG_ID");
        }

        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        manager = (WifiP2pManager) this.getSystemService(Context.WIFI_P2P_SERVICE);

        channel = manager.initialize(this, this.getMainLooper(), null);

        startRegistrationAndDiscovery();


        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        //Log.d("", "" + intent.getStringExtra("PARAM_1"));
        mServiceHandler.sendMessage(msg);

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.
            long endTime = System.currentTimeMillis() + 5*1000;
            while (System.currentTimeMillis() < endTime) {
                synchronized (this) {
                    try {
                      //  Log.d("", "runnning service" + count++);
                       //wait(endTime - System.currentTimeMillis());
                    } catch (Exception e) {
                    }
                }
            }
            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            //stopSelf(msg.arg1);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onConnectionInfoAvailable(WifiP2pInfo p2pInfo) {
        if (p2pInfo.isGroupOwner) {
            Log.d(TAG, "Connected as group owner");
            Log.d("", "Connected as group owner");
            Toast.makeText(getApplicationContext(), "OWNER", Toast.LENGTH_SHORT).show();
            // start new group owner socket handler
        } else {
            Log.d(TAG, "Connected as peer");
            Log.d("", "Connected as peer");
            Toast.makeText(getApplicationContext(), "Peer", Toast.LENGTH_SHORT).show();
        }
    }

    private void startRegistrationAndDiscovery() {
        Map<String, String> record = new HashMap<String, String>();

        record.put("RID", UI_Shell.myRegID);

        Log.d("RECORD", record.toString());

        //This is where we would also put the user's Matching data
        try {
            data.put("TYPE", "control");
            data.put("USER_NAME", UI_Shell.userName);
            data.put("REG_ID", rID);
            //data.put("MATCH_DATA", Category.getCurrentMatchData());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        final WifiP2pDnsSdServiceInfo service = WifiP2pDnsSdServiceInfo.newInstance(
                SERVICE_INSTANCE, SERVICE_REG_TYPE, record);

        manager.addLocalService(channel, service, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                Log.d("", "Added Local Service");
            }

            @Override
            public void onFailure(int error) {
                Log.d("", "Failed to add a service");
                Log.d("Error Code", "" + error);
            }
        });

        discoverService();
    }

    private void discoverService() {
        /*
         * Register listeners for DNS-SD services. These are callbacks invoked
         * by the system when a service is actually discovered.
         */

        manager.setDnsSdResponseListeners(channel,
                new DnsSdServiceResponseListener() {

                    @Override
                    public void onDnsSdServiceAvailable(String instanceName,
                                                        String registrationType, WifiP2pDevice srcDevice) {

                        // A service has been discovered. Is this our app?

                        if (instanceName.equalsIgnoreCase(SERVICE_INSTANCE)) {

                            // update the UI and add the item the discovered
                            // device.

                            WiFiP2pService service = new WiFiP2pService();
                            service.device = srcDevice;
                            service.instanceName = instanceName;
                            service.serviceRegistrationType = registrationType;

                            records.put(srcDevice.deviceAddress, instanceName);
                            Log.d("DEVICE_ADDRESS ", records.toString() + "" );

                            Log.d(TAG, "onBonjourServiceAvailable "
                                    + instanceName);

                        }
                    }
                }, new DnsSdTxtRecordListener() {

                    /**
                     * A new TXT record is available. Pick up the advertised
                     * buddy name.
                     */
                    @Override
                    public void onDnsSdTxtRecordAvailable(
                            String fullDomainName, Map<String, String> record,
                            WifiP2pDevice device) {

                       if (record.containsKey("RID")) {

                            Log.d("FOUND REGISTRATION ID", record.get("RID"));
                            //Send my userName to the found id

                           GCMstatic.gcm.sendMessage(data.toString(), record.get("RID"));


                       }
                    }
                });

        // After attaching listeners, create a service request and initiate
        // discovery.
        serviceRequest = WifiP2pDnsSdServiceRequest.newInstance();
        manager.addServiceRequest(channel, serviceRequest,
                new WifiP2pManager.ActionListener() {

                    @Override
                    public void onSuccess() {
                        Log.d("","Added service discovery request");
                    }

                    @Override
                    public void onFailure(int arg0) {
                        Log.d("","Failed adding service discovery request");
                    }
                });

        manager.discoverServices(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d("", "Service discovery initiated");
            }

            @Override
            public void onFailure(int arg0) {
                Log.d("", "Service discovery failed");

            }
        });
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Broadcasting stopped", Toast.LENGTH_SHORT).show();
        isRunning = false;

        if (manager != null && channel != null) {
            manager.removeGroup(channel, new WifiP2pManager.ActionListener() {

                @Override
                public void onFailure(int reasonCode) {
                    Log.d(TAG, "Disconnect failed. Reason :" + reasonCode);
                }

                @Override
                public void onSuccess() {
                }

            });
        }

        mServiceLooper.quit();
        super.onDestroy();
    }
}
