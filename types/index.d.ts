declare namespace MediaControls {
    interface CreateOptions {
        track?: string;
        artist?: string;
        album?: string;
        cover?: string|null;
        isPlaying?: boolean;
        dismissable?: boolean;
        hasPrev?: boolean;
        hasNext?: boolean;
        hasClose?: boolean;

        // iOS only
        duration?: number;
        elapsed?: number;
        hasSkipForward?: boolean;
        hasSkipBackward?: boolean;
        skipForwardInterval?: number;
        skipBackwardInterval?: number;
        hasScrubbing?: false;

        // Android only
        ticker?: string;
        playIcon?: string;
        pauseIcon?: string;
        prevIcon?: string;
        nextIcon?: string;
        closeIcon?: string;
        notificationIcon?: string;
    }

    type SuccessCallback = (arg: ['success']) => void;

    type MediaControlErrorCallback = (err: Error) => void;

    // The event callback receives a string, which is a JSON-encoded Event.
    type EventSubscriber = (event: string) => void|Promise<void>;

    type Event = {
        message: 'music-controls-play'
            |'music-controls-pause'
            |'music-controls-previous'
            |'music-controls-next'
            |'music-controls-stop-listening'
            |'music-controls-destroy'
            |'music-controls-toggle-play-pause'
            |'music-controls-seek-to'
            |'music-controls-media-button'
            |'music-controls-headset-unplugged'
            |'music-controls-headset-plugged'

            // Media buttons defined for Android
            |'music-controls-media-button-next'
            |'music-controls-media-button-pause'
            |'music-controls-media-button-play'
            |'music-controls-media-button-play-pause'
            |'music-controls-media-button-previous'
            |'music-controls-media-button-stop'
            |'music-controls-media-button-fast-forward'
            |'music-controls-media-button-rewind'
            |'music-controls-media-button-skip-backward'
            |'music-controls-media-button-skip-forward'
            |'music-controls-media-button-step-backward'
            |'music-controls-media-button-step-forward'
            |'music-controls-media-button-meta-left'
            |'music-controls-media-button-meta-right'
            |'music-controls-media-button-music'
            |'music-controls-media-button-volume-up'
            |'music-controls-media-button-volume-down'
            |'music-controls-media-button-volume-mute'
            |'music-controls-media-button-headset-hook'

            // Media buttons defined for iOS
            |'music-controls-skip-forward'
            |'music-controls-skip-backward';
    };

    interface UpdateElapsed {
        elapsed: number;
        isPlaying: boolean;
    }
}

declare var MusicControls: {
    /**
     * Create the media control.
     */
    create(
        options: MediaControls.CreateOptions,
        onSuccess: MediaControls.SuccessCallback,
        onError: MediaControls.MediaControlErrorCallback,
    ): void;

    /**
     * Destroy the media controller.
     */
    destroy(
        onSuccess: MediaControls.SuccessCallback,
        onError: MediaControls.MediaControlErrorCallback,
    ): void;

    /**
     * Register the callback for subscribing to the media controller events.
     */
    subscribe(subscriber: MediaControls.EventSubscriber): void;

    /**
     * Start listening for events.
     */
    listen(): void;

    /**
     * Toggle play/pause.
     */
    updateIsPlaying(playing: boolean): void;

    /**
     * Update whether the notification is dismissable.
     */
    updateDismissable(dismissable: boolean): void;

    /***
     * iOS only: Allows you to listen for iOS events fired from the scrubber in control center.
     */
    updateElapsed(update: MediaControls.UpdateElapsed): void;
};
