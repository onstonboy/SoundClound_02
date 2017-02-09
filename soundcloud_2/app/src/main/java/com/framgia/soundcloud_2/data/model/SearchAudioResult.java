package com.framgia.soundcloud_2.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchAudioResult {
    @SerializedName("collection")
    private List<Track> mTracks;

    public List<Track> getTracks() {
        return mTracks;
    }

    public void setTracks(List<Track> tracks) {
        mTracks = tracks;
    }
}
