package com.framgia.soundcloud_2.detailplayer;

import com.framgia.soundcloud_2.utils.Timer;

/**
 * Created by Vinh on 09/02/2017.
 */
public class DetailPlayerPresenter implements DetailPlayerContract.Presenter {
    private DetailPlayerContract.View mView;
    private Timer mTimer;

    public DetailPlayerPresenter(DetailPlayerContract.View view) {
        mView = view;
    }

    @Override
    public void start() {
        mView.start();
    }

    @Override
    public void updateSeekBar(int duration, int fullDuration) {
        mView.setSeekBar(mTimer.getProgressPercentage(duration, fullDuration), mTimer
            .milliSecondsToTimer
                (duration), mTimer.milliSecondsToTimer(fullDuration));
    }

    @Override
    public void updateSongDuration(int progressPercentage, int fullDuration) {
        mView
            .updateSeekBar(mTimer.progressToTimer(progressPercentage, fullDuration));
    }
}
