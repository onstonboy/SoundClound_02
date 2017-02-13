package com.framgia.soundcloud_2.songs;

import android.support.annotation.NonNull;

import com.framgia.soundcloud_2.data.GetCallback;
import com.framgia.soundcloud_2.data.SongDataSource;
import com.framgia.soundcloud_2.data.model.Category;
import com.framgia.soundcloud_2.data.model.Track;

import java.util.List;

public class ListSongPresenter implements ListSongContract.Presenter {
    private ListSongContract.View mView;
    private SongDataSource mSongDataSource;

    public ListSongPresenter(@NonNull ListSongContract.View listSongView,
                             SongDataSource songDataSource) {
        mView = listSongView;
        mView.setPresenter(this);
        mSongDataSource = songDataSource;
    }

    @Override
    public void getSongFromApi(Category category, int offset) {
        mView.showProgress(true);
        mSongDataSource.getDatas(category, offset, new GetCallback<Track>() {
            @Override
            public void onLoaded(List<Track> datas) {
                mView.showProgress(false);
                mView.showSong(datas);
            }

            @Override
            public void onNotAvailable() {
                mView.showProgress(false);
                mView.showError();
            }
        });
    }

    @Override
    public void getSongFromSearch(String query, int offset) {
        mView.showProgress(true);
        mSongDataSource.searchData(query, offset, new GetCallback<Track>() {
            @Override
            public void onLoaded(List<Track> datas) {
                mView.showProgress(false);
                mView.showSong(datas);
            }

            @Override
            public void onNotAvailable() {
                mView.showProgress(false);
                mView.showError();
            }
        });
    }

    @Override
    public void getSong(Category category, String query, int offset) {
        if (category == null) getSongFromSearch(query, offset);
        else getSongFromApi(category, offset);
    }

    @Override
    public void start() {
        mView.start();
    }
}
