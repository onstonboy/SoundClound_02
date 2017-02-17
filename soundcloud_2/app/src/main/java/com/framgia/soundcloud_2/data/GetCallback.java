package com.framgia.soundcloud_2.data;

import java.util.List;

/**
 * Created by tri on 13/02/2017.
 */
public interface GetCallback<T> {
    void onLoaded(List<T> datas, String nexthref);
    void onNotAvailable();
}
