package teli.com.kpcc.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nhaarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import teli.com.kpcc.R;
import teli.com.kpcc.Utils.AndroidUtils;
import teli.com.kpcc.Utils.CcRequestQueue;
import teli.com.kpcc.Utils.Constants;
import teli.com.kpcc.adapters.EventAdapter;
import teli.com.kpcc.fragments.MenuFragment;
import teli.com.kpcc.models.Constituency;
import teli.com.kpcc.models.Event;
import teli.com.kpcc.views.CcTextView;

/**
 * Created by naveen on 12/1/15.
 */
public class EventsActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    @InjectView(R.id.pBar)
    ProgressBar mPbar;

    @InjectView(R.id.events_list)
    ListView mEventsList;

    @InjectView(R.id.location_iv)
    ImageView mLocationIv;

    @InjectView(R.id.calendar_iv)
    ImageView mCalendarIv;

    @InjectView(R.id.filter_name)
    CcTextView mFilterName;

    @InjectView(R.id.fragmentofmenu)
    FrameLayout frameLayoutMenu;

    public android.app.FragmentManager fm;
    public FragmentTransaction ft;
    android.app.Fragment fragment;
    MenuItem menuItem;

    boolean pressed = false;
    private ArrayList<Event> events = new ArrayList<Event>();
    private EventAdapter adapter;
    private String months[] = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_events);

//        ActionBar actionBar = getActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setDisplayShowTitleEnabled(false);
        // setUpView();

        fetchEvents();


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
                intent=new Intent(EventsActivity.this,PoolActivity.class);
                startActivity(intent);
                return true;
            case R.id.feedsubmit:
                intent=new Intent(EventsActivity.this,UserFeedBackActivity.class);
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

    private void fetchEvents() {

        // String url = String.format(Constants.URL_EVENTS, AndroidUtils.getDeviceImei(this));
        String url = String.format(Constants.URL_EVENTS, AndroidUtils.getDeviceImei(this));
        Log.d("EventsFragment", "Url" + url);
        JsonArrayRequest request = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        Log.d("EventsFragment","Reasponse" + jsonArray.toString());

                        mPbar.setVisibility(View.GONE);
                        Gson gson = new Gson();

                        events = gson.fromJson(jsonArray.toString(), new TypeToken<List<Event>>() {
                        }.getType());

                        Log.d("EventsFragment","Reasponse" + events);

                        onDataReceived(events);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        mPbar.setVisibility(View.GONE);
                        Log.d("EventsFragment","Error" + volleyError.toString());
                       // Log.d("EventsFragment","Error" + volleyError.networkResponse.statusCode);
                    }
                });
        CcRequestQueue.getInstance(this).addToRequestQueue(request);

    }

    private void onDataReceived(ArrayList<Event> events) {
        if(events !=null && !events.isEmpty()){
            Log.d("EventsActivity","Filtered events" + events.size());
            adapter = new EventAdapter(EventsActivity.this,events);
            SwingBottomInAnimationAdapter animationAdapter = new
                    SwingBottomInAnimationAdapter(adapter);
            animationAdapter.setAbsListView(mEventsList);
            mEventsList.setAdapter(animationAdapter);
            mEventsList.setOnItemClickListener(EventsActivity.this);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Event event = (Event) adapterView.getItemAtPosition(i);
        Intent intent = new Intent(this,EventDescriptionActivity.class);
        intent.putExtra(EventDescriptionActivity.EVENT_DETAILS,event);
        startActivity(intent);
        finish();
    }
   /* private void setUpView() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        EventsFragment eventsFragment = new EventsFragment();
        transaction.replace(R.id.events_container,eventsFragment);
        transaction.commit();
    }*/

    @OnClick({R.id.calendar_iv,R.id.location_iv})
    public void onItemClicked(View view){

        switch (view.getId()){
            case R.id.calendar_iv:
                pressed = true;
                mCalendarIv.setImageResource(R.drawable.calender_icon_unsel);
                mLocationIv.setImageResource(R.drawable.event_loc_unsel);
                mFilterName.setVisibility(View.GONE);
                showCalender();
                break;

            case R.id.location_iv:
                pressed = true;
                mLocationIv.setImageResource(R.drawable.event_loc_sel);
                mCalendarIv.setImageResource(R.drawable.event_cal_unsel);
                fetchConstituency();
                break;
        }

    }

    private void showCalender() {
        Calendar calendar = Calendar.getInstance();
        int mMonth = calendar.get(Calendar.MONTH);
        int mYear = calendar.get(Calendar.YEAR);
        int mDay = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                //  mDob.setText("" + i + "-" + (i2 + 1) + "-" + i3);

                Log.d("EventsActivity","Date selected::" + i + "-" + (i2+1) + "-" + i3);
                String date = i + "-" + (i2 + 1) + "-" + i3;
                mFilterName.setVisibility(View.VISIBLE);
                mFilterName.setText(": "+i3+"th "+ months[i2]);
                filterCalendarEvents(date, i, i2 + 1, i3);
            }
        },mYear,mMonth,mDay);

        datePickerDialog.show();
    }

    private void filterCalendarEvents(String date1,int i1,int i2, int i3) {
        int month;
        int year;
        int day;

        Log.d("EventsActivity", "month" + i2);
        Log.d("EventsActivity", "year" + i1);
        Log.d("EventsActivity", "day" + i3);

        ArrayList<Event> calendarFilteredEvents = new ArrayList<Event>();
        for (Event event : events) {

            String date = event.getEventDate();
            Log.d("EventsActivity", "e date" + date);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date newdate = format.parse(date);
                Log.d("EventsActivity", "new date" + newdate);

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(newdate);

                month = calendar.get(Calendar.MONTH) + 1;
                day = calendar.get(Calendar.DATE);
                year = calendar.get(Calendar.YEAR);

                Log.d("EventsActivity", "e month" + month);
                Log.d("EventsActivity", " e year" + year);
                Log.d("EventsActivity", "e day" + day);

                if (year == i1 ){
                    if (month == i2){
                        if (day == i3){
                            Log.d("EventsActivity", "equal date" );
                            calendarFilteredEvents.add(event);
                        }
                    }
                }
            } catch (ParseException e) {
                Log.d("EventsActivity", "exception" + e);
                e.printStackTrace();
            }

           /* if (event.getEventDate().equals(date)) {
               *//* Log.d("EventsActivity", "events equal");
                calendarFilteredEvents.add(event);*//*
            }
            Log.d("EventsActivity","events date" + event.getEventDate());
            Log.d("EventsActivity","date" + date );*/
        }

        Log.d("EventsActivity","Filtered events" + calendarFilteredEvents);
        Log.d("EventsActivity","Filtered events size:::" + calendarFilteredEvents.size());

        if (calendarFilteredEvents.size() == 0){
            Toast.makeText(this,"No Events Found!",Toast.LENGTH_SHORT).show();
        }

        EventAdapter adapter1 = new EventAdapter(this,calendarFilteredEvents);
        adapter1.notifyDataSetChanged();
        mEventsList.setAdapter(adapter1);
    }

    private void showLocationList(List<Constituency> constituencies) {
        ArrayList<String> costituenciesName = new ArrayList<String>();



      /*  Collections.sort(costituenciesName, new Comparator<String>() {
            @Override
            public int compare(String text1, String text2) {
                return text1.compareTo(text2);
            }
        });*/

        Log.d("EventsActivity","Sorted order:::" +costituenciesName);
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.location_list_dialog);

        ListView locationList = (ListView) dialog.findViewById(R.id.location_list);

        //String constituencyString = CcDataManager.init(this).getConstituency();

       /* Type listType = new TypeToken<List<Constituency>>() {
        }.getType();
        List<Constituency> constituencies = (List<Constituency>) new Gson().fromJson(constituencyString, listType);*/

        if (constituencies.size() != 0) {
            for (Constituency constituency : constituencies) {
                costituenciesName.add(constituency.getConstituencyName());
            }
        }

        Collections.sort(costituenciesName);

        if (costituenciesName!= null && costituenciesName.size()!=0) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, costituenciesName);
            locationList.setAdapter(adapter);

           /* Window window = dialog.getWindow();
            window.setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);*/

            Window window = dialog.getWindow();
            window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, 900);
            //window.setGravity(B);

           /* WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            params.y = 200;
            dialog.getWindow().setAttributes(params);*/

            locationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String item = (String) adapterView.getItemAtPosition(i);
                   // Toast.makeText(EventsActivity.this, "you selected " + item, Toast.LENGTH_SHORT).show();

                    filterLocationEvents(item);
                    dialog.cancel();
                }
            });

            dialog.setCancelable(true);

            dialog.show();
        }
    }

    private void fetchConstituency() {

        JsonArrayRequest request = new JsonArrayRequest(Constants.URL_CONSTITUENCY,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        Log.d("SignUpActivity","Response::"+jsonArray.toString());

                        if (jsonArray!=null && jsonArray.length()!=0) {
                            String jsonOutput = jsonArray.toString();

                            Type listType = new TypeToken<List<Constituency>>() {
                            }.getType();
                            List<Constituency> constituencies = (List<Constituency>) new Gson().fromJson(jsonOutput, listType);
                            Log.d("SignUpActivity", "constituencies size::" + constituencies.size());

                            showLocationList(constituencies);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("SignUpActivity","Error::"+volleyError.toString());
                    }
                });
        CcRequestQueue.getInstance(this).addToRequestQueue(request);
    }

    private void filterLocationEvents(String item) {
        ArrayList<Event> filteredEvents = new ArrayList<Event>();
        for (Event event : events) {
            if (event.getEventLocation().equals(item)) {
                filteredEvents.add(event);
            }
        }

        Log.d("EventsActivity","Filtered events" + filteredEvents);
        Log.d("EventsActivity","Filtered events size:::" + filteredEvents.size());

        if (filteredEvents.size() == 0){
            Toast.makeText(this,"No Events Found!",Toast.LENGTH_SHORT).show();
        }

        EventAdapter adapter1 = new EventAdapter(this,filteredEvents);
        adapter1.notifyDataSetChanged();
        mEventsList.setAdapter(adapter1);
        mFilterName.setVisibility(View.VISIBLE);
        mFilterName.setText(": " +item);

    }

    @Override
    public void onBackPressed() {

        if (pressed) {
            if (events != null && !events.isEmpty()) {
                pressed = false;
                mCalendarIv.setImageResource(R.drawable.event_cal_unsel);
                mLocationIv.setImageResource(R.drawable.event_loc_unsel);
                mFilterName.setVisibility(View.GONE);
                onDataReceived(events);
            }
        }
        else {
             super.onBackPressed();
        }

    }

    @Override
    public void onClick(View view) {
        Intent intent=new Intent(EventsActivity.this,PoolActivity.class);
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
}
