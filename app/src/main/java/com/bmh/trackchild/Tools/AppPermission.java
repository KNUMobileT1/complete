package com.bmh.trackchild.Tools;

import android.annotation.TargetApi;
import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;


import com.bmh.trackchild.R;
import com.bmh.trackchild.UI.ConfirmDialogs;

import java.util.ArrayList;
import java.util.List;

public class AppPermission {


    private Activity activity;
    private SharedPrefs Prefs;


    public AppPermission(Activity activity) {
        this.activity = activity;
        Prefs = new SharedPrefs(activity);
    }

    public void askPermissions(String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

    //return false if all permissions are granted and true if there is permission not granted
    public boolean hasPermissions(String[] permissions) {
        for (String permission : permissions) {
            //permission is not granted
            if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }
        return false;
    }

    private boolean shouldShowRequest(String[] permissions) {
        for (String permission : permissions) {
            //permission is not granted
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                return true;
            }
        }
        return false;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void handlePermissionResult(String[] permissions) {
        if (!hasPermissions(permissions)) {
            ((AppPermissionInterFace) activity).onAllPermissionGranted();
        }
        //Permission is denied
        else {
            //Permission denied only
            if (shouldShowRequest(permissions)) {
                ConfirmDialogs dialogFragment = ConfirmDialogs.newInstance(activity.getResources().getString(R.string.first_deny), StaticValues.DIALOG_TYPE_YES_NO);
                dialogFragment.show(activity.getFragmentManager(), StaticValues.DENY_PERMISSION);
            }
            //permission denied and disabled
            else {
                ConfirmDialogs dialogFragment = ConfirmDialogs.newInstance(activity.getResources().getString(R.string.never_ask_deny), StaticValues.DIALOG_TYPE_YES_NO);
                dialogFragment.show(activity.getFragmentManager(), StaticValues.NEVER_ASK_PERMISSION);
            }
        }
    }

    public void navigateToAppSetting() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        activity.startActivity(intent);
    }


}
