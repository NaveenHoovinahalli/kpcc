package teli.com.kpcc.activities;

import android.content.Intent;
import android.os.Bundle;
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
import teli.com.kpcc.Utils.CcRequestQueue;
import teli.com.kpcc.Utils.Constants;
import teli.com.kpcc.adapters.NationalLeaderAdapter;
import teli.com.kpcc.models.NationalLeaderFeed;
import teli.com.kpcc.views.CcTextView;

/**
 * Created by naveen on 8/1/15.
 */
public class NationalLeaders  extends BaseActivity implements AdapterView.OnItemClickListener{

    @InjectView(R.id.nationalleader_list)
    ListView nationalleaders;

    @InjectView(R.id.progress_bar)
    ProgressBar mPbar;

    @InjectView(R.id.cctv_leader_category)
    CcTextView tvLeaderCategory;

    String url=String.format(Constants.URL_NATIONAL_LEADER);

    public static final String LEADER_CATEGORY="leader_category";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nationalleader_list);

        if(getIntent().hasExtra(LEADER_CATEGORY)) {
            if ((getIntent().getStringExtra(LEADER_CATEGORY)).equals("NATIONAL")) {
                tvLeaderCategory.setText("NATIONAL");
                url = String.format(Constants.URL_NATIONAL_LEADER);

            } else if ((getIntent().getStringExtra(LEADER_CATEGORY)).equals("STATE")) {
                tvLeaderCategory.setText("STATE");
                url = String.format(Constants.URL_STATE_LEADER);

            } else if ((getIntent().getStringExtra(LEADER_CATEGORY)).equals("CONSTITUENCY")) {
                tvLeaderCategory.setText("CONSTITUENCY");
                url = String.format(Constants.URL_CONSTITUENCY_LEADER);

            }

            fetchNationalLeaderData();
        }

    }

    private void fetchNationalLeaderData() {

        JsonArrayRequest videoRequest=new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        // Log.d("Videos Json", ""+jsonArray);
                        mPbar.setVisibility(View.GONE);
                        parceJsonValues(jsonArray);
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        CcRequestQueue.getInstance(this).addToRequestQueue(videoRequest);

    }

    private void parceJsonValues(JSONArray jsonArray) {

        Gson gson=new Gson();

        ArrayList<NationalLeaderFeed> nationalLeadersArrayList = gson.fromJson(jsonArray.toString(), new TypeToken<List<NationalLeaderFeed>>() {
        }.getType());

        NationalLeaderAdapter nationalLeaderAdapter=new NationalLeaderAdapter(this,nationalLeadersArrayList);
        nationalleaders.setAdapter(nationalLeaderAdapter);
        nationalleaders.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        NationalLeaderFeed feedObject= (NationalLeaderFeed) adapterView.getItemAtPosition(i);

        Intent intent=new Intent(NationalLeaders.this,NationalLeaderDetails.class);
        intent.putExtra("NLFEEDOBJECT", (android.os.Parcelable) feedObject);
        startActivity(intent);

    }


}
