package com.bmh.trackchild.Activities;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.bmh.trackchild.R;
import com.bmh.trackchild.Tools.AppPermission;
import com.bmh.trackchild.Tools.AppPermissionInterFace;
import com.bmh.trackchild.Tools.Images;
import com.bmh.trackchild.Tools.SharedPrefs;
import com.bmh.trackchild.Tools.StaticValues;
import com.bmh.trackchild.UI.ConfirmDialogInterface;

import android.annotation.SuppressLint;

import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import de.hdodenhof.circleimageview.CircleImageView;

/***
 * This is the first screen that :
 requests from user to register as a parent or child
 if user chooses to register as a parent
 >>user is a parent and he/she must input his/her child's phone number
 else
 >>user is a child and he/she must input his/her parent's phone number
 **/
public class RegistrationActivity extends AppCompatActivity implements OnClickListener, AppPermissionInterFace, ConfirmDialogInterface {

//    CircleImageView ivBrowse;
    EditText edtName, edtUserName, edtPhone;
    Button btnSave;
    Images images;
    int userType;
    String imgName = "";
    String imgContentDescription = "";
    AppPermission appPermission;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initComponent();
        images = new Images(this);
        appPermission = new AppPermission(this);
        setProfileImage(savedInstanceState);
    }

    private void initComponent() {
        edtName =  findViewById(R.id.edtName);
       // edtUserName =  findViewById(R.id.edtUserName);
        edtPhone =  findViewById(R.id.edtPhone);
        btnSave =  findViewById(R.id.btnSave);
       // ivBrowse =  findViewById(R.id.ivBrowse);
        btnSave.setOnClickListener(this);
     //   ivBrowse.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            userType = bundle.getInt(StaticValues.USER_TYPE);
            if (userType == StaticValues.USER_PARENT) {
       //         edtUserName.setHint(getResources().getString(R.string.childName));
                edtPhone.setHint(getResources().getString(R.string.childPhone));
            } else {
        //        edtUserName.setHint(getResources().getString(R.string.parentName));
                edtPhone.setHint(getResources().getString(R.string.parentPhone));
            }
        }
    }

    @SuppressLint("NewApi")
    private void setProfileImage(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            imgName = savedInstanceState.getString(StaticValues.IMAGE_NAME);
            imgContentDescription = savedInstanceState.getString(StaticValues.IMAGE_PATH);
            if (!imgContentDescription.equals("")) {
//                ivBrowse.setContentDescription(imgContentDescription);
//                ivBrowse.setImageURI(Uri.parse(imgContentDescription));
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSave:
                if (validatePhoneNum(edtPhone.getText().toString()) && !edtName.getText().toString().isEmpty()
                      /*  && !edtUserName.getText().toString().isEmpty()*/) {
                    if (userType == StaticValues.USER_PARENT) {
                        saveParentData();
                        startActivity(new Intent(this, ChooseActivity.class));
                        finish();

                    } else {
                        saveChildData();
                        startActivity(new Intent(this, ChildActivity.class));
                        finish();
                    }

                } else {
                    if (edtName.getText().toString().isEmpty()) {
                        edtName.setError(Html.fromHtml("<font color='red'>Enter Name</font>"));
                    } /*else if (edtUserName.getText().toString().isEmpty()) {
                        edtUserName.setError(Html.fromHtml("<font color='red'>Enter Name</font>"));
                    } */else if (edtPhone.getText().toString().isEmpty()) {
                        edtPhone.setError(Html.fromHtml("<font color='red'>Enter valid phone number</font>"));
                    } else if (!validatePhoneNum(edtPhone.getText().toString())) {
                        edtPhone.setError(Html.fromHtml("<font color='red'>Enter valid phone number</font>"));
                    }

                }
                break;
//            case R.id.ivBrowse:
//                browseImage();
//                break;
        }
    }

    private void browseImage() {
        imgName = "img_" + System.currentTimeMillis() + ".jpg";
        //require permissions in versions >= M
        images.getImageSource(imgName, StaticValues.SELECT_IMAGE_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        appPermission.handlePermissionResult(permissions);
    }

    @SuppressLint("NewApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (StaticValues.SELECT_IMAGE_CODE == requestCode && resultCode == RESULT_OK) {
            Uri ImageURI = images.closeImageIntent(imgName, data);
            imgContentDescription = images.UriToPath(ImageURI, data);
//            ivBrowse.setContentDescription(imgContentDescription);
//            ivBrowse.setImageURI(Uri.parse(imgContentDescription));

        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
//        bundle.putString(StaticValues.IMAGE_PATH, ivBrowse.getContentDescription() == null ? "" : ivBrowse.getContentDescription().toString());
        bundle.putString(StaticValues.IMAGE_NAME, imgName);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                finish();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
        super.onBackPressed();
    }

    public boolean validatePhoneNum(String num) {
        Pattern pattern = Pattern.compile("\\d{11}");
        Matcher matcher = pattern.matcher(num);
        if (matcher.matches())
            return true;
        return false;
    }

    private void saveParentData() {
        SharedPrefs sharedPrefs = new SharedPrefs(this);
        sharedPrefs.savePreferences(R.string.Key_ParentName, edtName.getText().toString());
//        sharedPrefs.savePreferences(R.string.Key_ParentImage, ivBrowse.getContentDescription() == null ? "" : ivBrowse.getContentDescription().toString());

        //sharedPrefs.savePreferences(R.string.Key_ChildName, edtUserName.getText().toString());
        sharedPrefs.savePreferences(R.string.Key_ChildPhone, edtPhone.getText().toString());
        sharedPrefs.savePreferences(R.string.Key_UserType, StaticValues.USER_IS_PARENT);


        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("phone", edtPhone.getText().toString());
        editor.commit();




    }

    private void saveChildData() {
        SharedPrefs sharedPrefs = new SharedPrefs(this);
        sharedPrefs.savePreferences(R.string.Key_ChildName, edtName.getText().toString());
//        sharedPrefs.savePreferences(R.string.Key_ChildImage, ivBrowse.getContentDescription() == null ? "" : ivBrowse.getContentDescription().toString());
//        sharedPrefs.savePreferences(R.string.Key_ParentName, edtUserName.getText().toString());
        sharedPrefs.savePreferences(R.string.Key_ParentPhone, edtPhone.getText().toString());
        sharedPrefs.savePreferences(R.string.Key_UserType, StaticValues.USER_IS_CHILD);



    }

    @Override
    public void onAllPermissionGranted() {

    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        dialog.dismiss();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        dialog.dismiss();
    }
}
