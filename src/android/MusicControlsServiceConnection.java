package com.homerours.musiccontrols;

import android.app.Activity;
import android.app.Notification;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
//import android.util.Log;

public class MusicControlsServiceConnection implements ServiceConnection {
    protected MusicControlsNotificationKiller service;
    protected Activity activity;

    MusicControlsServiceConnection(Activity activity) {
        this.activity = activity;
    }

    public void onServiceConnected(ComponentName className, IBinder binder) {
        service = ((KillBinder) binder).service;
        service.startService(new Intent(activity, MusicControlsNotificationKiller.class));
    }

    public void onServiceDisconnected(ComponentName className) {
    }

    void setNotification(Notification notification, boolean isPlaying) {
        if (this.service == null) {
            return;
        }
        this.service.setForeground(notification);

       /* if (isPlaying) {
            this.service.setForeground(notification);
        } else {
            this.service.clearForeground();
        } */
    }

    void killNotification() {
        if (this.service == null) {
            return;
        }
        this.service.killForeground();
    }
}
