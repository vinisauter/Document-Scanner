package com.scanlibrary;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;

/**
 * Created by jhansi on 28/03/15.
 */
@SuppressWarnings("JniMissingFunction")
public class ScanActivity extends Activity implements IScanner {

    private int barColor;
    private int barItemColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_layout);
        setupViews(getIntent());
        init();
    }

    private void init() {
        Uri uri = getIntent().getParcelableExtra(ScanConstants.SELECTED_BITMAP);
        if (uri != null) {
            ScanFragment fragment = new ScanFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(ScanConstants.SELECTED_BITMAP, uri);
            fragment.setArguments(bundle);
            android.app.FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.content, fragment);
            fragmentTransaction.commit();
            return;
        }
        PickImageFragment fragment = new PickImageFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ScanConstants.OPEN_INTENT_PREFERENCE, getPreferenceContent());
        fragment.setArguments(bundle);
        android.app.FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.content, fragment);
        fragmentTransaction.commit();
    }

    private void setupViews(@NonNull Intent intent) {
        barColor = intent.getIntExtra(Scan.EXTRA_BAR_COLOR, Color.BLACK);
        barItemColor = intent.getIntExtra(Scan.EXTRA_BAR_ITEM_COLOR, Color.WHITE);
    }

    protected int getPreferenceContent() {
        return getIntent().getIntExtra(ScanConstants.OPEN_INTENT_PREFERENCE, 0);
    }


    @Override
    public void onBitmapSelect(Uri uri) {
        ScanFragment fragment = new ScanFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ScanConstants.SELECTED_BITMAP, uri);
        bundle.putInt(Scan.EXTRA_BAR_COLOR, barColor);
        bundle.putInt(Scan.EXTRA_BAR_ITEM_COLOR, barItemColor);
        fragment.setArguments(bundle);
        android.app.FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.content, fragment);
        fragmentTransaction.addToBackStack(ScanFragment.class.toString());
        fragmentTransaction.commit();
    }

    @Override
    public void onScanFinish(Uri uri) {
        ResultFragment fragment = new ResultFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ScanConstants.SCANNED_RESULT, uri);
        fragment.setArguments(bundle);
        android.app.FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.content, fragment);
        fragmentTransaction.addToBackStack(ResultFragment.class.toString());
        fragmentTransaction.commit();
    }

    public native Bitmap getScannedBitmap(Bitmap bitmap, float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4);

    public native Bitmap getGrayBitmap(Bitmap bitmap);

    public native Bitmap getMagicColorBitmap(Bitmap bitmap);

    public native Bitmap getBWBitmap(Bitmap bitmap);

    public native float[] getPoints(Bitmap bitmap);

    static {
        System.loadLibrary("opencv_java3");
        System.loadLibrary("Scanner");
    }
}