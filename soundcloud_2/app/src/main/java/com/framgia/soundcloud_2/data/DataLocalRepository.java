package com.framgia.soundcloud_2.data;

import android.content.Context;

import com.framgia.soundcloud_2.data.local.SongLocalDataSource;
import com.framgia.soundcloud_2.data.model.Track;

import java.util.List;

public class DataLocalRepository implements LocalDataSource<Track> {
    private static DataLocalRepository sDataLocalRepository;
    private LocalDataSource mLocalDataSource;

    private DataLocalRepository(SongLocalDataSource songLocalDataSource) {
        mLocalDataSource = songLocalDataSource;
    }

    public static DataLocalRepository getInstance(Context context) {
        if (sDataLocalRepository == null) {
            sDataLocalRepository =
                new DataLocalRepository(SongLocalDataSource.getInstance(context));
        }
        return sDataLocalRepository;
    }

    @Override
    public void getDatas(final GetCallback<Track> getCallback) {
        mLocalDataSource.getDatas(new GetCallback<Track>() {
            @Override
            public void onNotAvailable() {
                getCallback.onNotAvailable();
            }

            @Override
            public void onLoaded(List<Track> datas) {
                getCallback.onLoaded(datas);
            }
        });
    }
}
