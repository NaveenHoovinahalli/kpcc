package teli.com.kpcc.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import teli.com.kpcc.R;
import teli.com.kpcc.Utils.Constants;
import teli.com.kpcc.models.OuterImage;
import teli.com.kpcc.views.CcTextView;

/**
 * Created by madhuri on 6/1/15.
 */
public class ImagesAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<OuterImage> outerImages;
    private ViewHolder viewHolder;

    public ImagesAdapter(Context context, ArrayList<OuterImage> outerImages) {
        this.mContext = context;
        this.outerImages = outerImages;
    }

    @Override
    public int getCount() {
        return outerImages.size();
    }

    @Override
    public Object getItem(int i) {
        return outerImages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view==null){
            view = View.inflate(mContext,R.layout.image_item,null);

            viewHolder = new ViewHolder();
            viewHolder.mGridImage = (ImageView) view.findViewById(R.id.grid_image);
            viewHolder.mLeaderText = (CcTextView) view.findViewById(R.id.image_text);

            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }

        if (outerImages.get(i).getImages().size()!=0)
            Picasso.with(mContext).load(Constants.BASE_URL+outerImages.get(i).getImages().get(0).getImageFile()).
                    placeholder(R.drawable.appicon).into(viewHolder.mGridImage);

       // Log.d("ImageAdapter","image link" + Constants.BASE_URL + outerImages.get(i).getImages().get(0).getImageFile());
        viewHolder.mLeaderText.setText(outerImages.get(i).getMainHead());

        return view;
    }

    class ViewHolder{
        private ImageView mGridImage;
        private CcTextView mLeaderText;
    }
}
