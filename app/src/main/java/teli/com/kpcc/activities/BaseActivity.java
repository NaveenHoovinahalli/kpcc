package teli.com.kpcc.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Window;

import butterknife.ButterKnife;

/**
 * Created by madhuri on 29/12/14.
 */
public class BaseActivity extends Activity{
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressDialog = new ProgressDialog(this);
        progressDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setMessage("Please wait...");
    }

    protected void showProgress(){

        if (progressDialog != null && !progressDialog.isShowing()){
            progressDialog.show();
        }
    }

    protected void hideProgress(){

        if (progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.inject(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
