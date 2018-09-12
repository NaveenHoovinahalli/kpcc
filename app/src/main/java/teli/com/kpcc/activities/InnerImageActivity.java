package teli.com.kpcc.activities;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import teli.com.kpcc.R;
import teli.com.kpcc.Utils.Constants;
import teli.com.kpcc.fragments.ImageInnerPager;
import teli.com.kpcc.models.InnerImage;
import teli.com.kpcc.models.Message;
import teli.com.kpcc.models.NationalLeaderFeed;
import teli.com.kpcc.models.OuterImage;

/**
 * Created by madhuri on 8/1/15.
 */
public class InnerImageActivity extends FragmentActivity {

    @InjectView(R.id.pager)
    ViewPager mPager;

    public static final String OUTER_IMAGE_VALUE ="outer_image";
    public static final String LEADER_IMAGES="leader_images";
    public static final String MESSAGE_PICTURES = "message_image";
    public static final String FEED_IMAGES = "feed_images";
    private ArrayList<ImageInnerPager> fragments = new ArrayList<ImageInnerPager>();
    private OuterImage outerImage;
    private ActionBar actionBar;
    ArrayList<String> imageslinks=new ArrayList<String>();

    private  NationalLeaderFeed nationalLeaderFeed;
    private ArrayList<InnerImage> innerImages = new ArrayList<InnerImage>() ;
    private Message message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.images_initial_layout);
        ButterKnife.inject(this);

        Toast.makeText(this, "Long press on the picture will enable the " +
                "share and save option ", Toast.LENGTH_LONG).show();


        if (getIntent().hasExtra(OUTER_IMAGE_VALUE)){
            outerImage = getIntent().getParcelableExtra(OUTER_IMAGE_VALUE);
            Log.d(Constants.KPCC_LOG,"Activity result:" + outerImage.getMainHead());

            for (int i = 0; i<outerImage.getImages().size();i++){
                ImageInnerPager innerImagePager = ImageInnerPager.newInstance(outerImage.getImages().get(i));
                fragments.add(innerImagePager);
            }

        }else if(getIntent().hasExtra(LEADER_IMAGES)){
            nationalLeaderFeed=  getIntent().getParcelableExtra(LEADER_IMAGES);
            innerImages=nationalLeaderFeed.getImages();
            Log.d("Images","Imagescount" +nationalLeaderFeed.getImages().size());

            for (int i = 0; i<nationalLeaderFeed.getImages().size();i++){
                ImageInnerPager innerImagePager = ImageInnerPager.newInstance(nationalLeaderFeed.getImages().get(i));
                fragments.add(innerImagePager);
            }
        }else if (getIntent().hasExtra(MESSAGE_PICTURES)){
            message = getIntent().getParcelableExtra(MESSAGE_PICTURES);

            for (int i = 0; i < message.getInnerImages().size();i++){
                ImageInnerPager innerImagePager = ImageInnerPager.newInstance(message.getInnerImages().get(i));
                fragments.add(innerImagePager);
            }
        }/*else if (getIntent().hasExtra(FEED_IMAGES)){

            ArrayList<FeedImage> feedImages = (ArrayList<FeedImage>) getIntent().getParcelableExtra(FEED_IMAGES);

            for (int i = 0; i < message.getInnerImages().size();i++){
                ImageInnerPager innerImagePager = ImageInnerPager.newInstance(feedImages);
                fragments.add(innerImagePager);
            }

        }
*/
        Log.d(Constants.KPCC_LOG,"Fragments size:" + fragments.size());

        setPager();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {

        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
               super.onBackPressed();
                break;
        }

        return true;
    }

    private void setPager() {
        if (fragments.size()!=0) {
            PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), fragments);
            mPager.setAdapter(adapter);
            Log.d(Constants.KPCC_LOG, "Pager set:");
        }
    }

    public class PagerAdapter extends FragmentPagerAdapter {

        ArrayList<ImageInnerPager> fragments = new ArrayList<ImageInnerPager>();

        public PagerAdapter(FragmentManager fm, ArrayList<ImageInnerPager> fragments) {

            super(fm);
            Log.d(Constants.KPCC_LOG, "pager:");
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int i) {
            return fragments.get(i);
        }

        @Override
        public int getCount() {
            Log.d(Constants.KPCC_LOG,"pager Fragments size in pager:" + fragments.size());
            return fragments.size();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fragments = null;
        outerImage = null;
    }
}
