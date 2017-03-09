package com.scanlibrary;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.IOException;

/**
 * Created by jhansi on 29/03/15.
 */
public class ResultFragment extends Fragment {

    private View view;
    private ImageView scannedImageView;
    private Bitmap original;
    private Bitmap transformed;

    public ResultFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.result_layout, null);
        init();
        return view;
    }

    private void init() {
        scannedImageView = (ImageView) view.findViewById(R.id.scannedImage);
        view.findViewById(R.id.original).setOnClickListener(new OriginalButtonClickListener());
        view.findViewById(R.id.magicColor).setOnClickListener(new MagicColorButtonClickListener());
        view.findViewById(R.id.grayMode).setOnClickListener(new GrayButtonClickListener());
        view.findViewById(R.id.BWMode).setOnClickListener(new BWButtonClickListener());
        view.findViewById(R.id.bt_done).setOnClickListener(new DoneButtonClickListener());
        view.findViewById(R.id.bt_return).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        view.findViewById(R.id.bt_rotate_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scannedImageView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (original != null) {
                            Matrix matrix = new Matrix();
                            matrix.postRotate(-90);
                            Bitmap scaledBitmap = Bitmap.createScaledBitmap(original, original.getWidth(), original.getHeight(), true);
                            original = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
                        }
                        if (transformed != null) {
                            Matrix matrix = new Matrix();
                            matrix.postRotate(-90);
                            Bitmap scaledBitmap = Bitmap.createScaledBitmap(transformed, transformed.getWidth(), transformed.getHeight(), true);
                            transformed = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
                            scannedImageView.setImageBitmap(transformed);
                        } else {
                            scannedImageView.setImageBitmap(original);
                        }
                    }
                });
            }
        });
        Bitmap bitmap = getBitmap();
        setScannedImage(bitmap);
    }

    private Bitmap getBitmap() {
        Uri uri = getUri();
        try {
            original = Utils.getBitmap(getActivity(), uri);
            getActivity().getContentResolver().delete(uri, null, null);
            return original;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Uri getUri() {
        Uri uri = getArguments().getParcelable(ScanConstants.SCANNED_RESULT);
        return uri;
    }

    public void setScannedImage(Bitmap scannedImage) {
        scannedImageView.setImageBitmap(scannedImage);
    }

    private class DoneButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent data = new Intent();
            Bitmap bitmap = transformed;
            if (bitmap == null) {
                bitmap = original;
            }
            Uri uri = Utils.getUri(getActivity(), bitmap);
            data.putExtra(ScanConstants.SCANNED_RESULT, uri);
            getActivity().setResult(Activity.RESULT_OK, data);
            original.recycle();
            System.gc();
            getActivity().finish();
        }
    }

    private class BWButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            transformed = ((ScanActivity) getActivity()).getBWBitmap(original);
            scannedImageView.setImageBitmap(transformed);
        }
    }

    private class MagicColorButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            transformed = ((ScanActivity) getActivity()).getMagicColorBitmap(original);
            scannedImageView.setImageBitmap(transformed);
        }
    }

    private class OriginalButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            transformed = original;
            scannedImageView.setImageBitmap(original);
        }
    }

    private class GrayButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            transformed = ((ScanActivity) getActivity()).getGrayBitmap(original);
            scannedImageView.setImageBitmap(transformed);
        }
    }

}