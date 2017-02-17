package com.framgia.soundcloud_2.service;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;

import com.framgia.soundcloud_2.R;
import com.framgia.soundcloud_2.data.model.Track;
import com.framgia.soundcloud_2.main.MainActivity;
import com.framgia.soundcloud_2.utils.DatabaseManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Random;

import static com.framgia.soundcloud_2.utils.Constant.ConstantApi.EXTRA_IMAGE_URL;
import static com.framgia.soundcloud_2.utils.Constant.ConstantApi.EXTRA_TITLE;
import static com.framgia.soundcloud_2.utils.Constant.ConstantApi.EXTRA_USER_NAME;
import static com.framgia.soundcloud_2.utils.Constant.KeyIntent.ACTION_BACKWARD;
import static com.framgia.soundcloud_2.utils.Constant.KeyIntent.ACTION_FORWARD;
import static com.framgia.soundcloud_2.utils.Constant.KeyIntent.ACTION_GET_SONG_STATE;
import static com.framgia.soundcloud_2.utils.Constant.KeyIntent.ACTION_NEXT;
import static com.framgia.soundcloud_2.utils.Constant.KeyIntent.ACTION_NO_REPEAT;
import static com.framgia.soundcloud_2.utils.Constant.KeyIntent.ACTION_NO_SHUFFLE;
import static com.framgia.soundcloud_2.utils.Constant.KeyIntent.ACTION_PAUSE;
import static com.framgia.soundcloud_2.utils.Constant.KeyIntent.ACTION_PLAY;
import static com.framgia.soundcloud_2.utils.Constant.KeyIntent.ACTION_PLAY_NEW_SONG;
import static com.framgia.soundcloud_2.utils.Constant.KeyIntent.ACTION_PREVIOUS;
import static com.framgia.soundcloud_2.utils.Constant.KeyIntent.ACTION_REPEAT;
import static com.framgia.soundcloud_2.utils.Constant.KeyIntent.ACTION_SEEK_TO;
import static com.framgia.soundcloud_2.utils.Constant.KeyIntent.ACTION_SHUFFLE;
import static com.framgia.soundcloud_2.utils.Constant.KeyIntent.ACTION_STOP;
import static com.framgia.soundcloud_2.utils.Constant.KeyIntent.ACTION_UPDATE_CONTROL;
import static com.framgia.soundcloud_2.utils.Constant.KeyIntent.ACTION_UPDATE_CONTROL_DURATION;
import static com.framgia.soundcloud_2.utils.Constant.KeyIntent.ACTION_UPDATE_SEEK_BAR;
import static com.framgia.soundcloud_2.utils.Constant.KeyIntent.ACTION_UPDATE_SONG;
import static com.framgia.soundcloud_2.utils.Constant.KeyIntent.ACTION_UPDATE_SONG_DURATION;
import static com.framgia.soundcloud_2.utils.Constant.KeyIntent.EXTRA_BACKWARD;
import static com.framgia.soundcloud_2.utils.Constant.KeyIntent.EXTRA_DURATION;
import static com.framgia.soundcloud_2.utils.Constant.KeyIntent.EXTRA_FORWARD;
import static com.framgia.soundcloud_2.utils.Constant.KeyIntent.EXTRA_FULL_DURATION;
import static com.framgia.soundcloud_2.utils.Constant.KeyIntent.EXTRA_MEDIA_STATE;
import static com.framgia.soundcloud_2.utils.Constant.KeyIntent.EXTRA_REPEAT_STATE;
import static com.framgia.soundcloud_2.utils.Constant.KeyIntent.EXTRA_SHUFFLE_STATE;
import static com.framgia.soundcloud_2.utils.StorePreferences.loadAudioIndex;
import static com.framgia.soundcloud_2.utils.StorePreferences.storeAudioIndex;
;

/**
 * Created by Vinh on 04/02/2017.
 */
public class PlayerService extends Service implements MediaPlayer.OnCompletionListener,
    MediaPlayer.OnPreparedListener {
    private Handler mSeekBarHandler = new Handler();
    private static final int NOTIFICATION_ID = 101;
    private final int SEEKBAR_DELAY_TIME = 1000;
    private DatabaseManager mDatabaseHelper;
    private int mSeekBackwardTime = 10000;
    private int mSeekForwardTime = 10000;
    private boolean isShuffle;
    private boolean isRepeat;
    private MediaPlayer mMediaPlayer;
    private List<Track> mListTrack;
    private int mSongIndex = -1;
    private int mResumePosition;
    private Bitmap mBitmap;
    private Track mTrack;

    private enum SongStatus {PLAYING, PAUSED}

    @Override
    public void onCreate() {
        super.onCreate();
        mDatabaseHelper = new DatabaseManager(getApplicationContext());
        loadSong();
        startMediaPlayer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        catchAction(intent);
        catchActionDuration(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    private void catchAction(Intent intent) {
        if (intent == null || intent.getAction() == null) return;
        String action = intent.getAction();
        Intent broadcastIntent = new Intent(ACTION_UPDATE_CONTROL);
        broadcastIntent.setAction(action);
        sendBroadcast(broadcastIntent);
        switch (action) {
            case ACTION_PLAY:
                play();
                break;
            case ACTION_PAUSE:
                pause();
                break;
            case ACTION_NEXT:
                next();
                break;
            case ACTION_PREVIOUS:
                previous();
                break;
            case ACTION_STOP:
                stop();
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
                repeat();
                break;
            case ACTION_NO_REPEAT:
                disbleRepeat();
                break;
            case ACTION_SHUFFLE:
                shuffle();
                break;
            case ACTION_NO_SHUFFLE:
                disbleShuffle();
                break;
            case ACTION_GET_SONG_STATE:
                sendBroadcast();
                break;
            default:
                break;
        }
    }

    private void catchActionDuration(Intent intent) {
        if (intent == null || intent.getAction() == null) return;
        String actionString = intent.getAction();
        Intent broadcastIntent = new Intent(ACTION_UPDATE_CONTROL_DURATION);
        broadcastIntent.setAction(actionString);
        sendBroadcast(broadcastIntent);
        switch (actionString) {
            case ACTION_GET_SONG_STATE:
                sendBroadcastDuration();
                break;
            case ACTION_SEEK_TO:
                int duration = intent.getExtras().getInt(EXTRA_DURATION);
                seekTo(duration);
            default:
                break;
        }
    }

    public void loadSong() {
        mListTrack = mDatabaseHelper.getListTrack();
        mSongIndex = loadAudioIndex(getApplicationContext());
        if (mSongIndex == -1 || mSongIndex > mListTrack.size()) mSongIndex = 0;
        mTrack = mListTrack.get(mSongIndex);
    }

    public void startMediaPlayer() {
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

    public void play() {
        if (mMediaPlayer.isPlaying()) return;
        mMediaPlayer.start();
        updateSeekBar();
        buildNotification(SongStatus.PLAYING);
    }

    public void playSelectedSong() {
        loadSong();
        if (mMediaPlayer.isPlaying()) {
            stop();
            mMediaPlayer.reset();
        }
        sendBroadcast();
        startMediaPlayer();
        buildNotification(SongStatus.PLAYING);
    }

    public void backWard() {
        mResumePosition = mMediaPlayer.getCurrentPosition();
        if (mResumePosition - mSeekBackwardTime >= 0) {
            mMediaPlayer.seekTo(mResumePosition - mSeekBackwardTime);
        } else {
            mMediaPlayer.seekTo(0);
        }
        updateSeekBar();
    }

    public void forWard() {
        mResumePosition = mMediaPlayer.getCurrentPosition();
        if (mResumePosition + mSeekForwardTime <= mMediaPlayer.getDuration()) {
            mMediaPlayer.seekTo(mResumePosition + mSeekForwardTime);
        } else {
            mMediaPlayer.seekTo(mMediaPlayer.getDuration());
        }
        updateSeekBar();
    }

    public void stop() {
        if (mMediaPlayer == null || !mMediaPlayer.isPlaying()) return;
        mMediaPlayer.stop();
        sendBroadcast();
    }

    public void pause() {
        if (!mMediaPlayer.isPlaying()) return;
        mMediaPlayer.pause();
        mResumePosition = mMediaPlayer.getCurrentPosition();
        mSeekBarHandler.removeCallbacks(mUpdateSeekBar);
        sendBroadcast();
        buildNotification(SongStatus.PAUSED);
    }

    public void next() {
        if (mSongIndex < (mListTrack.size() - 1)) {
            mSongIndex++;
        } else {
            mSongIndex = 0;
        }
        mTrack = mListTrack.get(mSongIndex);
        storeAudioIndex(getApplicationContext(), mSongIndex);
        stop();
        mMediaPlayer.reset();
        sendBroadcast();
        startMediaPlayer();
    }

    public void previous() {
        if (mSongIndex > 0) {
            mSongIndex--;
        } else {
            mSongIndex = mListTrack.size() - 1;
        }
        mTrack = mListTrack.get(mSongIndex);
        storeAudioIndex(getApplicationContext(), mSongIndex);
        stop();
        mMediaPlayer.reset();
        sendBroadcast();
        startMediaPlayer();
    }

    public void repeat() {
        isRepeat = true;
        isShuffle = false;
        sendBroadcast();
    }

    public void disbleRepeat() {
        isRepeat = false;
        sendBroadcast();
    }

    public void shuffle() {
        isShuffle = true;
        isRepeat = false;
        sendBroadcast();
    }

    public void disbleShuffle() {
        isShuffle = false;
        sendBroadcast();
    }

    public void buildNotification(SongStatus songStatus) {
        int notificationAction = android.R.drawable.ic_media_pause;
        PendingIntent playPauseAction = null;
        if (songStatus == SongStatus.PLAYING) {
            notificationAction = android.R.drawable.ic_media_pause;
            playPauseAction = playbackAction(ACTION_PAUSE);
        } else if (songStatus == SongStatus.PAUSED) {
            notificationAction = android.R.drawable.ic_media_play;
            playPauseAction = playbackAction(ACTION_PLAY);
        }
        if (mBitmap == null) {
            mBitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_songs);
            new GetBitmapImage().execute(mTrack.getArtworkUrl());
        }
        NotificationCompat.Builder notificationBuilder =
            (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setShowWhen(false)
                .setStyle(new NotificationCompat.MediaStyle()).setColor(getResources()
                    .getColor(R.color.color_purple))
                .setLargeIcon(mBitmap)
                .setSmallIcon(android.R.drawable.stat_sys_headset)
                .setContentText(mTrack.getUser().getUserName())
                .setContentTitle(mTrack.getTitle())
                .addAction(android.R.drawable.ic_media_previous, ACTION_PREVIOUS,
                    playbackAction(ACTION_PREVIOUS))
                .addAction(android.R.drawable.ic_media_rew, ACTION_BACKWARD, playbackAction
                    (ACTION_BACKWARD))
                .addAction(notificationAction, ACTION_PLAY, playPauseAction)
                .addAction(android.R.drawable.ic_media_next, ACTION_NEXT,
                    playbackAction(ACTION_NEXT))
                .addAction(android.R.drawable.ic_menu_close_clear_cancel, ACTION_STOP,
                    playbackAction(ACTION_STOP));
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
            new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(contentIntent);
        startForeground(NOTIFICATION_ID, notificationBuilder.build());
    }

    private PendingIntent playbackAction(String action) {
        Intent playbackAction = new Intent(this, PlayerService.class);
        playbackAction.setAction(action);
        return PendingIntent.getService(this, 0, playbackAction, 0);
    }

    public void cancleNotification() {
        NotificationManager notificationManager =
            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Runnable mUpdateSeekBar = new Runnable() {
        public void run() {
            try {
                if (mMediaPlayer.isPlaying()) {
                    Intent intent = new Intent(ACTION_UPDATE_CONTROL);
                    intent.setAction(ACTION_UPDATE_SEEK_BAR);
                    intent.putExtra(EXTRA_FULL_DURATION, mMediaPlayer.getDuration());
                    intent.putExtra(EXTRA_DURATION, mMediaPlayer.getCurrentPosition());
                    getApplicationContext().sendBroadcast(intent);
                }
                mSeekBarHandler.postDelayed(this, SEEKBAR_DELAY_TIME);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public void updateSeekBar() {
        try {
            mSeekBarHandler.postDelayed(mUpdateSeekBar, SEEKBAR_DELAY_TIME);
        } catch (Exception e) {
        }
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        play();
        sendBroadcast();
        sendBroadcastDuration();
        buildNotification(SongStatus.PLAYING);
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if (isRepeat) {
            mTrack = mListTrack.get(mSongIndex);
            storeAudioIndex(getApplicationContext(), mSongIndex);
            stop();
            mMediaPlayer.reset();
            sendBroadcast();
            startMediaPlayer();
        } else if (isShuffle) {
            Random rand = new Random();
            mSongIndex = rand.nextInt((mListTrack.size()));
            mTrack = mListTrack.get(mSongIndex);
            storeAudioIndex(getApplicationContext(), mSongIndex);
            stop();
            mMediaPlayer.reset();
            sendBroadcast();
            startMediaPlayer();
        } else {
            next();
            buildNotification(SongStatus.PLAYING);
        }
    }

    public void seekTo(int position) {
        mMediaPlayer.seekTo(position);
        if (mMediaPlayer.isPlaying()) return;
        mMediaPlayer.start();
        sendBroadcast();
        sendBroadcastDuration();
        updateSeekBar();
    }

    private void sendBroadcast() {
        Intent intent = new Intent(ACTION_UPDATE_CONTROL);
        intent.setAction(ACTION_UPDATE_SONG);
        intent.putExtra(EXTRA_TITLE, mTrack.getTitle());
        intent.putExtra(EXTRA_USER_NAME, mTrack.getUser().getUserName());
        intent.putExtra(EXTRA_IMAGE_URL, mTrack.getArtworkUrl());
        intent.putExtra(EXTRA_MEDIA_STATE, mMediaPlayer.isPlaying());
        intent.putExtra(EXTRA_REPEAT_STATE, isRepeat);
        intent.putExtra(EXTRA_SHUFFLE_STATE, isShuffle);
        intent.putExtra(EXTRA_FORWARD, mMediaPlayer.getCurrentPosition());
        intent.putExtra(EXTRA_BACKWARD, mMediaPlayer.getCurrentPosition());
        getApplicationContext().sendBroadcast(intent);
    }

    private void sendBroadcastDuration() {
        Intent intent = new Intent(ACTION_UPDATE_CONTROL_DURATION);
        intent.setAction(ACTION_UPDATE_SONG_DURATION);
        intent.putExtra(EXTRA_DURATION, mMediaPlayer.getCurrentPosition());
        intent.putExtra(EXTRA_FULL_DURATION, mMediaPlayer.getDuration());
        getApplicationContext().sendBroadcast(intent);
    }

    private Bitmap getBitmapFromURL(String strUrl) {
        if (strUrl == null) return BitmapFactory.decodeResource(getResources(),
            R.drawable.ic_songs);
        try {
            URL url = new URL(strUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public class GetBitmapImage extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            return getBitmapFromURL(strings[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap == null) return;
            mBitmap = bitmap;
            buildNotification(SongStatus.PLAYING);
        }
    }

    public static boolean checkServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager =
            (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager
            .getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            stop();
            mMediaPlayer.release();
        }
        cancleNotification();
        mDatabaseHelper.clearListTrack();
    }
}
