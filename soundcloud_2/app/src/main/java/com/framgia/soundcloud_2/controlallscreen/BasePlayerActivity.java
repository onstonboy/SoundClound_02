package com.framgia.soundcloud_2.controlallscreen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.framgia.soundcloud_2.R;
import com.framgia.soundcloud_2.service.PlayerService;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.framgia.soundcloud_2.utils.Constant.ConstantApi.EXTRA_IMAGE_URL;
import static com.framgia.soundcloud_2.utils.Constant.ConstantApi.EXTRA_TITLE;
import static com.framgia.soundcloud_2.utils.Constant.ConstantApi.EXTRA_USER_NAME;
import static com.framgia.soundcloud_2.utils.Constant.KeyIntent.ACTION_NEXT;
import static com.framgia.soundcloud_2.utils.Constant.KeyIntent.ACTION_PAUSE;
import static com.framgia.soundcloud_2.utils.Constant.KeyIntent.ACTION_PLAY;
import static com.framgia.soundcloud_2.utils.Constant.KeyIntent.ACTION_PREVIOUS;
import static com.framgia.soundcloud_2.utils.Constant.KeyIntent.ACTION_STOP;
import static com.framgia.soundcloud_2.utils.Constant.KeyIntent.ACTION_UPDATE_SONG;
import static com.framgia.soundcloud_2.utils.Constant.KeyIntent.ACTION_UPDATE_CONTROL;
import static com.framgia.soundcloud_2.utils.Constant.KeyIntent.EXTRA_MEDIA_STATE;

/**
 * Created by Vinh on 06/02/2017.
 */
public abstract class BasePlayerActivity extends AppCompatActivity implements BasePlayerContract
    .View, View.OnClickListener {
    @Nullable
    @BindView(R.id.image_album_art)
    ImageView mImageArt;
    @Nullable
    @BindView(R.id.text_songs_title)
    TextView mTitle;
    @Nullable
    @BindView(R.id.text_artist_name)
    TextView mArtist;
    @Nullable
    @BindView(R.id.image_play_previous)
    ImageButton mPreviousButton;
    @Nullable
    @BindView(R.id.image_play_pause)
    ImageButton mPlayPauseButton;
    @Nullable
    @BindView(R.id.image_play_next)
    ImageButton mNextButton;
    @Nullable
    @BindView(R.id.control_contain)
    LinearLayout mLinearLayout;
    private boolean mState;
    private BasePlayerContract.Presenter mPresenter;

    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        setPresenter(new BasePlayerPresenter(this));
    }

    @Override
    public void start() {
        displayControl(false);
        setStateIcon(mState);
        registerBroadcast();
        mPreviousButton.setOnClickListener(this);
        mPlayPauseButton.setOnClickListener(this);
        mNextButton.setOnClickListener(this);
        mLinearLayout.setOnClickListener(this);
    }

    @Override
    public void setPresenter(BasePlayerContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void displayControl(boolean display) {
        mLinearLayout.setVisibility(display ? View.VISIBLE : View.GONE);
    }

    @Override
    public void loadSong(Intent intent) {
        String title = intent.getExtras().getString(EXTRA_TITLE);
        String username = intent.getExtras().getString(EXTRA_USER_NAME);
        String imageurl = intent.getExtras().getString(EXTRA_IMAGE_URL);
        setStateIcon(intent.getExtras().getBoolean(EXTRA_MEDIA_STATE));
        if (title != null) mTitle.setText(title);
        mArtist.setText(username);
        if (imageurl != null)
            Picasso.with(getApplicationContext()).load(imageurl).into(mImageArt);
        else mImageArt.setImageResource(R.drawable.ic_songs);
        displayControl(true);
    }

    @Override
    public void setStateIcon(boolean stateIcon) {
        mPlayPauseButton.setImageResource(
            stateIcon ? android.R.drawable.ic_media_pause : android.R.drawable.ic_media_play);
        mState = stateIcon;
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
                    break;
                default:
                    break;
            }
        }
    };

    private void registerBroadcast() {
        IntentFilter intentFilter = new IntentFilter(ACTION_UPDATE_CONTROL);
        intentFilter.addAction(ACTION_UPDATE_SONG);
        intentFilter.addAction(ACTION_PAUSE);
        intentFilter.addAction(ACTION_PLAY);
        intentFilter.addAction(ACTION_STOP);
        registerReceiver(mUpdateControl, intentFilter);
    }

    private void startService(String action) {
        Intent intent = new Intent(getApplicationContext(), PlayerService.class);
        intent.setAction(action);
        getApplicationContext().startService(intent);
    }

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
            case R.id.control_contain:
                //TODO : Start main player
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mUpdateControl);
    }
}
