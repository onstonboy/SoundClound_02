package com.framgia.soundcloud_2.data;

import android.content.Context;

import com.framgia.soundcloud_2.data.model.Category;
import com.framgia.soundcloud_2.data.model.Track;
import com.framgia.soundcloud_2.data.remote.SongRemoteDataSource;

import java.util.List;

public class SongRepository implements SongDataSource<Track> {
    private static SongRepository sSongRepository;
    private SongDataSource mSongRemoteDataSource;

    private SongRepository(SongRemoteDataSource songRemoteDataSource) {
        mSongRemoteDataSource = songRemoteDataSource;
    }

    public static SongRepository getInstance(Context context) {
        if (sSongRepository == null) {
            sSongRepository = new SongRepository(SongRemoteDataSource.getInstance());
        }
        return sSongRepository;
    }

    @Override
    public void getDatas(final Category category, final int offset,
                         final GetCallback<Track> getCallback) {
        mSongRemoteDataSource.getDatas(category, offset, new GetCallback<Track>() {
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

    @Override
    public void searchData(final String query, final int offset,
                           final GetCallback<Track> getCallback) {
        mSongRemoteDataSource.searchData(query, offset, new GetCallback<Track>() {
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
