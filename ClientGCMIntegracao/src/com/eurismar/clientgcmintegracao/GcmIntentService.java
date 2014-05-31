package com.eurismar.clientgcmintegracao;
//package com.example.gcmexample;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * This {@code IntentService} does the actual handling of the GCM message.
 * {@code GcmBroadcastReceiver} (a {@code WakefulBroadcastReceiver}) holds a
 * partial wake lock for this service while the service does its work. When the
 * service is finished, it calls {@code completeWakefulIntent()} to release the
 * wake lock.
 */
public class GcmIntentService extends IntentService {
	String mes;
    private Handler handler;
	public GcmIntentService() {
		super("GcmIntentService");
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		handler = new Handler();
	}
	@Override
	protected void onHandleIntent(Intent intent) {
       Bundle extras = intent.getExtras();
       
       GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
       // The getMessageType() intent parameter must be the intent you received
       // in your BroadcastReceiver.
       String messageType = gcm.getMessageType(intent);

      mes = extras.getString("title");
      showToast();
      Log.i("GCM", "Received : (" +messageType+")  "+extras.getString("title"));
      
             
       
       GcmBroadcastReceiver.completeWakefulIntent(intent);

	}

	public void showToast(){
		handler.post(new Runnable() {
		    public void run() {
		        Toast.makeText(getApplicationContext(),mes , Toast.LENGTH_LONG).show();
		    }
		 });

	}

}

