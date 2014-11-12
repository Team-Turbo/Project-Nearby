package com.team1011.project.nearbyapp;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Filip on 2014-11-03.
 */
public class MyService extends Service implements
        WifiP2pManager.ConnectionInfoListener{

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;

    public static final String TAG = "wifidirectdemo";

    // TXT RECORD properties
    public static final String TXTRECORD_PROP_AVAILABLE = "available";
    public static final String SERVICE_INSTANCE = "_nearbyapp";
    public static final String SERVICE_REG_TYPE = "_presence._tcp";

    public static final int MESSAGE_READ = 0x400 + 1;
    public static final int MY_HANDLE = 0x400 + 2;
    private WifiP2pManager manager;

    static final int SERVER_PORT = 4546;

    private final IntentFilter intentFilter = new IntentFilter();
    private WifiP2pManager.Channel channel;
    private BroadcastReceiver receiver = null;
    private WifiP2pDnsSdServiceRequest serviceRequest;

    private WiFiDirectServicesList servicesList;

    private TextView statusTxtView;

    private WiFiDirectServicesList.WiFiDevicesAdapter adapter;

    HashMap<String, String> records = new HashMap<String, String>();

    static int count = 0;



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

        servicesList = new WiFiDirectServicesList();



        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter
                .addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter
                .addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        manager = (WifiP2pManager) this.getSystemService(Context.WIFI_P2P_SERVICE);

        channel = manager.initialize(this, this.getMainLooper(), null);

        startRegistrationAndDiscovery();

        servicesList = new WiFiDirectServicesList();


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
            // start new client socket  handler
        }


    }

    private void startRegistrationAndDiscovery() {
        Map<String, String> record = new HashMap<String, String>();
        record.put(TXTRECORD_PROP_AVAILABLE, "visible");

        if (UI_Shell.myRegID != null) {
            WifiP2pDnsSdServiceInfo service = WifiP2pDnsSdServiceInfo.newInstance(
                    SERVICE_INSTANCE + UI_Shell.myRegID, SERVICE_REG_TYPE, record);

            manager.addLocalService(channel, service, new WifiP2pManager.ActionListener() {

                @Override
                public void onSuccess() {
                    Log.d("", "Added Local Service");
                }

                @Override
                public void onFailure(int error) {
                    Log.d("", "Failed to add a service");
                }
            });
        }

        discoverService();

    }



    private void discoverService() {

        /*
         * Register listeners for DNS-SD services. These are callbacks invoked
         * by the system when a service is actually discovered.
         */

        manager.setDnsSdResponseListeners(channel,
                new WifiP2pManager.DnsSdServiceResponseListener() {

                    @Override
                    public void onDnsSdServiceAvailable(String instanceName,
                                                        String registrationType, WifiP2pDevice srcDevice) {

                        // A service has been discovered. Is this our app?

                        if (instanceName.substring(0, SERVICE_INSTANCE.length()).equalsIgnoreCase(SERVICE_INSTANCE)) {

                            // update the UI and add the item the discovered
                            // device.

                            WiFiP2pService service = new WiFiP2pService();
                            service.device = srcDevice;
                            service.instanceName = instanceName;
                            service.serviceRegistrationType = registrationType;

                            records.put(srcDevice.deviceAddress, instanceName);
                          //  adapter.add(service);
                            Log.d("DEVICE_ADDRESS ", records.toString() + "" );
                           // Log.d("INSTANCE_NAME ", service.instanceName);

                           // adapter.notifyDataSetChanged();


                            Log.d("FOUND REGISTRATION ID", instanceName.substring(SERVICE_INSTANCE.length()));

                            UI_Shell.gcm.sendMessage(UI_Shell.userName, instanceName.substring(SERVICE_INSTANCE.length()));

                        }

                    }
                }, new WifiP2pManager.DnsSdTxtRecordListener() {

                    /**
                     * A new TXT record is available. Pick up the advertised
                     * buddy name.
                     */
                    @Override
                    public void onDnsSdTxtRecordAvailable(
                            String fullDomainName, Map<String, String> record,
                            WifiP2pDevice device) {
                        Log.d(TAG,
                                device.deviceName + " is "
                                        + record.get(TXTRECORD_PROP_AVAILABLE));
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



    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onDestroy() {
        Toast.makeText(this, "Broadcasting stopped", Toast.LENGTH_SHORT).show();
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

        mServiceLooper.quitSafely();
        super.onDestroy();
    }
}
