package teli.com.kpcc.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

import butterknife.ButterKnife;
import butterknife.InjectView;
import teli.com.kpcc.R;
import teli.com.kpcc.Utils.AndroidUtils;
import teli.com.kpcc.Utils.CcDataManager;
import teli.com.kpcc.Utils.CcRequestQueue;
import teli.com.kpcc.Utils.Constants;

/**
 * Created by madhuri on 29/12/14.
 */
public class SplashActivity extends Activity {

    Handler handler;
    SharedPreferences sharedPreferences;
    @InjectView(R.id.linearsplash)
    LinearLayout linearLayout;
    @InjectView(R.id.tvsplash)
    TextView tvSplash;
    @InjectView(R.id.retryButton)
    Button retryButton;

    boolean isLoggedIn;

    private Runnable launcher = new Runnable() {
        @Override
        public void run() {
           launch();
        }
    };

    private void launch() {

      //  boolean isLoggedIn = CcDataManager.init(this).getIsLoggedIn();

        if(!AndroidUtils.isNetworkOnline(this)){
            finish();
            return;
        }

        Log.d("SPLASNOTIFICATIONSPLASH","callingmethod");
//        fetchPollCount();

//        if (!isLoggedIn){
        if (isLoggedIn){
            Intent intent = new Intent(SplashActivity.this,SignUpActivity.class);
            startActivity(intent);
            finish();
        }else {
            Intent intent = new Intent(SplashActivity.this,HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        ButterKnife.inject(this);
        handler = new Handler();

        if(!AndroidUtils.isNetworkOnline(this)){
            tvSplash.setVisibility(View.VISIBLE);
            retryButton.setVisibility(View.VISIBLE);
           // Toast.makeText(this, "Sorry! No Internet Connection", Toast.LENGTH_SHORT).show();
            return;
        }
        isLoggedIn = CcDataManager.init(this).getIsLoggedIn();

        if (handler!=null)
           handler.postDelayed(launcher, 3000);



    }


    @Override
    protected void onStop() {
        super.onStop();

        if (handler!=null){
            handler.removeCallbacks(launcher);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if(!AndroidUtils.isNetworkOnline(this)){
//            // Toast.makeText(this, "Sorry! No Internet Connection", Toast.LENGTH_SHORT).show();
//
//        }else if  (handler!=null){
//            if(tvSplash.getVisibility()==View.VISIBLE)
//            handler.postDelayed(launcher, 3000);
//        }

        if(AndroidUtils.isNetworkOnline(this)) {
            fetchPollCount();

        }

    }


    private void fetchPollCount() {
        String poll_url=String.format(Constants.URL_POLL_NOT_PARTICIPATED, AndroidUtils.getDeviceImei(this));
        Log.d("SPLASNOTIFICATIONSPLASH","insidemethod");
        Log.d("SPLASNOTIFICATIONSPLASH"," "+poll_url);

        JsonArrayRequest pollRequest=new JsonArrayRequest(poll_url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                    Log.d("JSONARRAY",""+jsonArray);
                       saveValues(jsonArray);

                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        CcRequestQueue.getInstance(this).addToRequestQueue(pollRequest);

    }

    private void saveValues(JSONArray jsonArray) {

        sharedPreferences=getSharedPreferences("SharedPollCount", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("POLL_COUNT", String.valueOf(jsonArray.length())).apply();



    }

  public void  retry(View view){
      boolean isLoggedIn = CcDataManager.init(this).getIsLoggedIn();

      if(!AndroidUtils.isNetworkOnline(this)){
          return;
      }

      Log.d("SPLASNOTIFICATIONSPLASH","callingmethod");
      fetchPollCount();

      if (!isLoggedIn){
          Intent intent = new Intent(SplashActivity.this,SignUpActivity.class);
          startActivity(intent);
          finish();
      }else {
          Intent intent = new Intent(SplashActivity.this,HomeActivity.class);
          startActivity(intent);
          finish();
      }
  }


}
