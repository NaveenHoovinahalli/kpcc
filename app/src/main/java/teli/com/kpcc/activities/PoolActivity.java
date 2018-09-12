package teli.com.kpcc.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import teli.com.kpcc.R;
import teli.com.kpcc.Utils.AndroidUtils;
import teli.com.kpcc.Utils.CcRequestQueue;
import teli.com.kpcc.Utils.Constants;
import teli.com.kpcc.adapters.PollListAdapter;
import teli.com.kpcc.models.PollNotParticipatedFeed;



/**
 * Created by naveen on 12/1/15.
 */
public class PoolActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @InjectView(R.id.poll_list_notparticipated)
    ListView pollListNotParticipated;

    @InjectView(R.id.poll_list_participated)
    ListView pollListParticipated;

    @InjectView(R.id.progress_bar)
    ProgressBar pBar;

    public String poll_url_NP,poll_url_P;
    SharedPreferences sharedPreferences;
    public String pollQuestion;

    int poll_count=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll);

        poll_url_NP =String.format(Constants.URL_POLL_NOT_PARTICIPATED, AndroidUtils.getDeviceImei(this));
        poll_url_P=String.format(Constants.URL_POLL_PARTICIPATED, AndroidUtils.getDeviceImei(this));

        if(AndroidUtils.isNetworkOnline(this)) {
            FetchListValues(poll_url_NP, 1);
            FetchListValues(poll_url_P, 2);
        }

    }

    public void FetchListValues(String poll_url, final int listNo) {


        //String poll_url=String.format(Constants.URL_POLL_PARTICIPATED, AndroidUtils.getDeviceImei(this));

        JsonArrayRequest pollRequest=new JsonArrayRequest(poll_url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                     pBar.setVisibility(View.INVISIBLE);
                           parceJsonArray(jsonArray,listNo);

                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        CcRequestQueue.getInstance(this).addToRequestQueue(pollRequest);
    }

    private void parceJsonArray(JSONArray jsonArray,int listNo) {

        Gson gson=new Gson();
        if(listNo==1) {
            Log.d("Poll count","1"+listNo);

            ArrayList<PollNotParticipatedFeed> pollNotParticipated = gson.fromJson(jsonArray.toString(), new TypeToken<List<PollNotParticipatedFeed>>() {
            }.getType());

            poll_count= pollNotParticipated.size();

            sharedPreferences=getSharedPreferences("SharedPollCount", Context.MODE_PRIVATE);
            sharedPreferences.edit().putString("POLL_COUNT", String.valueOf(poll_count)).apply();

            PollListAdapter polllistadapter = new PollListAdapter(this, pollNotParticipated);
            pollListNotParticipated.setAdapter(polllistadapter);
            pollListNotParticipated.setOnItemClickListener(this);
        }else {
            Log.d("Poll count","2"+listNo);

            ArrayList<PollNotParticipatedFeed> pollNotParticipated = gson.fromJson(jsonArray.toString(), new TypeToken<List<PollNotParticipatedFeed>>() {
            }.getType());

            PollListAdapter polllistadapter = new PollListAdapter(this, pollNotParticipated);
            pollListParticipated.setAdapter(polllistadapter);
            pollListParticipated.setOnItemClickListener(this);

        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {



          Intent intent;
        PollNotParticipatedFeed pollNotParticipated;

        switch (adapterView.getId()){

            case R.id.poll_list_notparticipated:
//                if(poll_count>=0) {
//                    poll_count = poll_count - 1;
//
//                    sharedPreferences = getSharedPreferences("SharedPollCount", Context.MODE_PRIVATE);
//                    sharedPreferences.edit().putString("POLL_COUNT", String.valueOf(poll_count)).apply();
//                }

                pollNotParticipated = (PollNotParticipatedFeed) adapterView.getItemAtPosition(i);

               pollQuestion=pollNotParticipated.getPollQuestion();

            intent = new Intent(this, CastVoteActivity.class);
            intent.putExtra(CastVoteActivity.POLLNOTFEED, (android.os.Parcelable) pollNotParticipated);
            intent.putExtra("POLLQUESTION",pollQuestion);

                startActivity(intent);
                finish();
                break;
            case R.id.poll_list_participated:
            pollNotParticipated = (PollNotParticipatedFeed) adapterView.getItemAtPosition(i);
                pollQuestion=pollNotParticipated.getPollQuestion();


                String url=String.format(Constants.URL_POLL_RESULT,pollNotParticipated.getId());
            intent=new Intent(this,PollResultActivity.class);
            intent.putExtra(PollResultActivity.RESULT_URL,url);
            intent.putExtra("already_voted","VOTED");
            intent.putExtra("POLLQUESTION",pollQuestion);
            startActivity(intent);
                break;
        }

        Log.d("POLLQUESTION",""+pollQuestion);
    }
}
