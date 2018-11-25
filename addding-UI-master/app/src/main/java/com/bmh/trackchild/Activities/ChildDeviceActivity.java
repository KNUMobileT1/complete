package com.bmh.trackchild.Activities;

import java.util.HashSet;

import com.bmh.trackchild.R;
import com.bmh.trackchild.Tools.SharedPrefs;
import com.bmh.trackchild.Tools.StaticValues;
import com.bmh.trackchild.UI.ConfirmDialogInterface;
import com.bmh.trackchild.UI.ConfirmDialogs;
import com.bmh.trackchild.UI.Drawer;
import com.bmh.trackchild.helper.BluetoothHelper;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


/***
 * This is the second screen that :
 requests from user(Parent) to save his/her child device name
 **/
public class ChildDeviceActivity extends AppCompatActivity implements ConfirmDialogInterface {


    ListView list;
    Button scanBtn;
    BluetoothHelper BTHelper;
    String deviceName, deviceMacAddress;
    String[] item;

    HashSet<BluetoothDevice> allDevices;
    SharedPrefs sharedPrefs;
    Drawer drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_device);
        drawer = new Drawer(this);
        list = (ListView) findViewById(R.id.devices_list_view);
        scanBtn = (Button) findViewById(R.id.btn_scan_devices);

        sharedPrefs = new SharedPrefs(this);

        BTHelper = new BluetoothHelper(this);
        allDevices = new HashSet<BluetoothDevice>();
        //if bluetooth is turned off , turn it on.
        if (!BTHelper.checkBTstatus()) {
            BTHelper.turnOnBluetooth(this);
        }
        //else Bluetooth is already turned ON,then get all bonded - "paired" - devices.
        else {
            BTHelper.scanPairedDevices();
        }

        scanBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                BTHelper.discoverDevices();
            }
        });

        list.setAdapter(BTHelper.getDevicesArrayAdapter());
        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                item = parent.getItemAtPosition(position).toString().split("\n");
                deviceName = item[0];
                ConfirmDialogs  dialogFragment =  ConfirmDialogs.newInstance(getResources().getString(R.string.dialog_save_device), StaticValues.DIALOG_TYPE_YES_NO);
                dialogFragment.show(getFragmentManager(), "ConfirmDialogs");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BluetoothHelper.REQUEST_ENABLE_BT && resultCode == RESULT_OK) {
            //NOW Bluetooth is enabled,so get all bonded - "paired" - devices
            BTHelper.scanPairedDevices();
        } else if (requestCode == BluetoothHelper.REQUEST_ENABLE_BT && resultCode == RESULT_CANCELED) {
            //User entered "no" to turn Bluetooth ON,
            //NOW Bluetooth is still disabled .. notify user to turn bluetooth on.
            Toast.makeText(getApplicationContext(), "You must turn Bluetooth on", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            BTHelper.unregisterBTReceiver();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    private String getDeviceAddress(String deviceName) {
        allDevices = BTHelper.getAllDevices();

        for (BluetoothDevice device : allDevices) {
            if (device.getName().equals(deviceName)) {
                deviceMacAddress = device.getAddress();
            }
        }
        return deviceMacAddress;
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        // save the selected device name and MAC Address
        sharedPrefs.savePreferences(R.string.Key_ChildDeviceName, deviceName);
        sharedPrefs.savePreferences(R.string.Key_ChildDeviceAddressMac, getDeviceAddress(deviceName));
        dialog.dismiss();
        /*startActivity(new Intent(ChildDeviceActivity.this, TrackActivity.class));
        finish();*/

        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        String phoneNumber = pref.getString("phone", "");
        String context = "찾아주세요";

        SendSMS(phoneNumber, context);

    }
    void SendSMS(String number, String msg) {
        SmsManager sms = SmsManager.getDefault();
        Log.d("test1", "sms1");
        sms.sendTextMessage(number, null, msg, null, null);
        Log.d("test1", "sms2");
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        Toast.makeText(getApplicationContext(), "No device selected", Toast.LENGTH_LONG).show();
        dialog.dismiss();
    }
}
