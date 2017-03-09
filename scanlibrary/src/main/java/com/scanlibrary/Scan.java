package com.scanlibrary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;

/**
 * Created by Vinicius Sauter on 08/03/2017.
 * ...
 */

@SuppressWarnings({"WeakerAccess", "unused"})
public class Scan {
    public static final int REQUEST_SCAN = 69690;
    private static final String EXTRA_PREFIX = "com.scanlibrary";
    public static final String EXTRA_BAR_COLOR = EXTRA_PREFIX + ".BarColor";
    public static final String EXTRA_BAR_ITEM_COLOR = EXTRA_PREFIX + ".BarItemColor";

    private final Bundle mOptionBundle;
    private final Intent mCropIntent;
    private Uri source;

    private Scan() {
        mCropIntent = new Intent();
        mOptionBundle = new Bundle();
    }

    public void start(@NonNull Activity activity) {
        start(activity, REQUEST_SCAN);
    }

    public void start(@NonNull Activity activity, int requestCode) {
        activity.startActivityForResult(getIntent(activity), requestCode);
    }

    /**
     * Get the intent to start {@link ScanActivity}
     *
     * @param context A Context of the application package implementing this class.
     * @return Intent for {@link ScanActivity}
     */
    public Intent getIntent(@NonNull Context context) {
        mCropIntent.setClass(context, ScanActivity.class);
        mCropIntent.putExtras(mOptionBundle);
        return mCropIntent;
    }

    @NonNull
    public Bundle getOptionBundle() {
        return mOptionBundle;
    }

    public void setBarColor(@ColorInt int color) {
        mOptionBundle.putInt(EXTRA_BAR_COLOR, color);
    }

    /**
     * @param color A int color to use at options bar
     */
    public void setBarItemColor(@ColorInt int color) {
        mOptionBundle.putInt(EXTRA_BAR_ITEM_COLOR, color);
    }

    /**
     * This method creates new Intent builder and sets source URI.
     *
     * @param source Uri for image to scan
     * @return Intent builder to @{@link ScanActivity}
     */
    public static Scan scan(Uri source) {
        Scan scan = new Scan();
        scan.source = source;
        return scan;
    }

    /**
     * This method creates new Intent builder and sets source URI.
     *
     * @return Intent builder to @{@link ScanActivity}
     */
    public static Scan scan() {
        Scan scan = new Scan();
        return scan;
    }

}
