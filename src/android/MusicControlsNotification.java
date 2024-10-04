package com.homerours.musiccontrols;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Notification.CarExtender;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.media.session.MediaSession.Token;
import android.util.Log;

import org.apache.cordova.CordovaInterface;
import java.util.List;


//import java.util.UUID;
//import java.util.Random;
//import android.R;
//import android.os.Bundle;


public class MusicControlsNotification {
	private Activity cordovaActivity;
	private NotificationManager notificationManager;
	private Notification.Builder notificationBuilder;
	private int notificationID;
	protected MusicControlsInfos infos;
	private Bitmap bitmapCover;
	private String CHANNEL_ID;
	private Token token;

	// Public Constructor
	public MusicControlsNotification(Activity cordovaActivity, int id, Token token) {
		this.CHANNEL_ID = "MusicControls2-Channel"; // UUID.randomUUID().toString() channel ID should be unique per app and constant
		this.notificationID = id;
		this.cordovaActivity = cordovaActivity;
		Context context = cordovaActivity;
		this.token = token;
		this.notificationManager = (NotificationManager) cordovaActivity.getSystemService(Context.NOTIFICATION_SERVICE);

		// use channelID for Oreo and higher
		if (Build.VERSION.SDK_INT >= 26) {
			// check if chan name is correct and only clear if not
			// Clean up old duplication notification channels
			List<NotificationChannel> chanList = this.notificationManager.getNotificationChannels();

			if (!chanList.isEmpty() && chanList.get(0).getId() != this.CHANNEL_ID) {
				for (int i = 0; i < chanList.size(); i++) {
					// System.out.println(list.get(i));
					String chanID = chanList.get(i).getId();
					this.notificationManager.deleteNotificationChannel(chanID);
				}
			}

			// The user-visible name of the channel.
			CharSequence name = "Audio Controls";
			// The user-visible description of the channel.
			String description = "Control Playing Audio";

			//Low is important to avoid notifications sounds
			int importance = NotificationManager.IMPORTANCE_LOW;

			NotificationChannel mChannel = new NotificationChannel(this.CHANNEL_ID, name, importance);

			// Configure the notification channel.
			mChannel.setDescription(description);

			// Don't show badges for this channel
			mChannel.setShowBadge(false);

			//Don't apply sound to the channel
			mChannel.setSound(null, null);

			this.notificationManager.createNotificationChannel(mChannel);
		}
	}

	// Show or update notification
	public void updateNotification(MusicControlsInfos newInfos) {
		// Check if the cover has changed
		if (!newInfos.cover.isEmpty() && (this.infos == null || !newInfos.cover.equals(this.infos.cover))) {
			Context context = this.cordovaActivity;
			this.bitmapCover = BitmapUtils.getBitmapCover(context, newInfos.cover);
		}
		this.infos = newInfos;
		this.createBuilder();
		Notification noti = this.notificationBuilder.build();
		Log.d("MusicControls","Notification Title"+ noti.extras.getString(noti.EXTRA_TITLE,"empty"));
		this.notificationManager.notify(this.notificationID, noti);
		this.onNotificationUpdated(noti);
	}

	// Toggle the play/pause button
	public void updateIsPlaying(boolean isPlaying) {
		if(!notificationInitialized()) {
			return;
		}
		this.infos.isPlaying = isPlaying;
		this.createBuilder();
		Notification noti = this.notificationBuilder.build();
		this.notificationManager.notify(this.notificationID, noti);
		this.onNotificationUpdated(noti);
	}

	// Toggle the dismissable status
	public void updateDismissable(boolean dismissable) {
		if(!notificationInitialized()) {
			return;
		}
		this.infos.dismissable = dismissable;
		this.createBuilder();
		Notification noti = this.notificationBuilder.build();
		this.notificationManager.notify(this.notificationID, noti);
		this.onNotificationUpdated(noti);
	}

	public boolean notificationInitialized() {
		/*
		 * The infos object is defined on the updateNotification method and is needed for the other methods.
		 * In case it doesn't exist it means the create action on the MusicControl was never called and thus we shouldn't update anything.
		 */
		return this.infos != null;
	}

	private CarExtender prepareCarExtenderNotification() {
		CarExtender carExtenderNotification = new CarExtender();
		if (!infos.cover.isEmpty() && this.bitmapCover != null) {
			carExtenderNotification.setLargeIcon(this.bitmapCover);
		}

		return carExtenderNotification;
	}

	private void createBuilder() {
		Context context = cordovaActivity;
		Notification.Builder builder = new Notification.Builder(context);

		// use channelID for Oreo and higher
		if (Build.VERSION.SDK_INT >= 26) {
			builder.setChannelId(this.CHANNEL_ID);
		}

		// Configure builder
		builder.setContentTitle(infos.track);
		if (!infos.artist.isEmpty()) {
			builder.setContentText(infos.artist);
		}
		builder.setWhen(0);

		// set if the notification can be destroyed by swiping
		if (infos.dismissable) {
			builder.setOngoing(false);
			Intent dismissIntent = new Intent("music-controls-destroy");
			dismissIntent.setPackage(context.getPackageName());
			PendingIntent dismissPendingIntent = PendingIntent.getBroadcast(context, 1, dismissIntent,
					Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ? PendingIntent.FLAG_IMMUTABLE : 0);
			builder.setDeleteIntent(dismissPendingIntent);
		} else {
			builder.setOngoing(true);
		}
		if (!infos.ticker.isEmpty()) {
			builder.setTicker(infos.ticker);
		}

		builder.setPriority(Notification.PRIORITY_MAX);

		// If 5.0 >= set the controls to be visible on lockscreen
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
			builder.setVisibility(Notification.VISIBILITY_PUBLIC);
		}

		// Set SmallIcon
		boolean usePlayingIcon = infos.notificationIcon.isEmpty();
		if (!usePlayingIcon) {
			int resId = this.getResourceId(infos.notificationIcon, 0);
			usePlayingIcon = resId == 0;
			if (!usePlayingIcon) {
				builder.setSmallIcon(resId);
			}
		}

		if (usePlayingIcon) {
			if (infos.isPlaying) {
				builder.setSmallIcon(this.getResourceId(infos.playIcon, android.R.drawable.ic_media_play));
			} else {
				builder.setSmallIcon(this.getResourceId(infos.pauseIcon, android.R.drawable.ic_media_pause));
			}
		}

		// color the notification based on coverart in Android 8+
		builder.setColorized(true);

		// Set LargeIcon
		if (!infos.cover.isEmpty() && this.bitmapCover != null) {
			builder.setLargeIcon(this.bitmapCover);
			builder.extend(prepareCarExtenderNotification());
		}

		// Open app if tapped
		Intent resultIntent = new Intent(context, cordovaActivity.getClass());
		resultIntent.setAction(Intent.ACTION_MAIN);
		resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent,
				Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ? PendingIntent.FLAG_IMMUTABLE : 0);
		builder.setContentIntent(resultPendingIntent);

		// Controls
		int nbControls = 0;

		if (infos.hasPrev) {
			/* Previous */
			nbControls++;
			Intent previousIntent = new Intent("music-controls-previous");
			previousIntent.setPackage(context.getPackageName());
			PendingIntent previousPendingIntent = PendingIntent.getBroadcast(context, 1, previousIntent,
					Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ? PendingIntent.FLAG_IMMUTABLE : 0);
			builder.addAction(this.getResourceId(infos.prevIcon, android.R.drawable.ic_media_previous), "",
					previousPendingIntent);
		}
		if (infos.isPlaying) {
			/* Pause */
			nbControls++;
			Intent pauseIntent = new Intent("music-controls-pause");
			pauseIntent.setPackage(context.getPackageName());
			PendingIntent pausePendingIntent = PendingIntent.getBroadcast(context, 1, pauseIntent,
					Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ? PendingIntent.FLAG_IMMUTABLE : 0);
			builder.addAction(this.getResourceId(infos.pauseIcon, android.R.drawable.ic_media_pause), "",
					pausePendingIntent);
		} else {
			/* Play */
			nbControls++;
			Intent playIntent = new Intent("music-controls-play");
			playIntent.setPackage(context.getPackageName());
			PendingIntent playPendingIntent = PendingIntent.getBroadcast(context, 1, playIntent,
					Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ? PendingIntent.FLAG_IMMUTABLE : 0);
			builder.addAction(this.getResourceId(infos.playIcon, android.R.drawable.ic_media_play), "",
					playPendingIntent);
		}

		if (infos.hasNext) {
			/* Next */
			nbControls++;
			Intent nextIntent = new Intent("music-controls-next");
			nextIntent.setPackage(context.getPackageName());
			PendingIntent nextPendingIntent = PendingIntent.getBroadcast(context, 1, nextIntent,
					Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ? PendingIntent.FLAG_IMMUTABLE : 0);
			builder.addAction(this.getResourceId(infos.nextIcon, android.R.drawable.ic_media_next), "",
					nextPendingIntent);
		}
		if (infos.hasClose) {
			/* Close */
			nbControls++;
			Intent destroyIntent = new Intent("music-controls-destroy");
			destroyIntent.setPackage(context.getPackageName());
			PendingIntent destroyPendingIntent = PendingIntent.getBroadcast(context, 1, destroyIntent,
					Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ? PendingIntent.FLAG_IMMUTABLE : 0);
			builder.addAction(this.getResourceId(infos.closeIcon, android.R.drawable.ic_menu_close_clear_cancel), "",
					destroyPendingIntent);
		}

		// If 5.0 >= use MediaStyle
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
			int[] args = new int[nbControls];
			for (int i = 0; i < nbControls; ++i) {
				args[i] = i;
			}
			builder.setStyle(
					new Notification.MediaStyle().setShowActionsInCompactView(args).setMediaSession(this.token));
		}
		this.notificationBuilder = builder;
	}

	private int getResourceId(String name, int fallback) {
		try {
			if (name.isEmpty()) {
				return fallback;
			}

			int resId = this.cordovaActivity.getResources().getIdentifier(name, "drawable",
					this.cordovaActivity.getPackageName());
			return resId == 0 ? fallback : resId;
		} catch (Exception ex) {
			return fallback;
		}
	}

	public void destroy() {
		this.notificationManager.cancel(this.notificationID);
		this.onNotificationDestroyed();
	}

	protected void onNotificationUpdated(Notification notification) {
	}

	protected void onNotificationDestroyed() {
	}
}
