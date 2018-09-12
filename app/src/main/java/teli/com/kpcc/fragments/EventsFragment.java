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
import teli.com.kpcc.activities.EventDescriptionActivity;
import teli.com.kpcc.adapters.EventAdapter;
import teli.com.kpcc.models.Event;

/**
 * Created by madhuri on 12/1/15.
 */
public class EventsFragment extends Fragment implements AdapterView.OnItemClickListener {

    @InjectView(R.id.pBar)
    ProgressBar mPbar;

    @InjectView(R.id.events_list)
    ListView mEventsList;

    public static EventsFragment newInstance() {
        Log.d(Constants.KPCC_LOG, "Fragment new instance");
        EventsFragment f = new EventsFragment();
        Bundle args = new Bundle();
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events,null);
        ButterKnife.inject(this,view);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fetchEvents();

    }

    private void fetchEvents() {

        if (getActivity() != null) {
          //  String url = String.format(Constants.URL_EVENTS, AndroidUtils.getDeviceImei(getActivity()));
            String url = String.format(Constants.URL_EVENTS, 1000);
            Log.d("EventsFragment","Url" + url);
            JsonArrayRequest request = new JsonArrayRequest(url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray jsonArray) {
                            Log.d("EventsFragment","Reasponse" + jsonArray.toString());

                            mPbar.setVisibility(View.GONE);
                            Gson gson = new Gson();

                            ArrayList<Event> events = gson.fromJson(jsonArray.toString(), new TypeToken<List<Event>>() {
                            }.getType());

                            Log.d("EventsFragment","Reasponse" + events);

                            if(events !=null && !events.isEmpty()){
                                EventAdapter adapter = new EventAdapter(getActivity(),events);
                                mEventsList.setAdapter(adapter);
                                mEventsList.setOnItemClickListener(EventsFragment.this);

                            }
                        }
                    },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    mPbar.setVisibility(View.GONE);
                    Log.d("EventsFragment","Error" + volleyError.toString());
                    Log.d("EventsFragment","Error" + volleyError.networkResponse.statusCode);
                }
            });
            CcRequestQueue.getInstance(getActivity()).addToRequestQueue(request);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(getActivity(),EventDescriptionActivity.class);
        startActivity(intent);
    }
}
