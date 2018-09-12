package teli.com.kpcc.activities;

import android.app.ActionBar;
import android.app.DownloadManager;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import teli.com.kpcc.R;
import teli.com.kpcc.Utils.AndroidUtils;
import teli.com.kpcc.Utils.CcRequestQueue;
import teli.com.kpcc.Utils.Constants;
import teli.com.kpcc.adapters.VideoAdapter;
import teli.com.kpcc.fragments.MenuFragment;
import teli.com.kpcc.fragments.Videofragment;
import teli.com.kpcc.models.OuterVideo;

/**
 * Created by naveen on 7/1/15.
 */
public class VideoActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener {


    @InjectView(R.id.grid_videos_container)
    GridView mGridVideos;

    @InjectView(R.id.progress_bar)
    ProgressBar mPbar;

    ActionBar actionBar;
    @InjectView(R.id.fragmentofmenu)
    FrameLayout frameLayoutMenu;

    @InjectView(R.id.framelayoutvideo)
    FrameLayout frameVideo;

    public android.app.FragmentManager fm;
    public FragmentTransaction ft;
    android.app.Fragment fragment;
    ArrayList<OuterVideo> outerVideos;
    ArrayList<OuterVideo> outerVideoSearch;
    VideoAdapter adapter;
    private MenuItem menuItem;
    public DownloadManager downloadManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_list);
        downloadManager= (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);


        if(AndroidUtils.isNetworkOnline(this)) {
            fetchVideos();
        }

    }


    private void fetchVideos() {

       String url=String.format(Constants.URL_VIDEOS, AndroidUtils.getDeviceImei(this));

        JsonArrayRequest videoRequest=new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        mPbar.setVisibility(View.GONE);
                        parceJsonValues(jsonArray);
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        CcRequestQueue.getInstance(this).addToRequestQueue(videoRequest);

    }

    private void parceJsonValues(JSONArray jsonArray) {
        Gson gson=new Gson();

         outerVideos = gson.fromJson(jsonArray.toString(), new TypeToken<List<OuterVideo>>() {
        }.getType());

        adapter=new VideoAdapter(this,outerVideos);
        mGridVideos.setAdapter(adapter);
        mGridVideos.setOnItemClickListener(VideoActivity.this);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent;

        OuterVideo outerVideo = (OuterVideo) adapterView.getItemAtPosition(i);

        if(outerVideo.getVideos().size()==1) {

//            Log.d("NO_OF_VIDEOS", "" + outerVideo.getVideos().size());
//            Log.d("VIDEO_FILE", "" + outerVideo.getVideos().get(0).getVideofile());
//           String id= outerVideo.getVideos().get(0).getVideoid();
//            String url=outerVideo.getVideos().get(0).getVideofile();
//            intent=new Intent(VideoActivity.this,PlayVideoActivity.class);
//            intent.putExtra(PlayVideoActivity.VIDEO_ID,id);
//            intent.putExtra(PlayVideoActivity.VIDEO_URL,Constants.BASE_URL+url);
//            startActivity(intent);
            frameVideo.setVisibility(View.VISIBLE);

            Bundle bundle=new Bundle();
            bundle.putString("ID",outerVideo.getVideos().get(0).getVideoid());
            bundle.putString("URL",outerVideo.getVideos().get(0).getVideofile());
            fm = getFragmentManager();
            ft = fm.beginTransaction();
            fragment = new Videofragment();
            fragment.setArguments(bundle);
            ft.replace(R.id.framelayoutvideo, fragment);
            ft.commit();


        }else {

            intent = new Intent(VideoActivity.this, InnerVideoActivity.class);
            intent.putExtra(InnerVideoActivity.OUTER_VIDEO_VALUE, outerVideo);
            startActivity(intent);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        SharedPreferences prefs = getSharedPreferences("SharedPollCount",MODE_PRIVATE);
        String pollCount= prefs.getString("POLL_COUNT","0");

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);

        SearchManager manager=(SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.search_bar);

        SearchView search = (SearchView) searchItem.getActionView();

        search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
        search.setOnQueryTextListener( new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.d("Search_Submit","Submit"+s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                outerVideoSearch= new ArrayList<OuterVideo>();
                for(OuterVideo outerVideo:outerVideos){
                    if(outerVideo.getMainHead().toLowerCase().startsWith(s.toString().toLowerCase().trim())){
                        outerVideoSearch.add(outerVideo);
                    }
                }
                adapter = new VideoAdapter(VideoActivity.this,outerVideoSearch);
                mGridVideos.setAdapter(adapter);


                return true;
            }
        });

        View count=menu.findItem(R.id.poll).getActionView();
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("Menu","onOptionsMenuClick");

        Intent intent;
        switch (item.getItemId()){
            case R.id.poll_notification:
                intent=new Intent(VideoActivity.this,PoolActivity.class);
                startActivity(intent);
                return true;
            case R.id.feedsubmit:
                intent=new Intent(VideoActivity.this,UserFeedBackActivity.class);
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
        Intent intent=new Intent(VideoActivity.this,PoolActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (frameVideo != null)
            frameVideo.setVisibility(View.INVISIBLE);


        if (frameLayoutMenu.getVisibility() == View.VISIBLE) {
            frameLayoutMenu.setVisibility(View.INVISIBLE);
            menuItem.setIcon(R.drawable.menu_settings);
        }
    }
}
