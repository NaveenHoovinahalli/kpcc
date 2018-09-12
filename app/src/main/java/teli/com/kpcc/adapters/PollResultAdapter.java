package teli.com.kpcc.adapters;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import teli.com.kpcc.R;
import teli.com.kpcc.models.VoteResult;

/**
 * Created by naveen on 14/1/15.
 */
public class PollResultAdapter extends BaseAdapter {

    Context context;
    ArrayList<VoteResult> voteResults;
    TextView choice,count,textandimage;
    int totalCount=0;
    public PollResultAdapter(Context context,ArrayList<VoteResult> voteResults){
        this.context=context;
        this.voteResults=voteResults;
       for(int i=0;i<voteResults.size();i++){
           totalCount= totalCount+Integer.parseInt(voteResults.get(i).getVoteCount());
       }
        Log.d("%%%",""+totalCount );

    }

    @Override
    public int getCount() {
        return voteResults.size();
    }

    @Override
    public Object getItem(int i) {
        return voteResults.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View row=View.inflate(context, R.layout.pollresultitem,null);

       choice= (TextView) row.findViewById(R.id.tvPollResultItemChoice);
        count= (TextView) row.findViewById(R.id.tvPollResultItemCount);
        textandimage=(TextView) row.findViewById(R.id.tvwithno);

        choice.setText(voteResults.get(i).getChoice());
//        count.setText(voteResults.get(i).getVoteCount());
        textandimage.setText((i+1)+")");



        float correct = Float.parseFloat(voteResults.get(i).getVoteCount());
        float questionNum = totalCount;
        try {

            int percent = (int) ((correct * 100.0f) / questionNum);
            count.setText(percent+"%");

            Log.d("%%%",""+ percent);

        }catch (Exception e) {
            Log.d("%%%", "" + e);
            count.setText("%");
        }


        return  row;
    }
}
