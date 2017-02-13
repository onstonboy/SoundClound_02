package com.framgia.soundcloud_2.main;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.framgia.soundcloud_2.R;
import com.framgia.soundcloud_2.adapter.ViewPagerAdapter;
import com.framgia.soundcloud_2.controlallscreen.BasePlayerActivity;
import com.framgia.soundcloud_2.songs.ListSongActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BasePlayerActivity
    implements MainContract.View, SearchView.OnQueryTextListener {
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private MainContract.Presenter mMainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setPresenter(new MainPresenter(this));
        mMainPresenter.start();
        setupToolbar();
    }

    @Override
    public void start() {
        super.start();
        FragmentManager manager = getSupportFragmentManager();
        ViewPagerAdapter adapter = new ViewPagerAdapter(manager, this);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.setTabsFromPagerAdapter(adapter);
    }

    public void setupToolbar() {
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setTitle(R.string.app_name);
        setSupportActionBar(mToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem searchItem = menu.findItem(R.id.item_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        mMainPresenter = presenter;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        startActivity(ListSongActivity.getSongFromSearch(this, query));
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
