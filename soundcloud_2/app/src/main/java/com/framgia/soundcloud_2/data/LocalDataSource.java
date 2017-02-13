package com.framgia.soundcloud_2.data;

/**
 * Created by tri on 13/02/2017.
 */
public interface LocalDataSource<T> {
    void getDatas(GetCallback<T> getCallback);
}
