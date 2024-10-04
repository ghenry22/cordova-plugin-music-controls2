package com.homerours.musiccontrols;

import org.apache.cordova.CallbackContext;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;

class CallbackUtils {

    @Nullable
    public static CallbackContext sendMessage(CallbackContext cb, String message, @Nullable String extra) {
        Log.d("MusicControls", "Trying cordova callback: "+message);
        if (cb != null) {
            String cbStr = "{\"message\": \"" + message + "\"";
            if (extra != null && extra != "") {
                cbStr = cbStr + ", \"extra\": \"" + extra + "\"";
            }
            cbStr = cbStr + "}";
            cb.success(cbStr);
            Log.d("MusicControls", "Success cordova callback: "+message);
        }
        return null;
    }

    @Nullable
    public static CallbackContext sendMessage(CallbackContext cb, String message) {
        return sendMessage(cb, message, null);
    }

    public static final String MUSIC_CONTROLS_PLAY = "music-controls-play";
    public static final String MUSIC_CONTROLS_PAUSE = "music-controls-pause";
    public static final String MUSIC_CONTROLS_PREVIOUS = "music-controls-previous";
    public static final String MUSIC_CONTROLS_NEXT = "music-controls-next";
    public static final String MUSIC_CONTROLS_SEEK_TO = "music-controls-seek-to";
    public static final String MUSIC_CONTROLS_SET_SHUFFLE_MODE = "music-controls-set-shuffle-mode";

    public static final String MUSIC_CONTROLS_DESTROY = "music-controls-destroy";
    public static final String MUSIC_CONTROLS_STOP_LISTENING = "music-controls-stop-listening";

    public static final String MUSIC_CONTROLS_HEADSET_UNPLUGGED = "music-controls-headset-unplugged";
    public static final String MUSIC_CONTROLS_HEADSET_PLUGGED = "music-controls-headset-plugged";
    public static final String MUSIC_CONTROLS_AUDIO_BECOMING_NOISY = "music-controls-audio-becoming-noisy";

    public static final String MUSIC_CONTROLS_MEDIA_BUTTON = "music-controls-media-button";
    public static final String MUSIC_CONTROLS_MEDIA_BUTTON_NEXT = "music-controls-media-button-next";
    public static final String MUSIC_CONTROLS_MEDIA_BUTTON_PREVIOUS = "music-controls-media-button-previous";
    public static final String MUSIC_CONTROLS_MEDIA_BUTTON_PAUSE = "music-controls-media-button-pause";
    public static final String MUSIC_CONTROLS_MEDIA_BUTTON_PLAY = "music-controls-media-button-play";
    public static final String MUSIC_CONTROLS_MEDIA_BUTTON_PLAY_PAUSE = "music-controls-media-button-play-pause";
    public static final String MUSIC_CONTROLS_MEDIA_BUTTON_STOP = "music-controls-media-button-stop";
    public static final String MUSIC_CONTROLS_MEDIA_BUTTON_FAST_FORWARD = "music-controls-media-button-fast-forward";
    public static final String MUSIC_CONTROLS_MEDIA_BUTTON_REWIND = "music-controls-media-button-rewind";
    public static final String MUSIC_CONTROLS_MEDIA_BUTTON_SKIP_BACKWARD = "music-controls-media-button-skip-backward";
    public static final String MUSIC_CONTROLS_MEDIA_BUTTON_SKIP_FORWARD = "music-controls-media-button-skip-forward";
    public static final String MUSIC_CONTROLS_MEDIA_BUTTON_STEP_BACKWARD = "music-controls-media-button-step-backward";
    public static final String MUSIC_CONTROLS_MEDIA_BUTTON_STEP_FORWARD = "music-controls-media-button-step-forward";
    public static final String MUSIC_CONTROLS_MEDIA_BUTTON_META_LEFT = "music-controls-media-button-meta-left";
    public static final String MUSIC_CONTROLS_MEDIA_BUTTON_META_RIGHT = "music-controls-media-button-meta-right";
    public static final String MUSIC_CONTROLS_MEDIA_BUTTON_MUSIC = "music-controls-media-button-music";
    public static final String MUSIC_CONTROLS_MEDIA_BUTTON_VOLUME_UP = "music-controls-media-button-volume-up";
    public static final String MUSIC_CONTROLS_MEDIA_BUTTON_VOLUME_DOWN = "music-controls-media-button-volume-down";
    public static final String MUSIC_CONTROLS_MEDIA_BUTTON_VOLUME_MUTE = "music-controls-media-button-volume-mute";
    public static final String MUSIC_CONTROLS_MEDIA_BUTTON_HEADSET_HOOK = "music-controls-media-button-headset-hook";
    public static final String MUSIC_CONTROLS_MEDIA_BUTTON_UNKNOWN = "music-controls-media-button-unknown";

    public static String switchMediaButtonKeyCode(int keyCode) {
        String result = "";

        switch (keyCode) {
            case KeyEvent.KEYCODE_MEDIA_PAUSE:
                result = CallbackUtils.MUSIC_CONTROLS_MEDIA_BUTTON_PAUSE;
                break;
            case KeyEvent.KEYCODE_MEDIA_PLAY:
                result = CallbackUtils.MUSIC_CONTROLS_MEDIA_BUTTON_PLAY;
                break;
            case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                result = CallbackUtils.MUSIC_CONTROLS_MEDIA_BUTTON_PREVIOUS;
                break;
            case KeyEvent.KEYCODE_MEDIA_NEXT:
                result = CallbackUtils.MUSIC_CONTROLS_MEDIA_BUTTON_NEXT;
                break;
            case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                result = CallbackUtils.MUSIC_CONTROLS_MEDIA_BUTTON_PLAY_PAUSE;
                break;
            case KeyEvent.KEYCODE_MEDIA_STOP:
                result = CallbackUtils.MUSIC_CONTROLS_MEDIA_BUTTON_STOP;
                break;
            case KeyEvent.KEYCODE_MEDIA_FAST_FORWARD:
                result = CallbackUtils.MUSIC_CONTROLS_MEDIA_BUTTON_FAST_FORWARD;
                break;
            case KeyEvent.KEYCODE_MEDIA_REWIND:
                result = CallbackUtils.MUSIC_CONTROLS_MEDIA_BUTTON_REWIND;
                break;
            case KeyEvent.KEYCODE_MEDIA_SKIP_BACKWARD:
                result = CallbackUtils.MUSIC_CONTROLS_MEDIA_BUTTON_SKIP_BACKWARD;
                break;
            case KeyEvent.KEYCODE_MEDIA_SKIP_FORWARD:
                result = CallbackUtils.MUSIC_CONTROLS_MEDIA_BUTTON_SKIP_FORWARD;
                break;
            case KeyEvent.KEYCODE_MEDIA_STEP_BACKWARD:
                result = CallbackUtils.MUSIC_CONTROLS_MEDIA_BUTTON_STEP_BACKWARD;
                break;
            case KeyEvent.KEYCODE_MEDIA_STEP_FORWARD:
                result = CallbackUtils.MUSIC_CONTROLS_MEDIA_BUTTON_STEP_FORWARD;
                break;
            case KeyEvent.KEYCODE_META_LEFT:
                result = CallbackUtils.MUSIC_CONTROLS_MEDIA_BUTTON_META_LEFT;
                break;
            case KeyEvent.KEYCODE_META_RIGHT:
                result = CallbackUtils.MUSIC_CONTROLS_MEDIA_BUTTON_META_RIGHT;
                break;
            case KeyEvent.KEYCODE_MUSIC:
                result = CallbackUtils.MUSIC_CONTROLS_MEDIA_BUTTON_MUSIC;
                break;
            case KeyEvent.KEYCODE_VOLUME_UP:
                result = CallbackUtils.MUSIC_CONTROLS_MEDIA_BUTTON_VOLUME_UP;
                break;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                result = CallbackUtils.MUSIC_CONTROLS_MEDIA_BUTTON_VOLUME_DOWN;
                break;
            case KeyEvent.KEYCODE_VOLUME_MUTE:
                result = CallbackUtils.MUSIC_CONTROLS_MEDIA_BUTTON_VOLUME_MUTE;
                break;
            case KeyEvent.KEYCODE_HEADSETHOOK:
                result = CallbackUtils.MUSIC_CONTROLS_MEDIA_BUTTON_HEADSET_HOOK;
                break;
            default:
                result = CallbackUtils.MUSIC_CONTROLS_MEDIA_BUTTON_UNKNOWN;
                break;
        }

        return result;
    }
}