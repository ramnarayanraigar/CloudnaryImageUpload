package raigar.ramnarayan.cloudnaryimageupload;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private Context mContext;
    private static final int PERMISSION_CODE =1;
    private static final int PICK_IMAGE=1;

    private ImageView mImageUploadImage;
    private Spinner mSpinnerSize;
    private GridView mGridImage;
    private RecyclerView rv;

    private String filePath;

    private List<String> listSizeOfImage;
    private String imageSize[] = {"30 * 40 Default", "300 * 450", "1000 * 800"};
    private int imageWidth = 30, imageHeight = 40;

    private DBManager dbManager;

    // get all cloudnary images
    //  http://res.cloudinary.com/dnzlufovh/image/upload/v1604124761/jluew3dngxblr77kv8bf.jpg
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        clicks();
        initCloudinary();
        initImageSizeList();
        initSpinner();
      //  loadGridView();

      //  loadGridView();

        loadRecyclerView();
    }

    private void initViews() {
        mContext = this;
        dbManager = new DBManager(mContext);

        rv = findViewById(R.id.rv);
        mImageUploadImage = findViewById(R.id.image_upload);
        mSpinnerSize = findViewById(R.id.spin_size);
        mGridImage = findViewById(R.id.grid_images);
    }

    private void initCloudinary() {
        try {
            HashMap<String, String> hmCloudnary = new HashMap<>();

            hmCloudnary.put("cloud_name", mContext.getResources().getString(R.string.cloud_name_cloudnary));
            hmCloudnary.put("api_key", mContext.getResources().getString(R.string.api_key_cloudnary));
            hmCloudnary.put("api_secret", mContext.getResources().getString(R.string.api_secret_cloudnary));
            MediaManager.init(MainActivity.this, hmCloudnary);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initImageSizeList() {
        if (listSizeOfImage == null) {
            listSizeOfImage = new ArrayList<>();

            listSizeOfImage.add(imageSize[0]);
            listSizeOfImage.add(imageSize[1]);
            listSizeOfImage.add(imageSize[2]);
        }
    }

    private void initSpinner() {
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listSizeOfImage);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        mSpinnerSize.setAdapter(dataAdapter);
    }

    private void clicks() {
        mImageUploadImage.setOnClickListener(this);
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            accessGalaryImages();
        } else {
            ActivityCompat.requestPermissions(
                    MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_CODE
            );
        }
    }

    private void accessGalaryImages() {
        @SuppressLint("IntentReset") Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        );
        i.setType("image/*");
        startActivityForResult(i, PICK_IMAGE);
    }

    @Override
    public void onClick(View view) {
        if (view == mImageUploadImage) {
            requestStoragePermission();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //get the image's file location
     //   filePath = getRealPathFromUri(data.getData(), MainActivity.this);

        if(requestCode==PICK_IMAGE && resultCode==RESULT_OK){
            filePath = getRealPathFromUri(data.getData(), MainActivity.this);
           /* try {

                filePath = getRealPathFromUri(data.getData(), MainActivity.this);
                //set picked image to the mProfile
             //   Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
              //  mProfile.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }*/

           uploadImageToCloudinary();
        }
    }

    private String getRealPathFromUri(Uri imageUri, Activity activity){
        Cursor cursor = activity.getContentResolver().query(imageUri, null, null, null, null);

        if(cursor==null) {
            return imageUri.getPath();
        }else{
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    private void uploadImageToCloudinary() {
        MediaManager.get().upload(filePath).callback(new UploadCallback() {
            @Override
            public void onStart(String requestId) {
               // uploading start
            }

            @Override
            public void onProgress(String requestId, long bytes, long totalBytes) {
                // uploading in progress
            }

            @Override
            public void onSuccess(String requestId, Map resultData) {
                // uploading success and upload image url is resultData.get("url").toString()

                Log.v(TAG, "upload url: " + resultData.get("url"));

                DBManager manager = new DBManager(mContext);

                manager.open();
                try {
                    long l = manager.insertInStudent(resultData.get("url").toString());

                    Toast.makeText(mContext, "inserted " + l , Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

              //  loadGridView();

                loadRecyclerView();
            }

            @Override
            public void onError(String requestId, ErrorInfo error) {
                // uploading error error.getDescription()
            }

            @Override
            public void onReschedule(String requestId, ErrorInfo error) {
                // uploading resheduled error.getDescription()
            }
        }).dispatch();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        if (position == 0) {
            imageWidth = 30;
            imageHeight = 40;
        } else if (position == 1) {
            imageWidth = 300;
            imageHeight = 450;
        } else if (position == 3) {
            imageWidth = 1000;
            imageHeight = 800;
        }

       // loadGridView();

        loadRecyclerView();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void loadGridView() {
        dbManager.open();
        mGridImage.setAdapter(new ImageAdapter(mContext, dbManager.fetchFromStudent(), imageWidth, imageHeight));
    }

    private void loadRecyclerView() {
      /*  dbManager.open();
        LinearLayoutManager manager = new LinearLayoutManager(mContext);

        rv.setLayoutManager(manager);
        rv.setAdapter(new ImageRecycleAdapter(mContext, dbManager.fetchFromStudent(), imageWidth, imageHeight));*/

        dbManager.open();
        mGridImage.setAdapter(new ImageAdapter(mContext, dbManager.fetchFromStudent(), imageWidth, imageHeight));
    }
}