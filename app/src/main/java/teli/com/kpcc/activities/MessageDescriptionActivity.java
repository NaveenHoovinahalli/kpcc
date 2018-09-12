package teli.com.kpcc.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.InjectView;
import butterknife.OnClick;
import teli.com.kpcc.R;
import teli.com.kpcc.fragments.MenuFragment;
import teli.com.kpcc.models.Message;
import teli.com.kpcc.views.CcTextView;

/**
 * Created by naveen on 15/1/15.
 */
public class MessageDescriptionActivity extends BaseActivity implements View.OnClickListener {

    public static String MESSAGE_DETAILS = "message_details";

    @InjectView(R.id.message_head)
    CcTextView mMessageHead;

    @InjectView(R.id.message_description)
    CcTextView mMessageDescription;

    @InjectView(R.id.pictures)
    Button mPictures;

    @InjectView(R.id.videos)
    Button mVideos;

    @InjectView(R.id.fragmentofmenu)
    FrameLayout frameLayoutMenu;


    public android.app.FragmentManager fm;
    public android.app.FragmentTransaction ft;
    android.app.Fragment fragment;
    MenuItem menuItem;
    private Message messageDetails;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_message_description);

        if (getIntent().hasExtra(MESSAGE_DETAILS)){
            messageDetails = getIntent().getParcelableExtra(MESSAGE_DETAILS);
            if (messageDetails!=null){
                initViews();
            }
        }

//        actionBar = getActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setDisplayShowTitleEnabled(false);

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
                intent=new Intent(MessageDescriptionActivity.this,PoolActivity.class);
                startActivity(intent);
                return true;
            case R.id.feedsubmit:
                intent=new Intent(MessageDescriptionActivity.this,UserFeedBackActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_settings:
                menuItem=item;
                Log.d("Menu", "onSettingClicked");
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

    private void initViews() {
        if (messageDetails.getMessageHead()!= null && !messageDetails.getMessageHead().isEmpty()){
            mMessageHead.setText(messageDetails.getMessageHead());
        }

        if (messageDetails.getMessageDescription()!=null && !messageDetails.getMessageDescription().isEmpty()){
            mMessageDescription.setText(messageDetails.getMessageDescription());
        }

       int videosCount= messageDetails.getInnerVideos().size();
        int imageCount=messageDetails.getInnerImages().size();

        if(imageCount==0) {
            mPictures.setBackground(getResources().getDrawable(R.drawable.gray_round_button));
            mPictures.setEnabled(false);
        } if(videosCount==0){
            mVideos.setBackground(getResources().getDrawable(R.drawable.gray_round_button));
            mVideos.setEnabled(false);
        }
    }

    @OnClick({R.id.pictures,R.id.videos})
    public void OnItemClicked(View view){
        switch (view.getId()){
            case R.id.pictures :
                showPictures();
                break;

            case R.id.videos : showVideos();
                break;
        }
    }

    private void showVideos() {
        if (messageDetails.getInnerVideos()!=null && messageDetails.getInnerVideos().size()!=0){
            Intent intent = new Intent(MessageDescriptionActivity.this,InnerVideoActivity.class);
            intent.putExtra(InnerVideoActivity.MESSAGE_VIDEOS, messageDetails);
            startActivity(intent);
        }else {
            Toast.makeText(this,"No Videos Found!",Toast.LENGTH_SHORT).show();
        }
    }

    private void showPictures() {
        if (messageDetails.getInnerImages()!=null && messageDetails.getInnerImages().size()!=0){
            Intent intent = new Intent(MessageDescriptionActivity.this,InnerImageActivity.class);
            intent.putExtra(InnerImageActivity.MESSAGE_PICTURES, messageDetails);
            startActivity(intent);
        }else {
            Toast.makeText(this,"No Pictures Found!",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent=new Intent(MessageDescriptionActivity.this,PoolActivity.class);
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
