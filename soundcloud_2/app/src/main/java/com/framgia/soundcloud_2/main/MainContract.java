package com.framgia.soundcloud_2.main;

import com.framgia.soundcloud_2.BasePresenter;
import com.framgia.soundcloud_2.BaseView;

/**
 * Created by tri on 02/02/2017.
 */
public interface MainContract {
    interface View extends BaseView<Presenter> {
    }

    interface Presenter extends BasePresenter {
    }
}
