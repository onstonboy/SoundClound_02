package com.framgia.soundcloud_2.data.local;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import com.framgia.soundcloud_2.data.GetCallback;
import com.framgia.soundcloud_2.data.LocalDataSource;
import com.framgia.soundcloud_2.data.model.Track;
import com.framgia.soundcloud_2.utils.Constant;

import java.util.ArrayList;

/**
 * Created by Vinh on 07/02/2017.
 */
public class SongLocalDataSource implements LocalDataSource<Track> {
    private static SongLocalDataSource sSongLocalDataSource;
    private Context mContext;

    private SongLocalDataSource(Context context) {
        this.mContext = context;
    }

    public static SongLocalDataSource getInstance(@NonNull Context context) {
        if (sSongLocalDataSource == null)
            sSongLocalDataSource = new SongLocalDataSource(context);
        return sSongLocalDataSource;
    }

    @Override
    public void getDatas(GetCallback<Track> getCallback) {
        ArrayList<Track> tracksList = new ArrayList<>();
        ContentResolver contentResolver = mContext.getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + Constant.CHECK_MUSIC;
        String sort = MediaStore.Audio.Media.TITLE + Constant.SORT_MUSIC;
        Cursor cursor = contentResolver.query(uri, null, selection, null, sort);
        if (cursor == null && cursor.getCount() <= 0) {
            getCallback.onNotAvailable();
        }
        while (cursor.moveToNext()) {
            Track track = new Track();
            track.setTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
            track.setUri(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
            track
                .setDuration(cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)));
            tracksList.add(track);
        }
        cursor.close();
        getCallback.onLoaded(tracksList);
    }
}
