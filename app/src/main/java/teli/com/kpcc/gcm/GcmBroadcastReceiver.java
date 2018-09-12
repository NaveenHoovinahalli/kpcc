package teli.com.kpcc.gcm;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by naveen on 15/5/14.
 */
public class GcmBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("GCMBroadcastReceiver", "GCMBroadcastReceiver1::" + intent);
        Log.d("GCMBroadcastReceiver", "GCMBroadcastReceiver2::" + context);
        ComponentName comp = new ComponentName(context.getPackageName(),
                GcmIntentService.class.getName());
        context.startService(intent.setComponent(comp));
        setResultCode(Activity.RESULT_OK);
    }
}
