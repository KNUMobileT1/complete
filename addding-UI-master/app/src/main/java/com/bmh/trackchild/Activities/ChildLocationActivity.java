package com.bmh.trackchild.Activities;

import com.bmh.trackchild.R;
import com.bmh.trackchild.Tools.SharedPrefs;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class ChildLocationActivity extends FragmentActivity  {

    EditText lonEdt, latEdt, phoneEdt;
    Button sendBtn;
    String smsContent, lat, lon;
    String loc[];
    LatLng latLong;// = new LatLng(21 , 57);
    // Google Map
    private GoogleMap googleMap;
    SharedPrefs sharedPrefs;
    Dialog ConfirmationDialog;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.child_location);
        sharedPrefs = new SharedPrefs(this);
        initComponents();

        bundle = getIntent().getExtras();
        if (bundle != null) {
            smsContent = bundle.getString("smsBody");
            loc = smsContent.split(":");
            lon = loc[1];
            lat = loc[2];
            latLong = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));

            lonEdt.setText("Lon: " + lon);
            latEdt.setText("Lat: " + lat);

        }

        sendBtn.setOnClickListener(new OnClickListener() {


            @Override
            public void onClick(View v) {

                sendSMS(sharedPrefs.getPreferences(R.string.Key_ChildPhone,""), "찾아주세요");
            }
        });


    }

    public void initComponents() {
        lonEdt = (EditText) findViewById(R.id.edit_lon);
        latEdt = (EditText) findViewById(R.id.edit_lat);
        sendBtn = (Button) findViewById(R.id.btn_send);
    }

    private void sendSMS(String phoneNumber, String message) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initilizeMap();

    }

    /**
     * function to load map If map is not created it will create it for you
     */
    private void initilizeMap() {
        if (googleMap == null) {
            googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(), "Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("MissingPermission")
    public void initMapComponents() {
        // Changing map type
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Showing / hiding your current location
        googleMap.setMyLocationEnabled(true);

        // Enable / Disable zooming controls
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        // Enable / Disable my location button
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);

        // Enable / Disable Compass icon
        googleMap.getUiSettings().setCompassEnabled(true);

        // Enable / Disable Rotate gesture
        googleMap.getUiSettings().setRotateGesturesEnabled(true);

        // Enable / Disable zooming functionality
        googleMap.getUiSettings().setZoomGesturesEnabled(true);

    }


}
