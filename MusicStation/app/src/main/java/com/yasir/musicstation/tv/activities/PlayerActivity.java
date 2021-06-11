package com.yasir.musicstation.tv.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gauravk.audiovisualizer.visualizer.CircleLineVisualizer;
import com.yasir.musicstation.tv.R;
import com.yasir.musicstation.tv.models.Song;
import com.yasir.musicstation.tv.player.LocalEventFromMainActivity;
import com.yasir.musicstation.tv.player.LocalEventFromMediaPlayerHolder;
import com.yasir.musicstation.tv.player.MediaPlayerHolder;
import com.yasir.musicstation.tv.utils.DataUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class PlayerActivity extends Activity {

    private ImageView imgIcon;
    private Song song;
    private MediaPlayerHolder mMediaPlayerHolder;
    private boolean isUserSeeking;
    private TextView txtSongName, txtArtistName, txtTotalDuration, txtCurrentTime, txtError, txtCategoryName;
    private CircleLineVisualizer blastVisualizer;
    private SeekBar seekBarAudio;
    private ImageButton btnPlayPause;
    private ProgressBar progressBarLoader;
    private Button btnDismiss;
    private RelativeLayout rlErrorLayout;
    private MediaPlayerHolder.PlayerState currentState = MediaPlayerHolder.PlayerState.LOADING;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        song = (Song) this.getIntent().getParcelableExtra(DataUtil.SONG_DETAIL);

        EventBus.getDefault().register(this);
        mMediaPlayerHolder = new MediaPlayerHolder(this);

        // Init Views
        initViews();

        setUI();
    }

    private void initViews() {
        imgIcon = findViewById(R.id.imgIcon);
        txtSongName = findViewById(R.id.txtSongName);
        txtArtistName = findViewById(R.id.txtArtistName);
        seekBarAudio = findViewById(R.id.seekBarAudio);
        btnPlayPause = findViewById(R.id.btnPlayPause);
        progressBarLoader = findViewById(R.id.progressBarLoader);
        rlErrorLayout = findViewById(R.id.rlErrorLayout);
        btnDismiss = findViewById(R.id.btnDismiss);
        txtTotalDuration = findViewById(R.id.txtTotalDuration);
        txtCurrentTime = findViewById(R.id.txtCurrentTime);
        txtError = findViewById(R.id.txtError);
        blastVisualizer = findViewById(R.id.blastVisualizer);
        txtCategoryName = findViewById(R.id.txtCategoryName);

        seekBarAudio.setPadding(15, 0, 15, 0); // Removing unwanted Padding of SeekBar

        btnPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePlayer();
            }
        });

        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        showLoader();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (blastVisualizer != null)
            blastVisualizer.release();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onPause() {
        if (mMediaPlayerHolder != null) {
            mMediaPlayerHolder.stop();
            mMediaPlayerHolder.release();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        if (song != null) {
            mMediaPlayerHolder.load(song.getSongURL());
        }
        super.onResume();
    }

    public void setupSeekBar() {
        seekBarAudio.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int userSelectedPosition = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    showLoader();
                    userSelectedPosition = progress;
                    isUserSeeking = true;
                    EventBus.getDefault().post(new LocalEventFromMainActivity.SeekTo(userSelectedPosition));
                    updateProgressTime(userSelectedPosition);
                    isUserSeeking = false;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isUserSeeking = false;
                EventBus.getDefault().post(new LocalEventFromMainActivity.SeekTo(
                        userSelectedPosition));
            }
        });
    }

    private void disableSeekBar(){
        seekBarAudio.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void showLoader() {
        if (progressBarLoader != null) {
            progressBarLoader.setVisibility(View.VISIBLE);
            disableUI();
        }
    }

    private void hideLoader() {
        if (progressBarLoader != null) {
            progressBarLoader.setVisibility(View.GONE);
            enableUI();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        hideLoader();
    }

    private void handlePlayer(){
        if (currentState == MediaPlayerHolder.PlayerState.PLAYING) {
            pauseSong();
        } else if (currentState == MediaPlayerHolder.PlayerState.PAUSED ||
                currentState == MediaPlayerHolder.PlayerState.COMPLETED ||
                currentState == MediaPlayerHolder.PlayerState.RESET) {
            playSong();
        }
    }

    private void setUI() {
        if (song != null) {

            txtSongName.setText(song.getSongName());
            txtArtistName.setText(song.getArtistName());
            txtCategoryName.setText(song.getCategoryName());

            Glide.with(this)
                    .load(song.getImageURL())
                    .centerCrop()
                    .circleCrop()
                    .placeholder(R.drawable.app_icon)
                    .error(R.drawable.app_icon)
                    .into(imgIcon);
        }

        setupSeekBar();
    }

    private void pauseSong() {
        EventBus.getDefault().post(new LocalEventFromMainActivity.PausePlayback());
    }

    private void playSong() {
        EventBus.getDefault().post(new LocalEventFromMainActivity.StartPlayback());
    }

    private void resetSong() {
        EventBus.getDefault().post(new LocalEventFromMainActivity.ResetPlayback());
    }

    public void log(StringBuffer formattedMessage) {
        Log.d(PlayerActivity.class.getSimpleName(), String.format("log: %s", formattedMessage));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LocalEventFromMediaPlayerHolder.UpdateLog event) {
        log(event.formattedMessage);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LocalEventFromMediaPlayerHolder.PlaybackDuration event) {
        seekBarAudio.setMax(event.duration);
        setTotalDuration(event.duration);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LocalEventFromMediaPlayerHolder.PlaybackPosition event) {
        if (!isUserSeeking) {
            seekBarAudio.setProgress(event.position, true);
            updateProgressTime(event.position);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LocalEventFromMediaPlayerHolder.StateChanged event) {
        hideLoader();
        currentState = event.currentState;
        switch (event.currentState) {
            case PLAYING:
                btnPlayPause.setImageResource(R.drawable.lb_ic_pause);
                if (mMediaPlayerHolder.getAudioSessionId() != -1 && blastVisualizer != null){
                    blastVisualizer.setAudioSessionId(mMediaPlayerHolder.getAudioSessionId());
                    blastVisualizer.show();
                }
                break;
            case PAUSED:
            case RESET:
            case COMPLETED:
                btnPlayPause.setImageResource(R.drawable.lb_ic_play);
                if (blastVisualizer != null){
                    blastVisualizer.hide();
                }
                break;
            case ERROR:
                showError();
                break;
        }
    }

    private void showError() {
        if(song != null){
            String title = song.getSongName();
            String artist = song.getArtistName();
            String songName = "Unable to play " + title + " (" + artist + ")";
            txtError.setText(songName);
        }
        rlErrorLayout.setVisibility(View.VISIBLE);
        btnDismiss.requestFocus();
    }

    private void setTotalDuration(int duration) {
        long totalSecs = TimeUnit.MILLISECONDS.toSeconds(duration);

        long hours = totalSecs / 3600;
        long minutes = (totalSecs % 3600) / 60;
        long seconds = totalSecs % 60;

        String totalDuration = String.format(Locale.ENGLISH, "%02d:%02d:%02d", hours, minutes, seconds);

        if (hours == 0) {
            totalDuration = String.format(Locale.ENGLISH, "%02d:%02d", minutes, seconds);
        }

        String text = " / " + totalDuration;

        txtTotalDuration.setText(text);
    }

    private void updateProgressTime(int position){
        long currentSecs = TimeUnit.MILLISECONDS.toSeconds(position);

        long hours = currentSecs / 3600;
        long minutes = (currentSecs % 3600) / 60;
        long seconds = currentSecs % 60;

        String currentDuration = String.format(Locale.ENGLISH, "%02d:%02d:%02d", hours, minutes, seconds);

        if (hours == 0) {
            currentDuration = String.format(Locale.ENGLISH, "%02d:%02d", minutes, seconds);
        }

        txtCurrentTime.setText(currentDuration);
    }

    private void disableUI(){
        seekBarAudio.setActivated(false);
        seekBarAudio.setEnabled(false);
        btnPlayPause.setEnabled(false);
        disableSeekBar();
    }

    private void enableUI(){
        seekBarAudio.setActivated(true);
        seekBarAudio.setEnabled(true);
        btnPlayPause.setEnabled(true);
        setupSeekBar();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_MEDIA_PLAY:
                if (currentState == MediaPlayerHolder.PlayerState.PAUSED ||
                        currentState == MediaPlayerHolder.PlayerState.COMPLETED ||
                        currentState == MediaPlayerHolder.PlayerState.RESET) {
                    playSong();
                }
                break;
            case KeyEvent.KEYCODE_MEDIA_PAUSE:
                if (currentState == MediaPlayerHolder.PlayerState.PLAYING) {
                    pauseSong();
                }
                break;
            case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                handlePlayer();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
