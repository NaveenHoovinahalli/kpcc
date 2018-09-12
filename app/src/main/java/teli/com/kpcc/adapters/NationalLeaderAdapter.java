package teli.com.kpcc.adapters;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import teli.com.kpcc.R;
import teli.com.kpcc.Utils.Constants;
import teli.com.kpcc.models.NationalLeaderFeed;
import teli.com.kpcc.views.CcTextView;

/**
 * Created by naveen on 9/1/15.
 */
public class NationalLeaderAdapter extends BaseAdapter {

    Context context;
    ArrayList<NationalLeaderFeed> nationalLeaders;
    ViewHolder viewHolder;
    String imageUrl;
    private int lastPosition=-1;

    public NationalLeaderAdapter(Context context, ArrayList<NationalLeaderFeed> nationalLeaders){

        this.context=context;
        this.nationalLeaders=nationalLeaders;
    }


    @Override
    public int getCount() {
        return nationalLeaders.size();
    }

    @Override
    public Object getItem(int i) {
        return nationalLeaders.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            view=View.inflate(context, R.layout.nationalleader_item,null);
            viewHolder=new ViewHolder();

            viewHolder.imageView=(ImageView) view.findViewById(R.id.iv_national_item);
            viewHolder.textView=(CcTextView)  view.findViewById(R.id.cctv_national_item);
            viewHolder.textViewdesc=(CcTextView) view.findViewById(R.id.cctv_national_item_description);
            view.setTag(viewHolder);
        }else {
            viewHolder=(ViewHolder) view.getTag();
        }

        imageUrl= nationalLeaders.get(i).getLeaderThumbnailImage();
        Log.d("ImageValue",""+imageUrl);
        if(imageUrl == null || imageUrl.isEmpty()) {
            viewHolder.imageView.setBackgroundResource(R.drawable.appicon);
        }else {
            Picasso.with(context).load(Constants.BASE_URL+nationalLeaders.get(i).getLeaderThumbnailImage()).
                  placeholder(R.drawable.appicon).into(viewHolder.imageView);
        }

        viewHolder.textView.setText(nationalLeaders.get(i).getLeaderName());
        viewHolder.textViewdesc.setText(nationalLeaders.get(i).getDesignation());

        Animation animation = AnimationUtils.loadAnimation(context, (i > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        view.startAnimation(animation);
        lastPosition = i;
        return view;
    }

    private class ViewHolder {
        ImageView imageView;
        CcTextView textView;
        CcTextView textViewdesc;

    }
}