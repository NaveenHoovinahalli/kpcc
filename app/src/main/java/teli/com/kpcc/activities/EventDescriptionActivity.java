package teli.com.kpcc.activities;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import butterknife.InjectView;
import butterknife.OnClick;
import teli.com.kpcc.R;
import teli.com.kpcc.Utils.AndroidUtils;
import teli.com.kpcc.Utils.CcRequestQueue;
import teli.com.kpcc.Utils.Constants;
import teli.com.kpcc.fragments.MenuFragment;
import teli.com.kpcc.models.Event;
import teli.com.kpcc.models.KpccCalendar;
import teli.com.kpcc.views.CcTextView;

/**
 * Created by naveen on 12/1/15.
 */
public class EventDescriptionActivity extends BaseActivity implements View.OnClickListener {

    @InjectView(R.id.event_head)
    CcTextView mEventHead;

    @InjectView(R.id.event_description)
    CcTextView mEventDescription;

    @InjectView(R.id.event_address)
    CcTextView mEventAddress;

    @InjectView(R.id.calendar_tv)
    CcTextView mCalendar;

    @InjectView(R.id.event_time_tv)
    CcTextView mTime;

    @InjectView(R.id.reminder_iv)
    ImageView mReminder;

    @InjectView(R.id.volunteer_iv)
    ImageView mVolunteer;

    @InjectView(R.id.fragmentofmenu)
    FrameLayout frameLayoutMenu;

    public android.app.FragmentManager fm;
    public FragmentTransaction ft;
    android.app.Fragment fragment;
    MenuItem menuItem;

    public static String EVENT_DETAILS = "event_details";
    private Event eventDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_event_description);

//        ActionBar actionBar = getActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setDisplayShowTitleEnabled(false);


        if (getIntent().hasExtra(EVENT_DETAILS)){
            eventDetails = getIntent().getParcelableExtra(EVENT_DETAILS);
            if (eventDetails!=null){
                initializeView(eventDetails);
            }
        }
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
                intent=new Intent(EventDescriptionActivity.this,PoolActivity.class);
                startActivity(intent);
                return true;
            case R.id.feedsubmit:
                intent=new Intent(EventDescriptionActivity.this,UserFeedBackActivity.class);
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

    private void initializeView(Event eventDetails) {

        if (eventDetails.getEventName()!=null && !eventDetails.getEventName().isEmpty()){
            mEventHead.setText(eventDetails.getEventName());
        }

        if (eventDetails.getEventLocation()!=null && !eventDetails.getEventLocation().isEmpty()){
            mEventAddress.setText(eventDetails.getEventLocation());
        }

        if (eventDetails.getEventDetails()!=null && !eventDetails.getEventDetails().isEmpty()){
            mEventDescription.setText(eventDetails.getEventDetails());
        }

        if (eventDetails.getEventDate()!=null && !eventDetails.getEventDate().isEmpty()){

            String eventDate = eventDetails.getEventDate();

            String[] splitted = eventDate.split("-");
            if (splitted!=null && splitted.length!=0) {
                mCalendar.setText(splitted[2] +"-"+ splitted[1]+ "-" + splitted[0]);
            }
        }

        if (eventDetails.getEventTime()!=null && ! eventDetails.getEventTime().isEmpty()) {
            Log.d("EventDescriptionActivity","Event time" + eventDetails.getEventTime());
            String time = eventDetails.getEventTime();
            DateFormat f1 = new SimpleDateFormat("hh:mm:ss");
            Date d = null;
            try {
                d = f1.parse(time);
                DateFormat f2 = new SimpleDateFormat("h:mma");
                f2.format(d).toLowerCase();
                Log.d("EventDescriptionActivity","Format time" +  f2.format(d).toUpperCase());
                mTime.setText(f2.format(d).toUpperCase());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        Log.d("Reminder flag","Event"+eventDetails.getReminderFlag());
        Log.d("Reminder flag","Volunteer"+eventDetails.getVolunteerFlag());

        if(eventDetails.getReminderFlag()!= null) {

            if (eventDetails.getReminderFlag().equals("0") || eventDetails.getReminderFlag() == null) {
                mReminder.setImageResource(R.drawable.setreminder_sel);
            } else if (eventDetails.getReminderFlag().equals("1")) {
                mReminder.setImageResource(R.drawable.setreminder_unsel);
                mReminder.setClickable(false);
            }

            if (eventDetails.getVolunteerFlag().equals("0") || eventDetails.getReminderFlag() == null) {
                mVolunteer.setImageResource(R.drawable.volunter_sel);
            } else if (eventDetails.getVolunteerFlag().equals("1")) {
                mVolunteer.setImageResource(R.drawable.volunter_unsel);
                mVolunteer.setClickable(false);
            }
        }
    }

    @OnClick({R.id.reminder_iv, R.id.volunteer_iv})
    public void onItemClicked(View view){
        switch (view.getId()){
            case R.id.volunteer_iv : showAlert("Are you sure you want to volunteer?", "volunteer");
                break;
            case R.id.reminder_iv : showAlert("Are you sure you want to set Reminder?", "reminder");
                break;
        }
    }

    private void showAlert(String s, final String method) {
        AlertDialog.Builder builder = new AlertDialog.Builder(EventDescriptionActivity.this);

        builder.setMessage(s);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Log.d("EventsDescription","Volunteer:" );

                if (method.equals("volunteer")){
                    setToServer();
                }if (method.equals("reminder")){
                    setReminder();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Log.d("EventsDescription", "Volunteer cancelled:");
            }
        });
        builder.show();
    }

    private void setReminder() {
        String url = String.format(Constants.URL_REMINDER,AndroidUtils.getDeviceImei(this),eventDetails.getId());
        StringRequest request = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.d("EventDescription","Reminder success" + s);

                        if (s.contains("success")) {
                            mReminder.setImageResource(R.drawable.setreminder_unsel);
                            mReminder.setClickable(false);
                            Toast.makeText(EventDescriptionActivity.this , "Success",Toast.LENGTH_SHORT).show();
                            setNativeCalendar();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("EventDescription","Reminder Error" + volleyError);
                    }
                });
        CcRequestQueue.getInstance(this).addToRequestQueue(request);
    }

    private void setNativeCalendar() {
        KpccCalendar styleCalender = new KpccCalendar(this);
        try {

            HashMap<Integer, ArrayList<Long>> map = new HashMap<Integer, ArrayList<Long>>();
            Long id = Long.parseLong(eventDetails.getId());

            Long eventId = styleCalender.addAllDayEvent(eventDetails);

        } catch (ParseException e) {
            Log.d("calendar_test", e.getMessage());
            e.printStackTrace();
        }
    }

    private void setToServer() {
        String url = String.format(Constants.URL_VOLUNTEER, AndroidUtils.getDeviceImei(this),eventDetails.getId());
        Log.d("EventDescription","URL" + url);
        StringRequest request = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                    Log.d("EventDescription","Volunteer success" + s);

                    if (s.contains("success")) {
                        mVolunteer.setImageResource(R.drawable.volunter_unsel);
                        mVolunteer.setClickable(false);
                        //eventDetails.setReminderFlag("1");
                        Toast.makeText(EventDescriptionActivity.this , "Success",Toast.LENGTH_SHORT).show();
                    }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("EventDescription","Volunteer Error" + volleyError);
                    }
                });
        CcRequestQueue.getInstance(this).addToRequestQueue(request);
    }

    @Override
    public void onClick(View view) {
        Intent intent=new Intent(EventDescriptionActivity.this,PoolActivity.class);
        startActivity(intent);

    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (frameLayoutMenu.getVisibility() == View.VISIBLE) {
            frameLayoutMenu.setVisibility(View.INVISIBLE);
            menuItem.setIcon(R.drawable.menu_settings);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(this,EventsActivity.class);
        startActivity(intent);
        finish();

    }
}
