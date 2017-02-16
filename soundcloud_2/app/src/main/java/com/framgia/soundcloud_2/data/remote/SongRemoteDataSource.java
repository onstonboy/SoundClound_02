package com.framgia.soundcloud_2.data.remote;

import com.framgia.soundcloud_2.BuildConfig;
import com.framgia.soundcloud_2.data.GetCallback;
import com.framgia.soundcloud_2.data.SongDataSource;
import com.framgia.soundcloud_2.data.model.AudioResponse;
import com.framgia.soundcloud_2.data.model.Category;
import com.framgia.soundcloud_2.data.model.CollectionTrack;
import com.framgia.soundcloud_2.data.model.SearchAudioResult;
import com.framgia.soundcloud_2.data.model.Track;
import com.framgia.soundcloud_2.service.API;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.framgia.soundcloud_2.utils.Constant.ConstantApi.PARAM_CLIENT_ID;
import static com.framgia.soundcloud_2.utils.Constant.ConstantApi.PARAM_GENRE;
import static com.framgia.soundcloud_2.utils.Constant.ConstantApi.PARAM_KIND;
import static com.framgia.soundcloud_2.utils.Constant.ConstantApi.PARAM_LIMIT;
import static com.framgia.soundcloud_2.utils.Constant.ConstantApi.PARAM_OFFSET;
import static com.framgia.soundcloud_2.utils.Constant.ConstantApi.PARAM_QUERY;
import static com.framgia.soundcloud_2.utils.Constant.ConstantApi.VALUE_KIND_TOP;
import static com.framgia.soundcloud_2.utils.Constant.ConstantApi.VALUE_LIMIT;

/**
 * Created by tri on 07/02/2017.
 */
public class SongRemoteDataSource implements SongDataSource<Track> {
    private static SongRemoteDataSource sSongRemoteDataSource;

    private SongRemoteDataSource() {
    }

    public static SongRemoteDataSource getInstance() {
        if (sSongRemoteDataSource == null)
            sSongRemoteDataSource = new SongRemoteDataSource();
        return sSongRemoteDataSource;
    }

    @Override
    public void getDatas(final Category category, final int offset,
                         final GetCallback<Track> getCallback) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_CLIENT_ID, BuildConfig.API_KEY);
        params.put(PARAM_GENRE, category.getCategoryParam());
        params.put(PARAM_KIND, VALUE_KIND_TOP);
        params.put(PARAM_LIMIT, VALUE_LIMIT);
        params.put(PARAM_OFFSET, String.valueOf(offset));
        API.getSong(params, new Callback<AudioResponse>() {
                @Override
                public void onResponse(Call<AudioResponse> call, Response<AudioResponse> response) {
                    if (response == null && response.body().getTracksList() == null) {
                        getCallback.onNotAvailable();
                        return;
                    }
                    List<Track> list = new ArrayList<>();
                    for (CollectionTrack collectionTrack : response.body().getTracksList()) {
                        list.add(collectionTrack.getTrack());
                    }
                    getCallback.onLoaded(list, response.body().getNextHref());
                }

                @Override
                public void onFailure(Call<AudioResponse> call, Throwable t) {
                    getCallback.onNotAvailable();
                }
            }
        );
    }

    @Override
    public void searchData(final String query, final int offset,
                           final GetCallback<Track>
                               getCallback) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_CLIENT_ID, BuildConfig.API_KEY);
        params.put(PARAM_LIMIT, VALUE_LIMIT);
        params.put(PARAM_QUERY, query);
        params.put(PARAM_OFFSET, String.valueOf(offset));
        API.getSongSearchResult(params, new Callback<SearchAudioResult>() {
            @Override
            public void onResponse(Call<SearchAudioResult> call,
                                   Response<SearchAudioResult> response) {
                if (response == null) {
                    getCallback.onNotAvailable();
                    return;
                }
                getCallback.onLoaded(response.body().getTracks(), response.body().getNextHref());
            }

            @Override
            public void onFailure(Call<SearchAudioResult> call, Throwable t) {
                getCallback.onNotAvailable();
            }
        });
    }
}
