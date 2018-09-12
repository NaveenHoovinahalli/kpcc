package teli.com.kpcc.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import teli.com.kpcc.models.User;

/**
 * Created by madhuri on 29/12/14.
 */
public class CcDataManager {

    private static final String GCM_REGISTER_ID = "gcm_register_id";
    private static final String APP_USER = "app_user";
    private static final String IS_LOGGED_IN = "is_logged_in";
    private static final String CONSTITUENCY = "constituency";
    private static final String DESIGNATION="designation";
    private final Context mContext;
    private final SharedPreferences mSharedPreferences;

    public static CcDataManager init(Context context){
        return  new CcDataManager(context);
    }

    public CcDataManager(Context context) {

        this.mContext = context;
        mSharedPreferences =
                context.getSharedPreferences(Constants.CC_PREFS, Context.MODE_PRIVATE);
    }


    public void saveGcmRegId(String regId) {
        mSharedPreferences.edit().putString(GCM_REGISTER_ID,regId).apply();
    }

    public String getGcmRegId() {
        return mSharedPreferences.getString(GCM_REGISTER_ID, "");
    }

    public void saveUser(String s) {
        mSharedPreferences.edit().putString(APP_USER,s).apply();
    }

    public void saveIsLoggedIn(boolean b) {
        mSharedPreferences.edit().putBoolean(IS_LOGGED_IN,b).apply();
    }

    public boolean getIsLoggedIn() {
        return mSharedPreferences.getBoolean(IS_LOGGED_IN,false);
    }

    public User getAppUser() {
        return new Gson().fromJson(getUserString(),User.class);
    }

    public String getUserString() {
        return mSharedPreferences.getString(APP_USER, "");
    }

    public void saveConstituency(String jsonOutput) {
        mSharedPreferences.edit().putString(CONSTITUENCY,jsonOutput).apply();
    }

    public String getConstituency() {
        return mSharedPreferences.getString(CONSTITUENCY,"");
    }

    public void saveDesignation(String jsonOutput){
        mSharedPreferences.edit().putString(DESIGNATION,jsonOutput).apply();
    }

    public String getDesignation(){
        return mSharedPreferences.getString(DESIGNATION,"");
    }
}
