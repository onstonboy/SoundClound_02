package com.framgia.soundcloud_2.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.framgia.soundcloud_2.R;
import com.framgia.soundcloud_2.ui.fragment.CategorySongsFragment;
import com.framgia.soundcloud_2.ui.fragment.LocalSongsFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private static final int POSITON_CATEGORY = 0;
    private static final int POSITON_LOCAL = 1;
    private static int TAB_NUMBER = 2;
    private Context mContext;

    public ViewPagerAdapter(FragmentManager fragmentManager, Context context) {
        super(fragmentManager);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case POSITON_CATEGORY:
                return new CategorySongsFragment();
            case POSITON_LOCAL:
                return new LocalSongsFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return TAB_NUMBER;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case POSITON_CATEGORY:
                return mContext.getString(R.string.title_category);
            case POSITON_LOCAL:
                return mContext.getString(R.string.title_local);
            default:
                return null;
        }
    }
}