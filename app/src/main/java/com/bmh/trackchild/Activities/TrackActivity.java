package com.bmh.trackchild.Activities;

import com.bmh.trackchild.R;
import com.bmh.trackchild.Tools.SharedPrefs;
import com.bmh.trackchild.UI.Drawer;
import com.bmh.trackchild.helper.BluetoothHelper;
import com.bmh.trackchild.services.BluetoothService;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Toast;

public class TrackActivity extends AppCompatActivity {
    BluetoothAdapter mBluetoothAdapter;
    ArrayAdapter<String> mArrayAdapter;
    BluetoothHelper BTHelper;
    CheckBox mCheckBox;
    SharedPrefs sharedPrefs;
    Drawer drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);
        drawer = new Drawer(this);
        sharedPrefs = new SharedPrefs(this);
        mCheckBox = (CheckBox) findViewById(R.id.checkBox1);
        BTHelper = new BluetoothHelper(this);
    }


    public void onCheckboxClicked(View v) {
        mCheckBox = (CheckBox) v;
        // Is the view now checked?
        if (mCheckBox.isChecked()) {
            //if Bluetooth is turned OFF ,then turn it ON
            if (!BTHelper.checkBTstatus()) {
                BTHelper.turnOnBluetooth(this);
            }
            //else Bluetooth is already turned ON, start service
            else {
                startService(new Intent(TrackActivity.this, BluetoothService.class));
            }
            sharedPrefs.savePreferences(R.string.IS_CHECKED, mCheckBox.isChecked());

        } else {
            stopService(new Intent(TrackActivity.this, BluetoothService.class));
            sharedPrefs.savePreferences(R.string.IS_CHECKED, mCheckBox.isChecked());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        sharedPrefs.savePreferences(R.string.IS_CHECKED, mCheckBox.isChecked());
    }

    @Override
    public void onResume() {
        super.onResume();
        mCheckBox.setChecked(sharedPrefs.getPreferences(R.string.IS_CHECKED, false));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //	super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BluetoothHelper.REQUEST_ENABLE_BT && resultCode == RESULT_OK) {
            //User entered "yes" to turn Bluetooth ON,
            //NOW Bluetooth is enabled,So start service
            startService(new Intent(TrackActivity.this, BluetoothService.class));
        } else if (requestCode == BluetoothHelper.REQUEST_ENABLE_BT && resultCode == RESULT_CANCELED) {
            //User entered "no" to turn Bluetooth ON,
            //NOW Bluetooth is still disabled .. notify user to turn bluetooth on.
            sharedPrefs.savePreferences(R.string.IS_CHECKED, false);
            Toast.makeText(getApplicationContext(), "You must turn Bluetooth on", Toast.LENGTH_LONG).show();
        }
    }


}

