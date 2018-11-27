package com.bmh.trackchild.Tools;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;

public class MediaAlert {

    MediaPlayer mediaPlayer;
    Context mContext;

    public MediaAlert(Context context){

        this.mContext=context;
    }

    public void startAlert() {
        mediaPlayer = MediaPlayer.create(mContext, getAlertUri());
        mediaPlayer.setLooping(true); // Set looping
        mediaPlayer.start();
    }

    public void stopAlert() {
        mediaPlayer.stop();
        mediaPlayer.reset();
        mediaPlayer.release();
        mediaPlayer = MediaPlayer.create(mContext, getAlertUri());
    }

    private Uri getAlertUri() {
        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if (alert == null) {
            // alert is null, using backup
            alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            if (alert == null) {
                // alert backup is null, using 2nd backup
                alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }
        }
        return alert;
    }
}
