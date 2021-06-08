package com.yasir.musicstation.tv.player;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.SystemClock;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MediaPlayerHolder {

    public static final int SEEKBAR_REFRESH_INTERVAL_MS = 500;

    private final MediaPlayer mMediaPlayer;
    private Context mContext;
    private ArrayList<String> mLogMessages = new ArrayList<>();
    private ScheduledExecutorService mExecutor;
    private Runnable mSeekbarProgressUpdateTask;
    private String urlPath = "";

    public MediaPlayerHolder(Context context) {
        mContext = context;
        EventBus.getDefault().register(this);
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                stopUpdatingSeekbarWithPlaybackProgress(true);
                logToUI("MediaPlayer playback completed");
                EventBus.getDefault().post(new LocalEventFromMediaPlayerHolder.PlaybackCompleted());
                EventBus.getDefault()
                        .post(new LocalEventFromMediaPlayerHolder.StateChanged(
                                PlayerState.COMPLETED));
            }
        });
        logToUI("mMediaPlayer = new MediaPlayer()");
    }

    public int getAudioSessionId(){
        if(mMediaPlayer != null){
            return mMediaPlayer.getAudioSessionId();
        } else
            return -1;
    }

    public void release() {
        logToUI("release() and mMediaPlayer = null");
        mMediaPlayer.release();
        EventBus.getDefault().unregister(this);
    }

    public void stop() {
        logToUI("stop() and mMediaPlayer = null");
        mMediaPlayer.stop();
    }

    public void play() {
        if (!mMediaPlayer.isPlaying()) {
            logToUI(String.format("start() %s", urlPath));
            mMediaPlayer.start();
            startUpdatingSeekbarWithPlaybackProgress();
            EventBus.getDefault()
                    .post(new LocalEventFromMediaPlayerHolder.StateChanged(PlayerState.PLAYING));
        }
    }

    public void pause() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            logToUI("pause()");
            EventBus.getDefault()
                    .post(new LocalEventFromMediaPlayerHolder.StateChanged(PlayerState.PAUSED));
        }
    }

    public void reset() {
        logToUI("reset()");
        mMediaPlayer.reset();
        load(urlPath);
        stopUpdatingSeekbarWithPlaybackProgress(true);
        EventBus.getDefault()
                .post(new LocalEventFromMediaPlayerHolder.StateChanged(PlayerState.RESET));
    }

    public void load(String url) {
        this.urlPath = url.replaceAll(" ", "%20");
        if (mMediaPlayer != null) {
            try {
                mMediaPlayer.setAudioAttributes(
                        new AudioAttributes
                                .Builder()
                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                .build());

                mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer player) {
                        initSeekbar();
                        play();
                    }
                });

                mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mp, int what, int extra) {
                        EventBus.getDefault()
                                .post(new LocalEventFromMediaPlayerHolder.StateChanged(PlayerState.ERROR));
                        return false;
                    }
                });

                mMediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                    @Override
                    public void onSeekComplete(MediaPlayer arg0) {
                        EventBus.getDefault().post(new LocalEventFromMediaPlayerHolder.StateChanged(PlayerState.PLAYING));
                        SystemClock.sleep(200);
                        mMediaPlayer.start();
                    }
                });
                logToUI("load() {1. setDataSource}");
                mMediaPlayer.setDataSource(urlPath);
                logToUI("load() {2. prepare}");
                mMediaPlayer.prepareAsync();
            } catch (Exception e) {
                EventBus.getDefault().post(new LocalEventFromMediaPlayerHolder.StateChanged(PlayerState.ERROR));
                logToUI(e.toString());
            }
        }
    }

    public void seekTo(int duration) {
        logToUI(String.format(Locale.ENGLISH, "seekTo() %d ms", duration));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            mMediaPlayer.seekTo(duration, MediaPlayer.SEEK_CLOSEST);
        else
            mMediaPlayer.seekTo(duration);
    }

    private void stopUpdatingSeekbarWithPlaybackProgress(boolean resetUIPlaybackPosition) {
        if (mExecutor != null) {
            mExecutor.shutdownNow();
        }
        mExecutor = null;
        mSeekbarProgressUpdateTask = null;
        if (resetUIPlaybackPosition) {
            EventBus.getDefault().post(new LocalEventFromMediaPlayerHolder.PlaybackPosition(0));
        }
    }

    private void startUpdatingSeekbarWithPlaybackProgress() {
        // Setup a recurring task to sync the mMediaPlayer position with the Seekbar.
        if (mExecutor == null) {
            mExecutor = Executors.newSingleThreadScheduledExecutor();
        }
        if (mSeekbarProgressUpdateTask == null) {
            mSeekbarProgressUpdateTask = new Runnable() {
                @Override
                public void run() {
                    if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                        int currentPosition = mMediaPlayer.getCurrentPosition();
                        EventBus.getDefault().post(
                                new LocalEventFromMediaPlayerHolder.PlaybackPosition(
                                        currentPosition));
                    }
                }
            };
        }
        mExecutor.scheduleAtFixedRate(
                mSeekbarProgressUpdateTask,
                0,
                SEEKBAR_REFRESH_INTERVAL_MS,
                TimeUnit.MILLISECONDS
        );
    }

    public void initSeekbar() {
        // Set the duration.
        final int duration = mMediaPlayer.getDuration();
        EventBus.getDefault().post(new LocalEventFromMediaPlayerHolder.PlaybackDuration(duration));
        logToUI(String.format(Locale.ENGLISH, "setting seekbar max %d sec", TimeUnit.MILLISECONDS.toSeconds(duration)));

    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMessageEvent(LocalEventFromMainActivity.SeekTo event) {
        seekTo(event.position);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMessageEvent(
            LocalEventFromMainActivity.StopUpdatingSeekbarWithMediaPosition event) {
        stopUpdatingSeekbarWithPlaybackProgress(false);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMessageEvent(
            LocalEventFromMainActivity.StartUpdatingSeekbarWithPlaybackPosition event) {
        startUpdatingSeekbarWithPlaybackProgress();
    }

    public void logToUI(String msg) {
        mLogMessages.add(msg);
        fireLogUpdate();
    }

    // Logging to UI methods.

    /**
     * update the MainActivity's UI with the debug log messages
     */
    public void fireLogUpdate() {
        StringBuffer formattedLogMessages = new StringBuffer();
        for (int i = 0; i < mLogMessages.size(); i++) {
            formattedLogMessages.append(i)
                    .append(" - ")
                    .append(mLogMessages.get(i));
            if (i != mLogMessages.size() - 1) {
                formattedLogMessages.append("\n");
            }
        }
        EventBus.getDefault().post(new LocalEventFromMediaPlayerHolder.UpdateLog(formattedLogMessages));
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMessageEvent(LocalEventFromMainActivity.PausePlayback event) {
        pause();
    }

    // Respond to playback localevents.

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMessageEvent(LocalEventFromMainActivity.StartPlayback event) {
        play();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMessageEvent(LocalEventFromMainActivity.ResetPlayback event) {
        reset();
    }

    public enum PlayerState {
        LOADING, PLAYING, PAUSED, COMPLETED, RESET, ERROR
    }

}
