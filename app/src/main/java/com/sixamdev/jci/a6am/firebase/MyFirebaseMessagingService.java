package com.sixamdev.jci.a6am.firebase;

/**
 * Created by avinashk on 05/11/17.
 */


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.sixamdev.jci.a6am.MainActivity;
import com.sixamdev.jci.a6am.R;

import org.json.JSONObject;


public class MyFirebaseMessagingService extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d("FIREBASE", "Message data payload: " + remoteMessage.getData());

            createNotification(remoteMessage.getData().toString());

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d("FIREBASE", "Message Notification Body: " + remoteMessage.getNotification().getBody());


        }

    }



    public void createNotification(String title){
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        //title of the notification!
        notificationBuilder.setContentTitle("6AM");
        notificationBuilder.setContentText(title);
        notificationBuilder.setAutoCancel(true);
        //icon to be displayed!
        notificationBuilder.setSmallIcon(R.drawable.park_maps);
        notificationBuilder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());


    }
}
