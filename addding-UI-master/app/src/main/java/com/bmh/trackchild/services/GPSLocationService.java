package com.bmh.trackchild.services;

import com.bmh.trackchild.helper.LocationHelper;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;


public class GPSLocationService extends Service {

    LocationHelper myLocationHelper;
    LocationWorker locationTask;
    double longitude, latitude;
    String fromPhoneNum;


    @Override
    public void onCreate() { //
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {

        Log.i("GPSLocationService", "service is at onstart");
        //the phone number received is Parent's phone number
        //that child's device will send to it SMS with its location
        fromPhoneNum = intent.getStringExtra("fromPhoneNum");

        myLocationHelper = new LocationHelper(this);

        //create new async task for fetching location and execute it
        locationTask = new LocationWorker();
        locationTask.execute();

    }

    private void sendSMS(String phoneNumber, String message) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    class LocationWorker extends AsyncTask<Void, Void, Boolean> {
        // (Boolean result) points to the return of doInBackground method
        @Override
        protected void onPostExecute(Boolean result) {
				/* Here you can call myLocationHelper.getLat() and
		        myLocationHelper.getLong() to get the location data.*/
            longitude = myLocationHelper.getLong();
            latitude = myLocationHelper.getLat();
            Log.i("GPSLocationService", longitude + "  " + latitude);

            sendSMS(fromPhoneNum, "Location:" + longitude + ":" + latitude);
        }


        @Override
        protected Boolean doInBackground(Void... params) {
            //while the location helper has not got a lock
            while (!myLocationHelper.gotLocation()) {
                //do nothing, just wait
                Log.i("GPSLocationService", "service getting location");
                //showToast("false");
            }
            //once done return true
            return true;
        }
    }

}
