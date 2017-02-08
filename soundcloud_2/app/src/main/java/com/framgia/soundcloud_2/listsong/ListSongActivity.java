package com.framgia.soundcloud_2.listsong;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.framgia.soundcloud_2.R;
import com.framgia.soundcloud_2.adapter.SongOnlineAdapter;
import com.framgia.soundcloud_2.data.SongRepository;
import com.framgia.soundcloud_2.data.model.Category;
import com.framgia.soundcloud_2.data.model.Track;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.framgia.soundcloud_2.utils.Constant.KeyIntent.EXTRA_CATEGORY;

public class ListSongActivity extends AppCompatActivity implements ListSongContract.View,
    SongOnlineAdapter.ItemClickListener {
    @BindView(R.id.recycle_listsong)
    RecyclerView mRecyclerView;
    private Category mCategory;
    private ListSongContract.Presenter mPresenter;
    private SongOnlineAdapter mSongOnlineAdapter;
    private List<Track> mTracks = new ArrayList<>();

    public static Intent getListSongItent(Context context, Category category) {
        Intent intent = new Intent(context, ListSongActivity.class);
        intent.putExtra(EXTRA_CATEGORY, category);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_song);
        setPresenter(new ListSongPresenter(this, SongRepository.getInstance(this)));
        ButterKnife.bind(this);
        mPresenter.start();
    }

    private void getIntentData() {
        mCategory = getIntent().getParcelableExtra(EXTRA_CATEGORY);
    }

    public void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(mCategory.getCategoryTitle());
        setSupportActionBar(toolbar);
    }

    private void initView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mSongOnlineAdapter = new SongOnlineAdapter(this, mTracks, this);
        mRecyclerView.setAdapter(mSongOnlineAdapter);
    }

    @Override
    public void onClick(int position, Track track) {
        // TODO : item click
    }

    @Override
    public void showSong(List<Track> list) {
        if (list == null) return;
        mTracks.addAll(list);
        mSongOnlineAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError() {
        Toast.makeText(this, R.string.error_get_song_fail, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setPresenter(ListSongContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void start() {
        initView();
        getIntentData();
        setupToolbar();
        mPresenter.getSongFromApi(mCategory);
    }
}
