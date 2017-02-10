package com.framgia.soundcloud_2.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.session.MediaSessionManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;

import com.framgia.soundcloud_2.data.model.Track;
import com.framgia.soundcloud_2.utils.DatabaseManager;

import java.io.IOException;
import java.util.List;

import static com.framgia.soundcloud_2.utils.Constant.KeyIntent.ACTION_BACKWARD;
import static com.framgia.soundcloud_2.utils.Constant.KeyIntent.ACTION_FORWARD;
import static com.framgia.soundcloud_2.utils.Constant.KeyIntent.ACTION_GET_AUDIO_STATE;
import static com.framgia.soundcloud_2.utils.Constant.KeyIntent.ACTION_NO_REPEAT;
import static com.framgia.soundcloud_2.utils.Constant.KeyIntent.ACTION_NO_SHUFFLE;
import static com.framgia.soundcloud_2.utils.Constant.KeyIntent.ACTION_PLAY_NEW_SONG;
import static com.framgia.soundcloud_2.utils.Constant.KeyIntent.ACTION_REPEAT;
import static com.framgia.soundcloud_2.utils.Constant.KeyIntent.ACTION_SEEK_TO;
import static com.framgia.soundcloud_2.utils.Constant.KeyIntent.ACTION_SHUFFLE;
import static com.framgia.soundcloud_2.utils.Constant.KeyIntent.ACTION_UPDATE_CONTROL;
import static com.framgia.soundcloud_2.utils.Constant.KeyIntent.ACTION_NEXT;
import static com.framgia.soundcloud_2.utils.Constant.KeyIntent.ACTION_PAUSE;
import static com.framgia.soundcloud_2.utils.Constant.KeyIntent.ACTION_PLAY;
import static com.framgia.soundcloud_2.utils.Constant.KeyIntent.ACTION_PREVIOUS;
import static com.framgia.soundcloud_2.utils.Constant.KeyIntent.ACTION_STOP;
import static com.framgia.soundcloud_2.utils.Constant.KeyIntent.ACTION_UPDATE_SEEK_BAR;
import static com.framgia.soundcloud_2.utils.Constant.KeyIntent.EXTRA_DURATION;
import static com.framgia.soundcloud_2.utils.Constant.KeyIntent.EXTRA_FULL_DURATION;
import static com.framgia.soundcloud_2.utils.StorePreferences.loadAudioIndex;
;

/**
 * Created by Vinh on 04/02/2017.
 */
public class PlayerService extends Service implements MediaPlayer.OnCompletionListener,
    MediaPlayer.OnPreparedListener {
    public static final String SONG_PLAYER = "SONG_PLAYER";
    private static final int NOTIFICATION_ID = 101;
    private final int SEEKBAR_DELAY_TIME = 1000;
    private Handler mSeekBarHandler = new Handler();
    private MediaPlayer mMediaPlayer;
    private MediaSessionManager mMediaSessionManager;
    private MediaSessionCompat mMediaSession;
    private MediaControllerCompat.TransportControls mTransportControls;
    private int mResumePosition;
    private AudioManager mAudioManager;
    private List<Track> mListTrack;
    private int mAudioIndex = -1;
    private Track mTrack;
    private DatabaseManager mDatabaseHelper;
    private Bitmap mIconBitmap;
    private boolean isShuffle;
    private boolean isRepeat;
    private int seekForwardTime = 10000;
    private int seekBackwardTime = 10000;

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            mDatabaseHelper = new DatabaseManager(getApplicationContext());
            loadSong();
        } catch (NullPointerException e) {
            stopSelf();
        }
        if (mMediaSessionManager == null) {
            try {
                initMediaSession();
                initMediaPlayer();
            } catch (RemoteException e) {
                e.printStackTrace();
                stopSelf();
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        catchPlayerEvent(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            try {
                if (mMediaPlayer.isPlaying()) {
                    Intent broadcastIntent = new Intent(ACTION_UPDATE_CONTROL);
                    broadcastIntent.setAction(ACTION_UPDATE_SEEK_BAR);
                    broadcastIntent.putExtra(EXTRA_FULL_DURATION, mMediaPlayer.getDuration());
                    broadcastIntent.putExtra(EXTRA_DURATION, mMediaPlayer.getCurrentPosition());
                    getApplicationContext().sendBroadcast(broadcastIntent);
                }
                mSeekBarHandler.postDelayed(this, SEEKBAR_DELAY_TIME);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public void updateSeekBar() {
        try {
            mSeekBarHandler.postDelayed(mUpdateTimeTask, SEEKBAR_DELAY_TIME);
        } catch (Exception e) {
        }
    }

    private void catchPlayerEvent(Intent playerEvent) {
        if (playerEvent == null || playerEvent.getAction() == null) return;
        String actionString = playerEvent.getAction();
        Intent broadcastIntent = new Intent(ACTION_UPDATE_CONTROL);
        broadcastIntent.setAction(actionString);
        sendBroadcast(broadcastIntent);
        switch (actionString) {
            case ACTION_PLAY:
                mTransportControls.play();
                break;
            case ACTION_PAUSE:
                mTransportControls.pause();
                break;
            case ACTION_NEXT:
                mTransportControls.skipToNext();
                break;
            case ACTION_PREVIOUS:
                mTransportControls.skipToPrevious();
                break;
            case ACTION_STOP:
                mTransportControls.stop();
                break;
            case ACTION_PLAY_NEW_SONG:
                playSelectedSong();
                break;
            case ACTION_BACKWARD:
                backWard();
                break;
            case ACTION_FORWARD:
                forWard();
                break;
            case ACTION_REPEAT:
                repeatSong();
                break;
            case ACTION_NO_REPEAT:
                noRepeatSong();
                break;
            case ACTION_SHUFFLE:
                shuffleSong();
                break;
            case ACTION_NO_SHUFFLE:
                noShuffleSong();
                break;
            case ACTION_GET_AUDIO_STATE:
                // TODO action state
                break;
            case ACTION_SEEK_TO:
                // TODO seek to
                break;
            default:
                break;
        }
    }

    public void loadSong() {
        mListTrack = mDatabaseHelper.getListTrack();
        mAudioIndex = loadAudioIndex(getApplicationContext());
        if (mAudioIndex == -1 || mAudioIndex > mListTrack.size())
            mAudioIndex = 0;
        mTrack = mListTrack.get(mAudioIndex);
    }

    public void initMediaPlayer() {
        if (mMediaPlayer == null) mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.reset();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mMediaPlayer.setDataSource(mTrack.getUri());
        } catch (IOException e) {
            e.printStackTrace();
            stopSelf();
        }
        mMediaPlayer.prepareAsync();
    }

    public void playMedia() {
        if (mMediaPlayer.isPlaying()) return;
        mMediaPlayer.start();
    }

    public void playSelectedSong() {
        loadSong();
        if (mMediaPlayer.isPlaying()) {
            stopMedia();
            mMediaPlayer.reset();
        }
    }

    public void backWard() {
        mResumePosition = mMediaPlayer.getCurrentPosition();
        if (mResumePosition - seekBackwardTime >= 0) {
            mMediaPlayer.seekTo(mResumePosition - seekBackwardTime);
        } else {
            mMediaPlayer.seekTo(0);
        }
    }

    public void forWard() {
        mResumePosition = mMediaPlayer.getCurrentPosition();
        if (mResumePosition + seekForwardTime <= mMediaPlayer.getDuration()) {
            mMediaPlayer.seekTo(mResumePosition + seekForwardTime);
        } else {
            mMediaPlayer.seekTo(mMediaPlayer.getDuration());
        }
    }

    public void stopMedia() {
        if (mMediaPlayer == null || !mMediaPlayer.isPlaying()) return;
        mMediaPlayer.stop();
    }

    public void pauseMedia() {
        if (!mMediaPlayer.isPlaying()) return;
        mMediaPlayer.pause();
        mResumePosition = mMediaPlayer.getCurrentPosition();
    }

    public void resumeMedia() {
        if (mMediaPlayer.isPlaying()) return;
        mMediaPlayer.seekTo(mResumePosition);
        mMediaPlayer.start();
    }

    public void skipToNext() {
        if (mAudioIndex == mListTrack.size() - 1) mAudioIndex = -1;
        mTrack = mListTrack.get(++mAudioIndex);
        stopMedia();
        mMediaPlayer.reset();
        initMediaPlayer();
    }

    public void skipToPrevious() {
        if (mAudioIndex == 0) mAudioIndex = mListTrack.size();
        mTrack = mListTrack.get(--mAudioIndex);
        stopMedia();
        mMediaPlayer.reset();
        initMediaPlayer();
    }

    public void repeatSong() {
        isRepeat = true;
        isShuffle = false;
    }

    public void noRepeatSong() {
        isRepeat = false;
    }

    public void shuffleSong() {
        isShuffle = true;
        isRepeat = false;
    }

    public void noShuffleSong() {
        isShuffle = false;
    }

    private void initMediaSession() throws RemoteException {
        if (mMediaSessionManager != null) return;
        mMediaSessionManager =
            (MediaSessionManager) getSystemService(Context.MEDIA_SESSION_SERVICE);
        mMediaSession = new MediaSessionCompat(getApplicationContext(), SONG_PLAYER);
        mTransportControls = mMediaSession.getController().getTransportControls();
        mMediaSession.setActive(true);
        mMediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mMediaSession.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public void onPlay() {
                super.onPlay();
                resumeMedia();
            }

            @Override
            public void onPause() {
                super.onPause();
                pauseMedia();
            }

            @Override
            public void onSkipToNext() {
                super.onSkipToNext();
                skipToNext();
            }

            @Override
            public void onSkipToPrevious() {
                super.onSkipToPrevious();
                skipToPrevious();
            }

            @Override
            public void onStop() {
                super.onStop();
                stopForeground(true);
                pauseMedia();
            }

            @Override
            public void onSeekTo(long position) {
                super.onSeekTo(position);
            }
        });
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        playMedia();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            stopMedia();
            mMediaPlayer.release();
        }
        mDatabaseHelper.clearListTrack();
    }
}
