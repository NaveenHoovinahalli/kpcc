package teli.com.kpcc.activities;

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
import teli.com.kpcc.adapters.ImagesAdapter;
import teli.com.kpcc.fragments.MenuFragment;
import teli.com.kpcc.models.OuterImage;
import teli.com.kpcc.views.CcEditText;

/**
 * Created by naveen on 4/1/15.
 */
public class ImageActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    @InjectView(R.id.progress_bar)
    ProgressBar mPbar;

    @InjectView(R.id.grid_images)
    GridView mGridImages;

    @InjectView(R.id.fragmentofmenu)
    FrameLayout frameLayoutMenu;

    public android.app.FragmentManager fm;
    public FragmentTransaction ft;
    android.app.Fragment fragment;
    private ImagesAdapter adapter;
    private ArrayList<String> mainHeads = new ArrayList<String>();
    ArrayList<OuterImage> outerImagesMain;
    ArrayList<OuterImage> outerImagesSearch;
    private MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.images);


        //setIcon();
        fetchImages();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar

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

                outerImagesSearch=new ArrayList<OuterImage>();
                for(OuterImage outerImage:outerImagesMain){
                   if(outerImage.getMainHead().toLowerCase().startsWith(s.toString().toLowerCase().trim())){
                       outerImagesSearch.add(outerImage);
                   }
                }
                adapter = new ImagesAdapter(ImageActivity.this,outerImagesSearch);
                mGridImages.setAdapter(adapter);


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
                intent=new Intent(ImageActivity.this,PoolActivity.class);
                startActivity(intent);
                return true;
            case R.id.feedsubmit:
                intent=new Intent(ImageActivity.this,UserFeedBackActivity.class);
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


    private void updateAdapter(CcEditText searchEt) {
        /*searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                mGridImages.setTextFilterEnabled(true);
              String filterdText = (String) ImageActivity.this.adapter.getItem(i);
                Log.d("ImageActivity","filtered text::" + filterdText);

                *//*String charText = searchEt.getText().toString().toLowerCase(Locale.getDefault());
                mainHeads.clear();
                if (charText.length() == 0) {
                    mainHeads.addAll(mainHeads);
                } else {
                    for (Object bean : mainHeads) {
                        *//**//*if (((AllBean) bean).getusername().toLowerCase(Locale.getDefault())
                                .contains(charText)) {
                            mainHeads.add(bean);
                        }*//**//*
                    }
                }
              notifyDataSetChanged();*//*
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });*/
    }

    private void fetchImages() {

        String url = String.format(Constants.URL_IMAGES, AndroidUtils.getDeviceImei(this));

        JsonArrayRequest request = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        mPbar.setVisibility(View.GONE);
                        Log.d(Constants.KPCC_LOG,"Inner Images Response:" + jsonArray.toString());

                        Gson gson = new Gson();

                        ArrayList<OuterImage> outerImages = gson.fromJson(jsonArray.toString(), new TypeToken<List<OuterImage>>() {
                        }.getType());

                        Log.d(Constants.KPCC_LOG,"outer Images Response:" + outerImages);


                        onDataReceived(outerImages);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        mPbar.setVisibility(View.GONE);
                        Log.d(Constants.KPCC_LOG,"Inner Images Error:" + volleyError.toString());
                    }
                });
        CcRequestQueue.getInstance(this).addToRequestQueue(request);
    }

    private void onDataReceived(ArrayList<OuterImage> outerImages) {

        outerImagesMain=outerImages;

        adapter = new ImagesAdapter(ImageActivity.this,outerImages);
        mGridImages.setAdapter(adapter);
        mGridImages.setOnItemClickListener(ImageActivity.this);

        for (int i=0;i<outerImages.size();i++){
            mainHeads.add(outerImages.get(i).getMainHead());
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Log.d("OnItemClicked","Clicked view value::" + adapterView.getItemAtPosition(i).toString());

        OuterImage outerImage = (OuterImage) adapterView.getItemAtPosition(i);

        Log.d("OnItemClicked","Clicked view value::" + outerImage.getMainHead());
        Log.d("OnItemClicked","Clicked view value::" + outerImage.getId());
        Log.d("OnItemClicked","Clicked view value::" + outerImage.getImages().size());

        Intent intent = new Intent(ImageActivity.this,InnerImageActivity.class);
        intent.putExtra(InnerImageActivity.OUTER_IMAGE_VALUE, outerImage);
        startActivity(intent);

        Log.d("OnItemClicked","Activity started::");

    }

    @Override
    public void onClick(View view) {
        Intent intent=new Intent(ImageActivity.this,PoolActivity.class);
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
