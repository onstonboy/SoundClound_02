package com.framgia.soundcloud_2.localsong;

import com.framgia.soundcloud_2.BasePresenter;
import com.framgia.soundcloud_2.BaseView;
import com.framgia.soundcloud_2.data.model.Track;

import java.util.List;

public interface SongOfflineContract {
    interface View extends BaseView<Presenter> {
        void showSongOffline(List<Track> list);
        void onDataNotAvailable();
        void playMusic(int songIndex);
        void verifyPermissions();
    }

    interface Presenter extends BasePresenter {
        void getSongOffline();
    }
}
