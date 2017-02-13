package com.framgia.soundcloud_2.controlallscreen;

import android.content.Intent;

/**
 * Created by Vinh on 06/02/2017.
 */
public interface BasePlayerContract {
    interface View {
        void start();
        void loadSong(Intent intent);
        void setStateIcon(boolean stateIcon);
        void displayControl(boolean display);
        void setPresenter(Presenter presenter);
    }

    interface Presenter {
        void start();
    }
}
