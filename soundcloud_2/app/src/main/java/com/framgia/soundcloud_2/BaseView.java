package com.framgia.soundcloud_2;

/**
 * Created by tri on 02/02/2017.
 */
public interface BaseView<T> {
    void setPresenter(T presenter);
    void start();
}
