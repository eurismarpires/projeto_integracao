package com.eurismar.clientgcmintegracao;


import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    String TAG = "GCM";
    Bundle extras;
    Intent intentMsgActivity;
    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
    	intentMsgActivity = new Intent(this,MensagensActivity.class);
        extras = intent.getExtras();       
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);        
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {              
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " +
                        extras.toString());
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {                
            	sendNotification("Received: " + extras.toString());             
            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

	private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);        
        Intent intent = new Intent(this,  MensagensActivity.class);
        Bundle params = new Bundle();        
        params.putString("msg", msg);        
        intent.putExtras(extras);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
        		intent,Intent.FLAG_ACTIVITY_NEW_TASK);                 
        
        String mensagem = extras.getString("mensagem");
        String assunto = extras.getString("assunto");
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
        .setSmallIcon(R.drawable.ic_launcher)
        .setContentTitle("Integração UFG")
        .setStyle(new NotificationCompat.BigTextStyle()        
        .bigText(mensagem))
        .setContentText(assunto);
        
        mBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        mBuilder.setAutoCancel(true);
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());               
    }

}

