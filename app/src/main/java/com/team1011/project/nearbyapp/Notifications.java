package com.team1011.project.nearbyapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * @author Filip
 */
public class Notifications {

    private Context mContext;


    public Notifications(Context context) {
        this.mContext = context;
    }

    public static void notify(Context context, String message, String  action, String from, String regid) {

        Notification myNotification;
        NotificationManager notificationManager;

        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        Bundle bundle = new Bundle();
        bundle.putString("FROM", from);
        bundle.putString("RID", regid);
        launchIntent.putExtras(bundle);

        if(action != null && launchIntent != null){
            launchIntent.setAction(action);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(context, -1, launchIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        myNotification = new Notification.Builder(context)
                .setContentTitle(action)
                .setContentText(message)
                .setTicker(message)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_action_error)
                .build();

        notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,myNotification);
    }

}
