package com.scanner.demo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.nguyenhoanglam.imagepicker.activity.ImagePickerActivity;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.scanlibrary.ScanActivity;
import com.scanlibrary.ScanConstants;
import com.scanlibrary.Utils;

import java.io.IOException;
import java.util.ArrayList;

import static android.graphics.BitmapFactory.decodeFile;


public class MainActivity extends ActionBarActivity {

    private static final int IMAGE_PICKER_REQUEST = 98;
    private static final int REQUEST_CODE = 99;
    private Button scanButton;
    private Button cameraButton;
    private Button mediaButton;
    private Button scanv2Button;
    private ImageView scannedImageView;
    private ArrayList<Image> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        scanButton = (Button) findViewById(R.id.scanButton);
        scanButton.setOnClickListener(new ScanButtonClickListener());
        cameraButton = (Button) findViewById(R.id.cameraButton);
        cameraButton.setOnClickListener(new ScanButtonClickListener(ScanConstants.OPEN_CAMERA));
        mediaButton = (Button) findViewById(R.id.mediaButton);
        mediaButton.setOnClickListener(new ScanButtonClickListener(ScanConstants.OPEN_MEDIA));
        scanv2Button = (Button) findViewById(R.id.scanv2Button);
        scanv2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ImagePickerActivity.class);
                intent.putExtra(ImagePickerActivity.INTENT_EXTRA_FOLDER_MODE, true);
                intent.putExtra(ImagePickerActivity.INTENT_EXTRA_MODE, ImagePickerActivity.MODE_SINGLE);
                intent.putExtra(ImagePickerActivity.INTENT_EXTRA_LIMIT, 1);
                intent.putExtra(ImagePickerActivity.INTENT_EXTRA_SHOW_CAMERA, true);
                intent.putExtra(ImagePickerActivity.INTENT_EXTRA_SELECTED_IMAGES, images);
                intent.putExtra(ImagePickerActivity.INTENT_EXTRA_FOLDER_TITLE, "Album");
                intent.putExtra(ImagePickerActivity.INTENT_EXTRA_IMAGE_TITLE, "");
                intent.putExtra(ImagePickerActivity.INTENT_EXTRA_IMAGE_DIRECTORY, "Camera");
                startActivityForResult(intent, IMAGE_PICKER_REQUEST);
            }
        });
        scannedImageView = (ImageView) findViewById(R.id.scannedImage);
    }

    private class ScanButtonClickListener implements View.OnClickListener {

        private int preference;

        public ScanButtonClickListener(int preference) {
            this.preference = preference;
        }

        public ScanButtonClickListener() {
        }

        @Override
        public void onClick(View v) {
            startScan(preference);
        }
    }

    protected void startScan(int preference) {
        Intent intent = new Intent(this, ScanActivity.class);
        intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICKER_REQUEST && data != null) {
            images = data.getParcelableArrayListExtra(ImagePickerActivity.INTENT_EXTRA_SELECTED_IMAGES);
            BitmapFactory.Options options = new BitmapFactory.Options();
            if (images.size() > 0) {
                String photoPath = images.get(0).getPath();
                options.inSampleSize = 3;
                Bitmap bitmap = decodeFile(photoPath, options);
                Uri uri = Utils.getUri(this, bitmap);
                bitmap.recycle();

                Intent intent = new Intent(this, ScanActivity.class);
                intent.putExtra(ScanConstants.SELECTED_BITMAP, uri);
                startActivityForResult(intent, REQUEST_CODE);
            }
        }
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getExtras().getParcelable(ScanConstants.SCANNED_RESULT);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                getContentResolver().delete(uri, null, null);
                scannedImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Bitmap convertByteArrayToBitmap(byte[] data) {
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
