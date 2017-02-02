package com.framgia.soundcloud_2.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tri on 02/02/2017.
 */
public class Track {
    @SerializedName("artwork_url")
    private String mArtworkUrl;
    @SerializedName("downloadable")
    private boolean mDownloadAble;
    @SerializedName("duration")
    private int mDuration;
    @SerializedName("id")
    private int mId;
    @SerializedName("uri")
    private String mUri;
    @SerializedName("playback_count")
    private double mPlaybackCount;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("user")
    private User mUser;

    public String getArtworkUrl() {
        return mArtworkUrl;
    }

    public void setArtworkUrl(String artworkUrl) {
        mArtworkUrl = artworkUrl;
    }

    public boolean isDownloadAble() {
        return mDownloadAble;
    }

    public void setDownloadAble(boolean downloadable) {
        mDownloadAble = downloadable;
    }

    public int getDuration() {
        return mDuration;
    }

    public void setDuration(int duration) {
        mDuration = duration;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getUri() {
        return mUri;
    }

    public void setUri(String uri) {
        mUri = uri;
    }

    public double getPlaybackCount() {
        return mPlaybackCount;
    }

    public void setPlaybackCount(double playbackCount) {
        mPlaybackCount = playbackCount;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }
}
