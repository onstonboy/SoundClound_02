package com.framgia.soundcloud_2.utils;

import android.content.Context;
import android.preference.PreferenceManager;

public class StorePreferences {
    private static final String PREF_AUDIO_INDEX = "AudioIndex";
    private static final int DEFAULT_AUDIO_INDEX = -1;

    public static void storeAudioIndex(Context context, int index) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt
            (PREF_AUDIO_INDEX, index).apply();
    }

    public static int loadAudioIndex(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt
            (PREF_AUDIO_INDEX, DEFAULT_AUDIO_INDEX);
    }
}
