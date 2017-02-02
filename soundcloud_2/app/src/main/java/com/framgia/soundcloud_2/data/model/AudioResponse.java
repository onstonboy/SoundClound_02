package com.framgia.soundcloud_2.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by tri on 02/02/2017.
 */
public class AudioResponse {
    @SerializedName("collection")
    private List<CollectionTrack> mTracksList;
    @SerializedName("next_href")
    private String mNextHref;

    public List<CollectionTrack> getTracksList() {
        return mTracksList;
    }

    public void setTracksList(List<CollectionTrack> tracksList) {
        mTracksList = tracksList;
    }

    public String getNextHref() {
        return mNextHref;
    }

    public void setNextHref(String nextHref) {
        mNextHref = nextHref;
    }
}