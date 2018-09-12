package teli.com.kpcc.activities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.InjectView;
import teli.com.kpcc.R;

/**
 * Created by naveen on 22/1/15.
 */
public class UserfeedbackNewActivity extends BaseActivity {




    @InjectView(R.id.linearlayout)
    LinearLayout linearlayoutView;

    @InjectView(R.id.edtInput)
    EditText enterValues;
    Bitmap bmp;
    String SelectedPath;

    Calendar calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userfeedback_new);
        calendar=Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());

    }

    public void enteredText(View view){
        TextView textView=new TextView(this);
        textView.setText(enterValues.getText());
        linearlayoutView.addView(textView);
        enterValues.setText("");
    }
    public void selectVideoOrImage(View view){
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/* video/*");
        startActivityForResult(intent, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //   if (data.toString().contains("image") && !data.toString().isEmpty())

        if (resultCode == RESULT_OK && requestCode == 1 && data.toString().contains("image")) {
            InputStream stream = null;
            try {
                if (bmp != null && !bmp.isRecycled()) {
//                    bmp.recycle();
                    bmp=null;
                }
                stream = getContentResolver().openInputStream(data.getData());
                try {
                    bmp = BitmapFactory.decodeStream(stream);

                ImageView imageView=new ImageView(this);
                imageView.setLayoutParams(new ViewGroup.LayoutParams(200,200));
                imageView.setBackgroundColor(Color.parseColor("#000000"));
                imageView.setImageBitmap(bmp);
                linearlayoutView.addView(imageView);
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

            //mUserPic.setImageBitmap(bmp);
        }
        if (requestCode == 1 && resultCode == RESULT_OK && data.toString().contains("video")) {

            Uri selectedUri = data.getData();
            SelectedPath = getPath(selectedUri);
            Log.d("SelectedPath", "Path" + SelectedPath);
            Bitmap bitmap= ThumbnailUtils.createVideoThumbnail(SelectedPath,
                    MediaStore.Video.Thumbnails.MINI_KIND);

            ImageView imageView=new ImageView(this);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(200, 200));
            imageView.setBackgroundColor(Color.parseColor("#000000"));
            imageView.setImageBitmap(bitmap);
            linearlayoutView.addView(imageView);



            // doFileUpload();
        }
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public void submitNew(View view){
        int month,year,date,hour,minute;
        TextView textView=new TextView(this);
        month=calendar.get(Calendar.MONTH)+1;
        year=calendar.get(Calendar.YEAR);
        date=calendar.get(Calendar.DATE);
        hour=calendar.get(Calendar.HOUR);
        minute=calendar.get(Calendar.MINUTE);
        String dateformat=date+"/"+month+"/"+year+":::"+hour+":"+minute;
        textView.setText(""+dateformat);
        linearlayoutView.addView(textView);

        View v=new View(this);
        v.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,5));
        v.setBackgroundColor(Color.parseColor("#B3B3B3"));
        linearlayoutView.addView(v);


    }

}
