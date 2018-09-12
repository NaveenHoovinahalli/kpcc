package teli.com.kpcc.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

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
import teli.com.kpcc.Utils.CcRequestQueue;
import teli.com.kpcc.adapters.PollResultAdapter;
import teli.com.kpcc.models.VoteResult;
import teli.com.kpcc.views.CcTextView;

/**
 * Created by naveen on 14/1/15.
 */
public class PollResultActivity extends BaseActivity {

    @InjectView(R.id.poll_tv_header)
    CcTextView pollMainHeader;

    @InjectView(R.id.list_poll_result)
    ListView pollResultList;

    @InjectView(R.id.tvpollquestiontodisplay)
    TextView pollQuestion;

    Boolean newPoll=false;

    static final String RESULT_URL = "result_url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pollresult);

        if(getIntent().hasExtra("already_voted")) {
            pollMainHeader.setText("YOU HAVE ALREADY VOTED");
           pollQuestion.setText(getIntent().getStringExtra("POLLQUESTION"));
            newPoll=true;
        }

        if(getIntent().hasExtra(RESULT_URL)){
            fetchResultValues(getIntent().getStringExtra(RESULT_URL));
            pollQuestion.setText(getIntent().getStringExtra("POLLQUESTION"));
        }
    }

    private void fetchResultValues(String url) {

        Log.d("Result URL",""+url);

        JsonArrayRequest resultPoll=new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        parcePollResult(jsonArray);
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("PollError",""+error);
            }
        });

        CcRequestQueue.getInstance(this).addToRequestQueue(resultPoll);
    }

    private void parcePollResult(JSONArray jsonArray) {


        Log.d("PollREsult", "" + jsonArray);

        Gson gson=new Gson();

        ArrayList<VoteResult> voteResults = gson.fromJson(jsonArray.toString(), new TypeToken<List<VoteResult>>() {
        }.getType());



        pollResultList.setAdapter(new PollResultAdapter(this,voteResults));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(!newPoll){
            Intent intent=new Intent(this,PoolActivity.class);
            startActivity(intent);

        }

    }
}
