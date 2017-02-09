package com.framgia.soundcloud_2.listsong;

import android.support.annotation.NonNull;

import com.framgia.soundcloud_2.data.DataSource;
import com.framgia.soundcloud_2.data.model.Category;
import com.framgia.soundcloud_2.data.model.Track;

import java.util.List;

public class ListSongPresenter implements ListSongContract.Presenter {
    private ListSongContract.View mView;
    private DataSource mDataSource;

    public ListSongPresenter(@NonNull ListSongContract.View listSongView,
                             DataSource songDataSource) {
        mView = listSongView;
        mView.setPresenter(this);
        mDataSource = songDataSource;
    }

    @Override
    public void getSongFromApi(Category category) {
        mDataSource.getDatas(category, new DataSource.GetCallback<Track>() {
            @Override
            public void onLoaded(List<Track> datas) {
                mView.showSong(datas);
            }

            @Override
            public void onNotAvailable() {
            }
        });
    }

    @Override
    public void getSongFromSearch(String query) {
        mDataSource.searchData(query, new DataSource.GetCallback<Track>() {
            @Override
            public void onLoaded(List<Track> datas) {
                mView.showSong(datas);
            }

            @Override
            public void onNotAvailable() {
                mView.showError();
            }
        });
    }

    @Override
    public void getSong(Category category, String query) {
        if (category == null) getSongFromSearch(query);
        else getSongFromApi(category);
    }

    @Override
    public void start() {
        mView.start();
    }
}
