package com.bmh.trackchild.Tools;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.app.AlertDialog;
import android.util.DisplayMetrics;


import com.bmh.trackchild.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission.CAMERA;


public class Images {
    private Uri outputFileUri;
    private AppPermission appPermission;
    private Activity mActivity;

    public Images(Activity activity) {
        this.mActivity = activity;
        appPermission = new AppPermission(activity);
    }



    public void getImageSource(final String imgName, final int requestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("Choose Image Source");

        builder.setItems(new CharSequence[]{"Gallery", "Camera"},
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    if (!appPermission.hasPermissions(new String[]{WRITE_EXTERNAL_STORAGE})) {
                                        selectGalleryImage(requestCode);
                                    } else {
                                        appPermission.askPermissions(new String[]{WRITE_EXTERNAL_STORAGE}, StaticValues.ACTION_REQUEST_GALLERY);
                                    }
                                } else {
                                    selectGalleryImage(requestCode);
                                }
                                break;
                            case 1:
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    if (!appPermission.hasPermissions(new String[]{CAMERA, WRITE_EXTERNAL_STORAGE})) {
                                        takePhotoByCamera(imgName, requestCode);
                                    } else {
                                        appPermission.askPermissions(new String[]{CAMERA, WRITE_EXTERNAL_STORAGE}, StaticValues.ACTION_REQUEST_CAMERA);
                                    }
                                } else {
                                    takePhotoByCamera(imgName, requestCode);
                                }
                                break;
                            default:
                                break;
                        }
                    }
                });
        builder.create();
        builder.show();
    }

    private void selectGalleryImage(int requestCode) {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        // Chooser of filesystem options.
        final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Image");
        mActivity.startActivityForResult(chooserIntent, requestCode);
    }

    private void takePhotoByCamera(String imgName, int requestCode) {
        // Determine Uri of camera image to save.
        final File root = new File(Environment.getExternalStorageDirectory() + File.separator + "MyDir" + File.separator);
        root.mkdirs();
        final File sdImageMainDirectory = new File(root, imgName);
        outputFileUri = Uri.fromFile(sdImageMainDirectory);

        final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        Intent cameraChooser = Intent.createChooser(captureIntent, "Capture photo");
        mActivity.startActivityForResult(cameraChooser, requestCode);
    }

    public Uri closeImageIntent(String imgName, Intent data) {
        final boolean isCamera;
        if (data == null) {
            isCamera = true;
        } else {
            final String action = data.getAction();
            if (action == null) {
                isCamera = false;
            } else {
                isCamera = action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
            }
        }

        Uri selectedImageUri;
        if (isCamera) {
            ///////////////////////////////////////////////
            selectedImageUri = outputFileUri;
            //////////////////////////////////////////////
        } else {
            selectedImageUri = data == null ? null : data.getData();
        }

        if (selectedImageUri == null) {
            final File root = new File(Environment.getExternalStorageDirectory() + File.separator + "MyDir" + File.separator);
            root.mkdirs();
            final File sdImageMainDirectory = new File(root, imgName);
            selectedImageUri = getImageContentUri(mActivity, sdImageMainDirectory);
        }
        return selectedImageUri;
    }

    public String UriToPath(Uri imageURI, Intent data) {
        if (imageURI.getPath().contains(".")) {
            return imageURI.getPath();
        } else if (imageURI.getPath().contains(":")) {
            imageURI = data.getData();

    /* now extract ID from Uri path using getLastPathSegment() and then split with ":"
    then call get Uri to for Internal storage or External storage for media I have used getUri()
    */
            String id = imageURI.getLastPathSegment().split(":")[1];
            final String[] imageColumns = {MediaStore.Images.Media.DATA};
            final String imageOrderBy = null;
            Uri uri = getUri();
            String selectedImagePath = "path";

            Cursor imageCursor = mActivity.managedQuery(uri, imageColumns,
                    MediaStore.Images.Media._ID + "=" + id, null, imageOrderBy);

            if (imageCursor.moveToFirst()) {
                selectedImagePath = imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            return selectedImagePath.toString();
        } else {
            return convertMediaUriToPath(mActivity, imageURI);
        }
    }

    private Uri getUri() {
        String state = Environment.getExternalStorageState();
        if (!state.equalsIgnoreCase(Environment.MEDIA_MOUNTED))
            return MediaStore.Images.Media.INTERNAL_CONTENT_URI;

        return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    }

    private static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }


    private String convertMediaUriToPath(Context context, Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(column_index);
        cursor.close();
        return path;
    }
}

