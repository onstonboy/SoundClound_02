package com.framgia.soundcloud_2.data;

import com.framgia.soundcloud_2.data.model.Category;

import java.util.List;

public interface DataSource<T> {
    void getDatas(Category category, String query, GetCallback<T> getCallback);
    interface GetCallback<T> {
        void onLoaded(List<T> datas);
        void onNotAvailable();
    }
}
