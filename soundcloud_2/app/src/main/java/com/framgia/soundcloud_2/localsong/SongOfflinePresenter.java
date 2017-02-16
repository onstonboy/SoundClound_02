package com.framgia.soundcloud_2.localsong;

import android.support.annotation.NonNull;

import com.framgia.soundcloud_2.data.DataLocalRepository;
import com.framgia.soundcloud_2.data.GetCallback;
import com.framgia.soundcloud_2.data.model.Track;

import java.util.List;

public class SongOfflinePresenter implements SongOfflineContract.Presenter {
    private SongOfflineContract.View mView;
    private DataLocalRepository mLocalDataRepository;

    public SongOfflinePresenter(@NonNull SongOfflineContract.View mSongOfflineView,
                                @NonNull DataLocalRepository dataRepository) {
        mView = mSongOfflineView;
        mView.setPresenter(this);
        mLocalDataRepository = dataRepository;
    }

    @Override
    public void getSongOffline() {
        mLocalDataRepository.getDatas(new GetCallback<Track>() {
            @Override
            public void onLoaded(List<Track> datas, String nexthref) {
                mView.showSongOffline(datas, nexthref);
            }

            @Override
            public void onNotAvailable() {
                mView.onDataNotAvailable();
            }
        });
    }

    @Override
    public void start() {
        mView.start();
    }
}
