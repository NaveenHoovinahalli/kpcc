package teli.com.kpcc.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Random;

import teli.com.kpcc.R;
import teli.com.kpcc.models.Feed;
import teli.com.kpcc.views.CcTextView;

/**
 * Created by madhuri on 2/1/15.
 */
public class FeedAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Feed> feeds = new ArrayList<Feed>();
    private ViewHolder viewHolder;
    private String[] months = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
    private String[] colors = {"#F58D86","#99D4B4","#C49A6C","#FFF766","#C2ABD3","#FFC299"};
    private int lastPosition = -1;
    public FeedAdapter(Context context, ArrayList<Feed> feeds) {
        this.mContext = context;
        this.feeds = feeds;
    }

    @Override
    public int getCount() {
        return feeds.size();
    }

    @Override
    public Object getItem(int i) {
        return feeds.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null){
            view = View.inflate(mContext,R.layout.feed_item,null);
            viewHolder = new ViewHolder();
            viewHolder.mDate = (CcTextView) view.findViewById(R.id.date_tv);
            viewHolder.mMonth = (CcTextView) view.findViewById(R.id.month_tv);
            viewHolder.mFeedHead = (CcTextView) view.findViewById(R.id.news_head_tv);
            viewHolder.mDateBox = (RelativeLayout) view.findViewById(R.id.date_box);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.mFeedHead.setText(feeds.get(i).getNewsHead());

        String publishDate = feeds.get(i).getPublishDate();
        Log.d("FeedAdapter","publishdate"+publishDate);

        if (publishDate!=null && !publishDate.isEmpty()) {
            String[] splitDate = publishDate.split("-");

            if (splitDate!=null && splitDate.length!=0) {
               viewHolder.mDate.setText(splitDate[2]);
                viewHolder.mMonth.setText(months[Integer.parseInt(splitDate[1]) - 1]);
                viewHolder.mDateBox.setBackgroundColor(Color.parseColor(colors[getRandomNumber()]));
            }
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (i > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        view.startAnimation(animation);
        lastPosition = i;
        return view;
    }

    private int getRandomNumber() {
        Random foo = new Random();
        int randomNumber = foo.nextInt(colors.length - 0);
        if(randomNumber == 0) {
            // Since the random number is between the min and max values, simply add 1
            return 0 + 1;
        }
        else {
            return randomNumber;
        }
    }

    class ViewHolder{
        private CcTextView mDate;
        private CcTextView mMonth;
        private CcTextView mFeedHead;
        private RelativeLayout mDateBox;
    }
}
