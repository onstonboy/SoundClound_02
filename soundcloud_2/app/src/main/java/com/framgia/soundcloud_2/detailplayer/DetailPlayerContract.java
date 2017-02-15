package com.framgia.soundcloud_2.detailplayer;

import android.content.Intent;

import com.framgia.soundcloud_2.BasePresenter;
import com.framgia.soundcloud_2.BaseView;

/**
 * Created by Vinh on 09/02/2017.
 */
public interface DetailPlayerContract {
    interface View extends BaseView<Presenter> {
        void loadSong(Intent intent);
        void loadSeekBar(Intent intent);
        void loadDuration(Intent intent);
        void songState();
        void setStateIcon(boolean stateIcon);
        void setSeekBar(int progressPercentage, String currentDuration, String totalDuration);
        void setStateRepeat(boolean stateRepeat);
        void setStateShuffle(boolean stateShuffle);
        void updateSeekBar(int duration);
    }

    interface Presenter extends BasePresenter {
        void updateSeekBar(int duration, int fullDuration);
        void updateSongDuration(int progressPercentage, int fullDuration);
    }
}
