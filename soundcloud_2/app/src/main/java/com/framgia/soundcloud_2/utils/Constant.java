package com.framgia.soundcloud_2.utils;

import com.framgia.soundcloud_2.BuildConfig;
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
    }

    public static class ConstantApi {
        public static final String URL_SOUNDCLOUD = "https://api-v2.soundcloud.com/";
        public static final String PATH_SONG = "charts";
        public static final String PARAM_CLIENT_ID = "client_id";
        public static final String PARAM_GENRE = "genre";
        public static final String PARAM_KIND = "kind";
        public static final String VALUE_KIND_TOP = "top";
        public static final String PARAM_LIMIT = "limit";
        public static final String VALUE_LIMIT = "50";
        public static final String STREAM_URL = "/stream?client_id" + "=" + BuildConfig.API_KEY;
    }

    public class RequestCode {
        public static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 1;
    }
}
