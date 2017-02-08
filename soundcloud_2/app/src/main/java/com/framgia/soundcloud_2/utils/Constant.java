package com.framgia.soundcloud_2.utils;

import com.framgia.soundcloud_2.R;

public class Constant {
    public static final int REQUEST_EXTERNAL_STORAGE = 1;
    public static final String CHECK_MUSIC = "!= 0";
    public static final String SORT_MUSIC = " ASC";

    public static class ResourceImage {
        public static int[] IMAGE_CATEGORY = {
            R.drawable.ic_altinaterock,
            R.drawable.ic_ambient,
            R.drawable.ic_classical,
            R.drawable.ic_country,
            R.drawable.ic_dance_edm
        };
    }

    public static class KeyIntent {
        public static final String EXTRA_CATEGORY = "category";
        public static final String ACTION_UPDATE_CONTROL = "com.soundcloud_2.action.ACTION_UPDATE_CONTROL";
        public static final String ACTION_PLAY_NEW_AUDIO = "com.soundcloud_2.action.ACTION_PLAY_NEW_AUDIO";
        public static final String ACTION_PLAY = "com.soundcloud_2.action.ACTION_PLAY";
        public static final String ACTION_PAUSE = "com.soundcloud_2.action.ACTION_PAUSE";
        public static final String ACTION_STOP = "com.soundcloud_2.action.ACTION_STOP";
        public static final String ACTION_PREVIOUS = "com.soundcloud_2.action.ACTION_PREVIOUS";
        public static final String ACTION_NEXT = "com.soundcloud_2.action.ACTION_NEXT";
    }

    public static class ConstantApi {
        public static final String URL_SOUNDCLOUD = "https://api-v2.soundcloud.com/";
        public static final String PATH_SONG = "charts";
        public static final String PARAM_CLIENT_ID = "client_id";
        public static final String PARAM_GENRE = "genre";
        public static final String PARAM_KIND = "kind";
        public static final String VALUE_KIND_TOP = "top";
    }
}
