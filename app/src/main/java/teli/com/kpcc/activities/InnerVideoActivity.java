package teli.com.kpcc.activities;

import android.app.ActionBar;
import android.app.DownloadManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import teli.com.kpcc.R;
import teli.com.kpcc.adapters.InnerVideoAdapter;
import teli.com.kpcc.fragments.MenuFragment;
import teli.com.kpcc.fragments.Videofragment;
import teli.com.kpcc.models.InnerVideos;
import teli.com.kpcc.models.Message;
import teli.com.kpcc.models.NationalLeaderFeed;
import teli.com.kpcc.models.OuterVideo;
import teli.com.kpcc.views.CcTextView;

/**
 * Created by naveen on 9/1/15.
 */
public class InnerVideoActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    public static final String OUTER_VIDEO_VALUE ="outer_video";
    public static final String LEADER_VIDEOS= "leaders_video";
    public static final String MESSAGE_VIDEOS= "message_videos";

    public DownloadManager downloadManager;
    public long myDownloadReference ;
    public BroadcastReceiver rceiverDownloadComplete;
    public BroadcastReceiver receiverDownloadClicked;
    public String videoUrl;
    public String videoId;
    public Boolean isDownloadig=false;

    @InjectView(R.id.fragmentofmenu)
    FrameLayout frameLayoutMenu;
    public android.app.FragmentManager fm2;
    public FragmentTransaction ft2;
    android.app.Fragment fragmenttwo;
    MenuItem menuItem;


    OuterVideo outerVideo;
    NationalLeaderFeed nationalLeaderFeed;
    ArrayList<InnerVideos> innerVideos;
    ActionBar actionBar;

    @InjectView(R.id.grid_videos_container)
    GridView mGridVideos;

    @InjectView(R.id.progress_bar)
    ProgressBar mPbar;
    private Message message;
    Intent intent;

    @InjectView(R.id.cctvVideo)
    CcTextView headerText;

    @InjectView(R.id.framelayoutvideo)
    FrameLayout frameVideo;

    public android.app.FragmentManager fm;
    public FragmentTransaction ft;
    android.app.Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_list);

        ButterKnife.inject(this);


//        actionBar = getActionBar();
//        if (actionBar!=null) {
//            actionBar.setDisplayShowTitleEnabled(false);
//            actionBar.setDisplayHomeAsUpEnabled(false);
//            actionBar.setHomeButtonEnabled(true);
//            actionBar.setLogo(R.drawable.home);
//        }

        downloadManager= (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        if(getIntent().hasExtra(OUTER_VIDEO_VALUE)){
            outerVideo= getIntent().getParcelableExtra(OUTER_VIDEO_VALUE);
           innerVideos= outerVideo.getVideos();


        }else if(getIntent().hasExtra(LEADER_VIDEOS)){
            nationalLeaderFeed=getIntent().getParcelableExtra(LEADER_VIDEOS);
             innerVideos= nationalLeaderFeed.getVideos();

        }else if (getIntent().hasExtra(MESSAGE_VIDEOS)){
          message =  getIntent().getParcelableExtra(MESSAGE_VIDEOS);
            innerVideos = message.getInnerVideos();
            headerText.setText("FEEDBACK VIDEO");
        }

        mPbar.setVisibility(View.INVISIBLE);

        InnerVideoAdapter adapter=new InnerVideoAdapter(this,innerVideos);
       mGridVideos.setAdapter(adapter);
        mGridVideos.setOnItemClickListener(InnerVideoActivity.this);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        InnerVideos innerVideos= (InnerVideos) adapterView.getItemAtPosition(i);
        videoUrl=innerVideos.getVideofile();
        videoId=innerVideos.getVideoid();


//        SharedPreferences prefs = getSharedPreferences("SharingUrlKPCC", MODE_PRIVATE);
//        String restoredText = prefs.getString(videoId, null);
//        Log.d("SAVED_VIDEO_FILE",""+restoredText);
//        intent=new Intent(InnerVideoActivity.this,PlayVideoActivity.class);
//        intent.putExtra(PlayVideoActivity.VIDEO_URL,videoUrl);
//        intent.putExtra(PlayVideoActivity.VIDEO_ID,videoId);
//        startActivity(intent);


        frameVideo.setVisibility(View.VISIBLE);

        Bundle bundle=new Bundle();
        bundle.putString("ID",videoId);
        bundle.putString("URL",videoUrl);
        fm = getFragmentManager();
        ft = fm.beginTransaction();
        fragment = new Videofragment();
        fragment.setArguments(bundle);
        ft.replace(R.id.framelayoutvideo, fragment);
        ft.commit();


       // downloadVideo(videoId,videoUrl);


    }

    public void downloadVideo(String videoId,String videoUrl){


        SharedPreferences prefs = getSharedPreferences("SharingUrlKPCC", MODE_PRIVATE);
    String restoredText = prefs.getString(videoId, null);
    if(restoredText==null){

        mPbar.setVisibility(View.VISIBLE);

        Uri uri=Uri.parse(videoUrl);
        DownloadManager.Request request=new DownloadManager.Request(uri);
        request.setDescription("Video Download").
                setTitle("downloading");

        request.setDestinationInExternalFilesDir(InnerVideoActivity.this,
                Environment.DIRECTORY_DOWNLOADS, "kpccvideo.mp4");

        request.setVisibleInDownloadsUi(true);

        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                DownloadManager.Request.NETWORK_MOBILE);

        myDownloadReference=downloadManager.enqueue(request);

    }else {
        Intent intent=new Intent(this,PlayVideoActivity.class);
        intent.putExtra("URL_PLAY",restoredText);
        startActivity(intent);
    }

}

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
        Log.d("VideoURL",""+videoUrl);

        IntentFilter intentFilter=new IntentFilter(DownloadManager.ACTION_NOTIFICATION_CLICKED);
        receiverDownloadClicked=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String extraId = DownloadManager
                        .EXTRA_NOTIFICATION_CLICK_DOWNLOAD_IDS;
                long[] references = intent.getLongArrayExtra(extraId);
                for (long reference : references) {
                    if (reference == myDownloadReference) {
//                        do something with the download file
                        Log.d("Prefernce", "" + references.length);
                        Log.d("Refrence",""+reference);

                    }
                }
            }
        };

        registerReceiver(receiverDownloadClicked,intentFilter);

        // filter for download
        IntentFilter intentFilter1=new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        rceiverDownloadComplete=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                long reference = intent.getLongExtra(DownloadManager
                        .EXTRA_DOWNLOAD_ID, -1);
                if (myDownloadReference == reference) {
//                    do something with the download file
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(reference);
                    Cursor cursor = downloadManager.query(query);

                    cursor.moveToFirst();
//                        get the status of the download
                    int columnIndex = cursor.getColumnIndex(DownloadManager
                            .COLUMN_STATUS);
                    int status = cursor.getInt(columnIndex);

                    int fileNameIndex = cursor.getColumnIndex(DownloadManager
                            .COLUMN_LOCAL_FILENAME);
                    String savedFilePath = cursor.getString(fileNameIndex);

//                        get the reason - more detail on the status
                    int columnReason = cursor.getColumnIndex(DownloadManager
                            .COLUMN_REASON);
                    int reason = cursor.getInt(columnReason);

                    switch (status) {
                        case DownloadManager.STATUS_SUCCESSFUL:

                            Log.d("TestDownload", "URL =" + savedFilePath);

                            SharedPreferences.Editor editor = getSharedPreferences("SharingUrlKPCC", MODE_PRIVATE).edit();
                            editor.putString(videoId,savedFilePath);
                            editor.commit();
                            mPbar.setVisibility(View.GONE);
                            isDownloadig=false;

//                           start activity to display the downloaded image
                            Intent intentDisplay = new Intent(InnerVideoActivity.this,PlayVideoActivity.class);
                            intentDisplay.putExtra("URL_PLAY", savedFilePath);
                            startActivity(intentDisplay);
//                            videoView.setVideoURI(Uri.parse(savedFilePath));
//                            videoView.start();
                            editor.putString("URL",savedFilePath);


                            break;
                        case DownloadManager.STATUS_FAILED:
                            Toast.makeText(InnerVideoActivity.this,
                                    "FAILED: " + reason,
                                    Toast.LENGTH_LONG).show();
                            break;
                        case DownloadManager.STATUS_PAUSED:
                            Toast.makeText(InnerVideoActivity.this,
                                    "PAUSED: " + reason,
                                    Toast.LENGTH_LONG).show();
                            break;
                        case DownloadManager.STATUS_PENDING:
                            Toast.makeText(InnerVideoActivity.this,
                                    "PENDING!",
                                    Toast.LENGTH_LONG).show();
                            break;
                        case DownloadManager.STATUS_RUNNING:
                            Toast.makeText(InnerVideoActivity.this,
                                    "RUNNING!",
                                    Toast.LENGTH_LONG).show();
                            break;
                    }
                    cursor.close();
                    mPbar.setVisibility(View.GONE);

                }
            }
        };
        registerReceiver(rceiverDownloadComplete, intentFilter1);

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(rceiverDownloadComplete);
        unregisterReceiver(receiverDownloadClicked);
        if(frameVideo!=null)
            frameVideo.setVisibility(View.INVISIBLE);

        if(frameLayoutMenu.getVisibility()==View.VISIBLE) {
            frameLayoutMenu.setVisibility(View.INVISIBLE);
            menuItem.setIcon(R.drawable.menu_settings);
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar

        SharedPreferences prefs = getSharedPreferences("SharedPollCount", MODE_PRIVATE);
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
                intent=new Intent(InnerVideoActivity.this,PoolActivity.class);
                startActivity(intent);
                return true;
            case R.id.feedsubmit:
                intent=new Intent(InnerVideoActivity.this,UserFeedBackActivity.class);
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
                    fm2 = getFragmentManager();
                    ft2 = fm2.beginTransaction();
                    fragmenttwo = new MenuFragment();
                    ft2.replace(R.id.fragmentofmenu, fragmenttwo);
                    ft2.commit();

                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    @Override
    public void onClick(View view) {
        Intent intent=new Intent(InnerVideoActivity.this,PoolActivity.class);
        startActivity(intent);
    }
}

