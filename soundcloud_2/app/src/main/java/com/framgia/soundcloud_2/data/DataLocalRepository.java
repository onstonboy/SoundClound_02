package com.framgia.soundcloud_2.data;

import android.content.Context;

import com.framgia.soundcloud_2.data.local.SongLocalDataSource;
import com.framgia.soundcloud_2.data.model.Category;
import com.framgia.soundcloud_2.data.model.Track;

import java.util.List;

public class DataLocalRepository implements DataSource<Track> {
    private static DataLocalRepository sDataLocalRepository;
    private DataSource mLocalDataSource;

    public static DataLocalRepository getInstance(Context context) {
        if (sDataLocalRepository == null)
            sDataLocalRepository = new DataLocalRepository(SongLocalDataSource.getInstance(context));
        return sDataLocalRepository;
    }

    private DataLocalRepository(SongLocalDataSource songLocalDataSource) {
        mLocalDataSource = songLocalDataSource;
    }

    @Override
    public void getDatas(Category category, final GetCallback<Track> getCallback) {
        mLocalDataSource.getDatas(category, new GetCallback<Track>() {
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
