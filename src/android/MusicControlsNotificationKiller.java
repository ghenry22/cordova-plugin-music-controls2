package com.homerours.musiccontrols;

import android.app.Notification;
import android.app.Service;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

//import android.os.Binder;

public class MusicControlsNotificationKiller extends Service {

	private static int NOTIFICATION_ID;
	private NotificationManager mNM;
	private final IBinder mBinder = new KillBinder(this);

	@Override
	public IBinder onBind(Intent intent) {
		this.NOTIFICATION_ID = intent.getIntExtra("notificationID", 1);
		return mBinder;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return Service.START_STICKY;
	}

	@Override
	public void onCreate() {
		mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		mNM.cancel(NOTIFICATION_ID);
	}

	@Override
	public void onDestroy() {
		mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		mNM.cancel(NOTIFICATION_ID);
	}

	public void setForeground(Notification notification) {
		this.startForeground(this.NOTIFICATION_ID, notification);
	}

	private void setForegroundService(int state) {
		if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
			return;
		}
		// Using STOP_FOREGROUND_DETACH instead of STOP_FOREGROUND_REMOVE will keep the
		// notification alive
		this.stopForeground(state);
	}

	public void clearForeground() {
		// using STOP_FOREGROUND_DETACH instead of STOP_FOREGROUND_REMOVE will keep the notification alive
		this.setForegroundService(STOP_FOREGROUND_DETACH);
	}

	public void killForeground() {
		// Using STOP_FOREGROUND_DETACH instead of STOP_FOREGROUND_REMOVE will keep the notification alive
		this.setForegroundService(STOP_FOREGROUND_REMOVE);
	}
}
