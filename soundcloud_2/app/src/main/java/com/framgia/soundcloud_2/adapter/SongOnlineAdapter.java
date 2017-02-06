package com.framgia.soundcloud_2.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.framgia.soundcloud_2.R;
import com.framgia.soundcloud_2.data.model.Track;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tri on 03/02/2017.
 */
public class SongOnlineAdapter
    extends RecyclerView.Adapter<SongOnlineAdapter.SongOnlineViewHolder> {
    private static final String FORMAT_NUMBER = "%1$,.0f";
    private List<Track> mListTracks;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ItemClickListener mClickListener;

    public SongOnlineAdapter(Context context, List<Track> list, ItemClickListener clickListener) {
        mContext = context;
        mListTracks = list;
        mLayoutInflater = LayoutInflater.from(context);
        mClickListener = clickListener;
    }

    @Override
    public SongOnlineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView =
            mLayoutInflater.inflate(R.layout.item_songs_online, parent, false);
        return new SongOnlineViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SongOnlineViewHolder holder, int position) {
        holder.bindData(mListTracks.get(position));
    }

    @Override
    public int getItemCount() {
        return mListTracks != null ? mListTracks.size() : 0;
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onClick(int position, Track track);
    }

    public class SongOnlineViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {
        @BindView(R.id.text_songs_online_title)
        TextView mSongTitle;
        @BindView(R.id.text_artist_name)
        TextView mSongArtistName;
        @BindView(R.id.text_playback_count)
        TextView mSongPlaybackCount;
        @BindView(R.id.button_download)
        ImageButton mButtonDownload;
        @BindView(R.id.image_songs_icon)
        ImageView mImageView;

        public SongOnlineViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            mButtonDownload.setOnClickListener(this);
        }

        public void bindData(Track track) {
            if (track == null || track.getUser() == null) return;
            mSongTitle.setText(track.getTitle());
            mSongArtistName.setText(track.getUser().getUserName());
            mSongPlaybackCount.setText(String.format(FORMAT_NUMBER, track.getPlaybackCount()));
            Picasso.with(mContext).load(track.getArtworkUrl()).into(mImageView);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onClick(getAdapterPosition(),
                mListTracks.get(getAdapterPosition()));
        }
    }
}
