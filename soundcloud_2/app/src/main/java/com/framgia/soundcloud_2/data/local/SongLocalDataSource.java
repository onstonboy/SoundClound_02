package com.framgia.soundcloud_2.data.local;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.framgia.soundcloud_2.data.GetCallback;
import com.framgia.soundcloud_2.data.LocalDataSource;
import com.framgia.soundcloud_2.data.model.Track;
import com.framgia.soundcloud_2.utils.Constant;
import com.framgia.soundcloud_2.utils.DatabaseManager;

import java.util.ArrayList;
import java.util.List;

import static com.framgia.soundcloud_2.utils.DatabaseManager.COLUMN_ARTWORK_URL;
import static com.framgia.soundcloud_2.utils.DatabaseManager.COLUMN_DOWNLOADABLE;
import static com.framgia.soundcloud_2.utils.DatabaseManager.COLUMN_DURATION;
import static com.framgia.soundcloud_2.utils.DatabaseManager.COLUMN_PLAYBACK_COUNT;
import static com.framgia.soundcloud_2.utils.DatabaseManager.COLUMN_TITLE;
import static com.framgia.soundcloud_2.utils.DatabaseManager.COLUMN_URI;
import static com.framgia.soundcloud_2.utils.DatabaseManager.COLUMN_USER;
import static com.framgia.soundcloud_2.utils.DatabaseManager.TABLE_TRACK;

/**
 * Created by Vinh on 07/02/2017.
 */
public class SongLocalDataSource extends AppCompatActivity implements LocalDataSource<Track> {
    private static SongLocalDataSource sSongLocalDataSource;
    private Context mContext;
    private DatabaseManager mDatabaseManager;

    private SongLocalDataSource(Context context) {
        this.mContext = context;
        mDatabaseManager = new DatabaseManager(context);
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
        getCallback.onLoaded(tracksList, null);
    }

    @Override
    public void clearListTrack() {
        SQLiteDatabase sqLiteDatabase = mDatabaseManager.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_TRACK, null, null);
        sqLiteDatabase.close();
    }

    @Override
    public void addListTrackLocal(List<Track> list) {
        SQLiteDatabase sqLiteDatabase = mDatabaseManager.getWritableDatabase();
        if (list == null) return;
        try {
            for (Track track : list) {
                if (track.getUser().getUserName() == null) return;
                ContentValues values = new ContentValues();
                values.put(COLUMN_TITLE, track.getTitle());
                values.put(COLUMN_ARTWORK_URL, track.getArtworkUrl());
                values.put(COLUMN_DOWNLOADABLE, track.isDownloadAble());
                values.put(COLUMN_DURATION, track.getDuration());
                values.put(COLUMN_URI, track.getFullUri());
                values.put(COLUMN_USER, track.getUser().getUserName());
                values.put(COLUMN_PLAYBACK_COUNT, track.getPlaybackCount());
                sqLiteDatabase.insertOrThrow(TABLE_TRACK, null, values);
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            sqLiteDatabase.close();
        }
    }
}
