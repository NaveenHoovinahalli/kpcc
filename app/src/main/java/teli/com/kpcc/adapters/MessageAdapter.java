package teli.com.kpcc.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

import teli.com.kpcc.R;
import teli.com.kpcc.Utils.Constants;
import teli.com.kpcc.models.Message;
import teli.com.kpcc.views.CcTextView;

/**
 * Created by madhuri on 14/1/15.
 */
public class MessageAdapter extends BaseAdapter {

    private ArrayList<Message> mMessages = new ArrayList<Message>();
    Context mContext;
    private ViewHolder viewHolder;
    private int lastPosition = -1;
    private String[] months = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
    private String[] colors = {"#F58D86","#99D4B4","#C49A6C","#FFF766","#C2ABD3","#FFC299"};


    public MessageAdapter(Context mContext, ArrayList<Message> partyMessages) {
        this.mContext = mContext;
        this.mMessages = partyMessages;
    }

    @Override
    public int getCount() {
        return mMessages.size();
    }

    @Override
    public Object getItem(int i) {
        return mMessages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null){
            view = View.inflate(mContext, R.layout.mesage_item,null);

            viewHolder = new ViewHolder();
            viewHolder.messageHead = (CcTextView) view.findViewById(R.id.news_head_tv);
//            viewHolder.mMonth = (CcTextView) view.findViewById(R.id.month_tv);
//            viewHolder.mDate = (CcTextView) view.findViewById(R.id.date_tv);
            viewHolder.mDateBox = (RelativeLayout) view.findViewById(R.id.date_box);
            viewHolder.partyworker =(CcTextView)view.findViewById(R.id.partyworker_tv);
            viewHolder.profilePic=(ImageView) view.findViewById(R.id.userpic_update);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.messageHead.setText(mMessages.get(i).getMessageHead());

        String publishDate = mMessages.get(i).getInsertDate();

        if(mMessages.get(i).getPartyWorker()!=null && !mMessages.get(i).getPartyWorker().isEmpty()){
            String pWorker=mMessages.get(i).getPartyWorker();
            String constituency="";
            if(mMessages.get(i).getConstituency()!=null && !mMessages.get(i).getConstituency().isEmpty()){
                constituency="----"+mMessages.get(i).getConstituency();
            }
             String pWorkerAppend=pWorker+"  "+constituency;

            viewHolder.partyworker.setText(pWorkerAppend);
        }
            if(mMessages.get(i).getProfilePic()!= null && !mMessages.get(i).getProfilePic().isEmpty())
        Picasso.with(mContext).load(Constants.BASE_URL+mMessages.get(i).getProfilePic()).
                placeholder(R.drawable.account_default).into(viewHolder.profilePic);


        if (publishDate!=null && !publishDate.isEmpty()) {
//            String[] splitDate = publishDate.split("-");
//            viewHolder.mDate.setText(splitDate[2]);
//            viewHolder.mDateBox.setBackgroundColor(Color.parseColor(colors[getRandomNumber()]));
//
//            viewHolder.mMonth.setText(months[Integer.parseInt(splitDate[1]) - 1]);
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
        private CcTextView messageHead;
        private CcTextView mMonth;
        private CcTextView mDate;
        private RelativeLayout mDateBox;
        private CcTextView partyworker;
        private ImageView profilePic;
    }
}
