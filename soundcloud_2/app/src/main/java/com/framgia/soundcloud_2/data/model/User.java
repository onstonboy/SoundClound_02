package com.framgia.soundcloud_2.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tri on 02/02/2017.
 */
public class User {
    @SerializedName("id")
    private int mId;
    @SerializedName("username")
    private String mUserName;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }
}
