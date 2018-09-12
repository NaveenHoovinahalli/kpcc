package teli.com.kpcc.activities;

import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import teli.com.kpcc.R;
import teli.com.kpcc.Utils.CcDataManager;
import teli.com.kpcc.Utils.CcRequestQueue;
import teli.com.kpcc.Utils.Constants;
import teli.com.kpcc.broadcastService.BirthdayNotification;
import teli.com.kpcc.fragments.MenuFragment;
import teli.com.kpcc.models.MessageNotofication;
import teli.com.kpcc.models.Quote;
import teli.com.kpcc.models.User;
import teli.com.kpcc.views.CcTextView;

/**
 * Created by naveen on 2/1/15.
 */
public class HomeActivity extends BaseActivity implements View.OnClickListener {

    @InjectView(R.id.quote_text)
    CcTextView mQuote;

    @InjectView(R.id.quoted_by)
    CcTextView mQuotedBy;


    @InjectView(R.id.fragmentofmenu)
    FrameLayout frameLayoutMenu;

    SharedPreferences sharedPreferences;
    BirthdayNotification birthdayNotification;


    private Quote quote;
    private ActionBar actionBar;
    private int poll_count;
    public String[] classNames;
    public android.app.FragmentManager fm;
    public FragmentTransaction ft;
    android.app.Fragment fragment;
    MenuItem menuItem;
    ArrayList<MessageNotofication> messageNotification;
    BroadcastReceiver broadcastReceiver;
    private AlarmManager alarmManager;
    private static long back_pressed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.inject(this);
        //Saving new polls

//        actionBar = getActionBar();
//        actionBar.setDisplayShowTitleEnabled(false);
//        actionBar.setDisplayHomeAsUpEnabled(false);
        fetchQuote();

       fetchNotificationMessage();

    }

    private void fetchNotificationMessage() {

        String url=String.format(Constants.URL_NOTIFICATION_MESSAGE);

        JsonArrayRequest videoRequest=new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        parceMessageJson(jsonArray);
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        CcRequestQueue.getInstance(this).addToRequestQueue(videoRequest);

    }

    private void parceMessageJson(JSONArray jsonArray) {
        Gson gson=new Gson();

        messageNotification = gson.fromJson(jsonArray.toString(), new TypeToken<List<MessageNotofication>>() {
        }.getType());

        String referMessage = "",birthdayMessage="";

        for(int i=0;i<messageNotification.size();i++) {
            Log.d("Message Notification", "Message " + messageNotification.get(i).getMessageText());
            Log.d("Message Notification", "Type" + messageNotification.get(i).getMessageText());

            if (messageNotification.get(i).getMessageType().equals("Refer to friend message")) {
                referMessage = messageNotification.get(i).getMessageText();
            } else if (messageNotification.get(i).getMessageType().equals("Birthday wish message")) {
                 birthdayMessage = messageNotification.get(i).getMessageText();

            }
        }


            SharedPreferences prefs = getSharedPreferences("SHARE",MODE_PRIVATE);
            prefs.edit().putString("SHAREMESSAGE", referMessage).apply();

           User user= CcDataManager.init(this).getAppUser();
            user.getDob();
            user.getName();
//            Log.d("Message Notification","Name"+user.getName()+""+user.getDob());


            SharedPreferences prefss = getSharedPreferences("BIRTHDAY",MODE_PRIVATE);
            String isBirthdaySet= prefss.getString("BIRTHDAY","NO");
           if(isBirthdaySet.equals("NO")) {
               setAlarm(user.getName(), birthdayMessage, user.getDob());
           }

    }

    private void setAlarm(String name,String message, String dob) {

        birthdayNotification=new BirthdayNotification();
        birthdayNotification.SetBroadcast(this,name,message,dob);
        sharedPreferences=getSharedPreferences("BIRTHDAY", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("BIRTHDAY", "SET").apply();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar

        SharedPreferences prefs = getSharedPreferences("SharedPollCount",MODE_PRIVATE);
        String pollCount= prefs.getString("POLL_COUNT","0");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_action_bar, menu);
        Log.d("POLL_COUNT","countin home "+pollCount);

        View count=menu.findItem(R.id.poll_notification).getActionView();
         TextView countText= (TextView) count.findViewById(R.id.tvCount);
        ImageView pollImage=(ImageView) count.findViewById(R.id.notification_main);
        pollImage.setOnClickListener(this);
        countText.setOnClickListener(this);
        LinearLayout ll=(LinearLayout) count.findViewById(R.id.llpolltv);
        if(pollCount.equals("0") || pollCount.isEmpty() )
        {
            ll.setVisibility(View.INVISIBLE);
        }else {
            ll.setVisibility(View.VISIBLE);

            countText.setText(pollCount);
        }
         return true;
       // return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.d("Menu","onOptionsMenuClick");

        Intent intent;
        switch (item.getItemId()){
            case R.id.poll_notification:
                 intent=new Intent(HomeActivity.this,PoolActivity.class);
                startActivity(intent);
                return true;
            case R.id.feedsubmit:
               intent=new Intent(HomeActivity.this,UserFeedBackActivity.class);
//               intent=new Intent(HomeActivity.this,UserfeedbackNewActivity.class);
                 startActivity(intent);
                return true;
            case R.id.action_settings:
                menuItem=item;
                Log.d("Menu","onSettingClicked");
                if(frameLayoutMenu.getVisibility() == View.VISIBLE){
                 frameLayoutMenu.setVisibility(View.INVISIBLE);
                    item.setIcon(R.drawable.menu_settings);
                }else {
                    item.setIcon(R.drawable.right_arrow_white);
                    frameLayoutMenu.setVisibility(View.VISIBLE);
                    fm = getFragmentManager();
                    ft = fm.beginTransaction();
                    fragment = new MenuFragment();
                    ft.replace(R.id.fragmentofmenu, fragment);
                    ft.commit();

                }
                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onBackPressed() {

        if(frameLayoutMenu.getVisibility()==View.VISIBLE) {
            frameLayoutMenu.setVisibility(View.INVISIBLE);
        }

        if (back_pressed + 2000 > System.currentTimeMillis()) super.onBackPressed();
        else Toast.makeText(getBaseContext(), "Press once again to exit!", Toast.LENGTH_SHORT).show();
        back_pressed = System.currentTimeMillis();


    }



    private void fetchQuote() {
        JsonObjectRequest request = new JsonObjectRequest(
                Constants.URL_QUOTE,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d(Constants.KPCC_LOG,"Response::" + jsonObject.toString());
                        displayQuote(jsonObject.toString());
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d(Constants.KPCC_LOG,"Error::" + volleyError.toString());
            }
        });
        CcRequestQueue.getInstance(this).addToRequestQueue(request);
    }

    private void displayQuote(String s) {
        quote = new Gson().fromJson(s,Quote.class);
        mQuote.setText(quote.getQoute().replaceAll("â", ""));
        mQuotedBy.setText("- " +quote.getQuotedBy());
    }

    @OnClick(R.id.quote_layout)
    public void onQuoteLayoutClicked(){

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_quote);

        if (quote!=null) {
            TextView text = (TextView) dialog.findViewById(R.id.full_quote);
            text.setText(quote.getQoute().replaceAll("â", ""));

            TextView text1 = (TextView) dialog.findViewById(R.id.full_quoted_by);
            text1.setText("- " + quote.getQuotedBy());
        }

        /*Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });*/

        dialog.setCancelable(true);

        dialog.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(frameLayoutMenu.getVisibility()==View.VISIBLE) {
            frameLayoutMenu.setVisibility(View.INVISIBLE);
            menuItem.setIcon(R.drawable.menu_settings);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);

    }

    @Override
    public void onClick(View view) {
        Intent intent=new Intent(HomeActivity.this,PoolActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }


}
