package com.bmh.trackchild.reciever;

import com.bmh.trackchild.Activities.ChildLocationActivity;
import com.bmh.trackchild.services.GPSLocationService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSReceiver extends BroadcastReceiver 
{

	String smsBody, fromPhoneNum;

	@Override
	public void onReceive(Context context, Intent intent) 
	{
		Bundle bundle = intent.getExtras();
		if (bundle == null) 
		{
			return;
		}

		String action = intent.getAction();
		// Get real sms or dummy one to try the receiver
		if (action.equals("android.provider.Telephony.SMS_RECEIVED")) 
		{

			/***
			 * A PDU is a "protocol data unit", 
			 * which is the industry format for an SMS message. 
			 * because SMSMessage reads/writes them you shouldn't need to dissect them.
			 * A large message might be broken into many, which is why it is an array of objects.
			 * to know more about pdu mode go http://www.simplecodestuffs.com/what-is-pdu-in-android/
			 */
			Object[] pdus = (Object[]) bundle.get("pdus");

			SmsMessage message = SmsMessage.createFromPdu((byte[]) pdus[0]);
			smsBody = message.getMessageBody();
			fromPhoneNum = message.getOriginatingAddress();

			// Child's mobile receive turning GPS Keyword
			if (smsBody.equals("gps"))// && fromPhoneNum.equals("+2"+new UserData(context).getParentPhoneNum())) 
			{
				intent = new Intent(context, GPSLocationService.class);
				intent.putExtra("fromPhoneNum", fromPhoneNum);
				Log.i("SMSReceiver", "startService for gps");
				context.startService(intent);
			}
			// ////////////////////////////////////////////////////////
			// Parent's mobile receive location SMS
			else if (smsBody.startsWith("Location")) 
			{
				intent = new Intent(context, ChildLocationActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("smsBody", smsBody);
		
				intent.putExtra("fromPhoneNum", fromPhoneNum);
				context.startActivity(intent);
			}

		}

	}

}
