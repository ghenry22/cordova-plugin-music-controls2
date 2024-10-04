package com.homerours.musiccontrols;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.KeyEvent;
import android.util.Log;

import org.apache.cordova.CallbackContext;

/*
* The onMediaButtonEvent method is called when using external devices, such as bluetooth devices.
* The other methods like OnPlay, OnPause and etc are called from the notification itself.
* Couldn't find where the BroadcastReceiver class is used, but apparently it is used for wired headsets (?).
*/

public class MediaSessionCallback extends MediaSessionCompat.Callback {

  private CallbackContext cb;
  private MediaSessionCompat mediaSessionCompat;

  public MediaSessionCallback(MediaSessionCompat mediaSessionCompat) {
    this.mediaSessionCompat = mediaSessionCompat;
  }

  public void setCallback(CallbackContext cb) {
    this.cb = cb;
  }

  public void sendMessage(String message) {
    sendMessage(message, null);
  }

  public void sendMessage(String message, String extra) {
    CallbackUtils.sendMessage(cb, message, extra);
  }

  @Override
  public void onPlay() {
    Log.d("MusicControls", "On Play");
    super.onPlay();
    handlePlay();
  }

  @Override
  public void onPause() {
    Log.d("MusicControls", "On Pause");
    super.onPause();
    handlePause();
  }

  @Override
  public void onSkipToNext() {
    Log.d("MusicControls", "On Skip to Next");
    super.onSkipToNext();
    sendMessage(CallbackUtils.MUSIC_CONTROLS_NEXT);
  }

  @Override
  public void onSkipToPrevious() {
    Log.d("MusicControls", "On Skip To Previous");
    super.onSkipToPrevious();
    sendMessage(CallbackUtils.MUSIC_CONTROLS_PREVIOUS);
  }

  @Override
  public void onSeekTo(long pos) {
    Log.d("MusicControls", "On Seek To");
    super.onSeekTo(pos);
    sendMessage(CallbackUtils.MUSIC_CONTROLS_SEEK_TO, Long.toString(pos));
  }

  @Override
  public void onPlayFromMediaId(String mediaId, Bundle extras) {
    Log.d("MusicControls", "On Play From Media");
    super.onPlayFromMediaId(mediaId, extras);
  }

  @Override
  public void onSetShuffleMode(int shuffleMode) {
    Log.d("MusicControls", "On Set Shuffle Mode");
    super.onSetShuffleMode(shuffleMode);
    sendMessage(CallbackUtils.MUSIC_CONTROLS_SET_SHUFFLE_MODE, Long.toString(shuffleMode));
  }

  @Override
  public void onCustomAction(String action, Bundle extras) {
    Log.d("MusicControls", "Unknown custom action " + action);
    super.onCustomAction(action, extras);
  }

  @Override
  public boolean onMediaButtonEvent(Intent mediaButtonIntent) {
    Log.d("MusicControls", "on Media Button Event");
    final KeyEvent event = (KeyEvent) mediaButtonIntent.getExtras().get(Intent.EXTRA_KEY_EVENT);
    if (event == null) {
      return super.onMediaButtonEvent(mediaButtonIntent);
    }

    Log.d("MusicControls", "on Media Button Event - Action: " + event.getAction());

    if (event.getAction() == KeyEvent.ACTION_DOWN) {
      final int keyCode = event.getKeyCode();
      final String message = CallbackUtils.switchMediaButtonKeyCode(keyCode);
      Log.d("MusicControls", "onMedia Button Event - Key Code: " + keyCode);

      if (keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE ||
          keyCode == KeyEvent.KEYCODE_MEDIA_PLAY ||
          keyCode == KeyEvent.KEYCODE_MEDIA_PAUSE) {
        if (isPlaying()) {
          handlePause();
        } else {
          handlePlay();
        }
      } else if (message == CallbackUtils.MUSIC_CONTROLS_MEDIA_BUTTON_UNKNOWN) {
        sendMessage(message, Integer.toString(keyCode));
      } else {
        sendMessage(message);
      }

      return super.onMediaButtonEvent(mediaButtonIntent);
    }

    return false;
  }

  private boolean isPlaying() {
    Log.d("MusicControls", "checking playing status against playback state");
    boolean result = false;
    PlaybackStateCompat playbackState = mediaSessionCompat.getController().getPlaybackState();

    if (playbackState != null) {
      int state = playbackState.getState();

      switch (state) {
        case PlaybackStateCompat.STATE_PLAYING:
          result = true;
          Log.d("MusicControls", "The media is playing");
          break;
        case PlaybackStateCompat.STATE_PAUSED:
          Log.d("MusicControls", "The media is paused");
          break;
        default:
          Log.d("PlaybackState", "Unknown state.");
          break;
      }
    }

    return result;
  }

  private void updatePlayState(boolean isPlaying) {
    PlaybackStateCompat currentPlaybackState = mediaSessionCompat.getController().getPlaybackState();
    long currentPosition = currentPlaybackState.getPosition();
    long actions = currentPlaybackState.getActions();
    int state = isPlaying ? PlaybackStateCompat.STATE_PLAYING : PlaybackStateCompat.STATE_PAUSED;
    this.mediaSessionCompat.setPlaybackState(new PlaybackStateCompat.Builder()
        .setActions(actions)
        .setState(state, currentPosition, 0.0f)
        .build());

  }

  private void handlePlay() {
    Log.d("MusicControls", "handlePlay - updatePlayState ");
    updatePlayState(true);
    sendMessage(CallbackUtils.MUSIC_CONTROLS_PLAY);
  }

  private void handlePause() {
    Log.d("MusicControls", "handlePause - updatePlayState ");
    updatePlayState(false);
    sendMessage(CallbackUtils.MUSIC_CONTROLS_PAUSE);
  }

}