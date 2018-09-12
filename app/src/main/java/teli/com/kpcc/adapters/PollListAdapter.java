package teli.com.kpcc.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.InjectView;
import teli.com.kpcc.R;
import teli.com.kpcc.models.PollNotParticipatedFeed;
import teli.com.kpcc.views.CcTextView;

/**
 * Created by naveen on 12/1/15.
 */
public class PollListAdapter extends BaseAdapter {

    Context context;
    ArrayList<PollNotParticipatedFeed> pollNotParticipated;
    ViewHolder viewHolder;

    @InjectView(R.id.tv_poll_question)
    TextView tvPollQuetion;
    private int lastPosition=-1;

    public PollListAdapter(Context context, ArrayList<PollNotParticipatedFeed> pollNotParticipated){
        this.context=context;
        this.pollNotParticipated=pollNotParticipated;
    }

    @Override
    public int getCount() {
        return pollNotParticipated.size();
      //  return pollNotParticipated.size();
    }

    @Override
    public Object getItem(int i) {
        return pollNotParticipated.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view == null){
            view=View.inflate(context, R.layout.poll_item_textview,null);
            viewHolder=new ViewHolder();

            viewHolder.textView=(CcTextView)  view.findViewById(R.id.poll_simple_tv);
            view.setTag(viewHolder);
        }else {
            viewHolder=(ViewHolder) view.getTag();
        }
        viewHolder.textView.setText(pollNotParticipated.get(i).getPollQuestion().toString());
        Animation animation = AnimationUtils.loadAnimation(context, (i > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        view.startAnimation(animation);
        lastPosition = i;
        return view;
    }

    private class ViewHolder {
        CcTextView textView;

    }
}
