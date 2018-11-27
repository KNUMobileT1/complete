package com.bmh.trackchild.Activities;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;

import com.bmh.trackchild.R;
import com.bmh.trackchild.Tools.StaticValues;
import com.bmh.trackchild.UI.ConfirmDialogInterface;
import com.bmh.trackchild.UI.ConfirmDialogs;
import com.bmh.trackchild.services.BluetoothService;

public class OutRangeActivity extends Activity
        implements ConfirmDialogInterface {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        ConfirmDialogs dialogFragment = ConfirmDialogs.newInstance(getResources().getString(R.string.out_of_range), StaticValues.DIALOG_TYPE_YES_NO);
        dialogFragment.show(getFragmentManager(), "ConfirmDialogs");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        BluetoothService.thread.interrupted();
        BluetoothService.mediaAlert.stopAlert();
        startActivity(new Intent(this, ChildLocationActivity.class));
        dialog.dismiss();
        finish();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        BluetoothService.mediaAlert.stopAlert();
        dialog.dismiss();
        finish();
    }
}
