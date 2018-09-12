package teli.com.kpcc.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import butterknife.ButterKnife;
import butterknife.InjectView;
import teli.com.kpcc.R;
import teli.com.kpcc.Utils.CcRequestQueue;
import teli.com.kpcc.Utils.Constants;
import teli.com.kpcc.activities.NewsDescriptionActivity;
import teli.com.kpcc.adapters.FeedAdapter;
import teli.com.kpcc.models.Feed;

/**
 * Created by madhuri on 2/1/15.
 */
public class HomeFeedFragment extends Fragment implements AdapterView.OnItemClickListener{

    @InjectView(R.id.feed_list)
    ListView mFeedList;

    @InjectView(R.id.pbar)
    ProgressBar mPbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_feed_list,null);
        ButterKnife.inject(this,view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        fetchFeeds();
    }

    private void fetchFeeds() {

        JsonArrayRequest request = new JsonArrayRequest(Constants.URL_FEED,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        Log.d(Constants.KPCC_LOG,"Response::" + jsonArray.toString());
                        mPbar.setVisibility(View.GONE);
                        Gson gson = new Gson();
                        ArrayList<Feed> feeds = gson.fromJson(jsonArray.toString(), new TypeToken<List<Feed>>() {
                        }.getType());

                        FeedAdapter adapter = new FeedAdapter(getActivity(),feeds);
                        /*SwingBottomInAnimationAdapter animationAdapter = new
                                SwingBottomInAnimationAdapter(adapter);
                        animationAdapter.setAbsListView(mFeedList);*/
                        mFeedList.setAdapter(adapter);
                        mFeedList.setOnItemClickListener(HomeFeedFragment.this);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        mPbar.setVisibility(View.GONE);
                        Log.d(Constants.KPCC_LOG,"ERROR::" + volleyError.toString());
                    }
                });
        CcRequestQueue.getInstance(getActivity()).addToRequestQueue(request);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Feed item = (Feed) adapterView.getItemAtPosition(i);
        Intent intent = new Intent(getActivity(), NewsDescriptionActivity.class);
        intent.putExtra(NewsDescriptionActivity.NEWS_DETAILS,item);
        startActivity(intent);
    }
}

