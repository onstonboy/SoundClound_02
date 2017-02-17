package com.framgia.soundcloud_2.songs;

import android.support.annotation.NonNull;

import com.framgia.soundcloud_2.data.GetCallback;
import com.framgia.soundcloud_2.data.LocalDataSource;
import com.framgia.soundcloud_2.data.SongDataSource;
import com.framgia.soundcloud_2.data.model.Category;
import com.framgia.soundcloud_2.data.model.Track;

import java.util.List;

public class ListSongPresenter implements ListSongContract.Presenter {
    private ListSongContract.View mView;
    private SongDataSource mSongDataSource;
    private LocalDataSource mLocalDataSource;

    public ListSongPresenter(@NonNull ListSongContract.View listSongView,
                             SongDataSource songDataSource, LocalDataSource localDataSource) {
        mView = listSongView;
        mView.setPresenter(this);
        mSongDataSource = songDataSource;
        mLocalDataSource = localDataSource;
    }

    @Override
    public void getSongFromApi(Category category, int offset) {
        mView.showProgress(true);
        mSongDataSource.getDatas(category, offset, new GetCallback<Track>() {
            @Override
            public void onLoaded(List<Track> datas, String nexthref) {
                mView.showProgress(false);
                mView.showSong(datas, nexthref);
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
            public void onLoaded(List<Track> datas, String nexthref) {
                mView.showProgress(false);
                mView.showSong(datas, nexthref);
            }

            @Override
            public void onNotAvailable() {
                mView.showProgress(false);
                mView.showError();
            }
        });
    }

    @Override
    public void getSong(Category category, String query, boolean canLoadMore, int offset) {
        if (!canLoadMore) return;
        if (category == null) getSongFromSearch(query, offset);
        else getSongFromApi(category, offset);
    }

    @Override
    public void clearListSong() {
        mLocalDataSource.clearListTrack();
    }

    @Override
    public void addListSong(List<Track> list) {
        mLocalDataSource.addListTrackLocal(list);
    }

    @Override
    public void start() {
        mView.start();
    }
}
