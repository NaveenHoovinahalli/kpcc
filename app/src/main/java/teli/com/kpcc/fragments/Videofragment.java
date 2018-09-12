package teli.com.kpcc.fragments;

import android.app.DownloadManager;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import teli.com.kpcc.R;
import teli.com.kpcc.Utils.Constants;
import teli.com.kpcc.activities.PlayVideoActivity;


/**
 * Created by naveen on 23/1/15.
 */
public class Videofragment extends Fragment {
    String videoUrl;
    String videoId;

    public DownloadManager downloadManager;
    public long myDownloadReference ;
    public BroadcastReceiver rceiverDownloadComplete;
    public BroadcastReceiver receiverDownloadClicked;
//     public ProgressDialog mPbar;

    ProgressBar mPbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.videofragmantprogress,container,false);
        mPbar= (ProgressBar) view.findViewById(R.id.ProgressBar01);

        downloadManager= (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);



        return view;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        mPbar =new ProgressDialog(getActivity());
//        mPbar.setTitle("Downloading video please wait...");
//        mPbar.setCancelable(false);

        String id=getArguments().getString("ID");
        String url=getArguments().getString("URL");
        downloadVideo(id,url);

    }

    public void downloadVideo(String Id,String Url){

        videoUrl=Url;
        videoId=Id;
        Log.d("VIDEOId,VideoURL", "" + videoId + " " + videoUrl);


        SharedPreferences prefs = getActivity().getSharedPreferences("SharingUrlKPCC", getActivity().MODE_PRIVATE);
        String restoredText = prefs.getString(videoId, null);
        if(restoredText==null){
            Log.d("VIDEO","Downloading first time");


            mPbar.setVisibility(View.VISIBLE);
//            mPbar.show();

            Uri uri=Uri.parse(Constants.BASE_URL+videoUrl);
            DownloadManager.Request request=new DownloadManager.Request(uri);
//            request.setDescription("Video Download").
//                    setTitle("downloading");

//            request.setDestinationInExternalFilesDir(getActivity(),
//                    Environment.DIRECTORY_DOWNLOADS, "kpccvideo.mp4");

            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"kpccvideo.mp4");

            request.setVisibleInDownloadsUi(true);

            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                    DownloadManager.Request.NETWORK_MOBILE);

            myDownloadReference=downloadManager.enqueue(request);

        }else {
            Log.d("VIDEO","Video Already downloaded");
            mPbar.setVisibility(View.INVISIBLE);
//            mPbar.dismiss();
//            videoView.setMediaController(controller);
//            videoView.setVideoURI(Uri.parse(restoredText));
//            videoView.start();
            Intent intent;

            intent=new Intent(getActivity(),PlayVideoActivity.class);
            intent.putExtra("video_id",videoId);
            intent.putExtra("video_url", Constants.BASE_URL+videoUrl);
            startActivity(intent);
        }

    }

    @Override
    public void onResume() {
        super.onResume();

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
                        Log.d("Prefernce", "" + references.length);
                        Log.d("Refrence",""+reference);

                    }
                }
            }
        };

        getActivity().registerReceiver(receiverDownloadClicked, intentFilter);

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

                            SharedPreferences.Editor editor = getActivity().getSharedPreferences("SharingUrlKPCC", getActivity().MODE_PRIVATE).edit();
                            editor.putString(videoId,savedFilePath);
                            editor.apply();
                            mPbar.setVisibility(View.GONE);
//                            mPbar.dismiss();
//                            isDownloadig=false;
                            intent=new Intent(getActivity(),PlayVideoActivity.class);
            intent.putExtra("video_id",videoId);
            intent.putExtra("video_url", Constants.BASE_URL+videoUrl);
            startActivity(intent);

//                            videoView.setVideoURI(Uri.parse(savedFilePath));
//                            videoView.start();
//                            if(mPbar.isShowing())
//                                mPbar.dismiss();

                            break;
                        case DownloadManager.STATUS_FAILED:
                            Toast.makeText(getActivity(),
                                    "FAILED: " + reason,
                                    Toast.LENGTH_LONG).show();
                            break;
                        case DownloadManager.STATUS_PAUSED:
                            Toast.makeText(getActivity(),
                                    "PAUSED: " + reason,
                                    Toast.LENGTH_LONG).show();
                            break;
                        case DownloadManager.STATUS_PENDING:
                            Toast.makeText(getActivity(),
                                    "PENDING!",
                                    Toast.LENGTH_LONG).show();
                            break;
                        case DownloadManager.STATUS_RUNNING:
                            Toast.makeText(getActivity(),
                                    "RUNNING!",
                                    Toast.LENGTH_LONG).show();
                            break;
                    }
                    cursor.close();
                    mPbar.setVisibility(View.GONE);
//                    mPbar.dismiss();

                }
            }
        };
        getActivity().registerReceiver(rceiverDownloadComplete, intentFilter1);

    }


}
