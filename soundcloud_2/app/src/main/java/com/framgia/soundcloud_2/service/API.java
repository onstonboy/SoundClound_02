package com.framgia.soundcloud_2.service;

import com.framgia.soundcloud_2.data.model.AudioResponse;
import com.framgia.soundcloud_2.data.model.SearchAudioResult;

import java.util.Map;

import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.framgia.soundcloud_2.utils.Constant.ConstantApi.URL_SOUNDCLOUD;

public abstract class API {
    private static final APIServices sAPIServices = new Retrofit.Builder()
        .baseUrl(URL_SOUNDCLOUD)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(APIServices.class);

    public static void getSong(Map<String, String> params,
                               Callback<AudioResponse> callback) {
        sAPIServices.getAudio(params)
            .enqueue(callback);
    }

    public static void getSongSearchResult(Map<String, String> params,
                                           Callback<SearchAudioResult> callback) {
        sAPIServices.getSearchResult(params)
            .enqueue(callback);
    }
}
