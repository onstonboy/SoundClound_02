package com.framgia.soundcloud_2.data;

import com.framgia.soundcloud_2.data.model.Category;

public interface SongDataSource<T> {
    void getDatas(Category category, int offset, GetCallback<T> getCallback);
    void searchData(String query, int offset, GetCallback<T> getCallback);
}
