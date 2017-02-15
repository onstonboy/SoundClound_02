package com.framgia.soundcloud_2.controlallscreen;

import android.support.annotation.NonNull;

/**
 * Created by Vinh on 06/02/2017.
 */
public class BasePlayerPresenter implements BasePlayerContract.Presenter {
    private BasePlayerContract.View mView;
    public BasePlayerPresenter(@NonNull BasePlayerContract.View baseView) {
        mView = baseView;
        mView.setPresenter(this);
    }
    @Override
    public void start() {
        mView.start();
    }
}
