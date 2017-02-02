package com.framgia.soundcloud_2.ui.activity;

/**
 * Created by tri on 02/02/2017.
 */
public class MainPresenter implements MainContract.Presenter {
    private MainContract.View mMainView;

    public MainPresenter(MainContract.View mainView) {
        mMainView = mainView;
        mainView.setPresenter(this);
    }

    @Override
    public void start() {
        mMainView.start();
    }
}
