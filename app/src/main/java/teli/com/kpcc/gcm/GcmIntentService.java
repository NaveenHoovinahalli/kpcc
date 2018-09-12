package teli.com.kpcc.gcm;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import teli.com.kpcc.R;
import teli.com.kpcc.activities.HomeActivity;

/**
 * Created by naveen on 15/5/14.
 */
public class GcmIntentService extends IntentService {

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);
        Log.d("GCMIntentService", "GCMIntentService MessageType::" + messageType);
        if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                showNotification(intent);
            }
        }
    }



    private void showNotification(Intent intent) {

        Bundle bundle = intent.getExtras();
        String data = bundle.getString("data");

        Log.d("push_test", "data::" + data);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.appicon)
                        .setContentTitle("KPCC")
                        .setContentText(data);

        Intent homeIntent = new Intent(this, HomeActivity.class);
        PendingIntent i = PendingIntent.getActivity(this, 1, homeIntent, 0);
        mBuilder.setContentIntent(i);
        mBuilder.setAutoCancel(true);
        Notification note = mBuilder.build();
        note.defaults |= Notification.DEFAULT_VIBRATE;
        note.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, note);

    }
}
