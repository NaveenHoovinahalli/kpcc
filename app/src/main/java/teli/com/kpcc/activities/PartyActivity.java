package teli.com.kpcc.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nhaarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import teli.com.kpcc.R;
import teli.com.kpcc.Utils.AndroidUtils;
import teli.com.kpcc.Utils.Constants;
import teli.com.kpcc.adapters.MessageAdapter;
import teli.com.kpcc.models.Message;
import teli.com.kpcc.views.CcTextView;

/**
 * Created by madhuri on 9/1/15.
 */
public class PartyActivity extends BaseActivity{

    public static String MESSAGE_TYPE = "messages";
    public String url;
    private String requestType;

    @InjectView(R.id.message_list)
    ListView mMessageList;

    @InjectView(R.id.progress_bar)
    ProgressBar mPbar;

    @InjectView(R.id.feedback_iv)
    ImageView mFeedback;

    @InjectView(R.id.feed_text)
    CcTextView mFeedText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_message_list);


        if(!AndroidUtils.isNetworkOnline(getApplicationContext())){
            Toast.makeText(getApplicationContext(), "Sorry! No Internet Connection", Toast.LENGTH_SHORT).show();
            return;
        }

        if(getIntent().hasExtra(MESSAGE_TYPE)) {
            if ((getIntent().getStringExtra(MESSAGE_TYPE)).equals("party_messages")) {
                mFeedback.setVisibility(View.GONE);
                mFeedText.setVisibility(View.GONE);
                url = Constants.URL_MESSAGES;
                requestType = "get_party_messages";
                new FetchMessages().execute();

            }else  if ((getIntent().getStringExtra(MESSAGE_TYPE)).equals("feedback_messages")) {
                url = Constants.URL_MESSAGES;
                mFeedback.setVisibility(View.VISIBLE);
                mFeedText.setVisibility(View.VISIBLE);
                requestType = "get_feedback_messages";
                new FetchMessages().execute();
            }
        }
    }


    class FetchMessages extends AsyncTask<String, Void, String> implements AdapterView.OnItemClickListener {

        @Override
        protected String doInBackground(String... strings) {

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("request_type",requestType));
                nameValuePairs.add(new BasicNameValuePair("imei_number", AndroidUtils.getDeviceImei(getApplicationContext())));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = (HttpResponse) httpclient.execute(httppost);
                StatusLine status = response.getStatusLine();
                if (status.toString().contains("200 OK")) {
                    Log.d("MainActivity", "LinkLog Success");
                    JSONArray jsonArray = setAdapter(response);
                    return jsonArray.toString();
                } else {
                    // mPbar.setVisibility(View.GONE);
                    Log.d("MainActivity", "LinkLog Failure");
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            mPbar.setVisibility(View.GONE);

            Log.d("PartyActivity","OnpostExecute::" + s);

            Gson gson = new Gson();

            ArrayList<Message> partyMessages = gson.fromJson(s, new TypeToken<List<Message>>() {
            }.getType());

            if (partyMessages!=null && !partyMessages.isEmpty()) {
                MessageAdapter messageAdapter = new MessageAdapter(PartyActivity.this, partyMessages);
                SwingBottomInAnimationAdapter animationAdapter = new
                        SwingBottomInAnimationAdapter(messageAdapter);
                animationAdapter.setAbsListView(mMessageList);
                mMessageList.setAdapter(animationAdapter);
                mMessageList.setOnItemClickListener(this);
            }
            super.onPostExecute(s);

        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            Message item = (Message) adapterView.getItemAtPosition(i);

            Intent intent = new Intent(PartyActivity.this , MessageDescriptionActivity.class);
            intent.putExtra(MessageDescriptionActivity.MESSAGE_DETAILS,item);
            startActivity(intent);
        }
    }

    private JSONArray setAdapter(HttpResponse response) {
        JSONArray jsonArray = new JSONArray();
        HttpEntity entity1 = response.getEntity();
        Log.d("MainActivity", "Before input stream converting:::" + entity1.toString());
        if (entity1!=null) {
            InputStream instream1 = null;
            try {
                instream1 = entity1.getContent();
                Log.d("MainActivity", "Before converting:::" + instream1.toString());
                jsonArray = convertInputStreamToJSONObject(instream1);
                instream1.close();

                if (jsonArray != null) {
                    Log.d("MainActivity", "Response:::" + jsonArray.toString());
                    return jsonArray;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("MainActivity", "Json exception:::" );
            }
        }
        return jsonArray;
    }

    private static JSONArray convertInputStreamToJSONObject(InputStream inputStream)
            throws JSONException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        try {
            while ((line = bufferedReader.readLine()) != null)
                result += line;

            inputStream.close();
        } catch (IOException e) {
            Log.d("MainActivity", "Exception:::" + e.toString());
            e.printStackTrace();
        }
        return new JSONArray(result);
    }

    @OnClick(R.id.feedback_iv)
    public void OnItemClicked(View view){

        Intent intent = new Intent(PartyActivity.this, UserFeedBackActivity.class);
        startActivity(intent);
    }


}
