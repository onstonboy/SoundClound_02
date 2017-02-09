package com.framgia.soundcloud_2.data;

import android.content.Context;

import com.framgia.soundcloud_2.data.model.Category;
import com.framgia.soundcloud_2.data.model.Track;
import com.framgia.soundcloud_2.data.remote.SongRemoteDataSource;

import java.util.List;

public class SongRepository implements DataSource<Track> {
    private static SongRepository sSongRepository;
    private DataSource mSongRemoteDataSource;

    private SongRepository(SongRemoteDataSource songRemoteDataSourc) {
        mSongRemoteDataSource = songRemoteDataSourc;
    }

    public static SongRepository getInstance(Context context) {
        if (sSongRepository == null)
            sSongRepository = new SongRepository(SongRemoteDataSource.getInstance());
        return sSongRepository;
    }

    @Override
    public void getDatas(final Category category, final String query, final GetCallback<Track>
        getCallback) {
        mSongRemoteDataSource.getDatas(category, query, new GetCallback<Track>() {
            @Override
            public void onLoaded(List<Track> datas) {
                getCallback.onLoaded(datas);
            }

            @Override
            public void onNotAvailable() {
                getCallback.onNotAvailable();
            }
        });
    }
}
