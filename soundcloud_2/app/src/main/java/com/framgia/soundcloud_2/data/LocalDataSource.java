package com.framgia.soundcloud_2.data;

import com.framgia.soundcloud_2.data.model.Track;

import java.util.List;

/**
 * Created by tri on 13/02/2017.
 */
public interface LocalDataSource<T> {
    void getDatas(GetCallback<T> getCallback);
    void clearListTrack();
    void addListTrackLocal(List<Track> list);
}
