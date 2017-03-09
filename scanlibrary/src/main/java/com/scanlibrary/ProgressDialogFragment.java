package com.scanlibrary;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.view.KeyEvent;

public class ProgressDialogFragment extends DialogFragment {

    public String message;

    public static ProgressDialogFragment getInstance(String message) {
        ProgressDialogFragment dialogFragment = new ProgressDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("MESSAGE", message);
        dialogFragment.setArguments(bundle);
        dialogFragment.message = message;
        return dialogFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getArguments().containsKey("MESSAGE"))
            message = getArguments().getString("MESSAGE");
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setIndeterminate(true);
        dialog.setMessage(message);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        // Disable the back button
        OnKeyListener keyListener = new OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {

                return keyCode == KeyEvent.KEYCODE_BACK;
            }

        };
        dialog.setOnKeyListener(keyListener);
        return dialog;
    }
}