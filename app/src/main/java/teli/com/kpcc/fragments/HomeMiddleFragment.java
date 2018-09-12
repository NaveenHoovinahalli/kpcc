package teli.com.kpcc.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import teli.com.kpcc.R;
import teli.com.kpcc.Utils.AndroidUtils;
import teli.com.kpcc.Utils.CcRequestQueue;
import teli.com.kpcc.Utils.Constants;
import teli.com.kpcc.activities.EventsActivity;
import teli.com.kpcc.activities.OurLeadersActivity;
import teli.com.kpcc.models.HomeImages;

/**
 * Created by madhuri on 2/1/15.
 */
public class HomeMiddleFragment extends Fragment  {

    @InjectView(R.id.leader_flipper)
    ViewFlipper mLeaderFlipper;

    @InjectView(R.id.event_flipper)
    ViewFlipper mEventFlipper;

   /* @InjectView(R.id.leader_image)
    ImageView mLeaderImage;
*/
   /* @InjectView(R.id.event_image)
    ImageView mEventImage;*/

    @InjectView(R.id.ourleader_home)
    RelativeLayout ourleader_layout;

    private int images[] = {R.drawable.ic_launcher,R.drawable.splash_screen};
    ArrayList<ViewFlipper> mFlippers = new ArrayList<ViewFlipper>();

    private Runnable mFlipRunnable = new Runnable() {
        int i = 0;

        @Override
        public void run() {
            /*if(!mLeaderFlipper.isFlipping()){
                getRandomFlipper().showNext();
            }*/

            mLeaderFlipper.startFlipping();
            mEventFlipper.startFlipping();
            handler.postDelayed(mFlipRunnable,8000);
        }
    };
    private Handler handler;
    private ImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_middle_layout,null);
        ButterKnife.inject(this,view);
        return view;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handler = new Handler();
        handler.post(mFlipRunnable);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //showDefaultImages();
        fetchHomePageImages();
    }

    private void fetchHomePageImages() {

        JsonObjectRequest request = new JsonObjectRequest(Constants.URL_HOME_PAGE_IMAGES,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d(Constants.KPCC_LOG,"Home Images Response:" + jsonObject.toString());
                        HomeImages images = new Gson().fromJson(jsonObject.toString(),HomeImages.class);
                        displayHomeImages(images);
                    }
                },
        new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d(Constants.KPCC_LOG,"Home Images Error:" + volleyError.toString());
            }
        });
        CcRequestQueue.getInstance(getActivity()).addToRequestQueue(request);
    }


    private void displayHomeImages(HomeImages images) {

//        Transformation transformation = new Transformation() {
//
//            @Override public Bitmap transform(Bitmap source) {
//                int targetWidth = imageView.getWidth();
//
//                double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
//                int targetHeight = (int) (targetWidth * aspectRatio);
//                Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
//                if (result != source) {
//                    // Same bitmap is returned if sizes are the same
//                    source.recycle();
//                }
//                return result;
//            }
//
//            @Override public String key() {
//                return "transformation" + " desiredWidth";
//            }
//        };


        for (int i = 0; i<images.getLeaderImages().size() ; i++){
            imageView = new ImageView(getActivity());
            imageView.setScaleType(ImageView.ScaleType.FIT_START);
            imageView.setAdjustViewBounds(true);
           // imageView.setScaleType(ImageView.ScaleType.FIT_END);
            //imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mLeaderFlipper.addView(imageView);
            Picasso.with(getActivity()).load(Constants.BASE_URL+images.getLeaderImages().get(i).getImage()).
                    placeholder(R.drawable.newback).into(imageView);

        }

        for (int i =0;i<images.getEventImages().size();i++){
            ImageView imageView = new ImageView(getActivity());
            imageView.setScaleType(ImageView.ScaleType.FIT_START);
            imageView.setAdjustViewBounds(true);
            mEventFlipper.addView(imageView);
            Picasso.with(getActivity()).load(Constants.BASE_URL+images.getEventImages().get(i).getImage()).
                    placeholder(R.drawable.newback).into(imageView);

        }

    }



    @OnClick({R.id.ourleader_home,R.id.events_home})
    public void onItemClicked(View view) {
        if (!AndroidUtils.isNetworkOnline(getActivity())) {
            Toast.makeText(getActivity(), "Sorry! No Internet Connection", Toast.LENGTH_SHORT).show();
        }

        switch (view.getId()){
            case R.id.ourleader_home : onOurLeaderClicked();
                break;
            case R.id.events_home : onEventClicked();
                break;
        }

    }

    private void onOurLeaderClicked() {
        Intent intent=new Intent(getActivity(), OurLeadersActivity.class);
        startActivity(intent);

    }

    private void onEventClicked(){
        Intent intent=new Intent(getActivity(), EventsActivity.class);
        startActivity(intent);
    }

}
