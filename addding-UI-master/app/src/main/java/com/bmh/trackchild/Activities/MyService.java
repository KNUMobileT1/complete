package com.bmh.trackchild.Activities;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.jcmore2.shakeit.ShakeIt;
import com.jcmore2.shakeit.ShakeListener;

public class MyService extends Service {

    void SendSMS(String number, String msg) {
        SmsManager sms = SmsManager.getDefault();
        Log.d("test1", "sms1");
        sms.sendTextMessage(number, null, msg, null, null);
        Log.d("test1", "sms2");
    }

    public MyService() {
    }


    @Override
    public void onCreate() {
        super.onCreate();

        // Toast.makeText(getApplicationContext(),"shake",Toast.LENGTH_SHORT).show();
        // ShakeIt.initializeShakeService(this, new ShakeListener() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ShakeIt.initializeShakeService(this, 50, 200, new ShakeListener() {

            @Override
            public void onShake(float force) {
                SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
                String phoneNumber = pref.getString("phone", "");
                String context = "살려주세요";

                SendSMS(phoneNumber, context);


            }

            @Override
            public void onAccelerationChanged(float x, float y, float z) {

            }
        });
        return super.onStartCommand(intent, flags, startId);
        /*
                Toast.makeText(getApplicationContext(),"shake",Toast.LENGTH_SHORT).show();
                Log.d("test","shake");
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"
                        + phoneNumber)));*/

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("test1", "program exit");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}