package teli.com.kpcc.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import teli.com.kpcc.R;

/**
 * Created by naveen on 13/1/15.
 */
public class SwipeAdapter extends BaseAdapter {

    ArrayList<SingleRow> list;
    Context context;
    LayoutInflater inflater;
    TextView textView;
    ImageView imageView;
   public static  String[] names={"Home","Leaders","Events","Images","Videos","Poll","Refer","Message","My Account"};
    int[] images={R.drawable.home,R.drawable.leader_t,R.drawable.events_t,R.drawable.images_t,
            R.drawable.videos_t,R.drawable.poll_t,R.drawable.refer_t,R.drawable.message_t, R.drawable.account_default};

    public SwipeAdapter(Context context){
        this.context=context;
        inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        list=new ArrayList<SingleRow>();
        for(int i=0;i<9;i++){

            list.add(new SingleRow(names[i],images[i]));
        }

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View row=inflater.inflate(R.layout.swipe_item,null);

        textView= (TextView) row.findViewById(R.id.tv_swipe);
        imageView= (ImageView) row.findViewById(R.id.iv_swipe);

        SingleRow temp=list.get(i);

        textView.setText(temp.name);
        imageView.setImageResource(temp.image);

        return row;
    }
}

class SingleRow{

    String name;
    int image;

    SingleRow(String name,int image){

        this.image=image;
        this.name=name;
    }

}
