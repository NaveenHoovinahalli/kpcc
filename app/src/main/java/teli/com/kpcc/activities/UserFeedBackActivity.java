package teli.com.kpcc.activities;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import teli.com.kpcc.R;
import teli.com.kpcc.Utils.AndroidUtils;
import teli.com.kpcc.Utils.Constants;
import teli.com.kpcc.fragments.MenuFragment;
import teli.com.kpcc.models.User;
import teli.com.kpcc.views.CcEditText;

/**
 * Created by madhuri on 15/1/15.
 */
public class UserFeedBackActivity extends BaseActivity implements View.OnClickListener {

    @InjectView(R.id.user_feedback)
    CcEditText mUserFeedBack;

    @InjectView(R.id.attachment)
    ImageView mAttachment;

    @InjectView(R.id.subject)
    CcEditText mSubject;

    @InjectView(R.id.fbivone)
    ImageView imageOne;

    @InjectView(R.id.fbivtwo)
    ImageView imagetwo;

    @InjectView(R.id.fbivthree)
    ImageView imagethree;

    @InjectView(R.id.fbivfour)
    ImageView imagefour;

    @InjectView(R.id.fbivfive)
    ImageView imagefive;

    @InjectView(R.id.llupdatedImages)
    LinearLayout linearLayoutImages;

    @InjectView(R.id.fragmentofmenu)
    FrameLayout frameLayoutMenu;

    private ActionBar actionBar;
    private User mUser;
    ArrayList<String> imageIds = new ArrayList<String>();
    ArrayList<String> videoIds = new ArrayList<String>();
    public String SelectedPath = "";
    ProgressDialog progressDialog;
    ArrayList<Bitmap> imagestoShow = new ArrayList<Bitmap>();
    ArrayList<Bitmap> multipleImages=new ArrayList<Bitmap>();

    public android.app.FragmentManager fm;
    public FragmentTransaction ft;
    android.app.Fragment fragment;
    MenuItem menuItem;
    Boolean submitbuttonclicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_feedback);
        progressDialog = new ProgressDialog(UserFeedBackActivity.this);
        progressDialog.setTitle("Please wait...");
        progressDialog.setCancelable(false);

//        actionBar = getActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setDisplayShowTitleEnabled(false);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar

        SharedPreferences prefs = getSharedPreferences("SharedPollCount", MODE_PRIVATE);
        String pollCount = prefs.getString("POLL_COUNT", "0");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_action_bar, menu);
        MenuItem submit = menu.findItem(R.id.feedsubmit).setVisible(false);

        View count = menu.findItem(R.id.poll_notification).getActionView();
        TextView countText = (TextView) count.findViewById(R.id.tvCount);
        ImageView pollImage = (ImageView) count.findViewById(R.id.notification_main);
        pollImage.setOnClickListener(this);
        countText.setOnClickListener(this);
        LinearLayout ll = (LinearLayout) count.findViewById(R.id.llpolltv);
        if (pollCount.equals("0") || pollCount.isEmpty()) {
            ll.setVisibility(View.INVISIBLE);
        } else {
            ll.setVisibility(View.VISIBLE);

            countText.setText(pollCount);
        }
        return true;
        // return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.d("Menu", "onOptionsMenuClick");

        Intent intent;
        switch (item.getItemId()) {
            case R.id.poll_notification:
                intent = new Intent(UserFeedBackActivity.this, PoolActivity.class);
                startActivity(intent);
                return true;
            case R.id.feedsubmit:
                intent = new Intent(UserFeedBackActivity.this, UserFeedBackActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_settings:
                menuItem = item;
                Log.d("Menu", "onSettingClicked");
                if (frameLayoutMenu.getVisibility() == View.VISIBLE) {
                    frameLayoutMenu.setVisibility(View.INVISIBLE);
                    item.setIcon(R.drawable.menu_settings);
                } else {
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

    @OnClick(R.id.attachment)
    public void OnAttachmentClicked(View view) {
        showAlert();
    }

    @OnClick(R.id.submit_btn)
    public void OnSubmitClicked(View view) {
        Log.d("UserFeedbackActivity", "Submit clicked before");
        if (!AndroidUtils.isNetworkOnline(this)) {
            Toast.makeText(this, "Sorry! No Internet Connection", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mSubject.getText().toString() == null || mSubject.getText().toString().isEmpty()) {
            Log.d("UserFeedbackActivity", "Submit clicked");
            Toast.makeText(this, "Please enter subject.", Toast.LENGTH_SHORT).show();
        } else if (mUserFeedBack.getText().toString() == null || mUserFeedBack.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter your feedback.", Toast.LENGTH_SHORT).show();
        } else {
            // submitFeedBack();
            progressDialog.show();
            submitbuttonclicked = true;
            new UploadToServer("feedback", SelectedPath,null).execute();
        }
    }

    private void showAlert() {

        CharSequence options[]=new  CharSequence[]{"Images","Videos"};

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Pick an option");
        builder.setItems(options,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i==0){
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    intent.putExtra("MULTIPLE", "images");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent,"Select Picture"), 1);
//                    intent.setType("image/* video/*");


                }else if(i==1){
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("video/*");
                    startActivityForResult(intent, 2);

                }

            }
        });
        builder.show();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("MULTIPLE IMAGES","Inside onActivityResult");

        if (resultCode == RESULT_OK && requestCode == 1 && data.toString().contains("has clip")) {
            Log.d("MULTIPLE IMAGES","Inside multiple images clips");
            ClipData clipData=data.getClipData();
            if(clipData!=null){
                for(int i=0;i<clipData.getItemCount();i++){
                    Bitmap bmp = null;
                    ClipData.Item item=clipData.getItemAt(i);
                    Uri uri=item.getUri();
                    try {

                        Log.d("UserFeedBack", "MImages bmp::" + bmp);


                        bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                        imagestoShow.add(bmp);

                        Log.d("MULTIPLE IMAGES","Inside the no of selection");

                        if (bmp.getHeight() > 860 || bmp.getWidth()>640) {
                            if(bmp.getWidth()>640){
                                bmp=getResizedBitmap(bmp,540,540);
                            }
                            bmp = getResizedBitmap(bmp, 540, 860);
                        }
                        Log.d("UserFeedBack", "MImages bmp Height::" + bmp.getHeight());
                        multipleImages.add(bmp);
                        ShowImagesOnView();

                        if (bmp != null) {
                            bmp=null;
                            System.gc();
                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                uploadImages(multipleImages);

            }

        }


        if (resultCode == RESULT_OK && requestCode == 1 && data.toString().contains("image")) {
            Log.d("MULTIPLE IMAGES","Inside the Image");
            Bitmap bmp = null;


            InputStream stream = null;
            try {

                stream = getContentResolver().openInputStream(data.getData());
                try {
                    bmp = BitmapFactory.decodeStream(stream);
                    imagestoShow.add(bmp);
                    if (bmp.getHeight() > 860) {
                        bmp = getResizedBitmap(bmp, 540, 860);
                    }


                } catch (OutOfMemoryError e) {

                }

                // mUserPic.setImageBitmap(bmp);

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
            ShowImagesOnView();
            uploadImage(bmp);
            //mUserPic.setImageBitmap(bmp);
            if (bmp != null) {
                bmp=null;
                System.gc();
            }

        }
        if (requestCode == 2 && resultCode == RESULT_OK && data.toString().contains("video")) {

            Uri selectedUri = data.getData();
            SelectedPath = getPath(selectedUri);
            Log.d("SelectedPath", "Path" + SelectedPath);
            Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(SelectedPath,
                    MediaStore.Video.Thumbnails.MINI_KIND);
            try {
                File file = new File(SelectedPath);
                long length = file.length();
                length = length / 1024;
                Log.d("USERFEEDBACK", "VideoSize" + length);

                if (length < 10204) {
                    progressDialog.show();
                    imagestoShow.add(bitmap);
                    ShowImagesOnView();
                    new UploadToServer("video", SelectedPath,null).execute();
                } else {
                    Toast.makeText(this, "Video size should be less than 10MB", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {

            }


            // doFileUpload();
        }
    }

    private void ShowImagesOnView() {


        if (imagestoShow.size() == 1) {
            imageOne.setVisibility(View.VISIBLE);
            imageOne.setImageBitmap(imagestoShow.get(0));
        } else if (imagestoShow.size() == 2) {
            imagetwo.setVisibility(View.VISIBLE);
            imagetwo.setImageBitmap(imagestoShow.get(1));
        } else if (imagestoShow.size() == 3) {
            imagethree.setVisibility(View.VISIBLE);
            imagethree.setImageBitmap(imagestoShow.get(2));
        } else if (imagestoShow.size() == 4) {
            imagefour.setVisibility(View.VISIBLE);
            imagefour.setImageBitmap(imagestoShow.get(3));
        } else if (imagestoShow.size() == 5) {
            imagefive.setVisibility(View.VISIBLE);
            imagefive.setImageBitmap(imagestoShow.get(4));
        }

    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void uploadImage(Bitmap bmp) {

        if (!AndroidUtils.isNetworkOnline(this)) {
            Toast.makeText(this, "Sorry! No Internet Connection", Toast.LENGTH_SHORT).show();
            return;
        }

        new UploadToServer("image", SelectedPath,bmp).execute();
    }

    public Bitmap getResizedBitmap(Bitmap image, int bitmapWidth, int bitmapHeight) {
        return Bitmap.createScaledBitmap(image, bitmapWidth, bitmapHeight,
                true);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(UserFeedBackActivity.this, PoolActivity.class);
        startActivity(intent);
        finish();
    }

    private void uploadImages(ArrayList<Bitmap> bmp) {

        if (!AndroidUtils.isNetworkOnline(this)) {
            Toast.makeText(this, "Sorry! No Internet Connection", Toast.LENGTH_SHORT).show();
            return;
        }

        for(int i=0;i<bmp.size();i++) {
            Log.d("MImages","SIZE"+bmp.size());

            new UploadToServer("image", SelectedPath,bmp.get(i)).execute();

        }
    }


    class UploadToServer extends AsyncTask<String, Void, String> {

        String method;
        String path;
        Bitmap bmp;


        public UploadToServer(String stg1, String selectedPath,Bitmap bitmap) {
            this.method = stg1;
            this.path   = selectedPath;
            this.bmp    = bitmap;
        }

        @Override
        protected String doInBackground(String... strings) {

            HttpClient httpclient = new DefaultHttpClient();
            HttpPut httpput = new HttpPut(Constants.URL_USER_FEEDBACK);
            MultipartEntity entity = new MultipartEntity();
            try {
                entity.addPart("imei_number", new StringBody(AndroidUtils.getDeviceImei(UserFeedBackActivity.this)));
                if (mSubject.getText() != null && !mSubject.getText().toString().isEmpty()) {
                    entity.addPart("feedback_subject", new StringBody(mSubject.getText().toString()));
                }

                if (method.equals("image")) {
                    Log.d("MImages","SIZE"+bmp);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    try {
                        bmp.compress(Bitmap.CompressFormat.JPEG, 75, bos);
                        byte[] data = bos.toByteArray();
                        ByteArrayBody bab = new ByteArrayBody(data, "kpcc.jpg");
                        if (bab != null) {
                            entity.addPart("image_file", bab);
                        }
                    } catch (Exception exception) {
                        Log.d("MImages","Exception"+exception);

                    }

                }  else if (method.equals("feedback")) {
                    if (mUserFeedBack.getText() != null && !mUserFeedBack.getText().toString().isEmpty()) {
                        entity.addPart("feedback_message", new StringBody(mUserFeedBack.getText().toString()));
                    }

                    if (imageIds.size() != 0) {
                        for (int i = 0; i < imageIds.size(); i++) {
                            entity.addPart("images", new StringBody(imageIds.get(i)));
                        }
                    }
                    if (videoIds.size() != 0) {
                        for (int i = 0; i < videoIds.size(); i++) {
                            entity.addPart("videos", new StringBody(videoIds.get(0)));
                        }
                    }
                } else if (method.equals("video")) {
                    entity.addPart("video_file", new FileBody(new File(path)));
                }

                httpput.setEntity(entity);
            } catch (UnsupportedEncodingException e1) {
                Log.d("SignUpActivity", "UnsupportedEncodingException" + e1);
                e1.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
                Log.d("SignUpActivity", "NullPointerException" + e);
            }
//                httpput.setEntity(entity);
            HttpResponse response = null;
            try {
                response = (HttpResponse) httpclient.execute(httpput);
                Log.d("Mimages","response"+response.toString());
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("SignUpActivity", "IOException" + e);
            }


            if (response != null) {
                StatusLine status = response.getStatusLine();
                Log.d("Mimages","response"+status.toString());

                HttpEntity entity1 = response.getEntity();

                if (method.equals("image")) {
                    try {
                        String json = EntityUtils.toString(response.getEntity());
                        Log.d("Mimages", "Mimages success:::" + json);
                        if (json != null && !json.isEmpty()) {
                            JSONObject object = new JSONObject(json);
                            imageIds.add(object.optString("image_id"));
                            Log.d("Mimages", "Mimages success:::" + object.optString("image_id"));
                            return "success";
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.d("Mimages", "error IO::" + e);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("Mimages", "Error JSON::" +e);

                    }
                    return "";
//                    if (entity1 != null) {
//                        Log.d("Mimages","entity not null");
//
//                        InputStream instream1 = null;
//                        try {
//                            instream1 = entity1.getContent();
//                            JSONObject jsonObject = convertInputStreamToJSONObject(instream1);
//                            instream1.close();
//
//                            if (jsonObject != null) {
//                                Log.d("MainActivity", "Image upload Response:::" + jsonObject.toString());
//                                Log.d("Mimages","response"+jsonObject.toString());
//
//                                return jsonObject.toString();
//                            }
//
//                            Log.d("Mimages","jsonobject null");
//
//                            // return jsonObject.toString();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                            Log.d("Mimages","error1"+e);
//
//                        } catch (JSONException e) {
//
//                            e.printStackTrace();
//                            Log.d("Mimages","error2"+e);
//                        }
//                    }
                } else if (method.equals("feedback")) {
                    Log.d("MainActivity", "Feedback:::");
                    try {
                        String json = EntityUtils.toString(response.getEntity());
                        Log.d("MainActivity", "Feedback success:::" + json);

                        if (json.contains("success")) {
                            Log.d("MainActivity", "Feedback success:::");
                            return "success";
                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return "";
                } else if (method.equals("video")) {
                    try {
                        String json = EntityUtils.toString(response.getEntity());
                        Log.d("MainActivity", "video success:::" + json);
                        if (json != null && !json.isEmpty()) {
                            JSONObject object = new JSONObject(json);
                            videoIds.add(object.optString("video_id"));
                            Log.d("MainActivity", "Video id" + videoIds.get(0));
                            return "success";
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return "";
                }
            } else {
                Toast.makeText(UserFeedBackActivity.this, "Upload Failed!", Toast.LENGTH_SHORT).show();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {

            if (getApplicationContext() != null) {
                if (s != null && !s.isEmpty()) {
                    if (method.equals("image")) {
                        Toast.makeText(UserFeedBackActivity.this, "Upload Success", Toast.LENGTH_SHORT).show();
//                        InnerImage image = new Gson().fromJson(s, InnerImage.class);
                        Log.d("Mimages"," Image upload success::" + s);
//                        imageIds.add(image.getImageId());
                    } else if (method.equals("feedback")) {
                        Log.d("UserFeedback", "feedback success onpost" + s);
                        progressDialog.dismiss();
                        finish();

                        Toast.makeText(UserFeedBackActivity.this, "Message sent successfully, will display once it is approved", Toast.LENGTH_SHORT).show();
                    } else if (method.equals("video")) {
                        progressDialog.dismiss();
                        // InnerVideos videos = new Gson().fromJson(s,InnerVideos.class);
                        Log.d("UserFeedback", "video success onpost" + s);
                        Toast.makeText(UserFeedBackActivity.this, "Upload Success!", Toast.LENGTH_SHORT).show();
                    }
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

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (frameLayoutMenu.getVisibility() == View.VISIBLE) {
            frameLayoutMenu.setVisibility(View.INVISIBLE);
            menuItem.setIcon(R.drawable.menu_settings);
        }
    }


    @Override
    public void onBackPressed() {
        if (!submitbuttonclicked) {
            new DeleteImages().execute();
            new DeleteVideos().execute();
        }
        super.onBackPressed();

    }



    class DeleteImages extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            if (imageIds.size() != 0) {
                String imageidtodelete=imageIds.toString().replace("[","").replace("]","");
                Log.d("IMAGE Delete", "imageid" + imageidtodelete);
                Log.d("IMAGE Delete", "imei" + AndroidUtils.getDeviceImei(UserFeedBackActivity.this));

                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(Constants.URL_DELETE_IMAGES);

                try {

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                    nameValuePairs.add(new BasicNameValuePair("imei_number", AndroidUtils.getDeviceImei(UserFeedBackActivity.this)));
                    nameValuePairs.add(new BasicNameValuePair("images", imageidtodelete));
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = (HttpResponse) httpClient.execute(httpPost);
                    StatusLine status = response.getStatusLine();

                    String json = EntityUtils.toString(response.getEntity());
                    Log.d("IMAGE Delete", "Json success:::" + json);

                    if (json.contains("success")) {
                        Log.d("Image Delate", "Delete success:::");
                        return "";
                    }


//                    Log.d("IMAGE Delete", "Status" + response.);
                    if (status.toString().contains("200 OK")) {
                        Log.d("IMAGE Delete", "deleted" + status);
                    } else {
                        Log.d("IMAGE Delete", "error");
                    }

                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

                return null;
            }
            return "";
        }
    }

    class DeleteVideos extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            if (videoIds.size() != 0) {
                String imageidtodelete=videoIds.toString().replace("[","").replace("]","");
                Log.d("IMAGE Delete", "video" + imageidtodelete);
                Log.d("IMAGE Delete", "imei" + AndroidUtils.getDeviceImei(UserFeedBackActivity.this));

                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(Constants.URL_DELETE_Videos);

                try {

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                    nameValuePairs.add(new BasicNameValuePair("imei_number", AndroidUtils.getDeviceImei(UserFeedBackActivity.this)));
                    nameValuePairs.add(new BasicNameValuePair("videos", imageidtodelete));
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = (HttpResponse) httpClient.execute(httpPost);
                    StatusLine status = response.getStatusLine();

                    String json = EntityUtils.toString(response.getEntity());
                    Log.d("IMAGE Delete", "Json success:::" + json);

                    if (json.contains("success")) {
                        Log.d("Image Delate", "Delete success:::");
                        return "";
                    }
                    Log.d("IMAGE Delete", "Status" + status);
                    if (status.toString().contains("200 OK")) {
                        Log.d("IMAGE Delete", "deleted" + status);
                    } else {
                        Log.d("IMAGE Delete", "error");
                    }

                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

                return null;
            }
            return "";
        }
    }

}
