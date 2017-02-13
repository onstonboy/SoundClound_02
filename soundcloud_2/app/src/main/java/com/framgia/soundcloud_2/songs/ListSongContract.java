package com.framgia.soundcloud_2.songs;

import com.framgia.soundcloud_2.BasePresenter;
import com.framgia.soundcloud_2.BaseView;
import com.framgia.soundcloud_2.data.model.Category;
import com.framgia.soundcloud_2.data.model.Track;

import java.util.List;

public interface ListSongContract {
    interface View extends BaseView<Presenter> {
        void showSong(List<Track> list);
        void showError();
        void checkPermissionDownload();
        void showProgress(boolean show);
        void addScrollViewListener();
    }

    interface Presenter extends BasePresenter {
        void getSongFromApi(Category category, int offset);
        void getSongFromSearch(String query, int offset);
        void getSong(Category category, String query, int offset);
    }
}
