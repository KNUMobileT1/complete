package com.bmh.trackchild.reciever;

import com.bmh.trackchild.Activities.BluetoothAvailabilityActivity;
import com.bmh.trackchild.R;
import com.bmh.trackchild.Tools.SharedPrefs;
import com.bmh.trackchild.helper.BluetoothHelper;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class BluetoothAvailability extends BroadcastReceiver{

	BluetoothHelper BTHelper;
	Context context;
	SharedPrefs sharedPrefs;
	@Override
	public void onReceive(Context context, Intent intent)
	{

		this.context=context;

		String action = intent.getAction();
		sharedPrefs=new SharedPrefs(context);
		BTHelper=new BluetoothHelper(context);
		if(BluetoothAdapter.ACTION_STATE_CHANGED.equals(action))
		 {
			 if ((BTHelper.getBluetoothAdapter().getState() == BluetoothAdapter. STATE_TURNING_OFF ||
					 BTHelper.getBluetoothAdapter().getState() == BluetoothAdapter. STATE_OFF)
					&& sharedPrefs.getPreferences(R.string.IS_CHECKED,false))
			{
				// the bluetooth is about to be turned off and user activates tracking
				// so alarm him that tracking stopped
				//displayBTAlertmDialog();
				sharedPrefs.savePreferences(R.string.IS_CHECKED,false);
				Intent i = new Intent(context, BluetoothAvailabilityActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(i);
			}
			else if (BTHelper.getBluetoothAdapter().getState() == BluetoothAdapter. STATE_OFF)
			{
				//Bluetooth now is turned off , so uncheck tracking your child
				sharedPrefs.savePreferences(R.string.IS_CHECKED,false);
			}

		 }
	}
}
