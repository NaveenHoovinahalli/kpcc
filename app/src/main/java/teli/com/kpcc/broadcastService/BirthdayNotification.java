package teli.com.kpcc.broadcastService;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import teli.com.kpcc.R;
import teli.com.kpcc.activities.HomeActivity;

/**
 * Created by naveen on 20/1/15.
 */
public class BirthdayNotification extends BroadcastReceiver {

    Context mycontext;
    String myname="";
    String mymessage="";
    @Override
    public void onReceive(Context context, Intent intent) {
        this.mycontext=context;
        Log.d("BIRTHDAY","Birthday");
        Toast.makeText(context,"HAPPY BIRTHDAY",Toast.LENGTH_SHORT).show();
        PowerManager powerManager=(PowerManager) context.getSystemService(context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock=powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"KPCC");
        myname= intent.getStringExtra("NAME");
        mymessage=intent.getStringExtra("MESSAGE");
        wakeLock.acquire();

        CallNotification(myname,mymessage);

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void CallNotification(String name, String message) {

        String newMessage=message.replace("$USER",name);

        String outputMessage = newMessage.substring(0, 1).toUpperCase() + newMessage.substring(1);

        NotificationCompat.Builder builder=new NotificationCompat.Builder(mycontext);
        builder.setSmallIcon(R.drawable.appicon);
        builder.setContentTitle("Happy Birthday ");
        builder.setContentText(outputMessage);
        builder.setTicker("KPCC wishes you");
        builder.setNumber(1);

        Intent notification=new Intent(mycontext, HomeActivity.class);

        TaskStackBuilder stackBuilder= TaskStackBuilder.create(mycontext);
        stackBuilder.addParentStack(HomeActivity.class);
        stackBuilder.addNextIntent(notification);

        NotificationManager notificationManager= (NotificationManager) mycontext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,builder.build());

        setNewAlarm();
    }

    private void setNewAlarm() {

        Calendar cal = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());

        Calendar currentCalender=Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());

        cal.set(Calendar.YEAR,currentCalender.get(Calendar.YEAR)+1);
        cal.set(Calendar.MONTH, currentCalender.get(Calendar.MONTH));
        cal.set(Calendar.DATE,currentCalender.get(Calendar.DATE));
        cal.set(Calendar.HOUR_OF_DAY,8);
        cal.set(Calendar.MINUTE,1);
        cal.set(Calendar.SECOND,1);


        AlarmManager alarmManager = (AlarmManager) mycontext.getSystemService(Context.ALARM_SERVICE);
        Intent intent=new Intent(mycontext,BirthdayNotification.class);
        intent.putExtra("NAME",myname);
        intent.putExtra("MESSAGE",mymessage);
        intent.setAction("BIRTHDAY");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mycontext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis(),pendingIntent);
    }

    public void SetBroadcast(Context context, String name, String message, String dob){
         Calendar cal = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());

         Calendar currentCalender=Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());



         this.mycontext=context;
         this.myname=name;
         this.mymessage=message;

        Log.d("BIRTHDAY","" +dob);

        String date=dob;
         String temp[];
         temp=date.split("-");
         for(int i=0;i<temp.length;i++) {
             Log.d("BIRTHDAY", temp[i]);
         }

        int mMonth = currentCalender.get(Calendar.MONTH)+1;
        int mDay = currentCalender.get(Calendar.DATE);

        Log.d("BIRTHDAY","month"+mMonth +"is"+Integer.parseInt(temp[1]));
        Log.d("BIRTHDAY","day"+mDay +"is"+Integer.parseInt(temp[2]));



        if(Integer.parseInt(temp[1])>=mMonth){
            if(Integer.parseInt(temp[2])>=mDay){
                cal.set(Calendar.YEAR, currentCalender.get(Calendar.YEAR));

            }
            else
                cal.set(Calendar.YEAR, currentCalender.get(Calendar.YEAR)+1);

        }else
            cal.set(Calendar.YEAR, currentCalender.get(Calendar.YEAR)+1);

         cal.set(Calendar.MONTH, Integer.parseInt(temp[1])-1);
         cal.set(Calendar.DATE,Integer.parseInt(temp[2]));
         cal.set(Calendar.HOUR_OF_DAY,8);
         cal.set(Calendar.MINUTE,1);
         cal.set(Calendar.SECOND,1);




         Log.d("BIRTHDAY","inside method");
         AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent=new Intent(context,BirthdayNotification.class);
        intent.putExtra("NAME",name);
         intent.putExtra("MESSAGE",message);
        intent.setAction("BIRTHDAY");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis(),pendingIntent);
    }

}
