package teli.com.kpcc.fragments;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import teli.com.kpcc.R;
import teli.com.kpcc.models.TouchImageView;

/**
 * Created by naveen on 24/1/15.
 */
@SuppressLint("ValidFragment")
public class ImageNewsFeedPager extends android.support.v4.app.Fragment {
    String image;
    TouchImageView imageView;
    View view;
    Button shareButton,saveButton;
    String[] nameOfAppsToShareWith;
    String imagePath;
//    ProgressBar mPbar;
ProgressDialog mPbar;


    public DownloadManager downloadManager;
    public long myDownloadReference ;
    public BroadcastReceiver rceiverDownloadComplete;
    public BroadcastReceiver receiverDownloadClicked;
    private String TAG = "download";

    @SuppressLint("ValidFragment")
    public ImageNewsFeedPager(String account_default) {
        image=account_default;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.imagefonews,container,false);
          imageView= (TouchImageView) view.findViewById(R.id.imageforfeed);
       shareButton=(Button) view.findViewById(R.id.shareButton);
//        mPbar=(ProgressBar) view.findViewById(R.id.pBar);
        saveButton=(Button) view.findViewById(R.id.saveButton);

        Picasso.with(getActivity()).load(image).
                placeholder(R.drawable.appicon).into(imageView);

        downloadManager= (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);


        mPbar =new ProgressDialog(getActivity());
        mPbar.setTitle("Downloading please wait...");
        mPbar.setCancelable(false);


        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                shareButton.setVisibility(View.VISIBLE);
                saveButton.setVisibility(View.VISIBLE);
                Log.d("ImageNewsFeedPager", "On long clicked");
                return true;
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share(image);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadImage(image);
            }
        });

    }

    public  void share(String imagefile) {
        Log.d("IMAGEPATH",""+imagefile);




        nameOfAppsToShareWith = new String[] { "google+", "facebook", "twitter", "gmail","hangouts","whatsapp","messages","messaging", "email"};
        String[] blacklist = new String[]{"com.any.package", "net.other.package"};

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        Bundle extras=new Bundle();
        extras.putString(Intent.EXTRA_SUBJECT,"Check this KPCC link");
        extras.putString(Intent.EXTRA_TEXT, image);
        intent.putExtras(extras);
        startActivityForResult(generateCustomChooserIntent(intent, blacklist), 0);

//        Uri imageUri=Uri.parse("/storage/emulated/0/Download/kpc-1.jpg");
//        Intent intent=new Intent(Intent.ACTION_SEND);
//        intent.setType("image/jpg");
////        intent.setData(imageUri);
//        intent.putExtra(Intent.EXTRA_STREAM,imageUri);
//        startActivityForResult(generateCustomChooserIntent(intent, blacklist), 0);


    }

    private Intent generateCustomChooserIntent(Intent prototype, String[] forbiddenChoices) {

        List<Intent> targetedShareIntents = new ArrayList<Intent>();
        List<HashMap<String, String>> intentMetaInfo = new ArrayList<HashMap<String, String>>();
        Intent chooserIntent;

        Intent dummy = new Intent(prototype.getAction());
        dummy.setType(prototype.getType());
        List<ResolveInfo> resInfo = getActivity().getPackageManager().queryIntentActivities(dummy, 0);

        if (!resInfo.isEmpty())
        {
            for (ResolveInfo resolveInfo : resInfo)
            {
                if (resolveInfo.activityInfo == null
                        || Arrays.asList(forbiddenChoices).contains(
                        resolveInfo.activityInfo.packageName))
                    continue;

                HashMap<String, String> info = new HashMap<String, String>();
                info.put("packageName", resolveInfo.activityInfo.packageName);
                info.put("className", resolveInfo.activityInfo.name);
                String appName = String.valueOf(resolveInfo.activityInfo
                        .loadLabel(getActivity().getPackageManager()));
                info.put("simpleName", appName);

                if (Arrays.asList(nameOfAppsToShareWith).contains(
                        appName.toLowerCase()))
                {
                    intentMetaInfo.add(info);
                }
            }

            for (HashMap<String, String> metaInfo : intentMetaInfo)
            {
                Intent targetedShareIntent = (Intent) prototype.clone();
                targetedShareIntent.setPackage(metaInfo.get("packageName"));
                targetedShareIntent.setClassName(
                        metaInfo.get("packageName"),
                        metaInfo.get("className"));
                targetedShareIntents.add(targetedShareIntent);
            }
            String shareVia = "SHARE VIA";
            String shareTitle = shareVia.substring(0, 1).toUpperCase()
                    + shareVia.substring(1);
            chooserIntent = Intent.createChooser(targetedShareIntents
                    .remove(targetedShareIntents.size() - 1), shareTitle);
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                    targetedShareIntents.toArray(new Parcelable[] {}));
            return chooserIntent;
        }

        return Intent.createChooser(prototype,"Share via");

    }



    public void downloadImage(String Url){

        imagePath=Url;


//            mPbar.setVisibility(View.VISIBLE);
            mPbar.show();

            Uri uri=Uri.parse(imagePath);
            DownloadManager.Request request=new DownloadManager.Request(uri);
//            request.setDescription("Video Download").
//                    setTitle("downloading");

//            request.setDestinationInExternalFilesDir(getActivity(),
//                    Environment.DIRECTORY_DOWNLOADS, "kpcc.png");

        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"kpc.jpg");

            request.setVisibleInDownloadsUi(true);

            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                    DownloadManager.Request.NETWORK_MOBILE);

            myDownloadReference=downloadManager.enqueue(request);



    }

    @Override
    public void onResume() {
        super.onResume();


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

//                            mPbar.setVisibility(View.GONE);
                            mPbar.dismiss();

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
//                    mPbar.setVisibility(View.GONE);
                    mPbar.dismiss();

                }
            }
        };
        getActivity().registerReceiver(rceiverDownloadComplete, intentFilter1);

    }

    @Override
    public void onPause() {
        super.onPause();

       getActivity().unregisterReceiver(rceiverDownloadComplete);
        getActivity().unregisterReceiver(receiverDownloadClicked);
    }
}


