package teli.com.kpcc.gcm;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import teli.com.kpcc.R;
import teli.com.kpcc.Utils.CcDataManager;
import teli.com.kpcc.Utils.CcRequestQueue;
import teli.com.kpcc.Utils.Constants;
import teli.com.kpcc.Utils.ValidationUtils;
import teli.com.kpcc.models.User;

/**
 * Created by naveen on 15/5/14.
 */
public class GcmRegisterService extends IntentService {

    public static final String ACTION_REGISTER = "action_register";

    public GcmRegisterService() {
        super("GcmRegisterService");
    }

    public static void registerGcm(Context context) {
        Intent intent = new Intent(context, GcmRegisterService.class);
        intent.setAction(ACTION_REGISTER);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (ACTION_REGISTER.equals(intent.getAction())) {
            String regId = getApplicationContext().getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE).getString("GCMREGDID", "");
            Log.d("GcmRegisterService","saved register id" + regId);
            if (ValidationUtils.isTextEmpty(regId)) {
                register();
            }
        }
    }

    private void register() {
        try {
            GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
            String regId = gcm.register(getString(R.string.gcm_server_id));
            Log.d("GcmRegisterService","register id" + regId);
            if (!ValidationUtils.isTextEmpty(regId)) {
                getApplicationContext()
                        .getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE).edit()
                        .putString("GCMREGDID", regId)
                        .apply();
                updateUser(regId);
            }
        } catch (IOException e) {
            Log.d("GcmRegisterService","Exception::"+e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateUser(String regId) {
        try {
            User user = CcDataManager.init(getApplicationContext()).getAppUser();
            if (user == null || ValidationUtils.isTextEmpty(user.getImeiNumber())) {
                return;
            }
            String userUpdateUrl = String.format(Constants.URL_UPDATE_USER, user.getImeiNumber());
            JSONObject params = new JSONObject();
            params.put("push_notification_id", regId);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT,
                    userUpdateUrl,
                    params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            Log.d("GcmRegisterService", "GcmRegisterResponse::" +jsonObject.toString());

                        }
                    },
                    new Response.ErrorListener(){

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("GcmRegisterService", "GcmRegisterError::" + error.toString());

                        }
                    });
            CcRequestQueue.getInstance(getApplicationContext()).addToRequestQueue((request));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
