package com.studyjams.mdvideo.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.studyjams.mdvideo.Bean.Video;
import com.studyjams.mdvideo.ProRecyclerView.RecyclerViewCursorAdapter;
import com.studyjams.mdvideo.ProRecyclerView.RecyclerViewCursorViewHolder;
import com.studyjams.mdvideo.R;
import com.studyjams.mdvideo.Util.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by syamiadmin on 2016/7/12.
 */
public class LocalVideoCursorAdapter extends RecyclerViewCursorAdapter<LocalVideoCursorAdapter.VideoViewHolder> {

    public static final Uri LOCAL_VIDEO_URI = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
    private List<Video> mVideoData;
    /**
     * Constructor.
     * @param context The Context the Adapter is displayed in.
     */
    public LocalVideoCursorAdapter(Context context) {
        super(context);

        setupCursorAdapter(null, 0, R.layout.local_video_list_item, false);
        mVideoData = new ArrayList<>();
    }

    /**
     * 返回单个item的数据
     * @param position
     * @return
     */
    public Video getItemData(int position){
        return mVideoData.get(position);
    }

    /**
     * Returns the ViewHolder to use for this adapter.
     */
    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VideoViewHolder(mCursorAdapter.newView(mContext, mCursorAdapter.getCursor(), parent));
    }

    /**
     * Moves the Cursor of the CursorAdapter to the appropriate position and binds the view for
     * that item.
     */
    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        // Move cursor to this position
        mCursorAdapter.getCursor().moveToPosition(position);

        // Set the ViewHolder
        setViewHolder(holder);

        // Bind this view
        mCursorAdapter.bindView(null, mContext, mCursorAdapter.getCursor());
    }

    /**
     * ViewHolder used to display a movie name.
     */
    public class VideoViewHolder extends RecyclerViewCursorViewHolder {

        public final ImageView mThumbnail;
        public final TextView mTitle;
        public final TextView mInfo;

        public VideoViewHolder(View view) {
            super(view);

            mThumbnail = (ImageView) view.findViewById(R.id.image);
            mTitle = (TextView) view.findViewById(R.id.title);
            mInfo = (TextView) view.findViewById(R.id.info);
        }

        @Override
        public void bindCursor(Cursor cursor) {
            Video video = new Video();
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
            String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.ALBUM));
            String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.ARTIST));
            String displayName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME));
            String mimeType = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE));
            String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
            long duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
            long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));

            video.setId(id);
            video.setTitle(title);
            video.setAlbum(album);
            video.setArtist(artist);
            video.setDisplayName(displayName);
            video.setMimeType(mimeType);
            video.setPath(path);
            video.setDuration(duration);
            video.setSize(size);
            //备份一份数据便于事件响应调用
            mVideoData.add(getAdapterPosition(),video);

            mTitle.setText(title);
            mInfo.setText(DateFormat.getTimeFormat(mContext).format(duration));
            ImageLoader.LoadNormalImage(mContext,path,mThumbnail);
        }
    }
}

