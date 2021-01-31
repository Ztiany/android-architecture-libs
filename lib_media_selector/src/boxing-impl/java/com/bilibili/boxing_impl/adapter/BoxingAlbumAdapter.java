/*
 *  Copyright (C) 2017 Bilibili
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.bilibili.boxing_impl.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bilibili.boxing.BoxingMediaLoader;
import com.bilibili.boxing.model.BoxingManager;
import com.bilibili.boxing.model.entity.AlbumEntity;
import com.bilibili.boxing.model.entity.impl.ImageMedia;
import com.ztiany.mediaselector.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


/**
 * Album window adapter.
 *
 * @author ChenSL
 */
public class BoxingAlbumAdapter extends RecyclerView.Adapter<BoxingAlbumAdapter.AlbumViewHolder> implements View.OnClickListener {

    private static final String UNKNOW_ALBUM_NAME = "?";

    private int mCurrentAlbumPos;

    private List<AlbumEntity> mAlums;
    private LayoutInflater mInflater;
    private OnAlbumClickListener mAlbumOnClickListener;
    private int mDefaultRes;

    public BoxingAlbumAdapter(Context context) {
        this.mAlums = new ArrayList<>();
        this.mAlums.add(AlbumEntity.createDefaultAlbum());
        this.mInflater = LayoutInflater.from(context);
        this.mDefaultRes = BoxingManager.getInstance().getBoxingConfig().getAlbumPlaceHolderRes();
    }

    public void setAlbumOnClickListener(OnAlbumClickListener albumOnClickListener) {
        this.mAlbumOnClickListener = albumOnClickListener;
    }

    @NotNull
    @Override
    public AlbumViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return new AlbumViewHolder(mInflater.inflate(R.layout.layout_boxing_album_item, parent, false));
    }

    @Override
    public void onBindViewHolder(AlbumViewHolder albumViewHolder, int position) {
        albumViewHolder.mCoverImg.setImageResource(mDefaultRes);
        final int adapterPos = albumViewHolder.getAdapterPosition();
        final AlbumEntity album = mAlums.get(adapterPos);

        if (album != null && album.hasImages()) {
            String albumName = TextUtils.isEmpty(album.mBucketName) ? albumViewHolder.mNameTxt.getContext().getString(R.string.boxing_default_album_name) : album.mBucketName;
            albumViewHolder.mNameTxt.setText(albumName);
            ImageMedia media = (ImageMedia) album.mImageList.get(0);
            if (media != null) {
                BoxingMediaLoader.getInstance().displayThumbnail(albumViewHolder.mCoverImg, media.getUri(), 50, 50);
                albumViewHolder.mCoverImg.setTag(R.string.boxing_app_name, media.getUri());
            }
            albumViewHolder.mLayout.setTag(adapterPos);
            albumViewHolder.mLayout.setOnClickListener(this);
            albumViewHolder.mCheckedImg.setVisibility(album.mIsSelected ? View.VISIBLE : View.GONE);
            albumViewHolder.mSizeTxt.setText(albumViewHolder.mSizeTxt.getResources().getString(R.string.boxing_album_images_fmt, album.mCount));
        } else {
            albumViewHolder.mNameTxt.setText(UNKNOW_ALBUM_NAME);
            albumViewHolder.mSizeTxt.setVisibility(View.GONE);
        }
    }

    public void addAllData(List<AlbumEntity> alums) {
        mAlums.clear();
        mAlums.addAll(alums);
        notifyDataSetChanged();
    }

    public List<AlbumEntity> getAlums() {
        return mAlums;
    }

    public int getCurrentAlbumPos() {
        return mCurrentAlbumPos;
    }

    public void setCurrentAlbumPos(int currentAlbumPos) {
        mCurrentAlbumPos = currentAlbumPos;
    }

    public AlbumEntity getCurrentAlbum() {
        if (mAlums == null || mAlums.size() <= 0) {
            return null;
        }
        return mAlums.get(mCurrentAlbumPos);
    }

    @Override
    public int getItemCount() {
        return mAlums != null ? mAlums.size() : 0;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.album_layout) {
            if (mAlbumOnClickListener != null) {
                mAlbumOnClickListener.onClick(v, (Integer) v.getTag());
            }
        }
    }

    static class AlbumViewHolder extends RecyclerView.ViewHolder {

        ImageView mCoverImg;
        TextView mNameTxt;
        TextView mSizeTxt;
        View mLayout;
        ImageView mCheckedImg;

        AlbumViewHolder(final View itemView) {
            super(itemView);
            mCoverImg = itemView.findViewById(R.id.album_thumbnail);
            mNameTxt = itemView.findViewById(R.id.album_name);
            mSizeTxt = itemView.findViewById(R.id.album_size);
            mLayout = itemView.findViewById(R.id.album_layout);
            mCheckedImg = itemView.findViewById(R.id.album_checked);
        }
    }

    public interface OnAlbumClickListener {
        void onClick(View view, int pos);
    }

}
