package com.framgia.soundcloud_2.listsong;

import android.support.annotation.NonNull;

import com.framgia.soundcloud_2.data.DataSource;
import com.framgia.soundcloud_2.data.model.Category;
import com.framgia.soundcloud_2.data.model.Track;

import java.util.List;

public class ListSongPresenter implements ListSongContract.Presenter {
    private ListSongContract.View mView;
    private DataSource mDataSource;

    public ListSongPresenter(@NonNull ListSongContract.View listSongView, DataSource dataSource) {
        mView = listSongView;
        mView.setPresenter(this);
        mDataSource = dataSource;
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
                mView.showError();
            }
        });
    }

    @Override
    public void start() {
        mView.start();
    }
}
