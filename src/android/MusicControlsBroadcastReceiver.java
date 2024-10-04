package com.homerours.musiccontrols;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.media.AudioManager;
import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;

//import android.app.Activity;
//import android.os.Bundle;

public class MusicControlsBroadcastReceiver extends BroadcastReceiver {
	private CallbackContext cb;
	private MusicControls musicControls;

	public MusicControlsBroadcastReceiver(MusicControls musicControls) {
		this.musicControls = musicControls;
	}

	public void setCallback(CallbackContext cb) {
		this.cb = cb;
	}

	public void sendMessage(String message) {
		sendMessage(message, null);
	}

	public void sendMessage(String message, String extra) {
		cb = CallbackUtils.sendMessage(cb, message, extra);
	}

	public void stopListening() {
		sendMessage(CallbackUtils.MUSIC_CONTROLS_STOP_LISTENING);
	}

	public void handleHeadSetState(int state) {
		switch (state) {
			case 0:
				sendMessage(CallbackUtils.MUSIC_CONTROLS_HEADSET_UNPLUGGED);
				//this.musicControls.unregisterMediaButtonEvent();
				break;
			case 1:
				sendMessage(CallbackUtils.MUSIC_CONTROLS_HEADSET_PLUGGED);
				//this.musicControls.registerMediaButtonEvent();
				break;
			default:
				break;
		}
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("MusicControls", "Broadcast on Receive");
		if (this.cb != null) {
			String message = intent.getAction();
			Log.d("MusicControls", "Broadcast on Receive - Message : " + message);

			switch (message) {
				case Intent.ACTION_HEADSET_PLUG:
					int state = intent.getIntExtra("state", -1);
					handleHeadSetState(state);
					break;

				case AudioManager.ACTION_AUDIO_BECOMING_NOISY:
					sendMessage(CallbackUtils.MUSIC_CONTROLS_AUDIO_BECOMING_NOISY);
					break;

				case CallbackUtils.MUSIC_CONTROLS_MEDIA_BUTTON:
					// Media button
					KeyEvent event = (KeyEvent) intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
					if (event.getAction() == KeyEvent.ACTION_DOWN) {

						int keyCode = event.getKeyCode();
						String mediaButtonMessage = CallbackUtils.switchMediaButtonKeyCode(keyCode);

						if (mediaButtonMessage == CallbackUtils.MUSIC_CONTROLS_MEDIA_BUTTON_UNKNOWN) {
							sendMessage(mediaButtonMessage, Integer.toString(keyCode));
						} else {
							sendMessage(mediaButtonMessage);
						}
					}
					break;

				case CallbackUtils.MUSIC_CONTROLS_DESTROY:
					sendMessage(CallbackUtils.MUSIC_CONTROLS_DESTROY);
					this.musicControls.destroyPlayerNotification();
					break;

				default:
					sendMessage(message);
					break;
			}

		}

	}
}
