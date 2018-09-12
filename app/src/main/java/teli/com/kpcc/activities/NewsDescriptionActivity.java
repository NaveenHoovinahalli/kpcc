package teli.com.kpcc.activities;

import android.annotation.TargetApi;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
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

import java.util.ArrayList;

import butterknife.InjectView;
import butterknife.OnClick;
import teli.com.kpcc.R;
import teli.com.kpcc.Utils.Constants;
import teli.com.kpcc.fragments.MenuFragment;
import teli.com.kpcc.models.Feed;
import teli.com.kpcc.views.CcTextView;

/**
 * Created by madhuri on 15/1/15.
 */
public class NewsDescriptionActivity extends BaseActivity implements View.OnClickListener {

    public static String NEWS_DETAILS = "news_details";
    private Feed newsDetails;

    @InjectView(R.id.news_description)
    CcTextView mNewsDescription;

    @InjectView(R.id.news_head)
    CcTextView mNewsHead;

    @InjectView(R.id.fragmentofmenu)
    FrameLayout frameLayoutMenu;

    @InjectView(R.id.pictures)
    Button mPictures;
    ArrayList<String> imageslinks=new ArrayList<String>();

    public android.app.FragmentManager fm;
    public FragmentTransaction ft;
    android.app.Fragment fragment;
    MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_news_description);

//        ActionBar actionBar = getActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setDisplayShowTitleEnabled(false);


        if(getIntent().hasExtra(NEWS_DETAILS)){
           newsDetails = getIntent().getParcelableExtra(NEWS_DETAILS);
            initViews(newsDetails);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar

        SharedPreferences prefs = getSharedPreferences("SharedPollCount",MODE_PRIVATE);
        String pollCount= prefs.getString("POLL_COUNT","0");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_action_bar, menu);
        Log.d("POLL_COUNT", "countin home " + pollCount);

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
                intent=new Intent(NewsDescriptionActivity.this,PoolActivity.class);
                startActivity(intent);
                return true;
            case R.id.feedsubmit:
                intent=new Intent(NewsDescriptionActivity.this,UserFeedBackActivity.class);
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

    @OnClick(R.id.pictures)
    public void OnPicturesClicked(View view){

        if(imageslinks.size()>0){

            Intent intent= new Intent(this,NewsImageActivity.class);
            intent.putStringArrayListExtra("IMAGESLINK", imageslinks);
            startActivity(intent);

        }



    }

    private void initViews(Feed newsDetails) {

        if (newsDetails.getNewsHead()!=null && !newsDetails.getNewsHead().isEmpty()){
            mNewsHead.setText(newsDetails.getNewsHead());
        }

        if (newsDetails.getNewsDescription()!=null && !newsDetails.getNewsDescription().isEmpty()){
            mNewsDescription.setText(newsDetails.getNewsDescription());
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent=new Intent(NewsDescriptionActivity.this,PoolActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
        imageCount();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void imageCount(){

        imageslinks.clear();


        if (newsDetails.getImages().size()>0) {

            for(int i=0;i<newsDetails.getImages().size();i++){
                Log.d("IMAGESTODISPLAY", "" + newsDetails.getImages().get(i).getImage());
                String url= Constants.BASE_URL+newsDetails.getImages().get(i).getImage();
                imageslinks.add(url);
            }


        }if(newsDetails.getImageLinks().size()>0){

            for(int i=0;i<newsDetails.getImageLinks().size();i++){
                Log.d("IMAGESTODISPLAY", "" + newsDetails.getImageLinks().get(i).getImageLink());
                String url=newsDetails.getImageLinks().get(i).getImageLink();
                imageslinks.add(url);
            }

        }
         if(imageslinks.size()>0){

         }else {
//             mPictures.setVisibility(View.INVISIBLE);
//             mPictures.setBackgroundColor(Color.parseColor("#D3D3D3"));
             mPictures.setEnabled(false);
             mPictures.setBackground(getResources().getDrawable(R.drawable.gray_round_button));

         }


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
