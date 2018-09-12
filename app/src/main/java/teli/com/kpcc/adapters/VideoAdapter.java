package teli.com.kpcc.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import teli.com.kpcc.R;
import teli.com.kpcc.models.OuterVideo;
import teli.com.kpcc.views.CcTextView;

/**
 * Created by naveen on 7/1/15.
 */
public class VideoAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<OuterVideo> outerVideos;
    private ViewHolder viewHolder;

    public VideoAdapter(Context context, ArrayList<OuterVideo> outerVideos){

        this.mContext=context;
        this.outerVideos=outerVideos;
        Log.d("VideoInAdapter","Inside Video Adapter");
    }




    @Override
    public int getCount() {
        return outerVideos.size();
    }

    @Override
    public Object getItem(int i) {
        return outerVideos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view == null){
            view=View.inflate(mContext, R.layout.video_item,null);
            viewHolder=new ViewHolder();

          viewHolder.imageView=(ImageView) view.findViewById(R.id.iv_video_thumbnail);
          viewHolder.textView=(CcTextView)  view.findViewById(R.id.iv_video_subname);
            view.setTag(viewHolder);
        }else {
            viewHolder=(ViewHolder) view.getTag();
        }

        SharedPreferences prefs = mContext.getSharedPreferences("SharingUrlKPCC", mContext.MODE_PRIVATE);
        String restoredText = prefs.getString(outerVideos.get(i).getVideos().get(0).getVideoid(), null);
        if(restoredText != null){
            Bitmap bitmap= ThumbnailUtils.createVideoThumbnail(restoredText,
                    MediaStore.Video.Thumbnails.MINI_KIND);
           viewHolder.imageView.setImageBitmap(bitmap);
        }
      //  viewHolder.imageView.setBackgroundResource(R.drawable.ic_launcher);
        viewHolder.textView.setText(outerVideos.get(i).getMainHead());
        return view;
    }

    private class ViewHolder {
        ImageView imageView;
        CcTextView textView;

    }
}
