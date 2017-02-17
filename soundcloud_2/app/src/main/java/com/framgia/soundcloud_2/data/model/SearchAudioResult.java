package com.framgia.soundcloud_2.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchAudioResult {
    @SerializedName("collection")
    private List<Track> mTracks;
    @SerializedName("next_href")
    private String mNextHref;
    public List<Track> getTracks() {
        return mTracks;
    }

    public void setTracks(List<Track> tracks) {
        mTracks = tracks;
    }

    public String getNextHref() {
        return mNextHref;
    }

    public void setNextHref(String nextHref) {
        mNextHref = nextHref;
    }
}
