package teli.com.kpcc.activities;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import butterknife.InjectView;
import butterknife.OnClick;
import teli.com.kpcc.R;
import teli.com.kpcc.Utils.AndroidUtils;
import teli.com.kpcc.Utils.CcDataManager;
import teli.com.kpcc.Utils.Constants;
import teli.com.kpcc.Utils.ValidationUtils;
import teli.com.kpcc.fragments.MenuFragment;
import teli.com.kpcc.models.User;
import teli.com.kpcc.views.CcEditText;
import teli.com.kpcc.views.CcTextView;

/**
 * Created by naveen on 5/1/15.
 */
public class AccountActivity extends BaseActivity implements View.OnClickListener {

    @InjectView(R.id.et_name)
    CcEditText mName;

    @InjectView(R.id.et_mobileNo)
    CcEditText mMobileNo;

    @InjectView(R.id.email_id)
    CcEditText mEmailId;

    @InjectView(R.id.user_pic)
    ImageView mUserPic;

    @InjectView(R.id.et_dob)
    CcTextView mDob;

    @InjectView(R.id.et_constituency)
    CcTextView mConstituency;
    @InjectView(R.id.et_designation)
    CcEditText mDesignation;

    private Bitmap bmp;

    @InjectView(R.id.fragmentofmenu)
    FrameLayout frameLayoutMenu;


    public android.app.FragmentManager fm;
    public FragmentTransaction ft;
    android.app.Fragment fragment;
    MenuItem menuItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_account);
//        ActionBar actionBar = getActionBar();
//        actionBar.setHomeButtonEnabled(true);
//
//        actionBar.setLogo(R.drawable.home);
//        actionBar.setDisplayShowTitleEnabled(false);

        initViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar

        SharedPreferences prefs = getSharedPreferences("SharedPollCount",MODE_PRIVATE);
        String pollCount= prefs.getString("POLL_COUNT","0");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_action_bar, menu);
        Log.d("POLL_COUNT","countin home "+pollCount);

        View count=menu.findItem(R.id.poll_notification).getActionView();
        TextView countText= (TextView) count.findViewById(R.id.tvCount);
        ImageView pollImage=(ImageView) count.findViewById(R.id.notification_main);
        pollImage.setOnClickListener(this);
        countText.setOnClickListener(this);
        LinearLayout ll=(LinearLayout) count.findViewById(R.id.llpolltv);
        if(pollCount.equals("0") || pollCount.isEmpty() )
        {
            ll.setVisibility(View.INVISIBLE);
        }else {
            ll.setVisibility(View.VISIBLE);

            countText.setText(pollCount);
        }
        return true;
        // return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.d("Menu","onOptionsMenuClick");

        Intent intent;
        switch (item.getItemId()){
            case R.id.poll_notification:
                intent=new Intent(AccountActivity.this,PoolActivity.class);
                startActivity(intent);
                return true;
            case R.id.feedsubmit:
                intent=new Intent(AccountActivity.this,UserFeedBackActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_settings:
                menuItem=item;
                Log.d("Menu","onSettingClicked");
                if(frameLayoutMenu.getVisibility() == View.VISIBLE){
                    frameLayoutMenu.setVisibility(View.INVISIBLE);
                    item.setIcon(R.drawable.menu_settings);
                }else {
                    item.setIcon(R.drawable.right_arrow_white);
                    frameLayoutMenu.setVisibility(View.VISIBLE);
                    fm = getFragmentManager();
                    ft = fm.beginTransaction();
                    fragment = new MenuFragment();
                    ft.replace(R.id.fragmentofmenu, fragment);
                    ft.commit();

                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void initViews() {
        User user = CcDataManager.init(this).getAppUser();
        if(user!=null) {

            if (user.getName() != null && !user.getName().isEmpty()) {
                mName.setText(user.getName());
            }

            if (user.getConstituency() != null && !user.getConstituency().isEmpty()) {
                mConstituency.setText(user.getConstituency());
            }

            if (user.getDob() != null && !user.getDob().isEmpty()) {
                mDob.setText(user.getDob());
            } else {
                mDob.setText("");
            }
            if (!user.getEmail().isEmpty()) {
                mEmailId.setText(user.getEmail());
            }

            if (user.getMobileNo() != null && !user.getMobileNo().isEmpty()) {
                mMobileNo.setText(user.getMobileNo());
            }
            if(user.getDesignation()!=null && !user.getDesignation().isEmpty())
                mDesignation.setText(user.getDesignation());

            if (user.getProfilePicture()!=null && !user.getProfilePicture().isEmpty()){

//                Picasso.with(getApplicationContext())
//                        .load(Constants.BASE_URL + user.getProfilePicture())
//                        .transform(new RoundedTransformation(150, 0))
//                        .fit()
//                        .into(mUserPic);
                Picasso.with(getApplicationContext())
                        .load(Constants.BASE_URL + user.getProfilePicture())
                        .into(mUserPic);
                Log.d("Account","Profile pic"+ user.getProfilePicture());
            }
        }

    }

    @OnClick(R.id.user_pic)
    public void ProfilePicClicked(){
        showAlert();
    }

    private void showAlert() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(AccountActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 1);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }

            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 1) {
            Bundle bundle = data.getExtras();
            bmp = (Bitmap) bundle.get("data");
            bmp = getResizedBitmap(bmp, 150, 150);

//            bmp = getRoundedShape(bmp);
            mUserPic.setImageBitmap(bmp);
        }
        if (requestCode == 2 && resultCode == RESULT_OK) {

            InputStream stream = null;
            try {
                if (bmp != null) {
                    bmp.recycle();
                }
                stream = getContentResolver().openInputStream(data.getData());
                try {
                    bmp = BitmapFactory.decodeStream(stream);
                    bmp = getResizedBitmap(bmp, 150, 150);
                }catch (OutOfMemoryError e){

                }
//                bmp=getRoundedShape(bmp);
                mUserPic.setImageBitmap(bmp);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }



    public static Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
        int targetWidth = 150;
        int targetHeight = 150;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                targetHeight,Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2,
                ((float) targetHeight - 1) / 2,
                (Math.min(((float) targetWidth),
                        ((float) targetHeight)) / 2),
                Path.Direction.CCW);

        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap,
                new Rect(0, 0, sourceBitmap.getWidth(),
                        sourceBitmap.getHeight()),
                new Rect(0, 0, targetWidth,
                        targetHeight), null);
        return targetBitmap;
    }


    public Bitmap getResizedBitmap(Bitmap image, int bitmapWidth, int bitmapHeight) {
        return Bitmap.createScaledBitmap(image, bitmapWidth, bitmapHeight,
                true);
    }

    @OnClick(R.id.done_btn)
    public void OnUpdateClicked(){

        if(!AndroidUtils.isNetworkOnline(getApplicationContext())){
            Toast.makeText(getApplicationContext(), "Sorry! No Internet Connection", Toast.LENGTH_SHORT).show();
            return;
        }

        if (validateDetails() != null){
            Toast.makeText(this,validateDetails(),Toast.LENGTH_SHORT).show();
        }else {
            new SubmitToServer().execute();
            Toast.makeText(AccountActivity.this,"Updated",Toast.LENGTH_SHORT).show();

        }

    }

    private String validateDetails() {
        if (isEmpty(mName))
            return "Please enter name.";
        else if (mDob.getText().toString().isEmpty())
            return "Please enter Date of birth.";
        else if (isEmpty(mMobileNo))
            return "Please enter mobile number.";
        else if (mMobileNo.getText().toString().length() < 10)
            return "Invalid phone number";
        else if (mConstituency.getText().toString().isEmpty())
            return "Please select constituency.";
        else if (!mEmailId.getText().toString().isEmpty()) {
            if (!ValidationUtils.isValidEmail(mEmailId.getText().toString().trim()))
                return "Please enter valid email";
        }
        return null;
    }

    boolean isEmpty(EditText editText) {
        return editText.getText().toString().isEmpty();
    }

    @Override
    public void onClick(View view) {
        Intent intent=new Intent(AccountActivity.this,PoolActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }


    class SubmitToServer extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String regId = getApplicationContext().getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE).getString("GCMREGDID", "");

            HttpClient httpclient = new DefaultHttpClient();
            HttpPut httpput = new HttpPut(Constants.URL_UPDATE_USER);
            MultipartEntity entity = new MultipartEntity();
            try {
                entity.addPart("name", new StringBody(mName.getText().toString()));
                entity.addPart("mobile_number", new StringBody(mMobileNo.getText().toString()));
                entity.addPart("constituency", new StringBody(mConstituency.getText().toString()));
                entity.addPart("date_of_birth", new StringBody(mDob.getText().toString()));
                entity.addPart("push_notification_id", new StringBody(regId));
                entity.addPart("mobile_make", new StringBody(AndroidUtils.getDeviceManufacturer()));
                entity.addPart("mobile_model", new StringBody(AndroidUtils.getDeviceModel()));
                entity.addPart("os_type", new StringBody("android"));
                entity.addPart("imei_number", new StringBody(AndroidUtils.getDeviceImei(AccountActivity.this)));

                if(bmp!=null) {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.JPEG, 75, bos);
                    byte[] data = bos.toByteArray();
                    ByteArrayBody bab = new ByteArrayBody(data, "forest.jpg");
                    if (bab != null) {
                        entity.addPart("profile_picture", bab);
                    }
                }
                if (mEmailId != null){
                    entity.addPart("email", new StringBody(mEmailId.getText().toString()));
                }

                httpput.setEntity(entity);
            } catch (UnsupportedEncodingException e1) {
                Log.d("SignUpActivity", "UnsupportedEncodingException" + e1);
                e1.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
                Log.d("SignUpActivity","NullPointerException" + e);
            }
//                httpput.setEntity(entity);
            HttpResponse response = null;
            try {
                response = (HttpResponse) httpclient.execute(httpput);
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("SignUpActivity","IOException" + e);
            }

            Log.d("MainActivity", "Response" + response.toString());

            if (response!=null) {
                StatusLine status = response.getStatusLine();
                Log.d("MainActivity", "response" + status.toString());

                HttpEntity entity1 = response.getEntity();

                if (entity1!=null) {
                    InputStream instream1 = null;
                    try {
                        instream1 = entity1.getContent();
                        JSONObject jsonObject = convertInputStreamToJSONObject(instream1);
                        instream1.close();

                        if (jsonObject != null) {
                            Log.d("MainActivity", "Response:::" + jsonObject.toString());
                          //  CcDataManager.init(AccountActivity.this).saveIsLoggedIn(true);
                            CcDataManager.init(AccountActivity.this).saveUser(jsonObject.toString());

                        }

                        // return jsonObject.toString();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }else {
                Toast.makeText(AccountActivity.this, "Update Failed!", Toast.LENGTH_SHORT).show();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {

//            if (getApplicationContext()!=null){
//               // Toast.makeText(AccountActivity.this,"Login Success",Toast.LENGTH_SHORT).show();
//                Intent i = new Intent(AccountActivity.this,HomeActivity.class);
//                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                getApplicationContext().startActivity(i);
//                finish();
//            }
        }
    }

    private static JSONObject convertInputStreamToJSONObject(InputStream inputStream)
            throws JSONException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        try {
            while ((line = bufferedReader.readLine()) != null)
                result += line;

            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new JSONObject(result);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        bmp = savedInstanceState.getParcelable("BITMAP");
        if (bmp!=null){
            mUserPic.setImageBitmap(bmp);
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("BITMAP",bmp);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(frameLayoutMenu.getVisibility()==View.VISIBLE) {
            frameLayoutMenu.setVisibility(View.INVISIBLE);
            menuItem.setIcon(R.drawable.menu_settings);
        }
    }
}
