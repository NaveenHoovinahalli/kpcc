package teli.com.kpcc.activities;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import teli.com.kpcc.R;
import teli.com.kpcc.fragments.MenuFragment;

/**
 * Created by naveen on 9/1/15.
 */
public class MessageActivity extends TabActivity implements View.OnClickListener {

    private ActionBar actionBar;

    @InjectView(R.id.fragmentofmenu)
    FrameLayout frameLayoutMenu;

    public android.app.FragmentManager fm;
    public FragmentTransaction ft;
    android.app.Fragment fragment;
    MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_message);

        ButterKnife.inject(this);
       final TabHost tabHost=getTabHost();

        TabHost.TabSpec spec;
        Intent intent;


        intent = new Intent().setClass(this,PartyActivity.class);
        intent.putExtra(PartyActivity.MESSAGE_TYPE,"party_messages");
        spec = tabHost.newTabSpec("tab1")
                .setIndicator("PARTY")
                .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this,PartyActivity.class);
        intent.putExtra(PartyActivity.MESSAGE_TYPE,"feedback_messages");
        spec = tabHost.newTabSpec("tab2")
                .setIndicator("MESSAGE")
                .setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTab(0);
        setTabColor(tabHost);

        tabHost.getTabWidget().setShowDividers(TabWidget.SHOW_DIVIDER_MIDDLE);
        tabHost.getTabWidget().setDividerDrawable(R.drawable.divider);

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                setTabColor(tabHost);
            }
        });
    }

    public void setTabColor(TabHost tabhost) {

        for(int i=0;i<tabhost.getTabWidget().getChildCount();i++)
            tabhost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#39B54A"));

            tabhost.getTabWidget().getChildAt(tabhost.getCurrentTab()).setBackgroundColor(Color.parseColor("#F7941E"));

        for(int i=0;i<tabhost.getTabWidget().getChildCount();i++)
        {
            TextView tv = (TextView) tabhost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(Color.parseColor("#FFFFFF"));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SharedPreferences prefs = getSharedPreferences("SharedPollCount",MODE_PRIVATE);
        String pollCount= prefs.getString("POLL_COUNT", "0");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_action_bar, menu);

        Log.d("POLL_COUNT","INSIDE MESSAGE"+pollCount);

        View count=menu.findItem(R.id.poll_notification).getActionView();
        TextView countText= (TextView) count.findViewById(R.id.tvCount);
        ImageView pollImage=(ImageView) count.findViewById(R.id.notification_main);
        pollImage.setOnClickListener(this);
        countText.setOnClickListener(this);
        LinearLayout ll=(LinearLayout) count.findViewById(R.id.llpolltv);
        if(pollCount.equals("0") || pollCount.isEmpty())
        {
            ll.setVisibility(View.INVISIBLE);
        }else {
            ll.setVisibility(View.VISIBLE);
            countText.setText(pollCount);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("OnItemSelected", "on Item");

        switch (item.getItemId()){
            case R.id.poll_notification:
                Intent intent=new Intent(MessageActivity.this,PoolActivity.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.feedsubmit:
                intent=new Intent(MessageActivity.this,UserFeedBackActivity.class);
                startActivity(intent);
                finish();
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
    public void onClick(View view) {

        Intent intent=new Intent(MessageActivity.this,PoolActivity.class);
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
        if(frameLayoutMenu.getVisibility()==View.VISIBLE) {
            frameLayoutMenu.setVisibility(View.INVISIBLE);
            menuItem.setIcon(R.drawable.menu_settings);
        }
    }
}
