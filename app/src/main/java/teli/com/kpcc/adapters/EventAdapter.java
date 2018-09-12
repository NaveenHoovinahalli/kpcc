package teli.com.kpcc.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import teli.com.kpcc.R;
import teli.com.kpcc.models.Event;
import teli.com.kpcc.views.CcTextView;

/**
 * Created by madhuri on 12/1/15.
 */
public class EventAdapter extends BaseAdapter {

    private final ArrayList<Event> events;
    private Context mContext;
    private ViewHolder viewHolder;
    private int lastPosition = -1;
    private String[] months = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
    private String[] colors = {"#F58D86","#99D4B4","#C49A6C","#FFF766","#C2ABD3","#FFC299"};

    public EventAdapter(Context context , ArrayList<Event> events) {
        this.events = events;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public Object getItem(int i) {
        return events.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null){
            view = View.inflate(mContext, R.layout.event_item ,null);
            viewHolder = new ViewHolder();
            viewHolder.mDate = (CcTextView) view.findViewById(R.id.date_tv);
            viewHolder.mMonth = (CcTextView) view.findViewById(R.id.month_tv);
            viewHolder.mEventHead = (CcTextView) view.findViewById(R.id.event_head);
            viewHolder.mEventDescription = (CcTextView) view.findViewById(R.id.event_description);
            viewHolder.mTime = (CcTextView) view.findViewById(R.id.event_time_tv);
            viewHolder.mDateBox = (RelativeLayout) view.findViewById(R.id.date_box);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.mEventHead.setText(events.get(i).getEventName());
        viewHolder.mEventDescription.setText(events.get(i).getEventLocation());

        String eventDate = events.get(i).getEventDate();

        String[] splitDate = eventDate.split("-");
        viewHolder.mDate.setText(splitDate[2]);

        viewHolder.mMonth.setText(months[Integer.parseInt(splitDate[1]) - 1]);

        if (events.get(i).getEventTime()!=null && !events.get(i).getEventTime().isEmpty()) {
           /* String time = events.get(i).getEventTime();
            String[] splitTime = time.split(":");
            viewHolder.mTime.setText(splitTime[1] + ":" + splitTime[2]);*/

            String time = events.get(i).getEventTime();
            DateFormat f1 = new SimpleDateFormat("hh:mm:ss");
            Date d = null;
            try {
                d = f1.parse(time);
                DateFormat f2 = new SimpleDateFormat("h:mma");
                viewHolder.mTime.setText(f2.format(d).toUpperCase());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            viewHolder.mDateBox.setBackgroundColor(Color.parseColor(colors[getRandomNumber()]));
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
        private RelativeLayout mDateBox;
        private CcTextView mEventHead;
        private CcTextView mEventDescription;
        private CcTextView mTime;
    }
}
