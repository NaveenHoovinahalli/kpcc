package teli.com.kpcc.activities;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import teli.com.kpcc.R;
import teli.com.kpcc.Utils.Constants;
import teli.com.kpcc.fragments.MenuFragment;
import teli.com.kpcc.models.NationalLeaderFeed;
import teli.com.kpcc.views.CcTextView;

/**
 * Created by naveen on 10/1/15.
 */
public class NationalLeaderDetails extends BaseActivity implements View.OnClickListener {

    @InjectView(R.id.cctv_national_header)
    CcTextView tvLeader;

    @InjectView(R.id.nl_image_main)
    ImageView nlMainImage;

    @InjectView(R.id.nl_name_only)
    TextView nlName;

    @InjectView(R.id.nl_name_desgination)
    TextView nlNameDesignation;


    @InjectView(R.id.nl_detail_text)
    TextView nlDescription;

    @InjectView(R.id.nl_images)
    Button nlImages;

    @InjectView(R.id.nl_videos)
     Button nlVideos;

    @InjectView(R.id.nl_twitterlink)
    ImageButton nlTwitter;

    @InjectView(R.id.nl_facebooklink)
    ImageButton nlFacebookLink;

    @InjectView(R.id.nl_twitterlink)
    ImageButton nlTwitterLink;

    @InjectView(R.id.nl_websiteLink)
    ImageButton nlWebsiteLink;

    ActionBar actionBar;
    @InjectView(R.id.fragmentofmenu)
    FrameLayout frameLayoutMenu;



    public android.app.FragmentManager fm;
    public FragmentTransaction ft;
    android.app.Fragment fragment;


    NationalLeaderFeed nationalLeaderFeed;
    private MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nationalleader_detail);

        ButterKnife.inject(this);

//        actionBar = getActionBar();
//        if (actionBar!=null) {
//            actionBar.setDisplayShowTitleEnabled(false);
//            actionBar.setDisplayHomeAsUpEnabled(false);
//            actionBar.setHomeButtonEnabled(true);
//            actionBar.setLogo(R.drawable.home);
//        }

        if(getIntent().hasExtra("NLFEEDOBJECT")){
            nationalLeaderFeed=getIntent().getParcelableExtra("NLFEEDOBJECT");
            setLayoutValues();
        }
      }

    private void setLayoutValues() {

        Log.d("Leader type",""+ nationalLeaderFeed.getLeaderType());

        tvLeader.setText(nationalLeaderFeed.getLeaderType().toString());

        //Top Image
        Picasso.with(this).load(Constants.BASE_URL+nationalLeaderFeed.getLeaderHeaderImage())
                .placeholder(R.drawable.appicon).into(nlMainImage);

        nlName.setText(nationalLeaderFeed.getLeaderName());

        //LeaderName
        nlNameDesignation.setText(nationalLeaderFeed.getDesignation());

        //Description
        nlDescription.setText(nationalLeaderFeed.getDescription());

        //FacebookLink
        int imagecount=nationalLeaderFeed.getImages().size();
        int videoCount=nationalLeaderFeed.getVideos().size();
        Log.d("NationalLeaderDetails","Image "+imagecount+""+"Video"+""+videoCount);
        if(imagecount==0) {
//           nlImages.setVisibility(View.INVISIBLE);
//            nlImages.setBackgroundColor(Color.parseColor("#D3D3D3"));
            nlImages.setEnabled(false);
            nlImages.setBackground(getResources().getDrawable(R.drawable.gray_round_button));

        } if(videoCount==0){
//            nlVideos.setBackgroundColor(Color.parseColor("#D3D3D3"));
            nlVideos.setEnabled(false);
            nlVideos.setBackground(getResources().getDrawable(R.drawable.gray_round_button));
        }




    }

    public void websiteLink(View view){

        String url=nationalLeaderFeed.getWebsiteLink();
        if(url == null || url.isEmpty()){

            Toast.makeText(this,"No facebook link available",Toast.LENGTH_SHORT).show();


        }else {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }

    }

    public void imagesClicked(View view){

    if(nationalLeaderFeed.getImages().size()!=0){
        Intent intent=new Intent(this,InnerImageActivity.class);
        intent.putExtra(InnerImageActivity.LEADER_IMAGES, (android.os.Parcelable) nationalLeaderFeed);
        Log.d("NL Details ", "Images" + nationalLeaderFeed.getImages().size());
        startActivity(intent);
    }else
        Toast.makeText(this,"No Images",Toast.LENGTH_SHORT).show();
    }

    public void videoClicked(View view){

       if( nationalLeaderFeed.getVideos().size()!=0) {
           Intent intent = new Intent(this, InnerVideoActivity.class);
           intent.putExtra(InnerVideoActivity.LEADER_VIDEOS, (android.os.Parcelable) nationalLeaderFeed);
           startActivity(intent);
       }else
           Toast.makeText(this,"No Videos",Toast.LENGTH_SHORT).show();
    }

    public void facebookLink(View view){

        String url=nationalLeaderFeed.getFacebookLink();
        if(url == null || url.isEmpty()){

            Toast.makeText(this,"Link not available",Toast.LENGTH_SHORT).show();


        }else {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }

    }

    public void twitterLink(View view){
        String url=nationalLeaderFeed.getTwitterLink();
        if(url == null || url.isEmpty()){
            Toast.makeText(this,"Link not available",Toast.LENGTH_SHORT).show();


        }else {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }

    }

    public void youtubeLink(View view){

        String url=nationalLeaderFeed.getYoutubeLink();
        if(url == null || url.isEmpty()){

            Toast.makeText(this,"Link not available",Toast.LENGTH_SHORT).show();


        }else {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
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
                intent=new Intent(NationalLeaderDetails.this,PoolActivity.class);
                startActivity(intent);
                return true;
            case R.id.feedsubmit:
                intent=new Intent(NationalLeaderDetails.this,UserFeedBackActivity.class);
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
    public void onClick(View view) {
        Intent intent=new Intent(NationalLeaderDetails.this,PoolActivity.class);
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
