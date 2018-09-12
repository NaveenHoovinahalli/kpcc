package teli.com.kpcc.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import teli.com.kpcc.R;
import teli.com.kpcc.models.InnerVideos;
import teli.com.kpcc.views.CcTextView;

/**
 * Created by naveen on 9/1/15.
 */
public class InnerVideoAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<InnerVideos> innerVideoses;
    private ViewHolder viewHolder;

    public InnerVideoAdapter(Context context, ArrayList<InnerVideos> innerVideoses){

        this.mContext=context;
        this.innerVideoses=innerVideoses;
    }




    @Override
    public int getCount() {
        return innerVideoses.size();
    }

    @Override
    public Object getItem(int i) {
        return innerVideoses.get(i);
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
        String restoredText = prefs.getString(innerVideoses.get(i).getVideoid(), null);
        if(restoredText != null){
            Bitmap bitmap= ThumbnailUtils.createVideoThumbnail(restoredText,
                    MediaStore.Video.Thumbnails.MINI_KIND);
            viewHolder.imageView.setImageBitmap(bitmap);
        }

       // viewHolder.imageView.setBackgroundResource(R.drawable.ic_launcher);
        viewHolder.textView.setText(innerVideoses.get(i).getVideoname());
        return view;
    }

    private class ViewHolder {
        ImageView imageView;
        CcTextView textView;

    }
}
