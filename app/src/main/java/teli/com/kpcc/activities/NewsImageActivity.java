package teli.com.kpcc.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.InjectView;
import teli.com.kpcc.R;
import teli.com.kpcc.fragments.ImageNewsFeedPager;

/**
 * Created by naveen on 23/1/15.
 */
public class NewsImageActivity extends FragmentActivity {

    @InjectView(R.id.imageforfeed)
    ImageView myImage;
    private static final int NUM_PAGES = 5;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    ArrayList<String> imageslink=new ArrayList<String>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imagesslidefeedpager);

        Toast.makeText(this, "Long press on the picture will enable the " +
                "share and save option ", Toast.LENGTH_LONG).show();

        if(getIntent().hasExtra("IMAGESLINK")){
          imageslink=getIntent().getStringArrayListExtra("IMAGESLINK");
            mPager = (ViewPager) findViewById(R.id.pagerforfeed);
            mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
            mPager.setAdapter(mPagerAdapter);

        }




    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            finish();
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }





    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Log.d("IMAGESLINK","from pager"+imageslink.get(position));

            return new ImageNewsFeedPager(imageslink.get(position));
        }

        @Override
        public int getCount() {
            return imageslink.size();
        }
    }


}
