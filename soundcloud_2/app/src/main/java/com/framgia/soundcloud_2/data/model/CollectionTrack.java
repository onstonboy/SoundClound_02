package com.framgia.soundcloud_2.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tri on 02/02/2017.
 */
public class CollectionTrack {
    @SerializedName("track")
    private Track mTrack;

    public Track getTrack() {
        return mTrack;
    }

    public void setTrack(Track track) {
        mTrack = track;
    }
}
