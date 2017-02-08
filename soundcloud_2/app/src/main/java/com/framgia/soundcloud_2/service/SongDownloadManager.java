package com.framgia.soundcloud_2.service;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import com.framgia.soundcloud_2.R;

public class SongDownloadManager {
    private final String mSongType = ".mp3";
    private Context mContext;
    private String mTitle;
    private String mUrl;

    public SongDownloadManager(Context context, String title, String url) {
        mContext = context;
        mTitle = title;
        mUrl = url;
    }

    public void startDownload() {
        DownloadManager downloadmanager = (DownloadManager) mContext.getSystemService(Context
            .DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(mUrl);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle(mTitle);
        request.setDescription(mContext.getString(R.string.msg_download));
        request
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
                mTitle + mSongType);
        downloadmanager.enqueue(request);
    }
}
