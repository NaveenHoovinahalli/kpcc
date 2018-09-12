package teli.com.kpcc.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import teli.com.kpcc.R;
import teli.com.kpcc.Utils.AndroidUtils;
import teli.com.kpcc.Utils.CcDataManager;
import teli.com.kpcc.Utils.CcRequestQueue;
import teli.com.kpcc.Utils.Constants;
import teli.com.kpcc.Utils.ValidationUtils;
import teli.com.kpcc.gcm.GcmRegisterService;
import teli.com.kpcc.models.Constituency;
import teli.com.kpcc.models.Designation;
import teli.com.kpcc.views.CcEditText;
import teli.com.kpcc.views.CcTextView;

/**
 * Created by madhuri on 29/12/14.
 */
public class SignUpActivity extends BaseActivity implements View.OnFocusChangeListener{

    @InjectView(R.id.et_name)
    CcEditText mName;

    @InjectView(R.id.et_mobileNo)
    CcEditText mMobileNo;

    @InjectView(R.id.et_dob)
    CcTextView mDob;

    @InjectView(R.id.email_id)
    CcEditText mEmailId;

    @InjectView(R.id.et_constituency)
    CcTextView mConstituency;

    @InjectView(R.id.et_designation)
    CcEditText mDesignation;

    @InjectView(R.id.submit_btn)
    Button mSubmitBtn;

    @InjectView(R.id.user_profile_pic)
    ImageView mProfilePic;

    @InjectView(R.id.sign_up_layout)
    LinearLayout mSignUpLayout;

    @InjectView(R.id.progress_bar)
    ProgressBar mPbar;

    private boolean isDataFetched;
    private boolean isDesignationFetched;
    ArrayList<String> costituenciesName = new ArrayList<String>();
    ArrayList<Designation> designationList=new ArrayList<Designation>();
    ArrayList<String> designationNames=new ArrayList<String>();
    private ArrayAdapter<String> arrayAdapter;
    private Bitmap bmp;
    private boolean showDialog=true;
    String dobReverce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);

        GcmRegisterService.registerGcm(this);
        fetchConstituency();
        fetchDesignation();
        mDesignation.setOnFocusChangeListener(this);
    }

    private void fetchConstituency() {
        mPbar.setVisibility(View.VISIBLE);

        JsonArrayRequest request = new JsonArrayRequest(Constants.URL_CONSTITUENCY,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        isDataFetched = true;
                        Log.d("SignUpActivity","Response::"+jsonArray.toString());

                        if (jsonArray!=null && jsonArray.length()!=0) {
                            String jsonOutput = jsonArray.toString();

                            CcDataManager.init(SignUpActivity.this).saveConstituency(jsonOutput);
                            Type listType = new TypeToken<List<Constituency>>() {
                            }.getType();
                            List<Constituency> constituencies = (List<Constituency>) new Gson().fromJson(jsonOutput, listType);
                            Log.d("SignUpActivity", "constituencies size::" + constituencies.size());

                            saveConstituencies(constituencies);
                            mPbar.setVisibility(View.INVISIBLE);

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("SignUpActivity","Error::"+volleyError.toString());
                        mPbar.setVisibility(View.INVISIBLE);

                    }
                });
        CcRequestQueue.getInstance(this).addToRequestQueue(request);
    }


    private void saveConstituencies(List<Constituency> constituencies) {
        for (Constituency constituency : constituencies) {
            costituenciesName.add(constituency.getConstituencyName());

            Collections.sort(costituenciesName);
        }
    }


    private void fetchDesignation(){

        JsonArrayRequest request = new JsonArrayRequest(Constants.URL_ALL_DESIGNATIONS,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        Log.d("SignUpActivity","Response::Designation"+jsonArray.toString());
                        isDesignationFetched=true;

                        if (jsonArray!=null && jsonArray.length()!=0) {
                            String jsonoutput=jsonArray.toString();
                            CcDataManager.init(SignUpActivity.this).saveConstituency(jsonoutput);


                            parceDesignation(jsonArray);

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("SignUpActivity","Error::"+volleyError.toString());

                    }
                });
        CcRequestQueue.getInstance(this).addToRequestQueue(request);

    }
  public void  parceDesignation(JSONArray jsonArray){
      Gson gson=new Gson();

      designationList = gson.fromJson(jsonArray.toString(), new TypeToken<List<Designation>>() {
      }.getType());

     for(int i=0;i<designationList.size();i++){

         designationNames.add(designationList.get(i).getDesignation());

         Log.d("SignUpActivity", "Designation" + designationList.get(i).getId());
         Log.d("SignUpActivity","Designation" +designationList.get(i).getDesignation());

     }
  }

    @OnClick(R.id.et_dob)
    public void DobClicked(){
        Log.d("SignUpActivity","DOB");
        Calendar calendar = Calendar.getInstance();
        int mMonth = calendar.get(Calendar.MONTH);
        int mYear = ((calendar.get(Calendar.YEAR))-18);
        int mDay = calendar.get(Calendar.DATE);

        Log.d("DOB","DATEPICKER"+mMonth+" -"+mYear+"-"+mDay);



        DatePickerDialog datePickerDialog= new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                Log.d("DATESIGNUP","i"+i+"i2"+i2+"i3"+i3);
                mDob.setText("" + i3 + "-" + (i2 + 1) + "-" + i);
                dobReverce=("" + i + "-" + (i2 + 1) + "-" + i3);
            }
        },mYear,mMonth,mDay);


        datePickerDialog.show();
    }

    @OnClick(R.id.et_constituency)
    public void constituencyClicked(){
        Log.d("SignUpActivity","et_constituency");

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.constituency_pop_up);

       CcEditText searchEt = (CcEditText) dialog.findViewById(R.id.search_et);
       ListView mConstituencyList = (ListView) dialog.findViewById(R.id.constituency_list);
        arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                costituenciesName
        );

        mConstituencyList.setAdapter(arrayAdapter);

        dialog.setCancelable(true);
        dialog.show();

        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
       // mSignUpLayout.setBackgroundColor(Color.parseColor("#BF000000"));


        mConstituencyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = (String) adapterView.getItemAtPosition(i);
               // Toast.makeText(SignUpActivity.this,"you selected " +item,Toast.LENGTH_SHORT).show();
                mConstituency.setText(item);
                dialog.cancel();
            }
        });

        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                SignUpActivity.this.arrayAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @OnClick(R.id.et_designation)
    public void designationClicked(){

        Log.d("SignupActivity","Inside the designation");

        if(showDialog) {
            showDialog=false;


            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.designation_popup);


            ListView mDesignationListview = (ListView) dialog.findViewById(R.id.designation_list);
            arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, designationNames);
            mDesignationListview.setAdapter(arrayAdapter);
            dialog.setCancelable(true);
            dialog.show();

            Window window = dialog.getWindow();
            window.setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);

            mDesignationListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String item = (String) adapterView.getItemAtPosition(i);
                    if (!item.equals("Other")) {
                        mDesignation.setText(item);
                    }
                    dialog.cancel();


                }
            });

        }

    }

    @OnClick(R.id.submit_btn)
    public void onSubmitClicked(){

        if(!AndroidUtils.isNetworkOnline(getApplicationContext())){
            Toast.makeText(getApplicationContext(), "Sorry! No Internet Connection", Toast.LENGTH_SHORT).show();
            return;
        }

        if (validateDetails() != null){
            Toast.makeText(this,validateDetails(),Toast.LENGTH_SHORT).show();
        }else {
            Log.d("SignUpActivity","regId::" +CcDataManager.init(this).getGcmRegId());
            Log.d("SignUpActivity","mMobileNo::" +mMobileNo.getText().toString());
            Log.d("SignUpActivity","mConstituency::" +mConstituency.getText().toString());
            Log.d("SignUpActivity","mDob::" +mDob.getText().toString());
            Log.d("SignUpActivity","setMobileMake::" +AndroidUtils.getDeviceManufacturer());
            Log.d("SignUpActivity","setMobileModel::" +AndroidUtils.getDeviceModel());
            Log.d("SignUpActivity","setImeiNumber::" +AndroidUtils.getDeviceImei(this));
            Log.d("SignUpActivity","setImeiNumber::" +mDesignation.getText().toString());


            new SubmitToServer().execute();
            mSubmitBtn.setEnabled(false);
        }
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if(showDialog) {

            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.designation_popup);


            ListView mDesignationListview = (ListView) dialog.findViewById(R.id.designation_list);
            arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, designationNames);
            mDesignationListview.setAdapter(arrayAdapter);
            dialog.setCancelable(true);
            dialog.show();

            Window window = dialog.getWindow();
            window.setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);

            mDesignationListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String item = (String) adapterView.getItemAtPosition(i);
                    if (!item.equals("Other")) {
                        mDesignation.setText(item);
                    }
                    dialog.cancel();


                }
            });
        }

        showDialog=false;


    }


    class SubmitToServer extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String regId = getApplicationContext().getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE).getString("GCMREGDID", "");

            HttpClient httpclient = new DefaultHttpClient();
            HttpPut httpput = new HttpPut(Constants.URL_CREATE_USER);
            MultipartEntity entity = new MultipartEntity();
            try {
                entity.addPart("name", new StringBody(mName.getText().toString()));
                entity.addPart("mobile_number", new StringBody(mMobileNo.getText().toString()));
                entity.addPart("constituency", new StringBody(mConstituency.getText().toString()));
//                entity.addPart("date_of_birth", new StringBody(mDob.getText().toString()));
                entity.addPart("date_of_birth", new StringBody(dobReverce));
                entity.addPart("push_notification_id", new StringBody(regId));
                entity.addPart("mobile_make", new StringBody(AndroidUtils.getDeviceManufacturer()));
                entity.addPart("mobile_model", new StringBody(AndroidUtils.getDeviceModel()));
                entity.addPart("os_type", new StringBody("android"));
                entity.addPart("imei_number", new StringBody(AndroidUtils.getDeviceImei(SignUpActivity.this)));
                entity.addPart("designation",new StringBody(mDesignation.getText().toString()));
                if(bmp!=null) {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.JPEG, 75, bos);
                    byte[] data = bos.toByteArray();
                    ByteArrayBody bab = new ByteArrayBody(data, "user.jpg");
                    if (bab != null) {
                        entity.addPart("profile_picture", bab);
                    }
                }
                if (mEmailId != null){
                    entity.addPart("email", new StringBody(mEmailId.getText().toString()));
                }

                httpput.setEntity(entity);
            } catch (UnsupportedEncodingException e1) {
                Log.d("SignUpActivity","UnsupportedEncodingException" + e1);
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

//            Log.d("MainActivity", "Response" + response.toString());

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
                        CcDataManager.init(SignUpActivity.this).saveIsLoggedIn(true);
                        CcDataManager.init(SignUpActivity.this).saveUser(jsonObject.toString());

                        return "success";
                    }

                     //return jsonObject.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            }else {

                new Thread()
                {
                    public void run()
                    {
                        SignUpActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(SignUpActivity.this,"Login Failed!",Toast.LENGTH_SHORT).show();
                                mSubmitBtn.setEnabled(true);

                            }
                        }
                        );
                    }
                }.start();

            }
            return "";
        }


        @Override
        protected void onPostExecute(String s) {

            if (getApplicationContext()!=null){

                if(s!= null && !s.isEmpty()) {

                    Toast.makeText(SignUpActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(SignUpActivity.this, HomeActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(i);
                    finish();
                }
            }
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
        else if (mDesignation.getText().toString().isEmpty()) {
            showDialog=true;
            return "Please select Designation";
        }
        else if (!mEmailId.getText().toString().isEmpty()) {
            if (!ValidationUtils.isValidEmail(mEmailId.getText().toString().trim()))
                return "Please enter valid email";

        }
        else if(bmp==null)
            return "Please update the picture";
        return null;
    }

    boolean isEmpty(EditText editText) {
        return editText.getText().toString().isEmpty();
    }

    @OnClick(R.id.user_profile_pic)
    public void onProfilePicClicked(){
        showAlert();

    }
    private void showAlert() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
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

//            bmp=getRoundedShape(bmp);
            mProfilePic.setImageBitmap(bmp);
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
//               bmp=getRoundedShape(bmp);

                mProfilePic.setImageBitmap(bmp);

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


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        bmp = savedInstanceState.getParcelable("BITMAP");
        if (bmp!=null){
            mProfilePic.setImageBitmap(bmp);
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("BITMAP",bmp);
    }
}
