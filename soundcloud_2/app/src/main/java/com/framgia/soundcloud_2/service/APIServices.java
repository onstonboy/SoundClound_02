package com.framgia.soundcloud_2.service;

import com.framgia.soundcloud_2.data.model.AudioResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

import static com.framgia.soundcloud_2.utils.Constant.ConstantApi.PATH_SONG;

public interface APIServices {
    @GET(PATH_SONG)
    Call<AudioResponse> getAudio(@QueryMap Map<String, String> params);
}

