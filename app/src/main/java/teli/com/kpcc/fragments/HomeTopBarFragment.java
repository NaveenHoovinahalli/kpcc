package teli.com.kpcc.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import teli.com.kpcc.R;
import teli.com.kpcc.Utils.AndroidUtils;
import teli.com.kpcc.activities.ImageActivity;
import teli.com.kpcc.activities.MessageActivity;
import teli.com.kpcc.activities.VideoActivity;
import teli.com.kpcc.views.CcTextView;

/**
 * Created by madhuri on 2/1/15.
 */
public class HomeTopBarFragment extends Fragment{

    @InjectView(R.id.tv_images)
    CcTextView mImages;

    @InjectView(R.id.tv_messages)
    CcTextView mMessages;

    @InjectView(R.id.tv_videos)
    CcTextView mVideos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_top_bar,null);
        ButterKnife.inject(this,view);
        return view;
    }

    @OnClick({R.id.tv_images,R.id.tv_messages,R.id.tv_videos})
    public void onItemClicked(View view) {
        if(!AndroidUtils.isNetworkOnline(getActivity())){
            Toast.makeText(getActivity(), "Sorry! No Internet Connection", Toast.LENGTH_SHORT).show();
        }

        switch (view.getId()){
            case R.id.tv_images : onImagesClicked();
                break;

            case R.id.tv_messages : onMessagesClicked();
                break;

            case R.id.tv_videos : onVideosClicked();
                break;
        }
    }

    private void onVideosClicked() {
        Intent intent = new Intent(getActivity(),VideoActivity.class);
        startActivity(intent);
    }

    private void onMessagesClicked() {
        Intent intent = new Intent(getActivity(),MessageActivity.class);
        startActivity(intent);
    }

    private void onImagesClicked() {
        Intent intent = new Intent(getActivity(),ImageActivity.class);
        startActivity(intent);
    }

}
