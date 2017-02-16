package com.framgia.soundcloud_2.detailplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.framgia.soundcloud_2.R;
import com.framgia.soundcloud_2.service.PlayerService;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
import static com.framgia.soundcloud_2.utils.Constant.KeyIntent.EXTRA_DURATION;
import static com.framgia.soundcloud_2.utils.Constant.KeyIntent.EXTRA_FULL_DURATION;
import static com.framgia.soundcloud_2.utils.Constant.KeyIntent.EXTRA_MEDIA_STATE;
import static com.framgia.soundcloud_2.utils.Constant.KeyIntent.EXTRA_REPEAT_STATE;
import static com.framgia.soundcloud_2.utils.Constant.KeyIntent.EXTRA_SHUFFLE_STATE;

/**
 * Created by Vinh on 09/02/2017.
 */
public class DetailPlayerActivity extends AppCompatActivity
    implements DetailPlayerContract.View, View
    .OnClickListener, SeekBar.OnSeekBarChangeListener {
    @Nullable
    @BindView(R.id.image_album_art)
    ImageView mImageArt;
    @Nullable
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.text_songs_title)
    TextView mTitle;
    @Nullable
    @BindView(R.id.text_artist_name)
    TextView mArtist;
    @Nullable
    @BindView(R.id.image_play_pause)
    ImageButton mButtonPlayPause;
    @Nullable
    @BindView(R.id.image_play_next)
    ImageButton mButtonPlayNext;
    @BindView(R.id.image_play_backward)
    ImageButton mButtonBackward;
    @Nullable
    @BindView(R.id.image_play_forward)
    ImageButton mButtonForward;
    @BindView(R.id.image_repeat)
    ImageButton mButtonRepeat;
    @BindView(R.id.image_shuffle)
    ImageButton mButtonShuffle;
    @BindView(R.id.seekbar_songs)
    SeekBar mSeekBarSong;
    @BindView(R.id.text_duration)
    TextView mAudioDuration;
    @BindView(R.id.text_full_duration)
    TextView mSongFullDuration;
    private DetailPlayerContract.Presenter mPresenter;
    private boolean mState, mRepeatState, mShuffleState;
    private String mSongTitle;
    private int mTotalDuration;
    private int mDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_songs);
        setPresenter(new DetailPlayerPresenter(this));
        ButterKnife.bind(this);
        mPresenter.start();
    }

    @Override
    public void start() {
        ButterKnife.bind(this);
        setStateIcon(mState);
        registerBroadcast();
        registerBroadcastDuration();
        songState();
        mSeekBarSong.setOnSeekBarChangeListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.image_play_previous, R.id.image_play_pause,
        R.id.image_play_next, R.id.image_repeat, R.id.image_shuffle, R.id.image_play_backward,
        R.id.image_play_forward})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_play_previous:
                startService(ACTION_PREVIOUS);
                break;
            case R.id.image_play_pause:
                startService(mState ? ACTION_PAUSE : ACTION_PLAY);
                break;
            case R.id.image_play_next:
                startService(ACTION_NEXT);
                break;
            case R.id.image_repeat:
                startService(mRepeatState ?
                    ACTION_NO_REPEAT : ACTION_REPEAT);
                break;
            case R.id.image_shuffle:
                startService(mShuffleState ? ACTION_NO_SHUFFLE : ACTION_SHUFFLE);
                break;
            case R.id.image_play_backward:
                startService(ACTION_BACKWARD);
                break;
            case R.id.image_play_forward:
                startService(ACTION_FORWARD);
                break;
            default:
                break;
        }
    }

    private BroadcastReceiver mUpdateControl = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case ACTION_PLAY:
                    setStateIcon(true);
                    break;
                case ACTION_PAUSE:
                    setStateIcon(false);
                    break;
                case ACTION_UPDATE_SONG:
                    loadSong(intent);
                    mToolbar.setTitle(mSongTitle);
                    break;
                case ACTION_BACKWARD:
                    break;
                case ACTION_FORWARD:
                    break;
                case ACTION_REPEAT:
                    setStateRepeat(true);
                    break;
                case ACTION_NO_REPEAT:
                    setStateRepeat(false);
                    break;
                case ACTION_SHUFFLE:
                    setStateShuffle(true);
                    break;
                case ACTION_NO_SHUFFLE:
                    setStateShuffle(false);
                    break;
                default:
                    break;
            }
        }
    };
    private BroadcastReceiver mUpdateDuration = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case ACTION_UPDATE_SONG_DURATION:
                    loadDuration(intent);
                    break;
                case ACTION_UPDATE_SEEK_BAR:
                    loadSeekBar(intent);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void loadSong(Intent intent) {
        mSongTitle = intent.getExtras().getString(EXTRA_TITLE);
        String username = intent.getExtras().getString(EXTRA_USER_NAME);
        String imageurl = intent.getExtras().getString(EXTRA_IMAGE_URL);
        setStateIcon(intent.getExtras().getBoolean(EXTRA_MEDIA_STATE));
        setStateRepeat(intent.getExtras().getBoolean(EXTRA_REPEAT_STATE));
        setStateShuffle(intent.getExtras().getBoolean(EXTRA_SHUFFLE_STATE));
        if (mSongTitle != null) mTitle.setText(mSongTitle);
        if (username != null) mArtist.setText(username);
        Picasso.with(getApplicationContext())
            .load(imageurl)
            .placeholder(R.drawable.ic_songs)
            .into(mImageArt);
    }

    @Override
    public void loadDuration(Intent intent) {
        loadSeekBar(intent);
    }

    @Override
    public void setStateIcon(boolean stateIcon) {
        mButtonPlayPause.setImageResource(
            stateIcon ? android.R.drawable.ic_media_pause : android.R.drawable.ic_media_play);
        mState = stateIcon;
    }

    @Override
    public void songState() {
        Intent intent = new Intent(getApplicationContext(), PlayerService.class);
        intent.setAction(ACTION_GET_SONG_STATE);
        startService(intent);
    }

    @Override
    public void loadSeekBar(Intent intent) {
        mDuration = intent.getExtras().getInt(EXTRA_DURATION);
        mTotalDuration = intent.getExtras().getInt(EXTRA_FULL_DURATION);
        mPresenter.updateSeekBar(mDuration, mTotalDuration);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int percentage, boolean isFromUser) {
        if (!isFromUser) return;
        mPresenter.updateSongDuration(percentage, mTotalDuration);
    }

    @Override
    public void setSeekBar(int progressPercentage, String currentDuration, String totalDuration) {
        mSeekBarSong.setProgress(progressPercentage);
        mAudioDuration.setText(currentDuration);
        mSongFullDuration.setText(totalDuration);
    }

    @Override
    public void updateSeekBar(int duration) {
        Intent intent = new Intent(getApplicationContext(), PlayerService.class);
        intent.putExtra(EXTRA_DURATION, duration);
        intent.setAction(ACTION_SEEK_TO);
        startService(intent);
    }

    @Override
    public void setStateRepeat(boolean stateRepeat) {
        if (!stateRepeat) {
            mButtonRepeat.setImageResource(R.drawable.bg_btn_repeat);
        } else {
            mButtonRepeat.setImageResource(R.drawable.bg_btn_repeat_focused);
            mButtonShuffle.setImageResource(R.drawable.bg_btn_shuffle);
        }
        mRepeatState = stateRepeat;
    }

    @Override
    public void setStateShuffle(boolean stateShuffle) {
        if (!stateShuffle) {
            mButtonShuffle.setImageResource(R.drawable.bg_btn_shuffle);
        } else {
            mButtonShuffle.setImageResource(R.drawable.bg_btn_shuffle_focused);
            mButtonRepeat.setImageResource(R.drawable.bg_btn_repeat);
        }
        mShuffleState = stateShuffle;
    }

    @Override
    public void setPresenter(DetailPlayerContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mUpdateControl);
        unregisterReceiver(mUpdateDuration);
    }

    private void registerBroadcast() {
        IntentFilter intentFilter = new IntentFilter(ACTION_UPDATE_CONTROL);
        intentFilter.addAction(ACTION_UPDATE_SONG);
        intentFilter.addAction(ACTION_PAUSE);
        intentFilter.addAction(ACTION_PLAY);
        intentFilter.addAction(ACTION_STOP);
        intentFilter.addAction(ACTION_BACKWARD);
        intentFilter.addAction(ACTION_FORWARD);
        intentFilter.addAction(ACTION_REPEAT);
        intentFilter.addAction(ACTION_NO_REPEAT);
        intentFilter.addAction(ACTION_SHUFFLE);
        intentFilter.addAction(ACTION_NO_SHUFFLE);
        registerReceiver(mUpdateControl, intentFilter);
    }

    private void registerBroadcastDuration() {
        IntentFilter intentFilter = new IntentFilter(ACTION_UPDATE_CONTROL_DURATION);
        intentFilter.addAction(ACTION_UPDATE_SONG_DURATION);
        intentFilter.addAction(ACTION_UPDATE_SEEK_BAR);
        registerReceiver(mUpdateDuration, intentFilter);
    }

    private void startService(String action) {
        Intent intent = new Intent(getApplicationContext(), PlayerService.class);
        intent.setAction(action);
        getApplicationContext().startService(intent);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }
}
