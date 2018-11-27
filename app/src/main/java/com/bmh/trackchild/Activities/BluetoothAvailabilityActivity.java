package com.bmh.trackchild.Activities;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;

import com.bmh.trackchild.R;
import com.bmh.trackchild.Tools.StaticValues;
import com.bmh.trackchild.UI.ConfirmDialogInterface;
import com.bmh.trackchild.UI.ConfirmDialogs;

public class BluetoothAvailabilityActivity extends Activity implements ConfirmDialogInterface {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        ConfirmDialogs dialogFragment = ConfirmDialogs.newInstance(getResources().getString(R.string.track_is_stop), StaticValues.DIALOG_TYPE_OK);
        dialogFragment.show(getFragmentManager(), "ConfirmDialogs");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        dialog.dismiss();
        finish();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }
}
